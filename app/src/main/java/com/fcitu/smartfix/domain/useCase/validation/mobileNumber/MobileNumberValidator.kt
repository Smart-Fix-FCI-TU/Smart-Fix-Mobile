package com.fcitu.smartfix.domain.useCase.validation.mobileNumber

import com.fcitu.smartfix.domain.exception.InvalidCountryCodeException

class MobileNumberValidator {
    fun isValid(countryCode: String, phoneNumber: String): Boolean {
        val country = getCountry(countryCode)
        return isMobileNumberValid(phoneNumber, country.regexPattern)
    }

    private fun getCountry(countryCode: String): Country {
        return Country.entries
            .firstOrNull { it.code == countryCode }
            ?: throw InvalidCountryCodeException(countryCode)
    }

    private fun isMobileNumberValid(
        number: String,
        regexPattern: Regex
    ) = number.matches(regexPattern)

    private enum class Country(val code: String, val regexPattern: Regex) {
        EGYPT("+20", "^(0)?1[0125]\\d{8}$".toRegex()),
    }
}