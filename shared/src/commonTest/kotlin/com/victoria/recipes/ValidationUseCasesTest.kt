package com.victoria.recipes

import com.victoria.recipes.domain.model.PasswordError
import com.victoria.recipes.domain.model.UsernameError
import com.victoria.recipes.domain.usecase.ValidatePasswordUseCase
import com.victoria.recipes.domain.usecase.ValidateUsernameUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ValidationUseCasesTest {

    private val validateUsername = ValidateUsernameUseCase()
    private val validatePassword = ValidatePasswordUseCase()

    @Test
    fun usernameValidation() {
        assertEquals(UsernameError.EMPTY, validateUsername("   "))
        assertEquals(UsernameError.TOO_SHORT, validateUsername("ab"))
        assertEquals(UsernameError.TOO_LONG, validateUsername("a".repeat(31)))
        assertEquals(UsernameError.INVALID_CHARACTERS, validateUsername("віка"))
        assertNull(validateUsername("emilys"))
        assertNull(validateUsername("victoria.b_01"))
    }

    @Test
    fun passwordValidation() {
        assertEquals(PasswordError.EMPTY, validatePassword(""))
        assertEquals(PasswordError.CONTAINS_SPACES, validatePassword("my password"))
        assertEquals(PasswordError.TOO_SHORT, validatePassword("short"))
        assertNull(validatePassword("emilyspass"))
    }
}
