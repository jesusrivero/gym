package com.jesus.gymcontrol.data.repository

import android.content.Context
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
        gimnasioCode: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val secondaryAuth = SecondaryFirebase.getSecondaryAuth(context)

            val result = secondaryAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return@withContext Result.failure(Exception("No se pudo obtener UID"))

            val userMap = mapOf(
                "uid" to uid,
                "email" to email,
                "name" to name,
                "phone" to phone,
                "idCard" to idCard,
                "gender" to gender,
                "age" to age,
                "membership" to membership,
                "code" to code,
                "gimnasioCode" to gimnasioCode,
                "rol" to rol,
                "estado" to "inactivo"
            )

            firestore.collection("users").document(uid).set(userMap).await()

            // Opcional: cerrar la sesión secundaria para evitar fugas
            secondaryAuth.signOut()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}