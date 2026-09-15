package pe.edu.upeu.bibliomovil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository
import pe.edu.upeu.bibliomovil.domain.model.Libro

class RegistrarLibroUseCase(
    private val libroRepository: LibroRepository
) {
    suspend operator fun invoke(
        titulo: String,
        autor: String,
        anio: String,
        ejemplares: String
    ): Result<Libro> = resultadoDe {
        val tituloLimpio = titulo.trim()
        val autorLimpio = autor.trim()
        val anioLimpio = anio.trim()
        val ejemplaresLimpio = ejemplares.trim()

        val anioNumerico = anioLimpio.toIntOrNull()
        val ejemplaresNumericos = ejemplaresLimpio.toIntOrNull()

        val errores = ErroresDeLibro(
            titulo = if (tituloLimpio.isBlank()) {
                "El título es obligatorio"
            } else {
                null
            },
            autor = if (autorLimpio.isBlank()) {
                "El autor es obligatorio"
            } else {
                null
            },
            anio = when {
                anioLimpio.isBlank() -> "El año es obligatorio"
                anioNumerico == null -> "El año debe ser un número entero"
                anioNumerico !in Libro.ANIO_MINIMO..Libro.ANIO_MAXIMO ->
                    "El año debe estar entre 1450 y 2026"

                else -> null
            },
            ejemplares = when {
                ejemplaresLimpio.isBlank() -> "Los ejemplares son obligatorios"
                ejemplaresNumericos == null ->
                    "Los ejemplares deben ser un número entero"

                ejemplaresNumericos < 0 ->
                    "Los ejemplares no pueden ser negativos"

                else -> null
            }
        )

        if (
            errores.titulo != null ||
            errores.autor != null ||
            errores.anio != null ||
            errores.ejemplares != null
        ) {
            throw LibroInvalidoException(errores)
        }

        val libro = Libro(
            id = 0L,
            titulo = tituloLimpio,
            autor = autorLimpio,
            anio = checkNotNull(anioNumerico),
            ejemplares = checkNotNull(ejemplaresNumericos)
        )

        libroRepository.registrar(libro)
    }
}