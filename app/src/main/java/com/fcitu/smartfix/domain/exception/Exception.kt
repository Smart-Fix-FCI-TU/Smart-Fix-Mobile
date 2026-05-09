package com.fcitu.smartfix.domain.exception


open class SmartFixException(message: String) : Exception(message)

class NoNetworkException : SmartFixException("No Internet Connection")
class InvalidRequestException() : SmartFixException("Invalid request")
class UnknownException : SmartFixException("Unknown Exception")

open class AuthenticationException(message: String) : SmartFixException(message)
class UserNotRegisteredException : AuthenticationException("email not registered")
class UnauthorizedException(message: String = "Incorrect email or password") : AuthenticationException(message)
class NotFoundException(message: String = "No account found") : SmartFixException(message)
class BadRequestException(message: String = "Invalid request body") : SmartFixException(message)
class InvalidCountryCodeException(countryCode: String) :
    AuthenticationException("country code: $countryCode is not valid or not supported yet")
class InvalidEmailException(
    email: String
) : AuthenticationException("email: $email doesn't match validation")
class InvalidPasswordException : AuthenticationException("password doesn't match validations")