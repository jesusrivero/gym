//package com.jesus.gymcontrol.domain.repository.sqlLight
//
//class PersonasLocalCache(private val queries: PersonasQueries) {
//
//	suspend fun insertPersona(persona: Persona) {
//		queries.insertPersona(
//			persona.id,
//			persona.nombre,
//			persona.cedula,
//			persona.correo,
//			persona.telefono,
//			persona.estado
//		)
//	}
//
//	suspend fun getAllPersonas(): List<Persona> {
//		return queries.selectAll().executeAsList()
//	}
//
//	suspend fun updatePersona(persona: Persona) {
//		queries.updatePersona(
//			persona.nombre,
//			persona.cedula,
//			persona.correo,
//			persona.telefono,
//			persona.estado,
//			persona.id
//		)
//	}
//
//	suspend fun deletePersona(personaId: String) {
//		queries.deletePersona(personaId)
//	}
//}