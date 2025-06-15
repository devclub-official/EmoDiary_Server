package com.fiveguysburger.emodiary.core.exception

class FcmException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
