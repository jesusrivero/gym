const admin = require("firebase-admin");

// ============================================
// 1. Configuración y utilidades
// ============================================

const conReintentos = async (operacion, intentosMaximos = 3, delayInicialMs = 500) => {
  let intento = 0;
  let delay = delayInicialMs;

  while (intento < intentosMaximos) {
    try {
      return await operacion();
    } catch (error) {
      intento++;
      console.warn(`⚠ Intento ${intento} fallido: ${error.message}`);
      if (intento >= intentosMaximos) throw error;
      await new Promise(resolve => setTimeout(resolve, delay));
      delay *= 2;
    }
  }
};

let serviceAccount;
if (process.env.FIREBASE_SERVICE_ACCOUNT) {
  serviceAccount = JSON.parse(process.env.FIREBASE_SERVICE_ACCOUNT);
} else {
  try {
    serviceAccount = require("./serviceAccountKey.json");
  } catch (err) {
    console.error("❌ No se encontró el archivo serviceAccountKey.json ni la variable de entorno FIREBASE_SERVICE_ACCOUNT");
    process.exit(1);
  }
}

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

console.log("✅ Firebase Admin inicializado. Versión:", admin.SDK_VERSION);
const db = admin.firestore();

// ============================================
// 2. Funciones principales
// ============================================

const enviarNotificacionInicioDeSemana = async () => {
  console.log("📅 Enviando notificación de inicio de semana...");

  const titulo = "¡Nueva semana, nuevas metas!";
  const mensaje = "Te recordamos que hoy inicia una nueva semana. ¡Dale con todo!";
  const type = "week_start";

  const gimnasiosSnapshot = await conReintentos(() => db.collection("gimnasios").get());

  for (const gymDoc of gimnasiosSnapshot.docs) {
    const gimnasioCode = gymDoc.id;

    const adminsSnapshot = await db.collection("users")
      .where("rol", "in", ["administrador", "dueño"])
      .where("gimnasioCode", "==", gimnasioCode)
      .get();

    const tokens = adminsSnapshot.docs
      .map(doc => doc.data().fcmToken)
      .filter(Boolean);

    if (tokens.length > 0) {
      await enviarNotificaciones(tokens, titulo, mensaje, type);
      await guardarNotificacion(gimnasioCode, titulo, mensaje);
    }
  }

  console.log("✅ Notificación de inicio de semana enviada.");
};

const enviarNotificaciones = async (tokens, titulo, mensaje, type = "") => {
  if (!tokens || tokens.length === 0) {
    console.warn("⚠ No hay tokens para enviar notificaciones");
    return;
  }

  const messaging = admin.messaging();

  const results = await Promise.all(
    tokens.map(token =>
      messaging.send({
        token,
        notification: { title: titulo, body: mensaje },
        data: { type }
      }).then(() => ({ success: true }))
        .catch(error => ({ success: false, error }))
    )
  );

  const successCount = results.filter(r => r.success).length;
  console.log(`📢 Notificaciones enviadas. Éxitos: ${successCount}, Fallos: ${results.length - successCount}`);
  return results;
};

const guardarNotificacion = async (gimnasioCode, titulo, mensaje) => {
  try {
    const notificacion = {
      titulo,
      mensaje,
      fecha: Date.now(),
      leido: false
    };
    await conReintentos(() =>
      db.collection("gimnasios").doc(gimnasioCode).collection("notificaciones").add(notificacion)
    );
    console.log(`📌 Notificación guardada en gimnasio ${gimnasioCode}`);
  } catch (error) {
    console.error("❌ Error al guardar notificación:", error.message);
  }
};

const notificarNuevosClientes = async () => {
  console.log("🧾 Revisando nuevos clientes registrados...");

  const ahora = Date.now();
  const hace5Min = ahora - 5 * 60 * 1000;

  const gimnasiosSnapshot = await conReintentos(() => db.collection("gimnasios").get());

  for (const gymDoc of gimnasiosSnapshot.docs) {
    const gimnasioCode = gymDoc.id;
    const usuariosRef = db.collection("gimnasios").doc(gimnasioCode).collection("usuarios");

    const nuevosClientesSnapshot = await conReintentos(() =>
      usuariosRef
        .where("rol", "==", "cliente")
        .where("date", ">=", hace5Min)
        .get()
    );

    if (nuevosClientesSnapshot.empty) continue;

    const adminsSnapshot = await db.collection("users")
      .where("rol", "in", ["administrador", "dueño"])
      .where("gimnasioCode", "==", gimnasioCode)
      .get();

    const tokensAdmins = adminsSnapshot.docs
      .map(doc => doc.data().fcmToken)
      .filter(Boolean);

    for (const userDoc of nuevosClientesSnapshot.docs) {
      const cliente = userDoc.data();
      const nombreCompleto = `${cliente.name || ""} ${cliente.lastname || ""}`.trim() || "Nuevo cliente";

      const titulo = "Nuevo cliente registrado";
      const mensaje = `Se ha registrado ${nombreCompleto} en tu gimnasio`;
      const type = "new_client";

      if (tokensAdmins.length > 0) {
        await enviarNotificaciones(tokensAdmins, titulo, mensaje, type);
        await guardarNotificacion(gimnasioCode, titulo, mensaje);
      }
    }
  }

  console.log("✅ Revisión de nuevos clientes completada.");
};

