package com.fcitu.smartfix.domain.useCase.validation.password

class PasswordValidator {
    fun isValid(password: String): Boolean {
        return password.length >= MIN_PASSWORD_LENGTH
    }

    private companion object {
        const val MIN_PASSWORD_LENGTH = 8
    }
}