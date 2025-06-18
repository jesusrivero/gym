package com.jesus.gymcontrol.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.domain.model.Pago
import com.jesus.gymcontrol.domain.repository.PagoRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PagoRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PagoRepository {

    override suspend fun addPago(pago: Pago): Result<Unit> = try {
        val pagoMap = mapOf(
            "id" to pago.id,
            "userId" to pago.userId,
            "userName" to pago.userName,
            "userCedula" to pago.userCedula,
            "membershipId" to pago.membershipId,
            "membershipName" to pago.membershipName,
            "tipoPago" to pago.tipoPago,
            "monto" to pago.monto,
            "descripcion" to pago.descripcion,
            "referencia" to pago.referencia,
            "fecha" to pago.fecha,
            "gimnasioCode" to pago.gimnasioCode
        )

        val gymRef = firestore.collection("gimnasios").document(pago.gimnasioCode)

        val pagoRef = gymRef.collection("pagos").document(pago.id)
        val userPagoRef = firestore.collection("users")
            .document(pago.userId)
            .collection("gimnasios")
            .document(pago.gimnasioCode)
            .collection("pagos")
            .document(pago.id)

        val membresiaUserRef = gymRef
            .collection("usuarios")
            .document(pago.membershipId)
            .collection("personas")
            .document(pago.userId)

        val userRef = firestore.collection("users").document(pago.userId)

        firestore.runBatch { batch ->
            batch.set(pagoRef, pagoMap)
            batch.set(userPagoRef, pagoMap)
            batch.set(membresiaUserRef, mapOf(
                "nombre" to pago.userName,
                "cedula" to pago.userCedula,
                "fechaPago" to pago.fecha
            ))
            batch.update(userRef, "activo", true)
        }.await()

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}