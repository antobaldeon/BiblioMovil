package pe.edu.upeu.bibliomobil.domain.usecase

class LectorInvalidoException(
    val errores: ErroresDeLector
) : IllegalArgumentException("Los datos del lector no son válidos")
