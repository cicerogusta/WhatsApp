package com.cicerodev.whatsapp.model

data class Result<T>(
    val success: Boolean = false,
    val exception: Exception? = null,
    val errorMessage: String = ""
)