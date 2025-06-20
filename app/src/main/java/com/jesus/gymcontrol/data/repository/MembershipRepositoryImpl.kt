package com.jesus.gymcontrol.data.repository


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.domain.model.Membership
import com.jesus.gymcontrol.domain.model.MembershipWithCount
import com.jesus.gymcontrol.domain.repository.MembershipRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MembershipRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth // <-- Inyectamos FirebaseAuth
) : MembershipRepository {

    override suspend fun createMembership(membership: Membership): Result<Unit> = try {
        val uid = auth.currentUser?.uid
            ?: return Result.failure(Exception("Usuario no autenticado"))

        // Obtener el gimnasioCode desde el usuario autenticado
        val userDoc = firestore.collection("users").document(uid).get().await()
        val gymCode = userDoc.getString("gimnasioCode")
            ?: return Result.failure(Exception("No se encontró el gimnasioCode del usuario"))

        val id = firestore.collection("gimnasios")
            .document(gymCode)
            .collection("membresias")
            .document().id

        val newMembership = membership.copy(id = id, gimnasioCode = gymCode)

        firestore.collection("gimnasios")
            .document(gymCode)
            .collection("membresias")
            .document(id)
            .set(newMembership)
            .await()

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getMemberships(): Result<List<Membership>> = try {
        val uid = auth.currentUser?.uid ?: return Result.failure(Exception("No autenticado"))
        val userDoc = firestore.collection("users").document(uid).get().await()
        val gymCode = userDoc.getString("gimnasioCode")
            ?: return Result.failure(Exception("Sin gimnasioCode"))

        val snapshot = firestore.collection("gimnasios")
            .document(gymCode)
            .collection("membresias")
            .get()
            .await()

        val memberships = snapshot.documents.mapNotNull { it.toObject(Membership::class.java) }
        Result.success(memberships)
    } catch (e: Exception) {
        Result.failure(e)
    }


    override suspend fun deleteMembershipIfNoUsers(membership: Membership): Result<Unit> {
        return try {
            val usersSnapshot = firestore.collection("gimnasios")
                .document(membership.gimnasioCode)
                .collection("membresias")
                .document(membership.id)
                .collection("usuarios")
                .get()
                .await()

            if (!usersSnapshot.isEmpty) {
                return Result.failure(Exception("No se puede eliminar: tiene usuarios inscritos"))
            }

            firestore.collection("gimnasios")
                .document(membership.gimnasioCode)
                .collection("membresias")
                .document(membership.id)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMembershipsWithUserCount(): Result<List<MembershipWithCount>> = try {
        val uid = auth.currentUser?.uid
            ?: return Result.failure(Exception("Usuario no autenticado"))

        val userDoc = firestore.collection("users").document(uid).get().await()
        val gymCode = userDoc.getString("gimnasioCode")
            ?: return Result.failure(Exception("No se encontró el gimnasioCode del usuario"))

        val membershipsSnapshot = firestore.collection("gimnasios")
            .document(gymCode)
            .collection("membresias")
            .get()
            .await()

        val membershipsWithCount = membershipsSnapshot.documents.map { doc ->
            val membership = doc.toObject(Membership::class.java)!!.copy(id = doc.id)

            val usersCount = firestore.collection("gimnasios")
                .document(gymCode)
                .collection("membresias")
                .document(doc.id)
                .collection("usuarios")
                .get()
                .await()
                .size()

            MembershipWithCount(membership, usersCount)
        }

        Result.success(membershipsWithCount)
    } catch (e: Exception) {
        Result.failure(e)
    }
	
	override suspend fun updateMembership(membership: Membership): Result<Unit> {
		return try {
			val gymCode = membership.gimnasioCode
			val id = membership.id
			
			if (gymCode.isBlank() || id.isBlank()) {
				return Result.failure(Exception("Datos incompletos para editar la membresía"))
			}
			
			firestore.collection("gimnasios")
				.document(gymCode)
				.collection("membresias")
				.document(id)
				.update(
					mapOf(
						"nombre" to membership.nombre,
						"precio" to membership.precio,
						"duracionDias" to membership.duracionDias
					)
				)
				.await()
			
			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}
}
