package com.jesus.gymcontrol.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun assignGymToUser(uid: String, gym: Gym, rol: String): Result<Unit> {
        return try {
            // 1. Obtener datos básicos del usuario
            val userSnapshot = firestore.collection("users").document(uid).get().await()
            val userName = userSnapshot.getString("name") ?: "Desconocido"

            // 2. Datos para guardar en users/{uid}/gimnasios/{gym.code}
            val userGymData = mapOf(
                "code" to gym.code,
                "name" to gym.name,
                "direction" to gym.direction,
                "phone" to gym.phone,
                "state" to "activo",
                "registrationDate" to FieldValue.serverTimestamp()
            )

            // 3. Datos para guardar en gimnasios/{gym.code}/usuarios/{uid}
            val gymUserData = mapOf(
                "uid" to uid,
                "name" to userName,
                "rol" to rol,
                "registrationDate" to FieldValue.serverTimestamp()
            )

            // 4. Referencias principales
            val userGymRef = firestore.collection("users")
                .document(uid)
                .collection("gimnasios")
                .document(uid) // usamos gym.code como ID único

            val gymUserRef = firestore.collection("gimnasios")
                .document(gym.code)
                .collection("usuarios")
                .document(uid)

            val userRef = firestore.collection("users").document(uid)

            // 5. Subcolección de pagos personalizada del usuario
            val pagosRef = userGymRef.collection("pagos").document("pagos")

            // 6. Subcolecciones compartidas del gimnasio
            val membresiasRef = firestore.collection("gimnasios")
                .document(gym.code)
                .collection("membresias")
                .document("basica") // puedes cambiar "basica" por un plan inicial

            val promocionesRef = firestore.collection("gimnasios")
                .document(gym.code)
                .collection("promociones")
                .document("inicial")

            val mensajesRef = firestore.collection("gimnasios")
                .document(gym.code)
                .collection("mensajes")
                .document("bienvenida")

            // 7. Transacción en lote
            val batch = firestore.batch()

            // Inscripción del usuario al gimnasio
            batch.set(userGymRef, userGymData)

            // Registro del usuario dentro del gimnasio (rol)
            batch.set(gymUserRef, gymUserData)

            // Actualizar información del usuario en su documento raíz
            batch.update(
                userRef, mapOf(
                    "rol" to rol,
                    "gimnasio" to gym.name,
                    "gimnasioCode" to gym.code
                )
            )

            // Crear subcolección de pagos (personal del usuario)
            batch.set(
                pagosRef, mapOf(
                    "mensaje" to "No hay pagos registrados aún"
                )
            )

            // Crear membresía inicial en el gimnasio (si aún no existe)
            batch.set(
                membresiasRef, mapOf(
                    "nombre" to "Membresía Básica",
                    "descripcion" to "Plan de acceso básico",
                    "precio" to 0,
                    "estado" to "activa"
                )
            )

            // Crear promoción inicial (si aún no existe)
            batch.set(
                promocionesRef, mapOf(
                    "titulo" to "Sin promociones",
                    "descripcion" to "Aún no hay promociones activas"
                )
            )

            // Crear mensaje de bienvenida del gimnasio
            batch.set(
                mensajesRef, mapOf(
                    "contenido" to "¡Bienvenido al gimnasio ${gym.name}!",
                    "fecha" to FieldValue.serverTimestamp()
                )
            )

            // Ejecutar transacción
            batch.commit().await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}