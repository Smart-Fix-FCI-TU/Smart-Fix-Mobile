package com.fcitu.smartfix.domain.useCase

import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.exception.InvalidEmailException
import com.fcitu.smartfix.domain.exception.InvalidPasswordException
import com.fcitu.smartfix.domain.repository.AuthRepository
import com.fcitu.smartfix.domain.useCase.validation.email.EmailValidator
import com.fcitu.smartfix.domain.useCase.validation.password.PasswordValidator

class LoginUseCase(
    private val authenticationRepository: AuthRepository,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator
) {
    suspend fun login(email: String, password: String): User {
        if (!isPasswordValid(password)) throw InvalidPasswordException()
        if (!isEmailValid(email)) throw InvalidEmailException(email)
        return authenticationRepository.login(
            email = email,
            password = password
        )
    }

    fun isEmailValid(email: String): Boolean {
        return emailValidator.isValid(email = email)
    }

    fun isPasswordValid(password: String) = passwordValidator.isValid(password)
}
