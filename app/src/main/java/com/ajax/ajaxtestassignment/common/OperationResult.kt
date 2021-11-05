package com.ajax.ajaxtestassignment.common

import java.lang.Exception

sealed class OperationResult<out T> {
    object Loading : OperationResult<Nothing>()
    data class Success<out T>(val value: T) : OperationResult<T>()
    data class Failed(val value: Exception) : OperationResult<Nothing>()
}