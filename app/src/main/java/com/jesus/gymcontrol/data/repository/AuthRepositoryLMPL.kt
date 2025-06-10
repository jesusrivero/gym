package com.jesus.gymcontrol.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await


class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun registerUser(name: String, email: String, password: String): Result<Unit> {
        return try {

            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("Error al obtener UID del usuario")


            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }
            result.user?.updateProfile(profileUpdates)?.await()

            val userData = mapOf(
                "name" to name,
                "email" to email,
                "rol" to "",
                "codigo" to "",
                "gym" to "",
                "cedula" to "",
                "edad" to "",
                "numero" to "",
                "sexo" to "",

            )
            firestore.collection("users").document(uid).set(userData).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginUser(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun recoverPassword(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun updateRolAndCode(
        uid: String,
        rol: String,
        codigo: String
    ): Result<Unit> {
       return try {
           val updates = mapOf(
               "rol" to rol,
               "codigo" to codigo,
           )
           firestore.collection("users").document(uid).update(updates).await()
           Result.success(Unit)
       } catch (e: Exception) {
           Result.failure(e)
       }
    }



    override suspend fun updateDatesUser(
        uid: String,
        cedula: String,
        edad: String,
        numero: String,
        sexo: String
    ): Result<Unit> {
        return try {
            val updates = mapOf(
                "cedula" to cedula,
                "edad" to edad,
                "numero" to numero,
                "sexo" to sexo,
            )
            firestore.collection("users").document(uid).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}


