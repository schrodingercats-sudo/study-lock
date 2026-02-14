package com.studylock.app.domain.usecase.auth

import com.studylock.app.domain.model.User
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor() {
    operator fun invoke(): User? {
        // TODO: Implement actual Firebase Auth logic
        return null
    }
}
