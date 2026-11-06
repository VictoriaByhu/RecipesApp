package com.victoria.recipes.domain.model

enum class UsernameError { EMPTY, TOO_SHORT, TOO_LONG, INVALID_CHARACTERS }

enum class PasswordError { EMPTY, TOO_SHORT, CONTAINS_SPACES }