const revisarEstados = async () => {
  console.log("⏰ Iniciando revisión de estados...");
  try {
    const now = Date.now();
    const gimnasiosSnapshot = await conReintentos(() => db.collection("gimnasios").get());
    console.log(`🏋️‍♂️ Gimnasios a procesar: ${gimnasiosSnapshot.size}`);

    for (const gymDoc of gimnasiosSnapshot.docs) {
      const gimnasioCode = gymDoc.id;
      const usuariosSnapshot = await conReintentos(() =>
        db.collection("gimnasios").doc(gimnasioCode).collection("usuarios").get()
      );

      for (const usuarioDoc of usuariosSnapshot.docs) {
        const userId = usuarioDoc.id;
        const usuarioData = usuarioDoc.data();

        if (usuarioData.rol !== "cliente") continue;
        const fechaVencimiento = usuarioData.fechaVencimiento;
        if (!fechaVencimiento) continue;

        const diasRestantes = Math.ceil((fechaVencimiento - now) / (1000 * 60 * 60 * 24));
        let nuevoEstado;
        if (diasRestantes <= 0) nuevoEstado = "inactivo";
        else if (diasRestantes <= 5) nuevoEstado = "pendiente";
        else nuevoEstado = "activo";

        const estadoAnterior = usuarioData.state;
        if (estadoAnterior === nuevoEstado) continue;

        const nombreUsuario = `${usuarioData.name || ""} ${usuarioData.lastname || ""}`.trim() || userId;

        await conReintentos(() =>
          db.collection("gimnasios").doc(gimnasioCode).collection("usuarios").doc(userId).update({ state: nuevoEstado })
        );

        console.log(`🔄 Estado actualizado para ${userId} en gimnasio ${gimnasioCode} a ${nuevoEstado}`);

        const adminsSnapshot = await db.collection("users")
          .where("rol", "in", ["administrador", "dueño"])
          .where("gimnasioCode", "==", gimnasioCode)
          .get();

        const tokensAdmins = adminsSnapshot.docs
          .map(doc => doc.data().fcmToken)
          .filter(Boolean);

        const mensaje = `El estado de ${nombreUsuario} ha cambiado de \"${estadoAnterior}\" a \"${nuevoEstado}\"`;

        if (tokensAdmins.length > 0) {
          await enviarNotificaciones(tokensAdmins, "Cambio de estado de usuario", mensaje, "membership_expiration");
          await guardarNotificacion(gimnasioCode, "Cambio de estado de usuario", mensaje);
        }
      }
    }

    console.log("✅ Revisión de estados completada exitosamente");
  } catch (error) {
    console.error(`❌ Error crítico en revisión de estados: ${error.message}`);
  }
};

const desactivarPromocionesVencidas = async () => {
  console.log("⏳ Verificando promociones vencidas...");
  const now = Date.now();
  const gimnasiosSnapshot = await conReintentos(() => db.collection("gimnasios").get());

  for (const gymDoc of gimnasiosSnapshot.docs) {
    const gimnasioCode = gymDoc.id;
    const promocionesRef = db.collection("gimnasios").doc(gimnasioCode).collection("promociones");

    const promocionesSnapshot = await conReintentos(() =>
      promocionesRef.where("activo", "==", true).get()
    );

    for (const promoDoc of promocionesSnapshot.docs) {
      const promoData = promoDoc.data();
      const fechaVencimiento = promoData.fechaVencimiento;

      if (!fechaVencimiento || fechaVencimiento > now) continue;

      await conReintentos(() =>
        promocionesRef.doc(promoDoc.id).update({ activo: false })
      );

      console.log(`🔕 Promoción desactivada: ${promoDoc.id} en gimnasio ${gimnasioCode}`);

      const adminsSnapshot = await db.collection("users")
        .where("rol", "in", ["administrador", "dueño"])
        .where("gimnasioCode", "==", gimnasioCode)
        .get();

      const tokensAdmins = adminsSnapshot.docs
        .map(doc => doc.data().fcmToken)
        .filter(Boolean);

      const mensaje = `La promoción \"${promoData.nombre || promoDoc.id}\" ha sido desactivada, ya pasaron sus días de uso`;

      if (tokensAdmins.length > 0) {
        await enviarNotificaciones(tokensAdmins, "Promoción desactivada", mensaje, "promotion_expiration");
        await guardarNotificacion(gimnasioCode, "Promoción desactivada", mensaje);
      }
    }
  }
};

// ============================================
// 3. Ejecución dinámica
// ============================================

const main = async () => {
  const tarea = process.argv[2];

  console.log("\n====================================");
  console.log(`🚀 INICIANDO SERVICIO: ${tarea || "todas las tareas"}`);
  console.log("====================================\n");

  try {
    switch (tarea) {
      case "estados":
        await revisarEstados();
        await desactivarPromocionesVencidas();
        break;
      case "clientes":
        await notificarNuevosClientes();
        break;
      case "semana":
        await enviarNotificacionInicioDeSemana();
        break;
      default:
        await revisarEstados();
        await desactivarPromocionesVencidas();
        await notificarNuevosClientes();
        await enviarNotificacionInicioDeSemana();
    }

    console.log("\n✅ Ejecución completada correctamente");
    process.exit(0);
  } catch (error) {
    console.error("❌ Error durante la ejecución:", error);
    process.exit(1);
  }
};

main();
