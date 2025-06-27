package com.jesus.gymcontrol.data.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.domain.model.PasswordChangeRequest
import com.jesus.gymcontrol.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await


class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun registerUser(name: String, email: String, password: String, idcard:String): Result<Unit> {
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
                "idcard" to idcard,
                "rol" to "",
                "code" to "",
                "age" to "",
                "phone" to "",
                "gender" to "",
                "state" to "",
                "enabled" to "",
                "lastpayment" to "",
                "membership" to ""

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
        code: String
    ): Result<Unit> {
       return try {
           val updates = mapOf(
               "rol" to rol,
               "code" to code,
           )
           firestore.collection("users").document(uid).update(updates).await()
           Result.success(Unit)
       } catch (e: Exception) {
           Result.failure(e)
       }
    }



    override suspend fun updateDatesUser(
        uid: String,
        idcard: String,
        age: String,
        phone: String,
        gender: String
    ): Result<Unit> {
        return try {
            val updates = mapOf(
                "idcard" to idcard,
                "age" to age,
                "phone" to phone,
                "gender" to gender,
            )
            firestore.collection("users").document(uid).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
	
	override suspend fun changePassword(request: PasswordChangeRequest): Result<Unit> {
		return try {
			val currentUser = firebaseAuth.currentUser
				?: return Result.failure(Exception("Usuario no autenticado"))
			
			val email = currentUser.email
				?: return Result.failure(Exception("No se encontró el correo electrónico del usuario"))
			
			// Validación de nueva contraseña
			if (request.newPassword != request.confirmPassword) {
				return Result.failure(Exception("La nueva contraseña y la confirmación no coinciden"))
			}
			
			// 1. Re-autenticación
			val credential = EmailAuthProvider.getCredential(email, request.currentPassword)
			currentUser.reauthenticate(credential).await()
			
			// 2. Cambiar la contraseña
			currentUser.updatePassword(request.newPassword).await()
			
			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}
	
	
	
}


