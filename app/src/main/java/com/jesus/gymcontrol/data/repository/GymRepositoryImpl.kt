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
                "admin" to admin,
                "entrenador" to entrenador,
                "direccion" to direccion,
                "telefono" to telefono,
                "codigo" to codigo,
                "fechaCreacion" to FieldValue.serverTimestamp()
            )

            val userGymRef = firestore
                .collection("users")
                .document(uid)
                .collection("gimnasio")
                .document("gimnasio")

            val globalGymRef = firestore
                .collection("gimnasios")
                .document(codigo)

            val batch = firestore.batch()

            batch.set(userGymRef, gymData)
            batch.set(globalGymRef, gymData)

            // Subcolecciones iniciales con documentos de placeholder
            val adminRef = globalGymRef.collection("administradores").document("_placeholder")
            val entrenadorRef = globalGymRef.collection("entrenadores").document("_placeholder")
            val clientesRef = globalGymRef.collection("clientes").document("_placeholder")

            val placeholderData = mapOf("init" to true)

            batch.set(adminRef, placeholderData)
            batch.set(entrenadorRef, placeholderData)
            batch.set(clientesRef, placeholderData)

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



