package com.jesus.gymcontrol.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.domain.model.GymUserSummary
import com.jesus.gymcontrol.domain.model.ListUser
import com.jesus.gymcontrol.domain.model.UserUpdate
import com.jesus.gymcontrol.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
	private val firestore: FirebaseFirestore,
	private val auth: FirebaseAuth
) : UserRepository {
	
	override suspend fun assignGymToUser(uid: String, gym: Gym, rol: String): Result<Unit> {
		return try {
			// 1. Obtener datos básicos del usuario
			val userSnapshot = firestore.collection("users").document(uid).get().await()
			val userName = userSnapshot.getString("name") ?: "Desconocido"
			val email = userSnapshot.getString("email") ?: "Desconocido"
			val idcard = userSnapshot.getString("idcard") ?: "Desconocido"
			val date = userSnapshot.getLong("date")
			
			// 2. Datos para guardar en users/{uid}/gimnasios/{uid} (o podrías usar gym.code como ID si prefieres)
			val userGymData = mapOf(
				"code" to gym.code,
				"name" to gym.name,
				"direction" to gym.direction,
				"phone" to gym.phone,
				"state" to "activo",
				"date" to date
			)
			
			// 3. Datos para guardar en gimnasios/{gym.code}/usuarios/{uid}
			val gymUserData = mapOf(
				"uid" to uid,
				"name" to userName,
				"rol" to rol,
				"email" to email,
				"idcard" to idcard,
				"state" to "inactivo",           // ⬅️ NUEVO
				"enable" to false,            // ⬅️ NUEVO
				"lastpayment" to null,
				"date" to date ,               // ⬅️ NUEVO
			)
			
			// 4. Referencias principales
			val userGymRef = firestore.collection("users")
				.document(uid)
				.collection("gimnasios")
				.document(uid) // o usa gym.code si prefieres
			
			val gymUserRef = firestore.collection("gimnasios")
				.document(gym.code)
				.collection("usuarios")
				.document(uid)
			
			val userRef = firestore.collection("users").document(uid)
			
			// 5. Subcolección de pagos personalizada del usuario
			val pagosRef = userGymRef.collection("pagos").document("pagos")
			
			// 6. Subcolecciones compartidas del gimnasio
			val membresiasRef = firestore.collection("gimnasios")
				.document(gym.code)
				.collection("membresias")
				.document("basica")
			
			val promocionesRef = firestore.collection("gimnasios")
				.document(gym.code)
				.collection("promociones")
				.document("inicial")
			
			val mensajesRef = firestore.collection("gimnasios")
				.document(gym.code)
				.collection("mensajes")
				.document("bienvenida")
			
			// 7. Transacción en lote
			val batch = firestore.batch()
			
			// Inscripción del usuario al gimnasio
			batch.set(userGymRef, userGymData)
			
			// Registro del usuario dentro del gimnasio (con nuevos campos)
			batch.set(gymUserRef, gymUserData)
			
			// Actualizar información del usuario en su documento raíz
			batch.update(
				userRef, mapOf(
					"rol" to rol,
					"gimnasio" to gym.name,
					"gimnasioCode" to gym.code
				)
			)
			
			// Crear subcolección de pagos (personal del usuario)
			batch.set(
				pagosRef, mapOf(
					"mensaje" to "No hay pagos registrados aún"
				)
			)
			
			// Crear membresía inicial
			batch.set(
				membresiasRef, mapOf(
					"name" to "Membresía Básica",
					"description" to "Plan de acceso básico",
					"price" to 0,
					"state" to "activa"
				)
			)
			
			
			// Crear promoción inicial
			batch.set(
				promocionesRef, mapOf(
					"title" to "Sin promociones",
					"description" to "Aún no hay promociones activas"
				)
			)
			
			// Crear mensaje de bienvenida
			batch.set(
				mensajesRef, mapOf(
					"content" to "¡Bienvenido al gimnasio ${gym.name}!",
					"date" to FieldValue.serverTimestamp()
				)
			)
			
			// Ejecutar la transacción
			batch.commit().await()
			
			Result.success(Unit)
			
		} catch (e: Exception) {
			Result.failure(e)
		}
	}
	
	override suspend fun getGymUserSummary(gymCode: String): Result<GymUserSummary> {
		return try {
			val usuariosSnapshot = firestore
				.collection("gimnasios")
				.document(gymCode)
				.collection("usuarios")
				.get()
				.await()
			
			var total = 0
			var activos = 0
			var inactivos = 0
			
			for (doc in usuariosSnapshot.documents) {
				total++
				
				val estado = doc.getString("state") ?: "inactivo"
				
				when (estado.lowercase()) {
					"activo" -> activos++
					"inactivo", "pendiente" -> inactivos++
				}
			}
			
			val summary = GymUserSummary(
				total = total,
				activos = activos,
				inactivos = inactivos
			)
			
			Result.success(summary)
			
		} catch (e: Exception) {
			Result.failure(e)
		}
	}
	
	
	override suspend fun getUserByGym(gymCode: String): Result<List<ListUser>> = try {
		val snapshot = firestore.collection("gimnasios")
			.document(gymCode)
			.collection("usuarios")
			.orderBy("date", Query.Direction.DESCENDING)
			.get()
			.await()
		
		val listUsers = snapshot.documents.map { doc ->
			val data = doc.data ?: emptyMap<String, Any>()
			ListUser(
				id = doc.id,
				name = data["name"] as? String ?: "",
				idcard = data["idcard"] as? String ?: "",
				phone = data["phone"] as? String ?: "",
				email = data["email"] as? String ?: "",
				state = data["state"] as? String ?: "",
				rol = data["rol"] as? String ?: "",
				enabled = data["isActive"] as? Boolean ?: false,
				date = when (val d = data["date"]) {
					is Long -> d
					is Double -> d.toLong()
					else -> null
				}
			)
		}
		
		Result.success(listUsers)
	} catch (e: Exception) {
		Result.failure(e)
	}
	
	override suspend fun updateUserProfile(
		uid: String,
		gymCode: String,
		userUpdate: UserUpdate
	): Result<Unit> {
		return try {
			// No actualizamos el email en FirebaseAuth aquí
			
			// Actualizar Firestore: users/{uid}
			val userMap = mapOf(
				"name" to userUpdate.name,
				"phone" to userUpdate.phone,
				"age" to userUpdate.age,
				"gender" to userUpdate.gender,
				"weight" to userUpdate.weight,
				"height" to userUpdate.height
			)
			
			firestore.collection("users")
				.document(uid)
				.update(userMap)
				.await()
			
			// Actualizar en gimnasios/{gymCode}/usuarios/{uid}
			val gymUserMap = mapOf(
				"name" to userUpdate.name,
				"phone" to userUpdate.phone
			)
			
			firestore.collection("gimnasios")
				.document(gymCode)
				.collection("usuarios")
				.document(uid)
				.update(gymUserMap)
				.await()
			
			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}
	
	
}

