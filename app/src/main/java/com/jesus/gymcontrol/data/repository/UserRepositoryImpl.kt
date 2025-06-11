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

    override suspend fun assignGymToUser(uid: String, gym: Gym): Result<Unit> {
        return try {
            val userGymData = mapOf(
                "codigo" to gym.codigo,
                "nombre" to gym.nombre,
                "direccion" to gym.direccion,
                "telefono" to gym.telefono,
                "estado" to "activo",
                "fechaRegistro" to System.currentTimeMillis()
            )

            val gymUserData = mapOf(
                "uid" to uid,
                "rol" to "cliente",
                "fechaIngreso" to FieldValue.serverTimestamp()
            )

            val userGymRef = firestore
                .collection("users")
                .document(uid)
                .collection("gimnasios")
                .document(gym.codigo)

            val gymUserRef = firestore
                .collection("gimnasios")
                .document(gym.codigo)
                .collection("usuarios")
                .document(uid)

            val userRef = firestore.collection("users").document(uid)

            val batch = firestore.batch()

            batch.set(userGymRef, userGymData)
            batch.set(gymUserRef, gymUserData)

            batch.update(userRef, mapOf(
                "rol" to "cliente",
                "gimnasio" to gym.nombre
            ))

            batch.commit().await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}