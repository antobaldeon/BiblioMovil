package pe.edu.upeu.bibliomovil.domain.usecase

class LectorInvalidoException(
    val errores: ErroresDeLector
) : IllegalArgumentException("Los datos del lector no son válidos")