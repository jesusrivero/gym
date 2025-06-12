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

    override suspend fun validateOwnerCode(code: String): Result<Boolean> {
        return try {
            val snapshot = firestore.collection("codigos")
                .whereEqualTo("codigo", code)
                .limit(1)
                .get()
                .await()

            val doc = snapshot.documents.firstOrNull()

            val isValid = doc != null &&
                    (doc.getString("rol") == "dueño") &&
                    !(doc.getBoolean("usado") ?: false)

            Result.success(isValid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun validateClientCode(code: String, gymCode: String): Result<Boolean> {
        return try {
            val snapshot = firestore.collection("codigos")
                .whereEqualTo("codigo", code)
                .limit(1)
                .get()
                .await()

            val doc = snapshot.documents.firstOrNull()

            val actualCode = doc?.getString("codigo") ?: ""
            val codeParts = actualCode.split("-")

            val isValid = doc != null &&
                    (doc.getString("rol").isNullOrBlank()) &&
                    !(doc.getBoolean("usado") ?: false) &&
                    codeParts.firstOrNull() == gymCode

            Result.success(isValid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun markCodeAsUsed(code: String, rol: String?): Result<Unit> {
        return try {
            val snapshot = firestore.collection("codigos")
                .whereEqualTo("codigo", code)
                .limit(1)
                .get()
                .await()

            val document = snapshot.documents.firstOrNull()
            val docRef = document?.reference

            if (docRef != null) {
                val updates = mutableMapOf<String, Any>(
                    "usado" to true
                )

                rol?.let {
                    updates["rol"] = it
                }

                docRef.update(updates).await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Código no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun generateCode(gymCode: String): Result<String> {
        return try {

            val suffix = generateRandomCode(6)


            val fullCode = "$gymCode-$suffix"


            val data = mapOf(
                "codigo" to fullCode,
                "rol" to "",
                "usado" to false,
                "gimnasioCode" to gymCode,
                "fechaCreacion" to FieldValue.serverTimestamp()
            )


            firestore.collection("codigos")
                .add(data)
                .await()


            Result.success(fullCode)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun generateRandomCode(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    override suspend fun getGymByOwnerUid(uid: String): Result<Gym> {
        return try {
            val snapshot = firestore.collection("gimnasios")
                .whereEqualTo("owner", uid)
                .limit(1)
                .get()
                .await()

            val gym = snapshot.documents.firstOrNull()?.toObject(Gym::class.java)
            if (gym != null) {
                Result.success(gym)
            } else {
                Result.failure(Exception("Gimnasio no encontrado para este dueño"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}






