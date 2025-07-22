package com.jesus.gymcontrol.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.domain.model.UserRegistrationData
import com.jesus.gymcontrol.domain.model.getAdmin.AdminSummary
import com.jesus.gymcontrol.domain.repository.UserAdminRepository
import com.jesus.gymcontrol.infraestructure.di.SecondaryFirebase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject


class UserAdminRepositoryImpl @Inject constructor(
	private val firestore: FirebaseFirestore,
	private val context: Context,
) : UserAdminRepository {
	
//	override suspend fun registerUserFromAdmin(userData: UserRegistrationData): Result<Unit> =
//		withContext(Dispatchers.IO) {
//			try {
//				val code = userData.code
//
//				// Validar código antes de crear el usuario
//				val codigoSnapshot = firestore.collection("codigos")
//					.whereEqualTo("code", code)
//					.limit(1)
//					.get()
//					.await()
//
//				val codigoDocument = codigoSnapshot.documents.firstOrNull()
//					?: return@withContext Result.failure(Exception("El código '$code' no existe."))
//
//				val yaUsado = codigoDocument.getBoolean("used") == true
//				if (yaUsado) return@withContext Result.failure(Exception("El código '$code' ya fue usado."))
//
//				// UID del administrador actual
//				val adminUid = FirebaseAuth.getInstance().currentUser?.uid
//					?: return@withContext Result.failure(Exception("No se pudo obtener UID del administrador"))
//
//				// Obtener gimnasioCode real desde el documento del admin
//				val adminDoc = firestore.collection("users").document(adminUid).get().await()
//				val gymCode = adminDoc.getString("gimnasioCode")
//					?: return@withContext Result.failure(Exception("El administrador no tiene gimnasioCode registrado"))
//				val gymName = adminDoc.getString("gimnasio") ?: "Gimnasio"
//
//				// Crear usuario con instancia secundaria
//				val secondaryAuth = SecondaryFirebase.getSecondaryAuth(context)
//				val result =
//					secondaryAuth.createUserWithEmailAndPassword(userData.email, userData.password).await()
//				val uid = result.user?.uid
//					?: return@withContext Result.failure(Exception("No se pudo obtener UID del nuevo usuario"))
//
//				// Crear documento principal del usuario
//				val userMap = mapOf(
//					"uid" to uid,
//					"email" to userData.email,
//					"name" to userData.name,
//					"lastname" to userData.lastname,
//					"idcard" to userData.idCard,
//					"gender" to userData.gender,
//					"age" to userData.age,
//					"membership" to userData.membership,
//					"code" to userData.code,
//					"gimnasioCode" to gymCode,
//					"rol" to userData.rol,
//					"state" to "activo",
//					"phone" to userData.phone,
//					"date" to userData.date,
//				)
//
//				firestore.collection("users").document(uid).set(userMap).await()
//
//				// Subcolección del gimnasio del usuario
//				firestore.collection("users").document(uid)
//					.collection("gimnasios").document(gymCode).set(
//						mapOf(
//							"code" to gymCode,
//							"name" to gymName,
//							"state" to "inactivo"       //ESTADO DEL GIMNASIO
//						)
//					).await()
//
//				// Agregar usuario en la colección de usuarios del gimnasio
//				firestore.collection("gimnasios").document(gymCode)
//					.collection("usuarios").document(uid).set(
//						mapOf(
//							"uid" to uid,
//							"name" to userData.name,
//							"lastname" to userData.lastname,
//							"rol" to userData.rol,
//							"state" to "inactivo",
//							"email" to userData.email,
//							"phone" to userData.phone,
//							"idcard" to userData.idCard,
//							"date" to userData.date
//						)
//					).await()
//
//				// Marcar código como usado
//				codigoDocument.reference.update("used", true).await()
//
//				// Cerrar sesión secundaria
//				secondaryAuth.signOut()
//
//				Result.success(Unit)
//
//			} catch (e: Exception) {
//				Result.failure(e)
//			}
//		}
	
	override suspend fun registerUserFromAdmin(userData: UserRegistrationData): Result<Unit> =
		withContext(Dispatchers.IO) {
			try {
				val code = userData.code
				
				// Validar código antes de crear el usuario
				val codigoSnapshot = firestore.collection("codigos")
					.whereEqualTo("code", code)
					.limit(1)
					.get()
					.await()
				
				val codigoDocument = codigoSnapshot.documents.firstOrNull()
					?: return@withContext Result.failure(Exception("El código '$code' no existe."))
				
				val yaUsado = codigoDocument.getBoolean("used") == true
				if (yaUsado) return@withContext Result.failure(Exception("El código '$code' ya fue usado."))
				
				// UID del administrador actual
				val adminUid = FirebaseAuth.getInstance().currentUser?.uid
					?: return@withContext Result.failure(Exception("No se pudo obtener UID del administrador"))
				
				// Obtener gimnasioCode real desde el documento del admin
				val adminDoc = firestore.collection("users").document(adminUid).get().await()
				val gymCode = adminDoc.getString("gimnasioCode")
					?: return@withContext Result.failure(Exception("El administrador no tiene gimnasioCode registrado"))
				val gymName = adminDoc.getString("gimnasio") ?: "Gimnasio"
				
				val uid: String
				
				if (userData.rol == "administrador") {
					// Crear usuario con instancia secundaria en FirebaseAuth
					val secondaryAuth = SecondaryFirebase.getSecondaryAuth(context)
					val result =
						secondaryAuth.createUserWithEmailAndPassword(userData.email, userData.password).await()
					uid = result.user?.uid
						?: return@withContext Result.failure(Exception("No se pudo obtener UID del nuevo usuario"))
					
					// Cerrar sesión secundaria
					secondaryAuth.signOut()
				} else {
					// Generar UID para cliente
					uid = UUID.randomUUID().toString()
				}
				
				// Crear documento principal del usuario
				val userMap = mapOf(
					"uid" to uid,
					"email" to userData.email,
					"name" to userData.name,
					"lastname" to userData.lastname,
					"idcard" to userData.idCard,
					"gender" to userData.gender,
					"age" to userData.age,
					"membership" to userData.membership,
					"code" to userData.code,
					"gimnasioCode" to gymCode,
					"rol" to userData.rol,
					"state" to "activo",
					"phone" to userData.phone,
					"date" to userData.date,
				)
				
				firestore.collection("users").document(uid).set(userMap).await()
				
				// Subcolección del gimnasio del usuario
				firestore.collection("users").document(uid)
					.collection("gimnasios").document(gymCode).set(
						mapOf(
							"code" to gymCode,
							"name" to gymName,
							"state" to "inactivo"
						)
					).await()
				
				// Agregar usuario en la colección de usuarios del gimnasio
				firestore.collection("gimnasios").document(gymCode)
					.collection("usuarios").document(uid).set(
						mapOf(
							"uid" to uid,
							"name" to userData.name,
							"lastname" to userData.lastname,
							"rol" to userData.rol,
							"state" to "inactivo",
							"email" to userData.email,
							"phone" to userData.phone,
							"idcard" to userData.idCard,
							"date" to userData.date
						)
					).await()
				
				// Marcar código como usado
				codigoDocument.reference.update("used", true).await()
				
				Result.success(Unit)
				
			} catch (e: Exception) {
				Result.failure(e)
			}
		}
	
	
	override suspend fun getAdministradoresByGym(gymCode: String): Result<List<AdminSummary>> =
		withContext(Dispatchers.IO) {
			try {
				val snapshot = firestore
					.collection("gimnasios")
					.document(gymCode)
					.collection("usuarios")
					.whereEqualTo("rol", "administrador")
					.get()
					.await()
				
				val lista = snapshot.documents.map { doc ->
					AdminSummary(
						uid = doc.getString("uid") ?: "",
						name = doc.getString("name") ?: "",
						lastname = doc.getString("lastname") ?: "",
						email = doc.getString("email") ?: "",
						state = doc.getString("state") ?: "inactivo"
					)
				}
				
				Result.success(lista)
			} catch (e: Exception) {
				Result.failure(e)
			}
		}
	
	
	override suspend fun updateAdminState(gymCode: String, adminUid: String, newState: String): Result<Unit> {
		return try {
			val batch = firestore.batch()
			
			val gymUserRef = firestore.collection("gimnasios")
				.document(gymCode)
				.collection("usuarios")
				.document(adminUid)
			
			val userRef = firestore.collection("users")
				.document(adminUid)
			
			batch.update(gymUserRef, "state", newState)
			batch.update(userRef, "state", newState)
			
			batch.commit().await()
			
			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}
	
	
}

