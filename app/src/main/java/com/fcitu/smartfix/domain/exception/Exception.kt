package com.fcitu.smartfix.domain.exception


open class SmartFixException(message: String) : Exception(message)

class NoNetworkException : SmartFixException("No Internet Connection")
class InvalidRequestException() : SmartFixException("Invalid request")
class UnknownException : SmartFixException("Unknown Exception")

open class AuthenticationException(message: String) : SmartFixException(message)
class UserNotRegisteredException : AuthenticationException("Phone number not registered")
class InvalidCountryCodeException(countryCode: String) :
    AuthenticationException("country code: $countryCode is not valid or not supported yet")
class InvalidMobileNumberException(
    mobileNumber: String
) : AuthenticationException("mobile number: $mobileNumber doesn't match validation")
class InvalidPasswordException : AuthenticationException("password doesn't match validations")