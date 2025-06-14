package com.fiveguysburger.emodiary.core.dto

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
) {
    companion object {
        fun <T> success(
            data: T? = null,
            message: String? = null,
        ): ApiResponse<T> = ApiResponse(success = true, data = data, message = message)

        fun <T> error(message: String): ApiResponse<T> = ApiResponse(success = false, message = message)
    }
}
