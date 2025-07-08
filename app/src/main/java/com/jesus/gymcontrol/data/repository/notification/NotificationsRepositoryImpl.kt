package com.jesus.gymcontrol.data.repository.notification


import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.notification.Notificacion
import com.jesus.gymcontrol.domain.repository.notification.NotificacionRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class NotificacionRepositoryImpl @Inject constructor(
	private val firestore: FirebaseFirestore,
	private val sessionManager: SessionManager,
) : NotificacionRepository {
	
	override suspend fun agregarNotificacion(gymCode: String, notificacion: Notificacion) {
		firestore.collection("gimnasios")
			.document(gymCode)
			.collection("notificaciones")
			.add(notificacion)
			.await()
	}
	
	override fun obtenerNotificaciones(gymCode: String): Flow<List<Notificacion>> = callbackFlow {
		val ref = firestore.collection("gimnasios")
			.document(gymCode)
			.collection("notificaciones")
			.orderBy("fecha", Query.Direction.DESCENDING)
		
		val listener = ref.addSnapshotListener { snapshot, error ->
			if (error != null) return@addSnapshotListener
			val notificaciones = snapshot?.documents?.mapNotNull {
				it.toObject(Notificacion::class.java)?.copy(id = it.id)
			} ?: emptyList()
			trySend(notificaciones)
		}
		
		awaitClose { listener.remove() }
	}
	
	override suspend fun eliminarTodasNotificaciones(gymCode: String) {
		val ref = firestore.collection("gimnasios")
			.document(gymCode)
			.collection("notificaciones")
		
		val snapshot = ref.get().await()
		snapshot.documents.forEach { it.reference.delete() }
	}
	
	override suspend fun eliminarNotificacionesAntiguas(gymCode: String, max: Int) {
		val ref = firestore.collection("gimnasios")
			.document(gymCode)
			.collection("notificaciones")
			.orderBy("fecha", Query.Direction.DESCENDING)
		
		val snapshot = ref.get().await()
		val notificaciones = snapshot.documents
		
		val toDelete = if (max == 0) notificaciones else notificaciones.drop(max)
		
		toDelete.forEach {
			it.reference.delete()
		}
	}
	
	override suspend fun marcarTodasComoLeidas(gymCode: String) {
		val ref = firestore.collection("gimnasios")
			.document(gymCode)
			.collection("notificaciones")
		
		val snapshot = ref.whereEqualTo("leido", false).get().await()
		snapshot.documents.forEach {
			it.reference.update("leido", true)
		}
	}
	
	override suspend fun marcarComoLeida(gymId: String, notificacionId: String) {
		firestore.collection("gimnasios")
			.document(gymId)
			.collection("notificaciones")
			.document(notificacionId)
			.update("leido", true)
			.await()
	}
}