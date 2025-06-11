package com.jesus.gymcontrol.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.domain.repository.GymRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class GymRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : GymRepository {

    override suspend fun createGymForUser(
        uid: String,
        code: String,
        name: String,
        direction: String,
        phone: String
    ): Result<Unit> {
        return try {
            val gymData = mapOf(
                "owner" to uid,
                "name" to name,
                "direction" to direction,
                "phone" to phone,
                "code" to code,
                "creationDate" to FieldValue.serverTimestamp()
            )


            val globalGymRef = firestore
                .collection("gimnasios")
                .document(code)

            val batch = firestore.batch()

            batch.set(globalGymRef, gymData)

            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllGyms(): Result<List<Gym>> {
        return try {
            val snapshot = firestore
                .collection("gimnasios")
                .get()
                .await()

            val gyms = snapshot.documents.mapNotNull { it.toObject(Gym::class.java) }
            Result.success(gyms)
        } catch (e: Exception) {
            Result.failure(e)
        }


    }

    override suspend fun validateGymCode(code: String): Result<Boolean> {
        return try {
            val querySnapshot = firestore.collection("codigos")
                .whereEqualTo("codigo", code)
                .limit(1)
                .get()
                .await()

            val doc = querySnapshot.documents.firstOrNull()
            val isValid = doc != null && !(doc.getBoolean("usado") ?: false)
            Result.success(isValid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markCodeAsUsed(code: String): Result<Unit> {
        return try {
            val querySnapshot = firestore.collection("codigos")
                .whereEqualTo("codigo", code)
                .limit(1)
                .get()
                .await()

            val docRef = querySnapshot.documents.firstOrNull()?.reference
            if (docRef != null) {
                docRef.update("usado", true).await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Código no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}






