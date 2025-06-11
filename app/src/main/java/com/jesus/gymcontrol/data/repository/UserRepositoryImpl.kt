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
                "code" to gym.code,
                "name" to gym.name,
                "direction" to gym.direction,
                "phone" to gym.phone,
                "state" to "activo",
                "registrationDate" to System.currentTimeMillis()
            )

            val userSnapshot = firestore.collection("users").document(uid).get().await()
            val nameUser= userSnapshot.getString("name") ?: "Desconocido"

            val gymUserData = mapOf(
                "uid" to uid,
                "nname" to nameUser,
                "rol" to "cliente",
                "registrationDate" to FieldValue.serverTimestamp()
            )

            val userGymRef = firestore
                .collection("users")
                .document(uid)
                .collection("gimnasios")
                .document(gym.name)

            val gymUserRef = firestore
                .collection("gimnasios")
                .document(gym.code)
                .collection("usuarios")
                .document(uid)

            val userRef = firestore.collection("users").document(uid)

            val batch = firestore.batch()

            batch.set(userGymRef, userGymData)
            batch.set(gymUserRef, gymUserData)

            batch.update(userRef, mapOf(
                "rol" to "cliente",
                "gimnasio" to gym.name
            ))

            batch.commit().await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}