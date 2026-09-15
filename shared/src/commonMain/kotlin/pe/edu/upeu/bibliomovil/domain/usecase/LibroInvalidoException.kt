package pe.edu.upeu.bibliomovil.domain.usecase


class LibroInvalidoException(
    val errores: ErroresDeLibro
) : IllegalArgumentException("Los datos del libro no son válidos")