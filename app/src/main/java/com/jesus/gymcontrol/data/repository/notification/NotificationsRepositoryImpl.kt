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
	private val sessionManager: SessionManager
) : NotificacionRepository {
	
	override suspend fun agregarNotificacion(gymId: String, notificacion: Notificacion) {
		firestore.collection("gimnasios")
			.document(gymId)
			.collection("notificaciones")
			.add(notificacion)
			.await()
	}
	
	override fun obtenerNotificaciones(gymId: String): Flow<List<Notificacion>> = callbackFlow {
		val ref = firestore.collection("gimnasios")
			.document(gymId)
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
	
	override suspend fun eliminarNotificacion(gymId: String, notificacionId: String) {
		firestore.collection("gimnasios")
			.document(gymId)
			.collection("notificaciones")
			.document(notificacionId)
			.delete()
			.await()
	}
	
	override suspend fun eliminarNotificacionesAntiguas(gymId: String, max: Int) {
		val ref = firestore.collection("gimnasios")
			.document(gymId)
			.collection("notificaciones")
			.orderBy("fecha", Query.Direction.DESCENDING)
		
		val snapshot = ref.get().await()
		val notificaciones = snapshot.documents
		
		if (notificaciones.size > max) {
			val excedentes = notificaciones.drop(max)
			excedentes.forEach {
				it.reference.delete()
			}
			}
		}
}