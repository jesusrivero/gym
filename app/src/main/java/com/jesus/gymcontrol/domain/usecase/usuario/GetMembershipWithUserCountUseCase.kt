package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.model.MembershipWithCount
import com.jesus.gymcontrol.domain.repository.MembershipRepository
import javax.inject.Inject

class GetMembershipsWithUserCountUseCase @Inject constructor(
    private val repository: MembershipRepository
) {
    suspend operator fun invoke(): Result<List<MembershipWithCount>> {
        return repository.getMembershipsWithUserCount()
    }
}