package com.jesus.gymcontrol.domain.usecase.usuario.codes

import com.jesus.gymcontrol.domain.repository.GymRepository
import javax.inject.Inject


class GetAvailableCodesUseCase @Inject constructor(
	private val repository: GymRepository
) {
	suspend operator fun invoke(): Result<List<String>> {
		return repository.getAvailableCodes()
		}
}