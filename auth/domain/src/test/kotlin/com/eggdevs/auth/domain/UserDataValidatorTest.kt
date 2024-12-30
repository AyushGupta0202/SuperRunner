package com.eggdevs.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class UserDataValidatorTest {

    private lateinit var userDataValidator: UserDataValidator

    @BeforeEach
    fun setup() {
        userDataValidator = UserDataValidator(
            patternValidator = object: PatternValidator {
                override fun matches(value: String): Boolean {
                    return true
                }
            }
        )
    }

    @Test
    fun testValidateEmail() {
        val email = "test@test.com"
        val result = userDataValidator.isValidEmail(email) // this is pointless because the function has no logic
        assertThat(result).isTrue()
    }

    @Test
    fun testValidatePassword_passwordValid() {
        val password = "Test12345"
        val state = userDataValidator.validatePassword(password)
        assertThat(state.isValidPassword).isTrue()
        assertThat(state.hasMinLength).isTrue()
        assertThat(state.hasUpperCaseCharacter).isTrue()
        assertThat(state.hasLowerCaseCharacter).isTrue()
        assertThat(state.hasNumber).isTrue()
    }

    @ParameterizedTest
    @CsvSource(
        "Test12345, true",
        "Test1234, false",
        "test12345, false",
        "TEST12345, false",
        "TestTestTest, false",
        "12345, false",
        "Test-1234, true"
    )
    fun testValidatePassword_parameterizedTest(password: String, expectedIsValid: Boolean) {
        val state = userDataValidator.validatePassword(password)
        assertThat(state.isValidPassword).isEqualTo(expectedIsValid)
    }

    @Test
    fun testValidatePassword_passwordInvalid_invalidLength() {
        val password = "Test1234"
        val state = userDataValidator.validatePassword(password)
        assertThat(state.isValidPassword).isFalse()
        assertThat(state.hasMinLength).isFalse()
        assertThat(state.hasUpperCaseCharacter).isTrue()
        assertThat(state.hasLowerCaseCharacter).isTrue()
        assertThat(state.hasNumber).isTrue()
    }

    @Test
    fun testValidatePassword_passwordInvalid_noUpperCaseCharacter() {
        val password = "test12345"
        val state = userDataValidator.validatePassword(password)
        assertThat(state.isValidPassword).isFalse()
        assertThat(state.hasMinLength).isTrue()
        assertThat(state.hasUpperCaseCharacter).isFalse()
        assertThat(state.hasLowerCaseCharacter).isTrue()
        assertThat(state.hasNumber).isTrue()
    }

    @Test
    fun testValidatePassword_passwordInvalid_noLowerCaseCharacter() {
        val password = "TEST12345"
        val state = userDataValidator.validatePassword(password)
        assertThat(state.isValidPassword).isFalse()
        assertThat(state.hasMinLength).isTrue()
        assertThat(state.hasUpperCaseCharacter).isTrue()
        assertThat(state.hasLowerCaseCharacter).isFalse()
        assertThat(state.hasNumber).isTrue()
    }

    @Test
    fun testValidatePassword_passwordInvalid_noNumber() {
        val password = "TestTestTest"
        val state = userDataValidator.validatePassword(password)
        assertThat(state.isValidPassword).isFalse()
        assertThat(state.hasMinLength).isTrue()
        assertThat(state.hasUpperCaseCharacter).isTrue()
        assertThat(state.hasLowerCaseCharacter).isTrue()
        assertThat(state.hasNumber).isFalse()
    }
}