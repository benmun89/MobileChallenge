package com.mobilechallenge.core.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.logging.Level
import java.util.logging.Logger

abstract class UseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {
    suspend operator fun invoke(parameters: P): Result<R> {
        return try {
            withContext(coroutineDispatcher) {
                execute(parameters).let {
                    Result.success(it)
                }
            }
        } catch (e: Exception) {
            Logger.getLogger(UseCase::class.java.simpleName)
                .log(Level.WARNING, "throw error", e)
            Result.failure(e)
        }
    }
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(param: P): R

    companion object {
        val TAG: String = UseCase::class.java.simpleName
    }
}