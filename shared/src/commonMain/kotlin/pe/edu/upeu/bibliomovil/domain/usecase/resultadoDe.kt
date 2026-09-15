package pe.edu.upeu.bibliomovil.domain.usecase

import kotlinx.coroutines.CancellationException

suspend fun <T> resultadoDe(
    bloque: suspend () -> T
): Result<T> {
    return try {
        Result.success(bloque())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}