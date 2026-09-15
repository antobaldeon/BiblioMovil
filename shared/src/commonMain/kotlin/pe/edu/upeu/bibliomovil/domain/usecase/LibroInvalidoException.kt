package pe.edu.upeu.bibliomobil.domain.usecase


class LibroInvalidoException(
    val errores: ErroresDeLibro
) : IllegalArgumentException("Los datos del libro no son válidos")
