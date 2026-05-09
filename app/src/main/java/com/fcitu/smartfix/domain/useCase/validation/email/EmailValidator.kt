package com.fcitu.smartfix.domain.useCase.validation.email

class EmailValidator {
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    fun isValid(email: String): Boolean {
        return email.isNotBlank() && emailRegex.matches(email)
    }
}