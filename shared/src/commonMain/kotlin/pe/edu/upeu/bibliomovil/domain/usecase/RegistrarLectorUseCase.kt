package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository
import pe.edu.upeu.bibliomovil.domain.usecase.ErroresDeLector
import pe.edu.upeu.bibliomovil.domain.usecase.LectorInvalidoException
import pe.edu.upeu.bibliomovil.domain.usecase.resultadoDe

class RegistrarLectorUseCase(
    private val lectorRepository: LectorRepository
) {
    private val correoRegex = Regex(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )

    private val telefonoRegex = Regex("^\\d{6,9}$")

    suspend operator fun invoke(
        nombre: String,
        correo: String,
        telefono: String
    ): Result<Lector> = resultadoDe {
        val nombreLimpio = nombre.trim()
        val correoLimpio = correo.trim()
        val telefonoLimpio = telefono.trim()
        val telefonoNormalizado = telefonoLimpio.ifBlank { null }

        val errores = ErroresDeLector(
            nombre = if (nombreLimpio.isBlank()) {
                "El nombre es obligatorio"
            } else {
                null
            },
            correo = when {
                correoLimpio.isBlank() -> "El correo es obligatorio"
                !correoRegex.matches(correoLimpio) ->
                    "El correo no tiene un formato válido"
                else -> null
            },
            telefono = if (
                telefonoNormalizado != null &&
                !telefonoRegex.matches(telefonoNormalizado)
            ) {
                "El teléfono debe tener entre 6 y 9 dígitos"
            } else {
                null
            }
        )

        if (
            errores.nombre != null ||
            errores.correo != null ||
            errores.telefono != null
        ) {
            throw LectorInvalidoException(errores)
        }

        val lector = Lector(
            id = 0L,
            nombre = nombreLimpio,
            correo = correoLimpio,
            telefono = telefonoNormalizado
        )

        lectorRepository.registrar(lector)
    }
}