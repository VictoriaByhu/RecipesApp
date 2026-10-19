package com.victoria.recipes

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform