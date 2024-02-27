package com.example.komatsu.ui.view

data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null,
)