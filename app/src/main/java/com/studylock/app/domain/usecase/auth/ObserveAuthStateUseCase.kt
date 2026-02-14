package com.studylock.app.domain.usecase.auth

import com.studylock.app.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ObserveAuthStateUseCase @Inject constructor() {
    operator fun invoke(): Flow<User?> {
        // TODO: Implement actual Firebase Auth observation
        return flowOf(null)
    }
}
