package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.model.ListUser
import com.jesus.gymcontrol.domain.repository.UserRepository



class SearchUserUseCase(
	private val repository: UserRepository
) {
	suspend operator fun invoke(gymCode: String, query: String): Result<List<ListUser>> {
		return repository.searchUserByIdCard(gymCode, query)
	}
}
