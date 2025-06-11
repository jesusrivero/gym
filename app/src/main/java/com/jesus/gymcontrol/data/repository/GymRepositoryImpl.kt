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
        codigo: String,
        nombre: String,
        admin: String,
        entrenador: String,
        direccion: String,
        telefono: String
    ): Result<Unit> {
        return try {
            val gymData = mapOf(
                "propietario" to uid,
                "nombre" to nombre,
                "entrenador" to entrenador,
                "admin" to admin,
                "direccion" to direccion,
                "telefono" to telefono,
                "codigo" to codigo,
                "fechaCreacion" to FieldValue.serverTimestamp()
            )


            val globalGymRef = firestore
                .collection("gimnasios")
                .document(codigo)

// AQUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
//            val userGymRef = firestore
//                .collection("users")
//                .document(uid)
//                .collection("gimnasios")
//                .document(codigo)

            val batch = firestore.batch()

            batch.set(globalGymRef, gymData)
//            batch.set(userGymRef, gymData)


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
}






