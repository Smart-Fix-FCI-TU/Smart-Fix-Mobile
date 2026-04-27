package com.fcitu.smartfix.domain.useCase

import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.exception.InvalidMobileNumberException
import com.fcitu.smartfix.domain.exception.InvalidPasswordException
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.domain.repository.AuthRepository
import com.fcitu.smartfix.domain.useCase.validation.mobileNumber.MobileNumberValidator
import com.fcitu.smartfix.domain.useCase.validation.password.PasswordValidator

class LoginUseCase(
    private val authenticationRepository: AuthRepository,
    private val mobileNumberValidator: MobileNumberValidator,
    private val passwordValidator: PasswordValidator
) {
    suspend fun login(role: UserRole, phoneNumber: String, password: String): User {
        if (!isPasswordValid(password)) throw InvalidPasswordException()
        if (!isMobileNumberValid(phoneNumber = phoneNumber)) throw InvalidMobileNumberException(phoneNumber)
        return authenticationRepository.login(
            phoneNumber = phoneNumber,
            password = password,
            role = role
        )
    }

    fun isMobileNumberValid(countryCode: String = "+20", phoneNumber: String): Boolean {
        return mobileNumberValidator.isValid(countryCode = countryCode, phoneNumber = phoneNumber)
    }

    fun isPasswordValid(password: String) = passwordValidator.isValid(password)
}