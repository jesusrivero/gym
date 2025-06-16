package com.jesus.gymcontrol.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.domain.repository.UserAdminRepository
import com.jesus.gymcontrol.infraestructure.di.SecondaryFirebase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UserAdminRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val context: Context
) : UserAdminRepository {

    override suspend fun registerUserFromAdmin(
        email: String,
        password: String,
        name: String,
        phone: String,
        idCard: String,
        gender: String,
        age: Int,
        rol: String,
        membership: String,
        code: String,
        gimnasioCode: String // ya no se usa como parámetro directo
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // VALIDAR CÓDIGO antes de crear el usuario
            val codigoSnapshot = firestore.collection("codigos")
                .whereEqualTo("codigo", code)
                .limit(1)
                .get()
                .await()

            val codigoDocument = codigoSnapshot.documents.firstOrNull()
                ?: return@withContext Result.failure(Exception("El código '$code' no existe."))

            val yaUsado = codigoDocument.getBoolean("usado") == true
            if (yaUsado) return@withContext Result.failure(Exception("El código '$code' ya fue usado."))

            // UID del administrador actual
            val adminUid = FirebaseAuth.getInstance().currentUser?.uid
                ?: return@withContext Result.failure(Exception("No se pudo obtener UID del administrador"))

            // Obtener gimnasioCode real desde el documento del admin
            val adminDoc = firestore.collection("users").document(adminUid).get().await()
            val gymCode = adminDoc.getString("gimnasioCode")
                ?: return@withContext Result.failure(Exception("El administrador no tiene gimnasioCode registrado"))
            val gymName = adminDoc.getString("gimnasio") ?: "Gimnasio"

            // ✅ Ahora sí: crear usuario con instancia secundaria
            val secondaryAuth = SecondaryFirebase.getSecondaryAuth(context)
            val result = secondaryAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid
                ?: return@withContext Result.failure(Exception("No se pudo obtener UID del nuevo usuario"))

            // Crear documento principal del usuario
            val userMap = mapOf(
                "uid" to uid,
                "email" to email,
                "name" to name,
                "idCard" to idCard,
                "gender" to gender,
                "age" to age,
                "membership" to membership,
                "code" to code,
                "gimnasioCode" to gymCode,
                "rol" to rol,
                "estado" to "inactivo",
                "phone" to phone
            )

            firestore.collection("users").document(uid).set(userMap).await()

            // Subcolección del gimnasio del usuario
            firestore.collection("users").document(uid)
                .collection("gimnasios").document(gymCode).set(
                    mapOf(
                        "codigo" to gymCode,
                        "nombre" to gymName,
                        "estado" to "inactivo"
                    )
                ).await()

            // Agregar usuario en la colección de usuarios del gimnasio
            firestore.collection("gimnasios").document(gymCode)
                .collection("usuarios").document(uid).set(
                    mapOf(
                        "uid" to uid,
                        "nombre" to name,
                        "rol" to rol,
                        "estado" to "inactivo"
                    )
                ).await()

            // Marcar código como usado
            codigoDocument.reference.update("usado", true).await()

            // Cerrar sesión secundaria
            secondaryAuth.signOut()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}