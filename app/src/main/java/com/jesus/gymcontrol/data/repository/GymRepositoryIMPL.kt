package com.jesus.gymcontrol.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.domain.repository.GymRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GymRepositoryIMPL @Inject constructor(
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
            val data = mapOf(
                "nombre" to nombre,
                "admin" to admin,
                "entrenador" to entrenador,
                "direccion" to direccion,
                "telefono" to telefono,
                "codigo" to codigo
            )

            firestore
                .collection("usuarios")
                .document(uid)
                .collection("gimnasio")
                .document(codigo)
                .set(data)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}