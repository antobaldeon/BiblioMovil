package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import pe.edu.upeu.bibliomobil.testutil.FakeLibroRepository

class RegistrarLibroUseCaseTest {
    @Test
    fun aceptaLibroValidoYRecortaTexto() = runTest {
        val resultado = casoDeUso()("  Clean Code  ", "  Martin  ", "2008", "3")

        assertTrue(resultado.isSuccess)
        assertEquals("Clean Code", resultado.getOrThrow().titulo)
        assertEquals("Martin", resultado.getOrThrow().autor)
    }

    @Test fun tituloObligatorio() = valida("", "Autor", "2020", "1", "El título es obligatorio") { it.titulo }
    @Test fun autorObligatorio() = valida("Título", "", "2020", "1", "El autor es obligatorio") { it.autor }
    @Test fun anioObligatorio() = valida("Título", "Autor", "", "1", "El año es obligatorio") { it.anio }
    @Test fun anioDebeSerEntero() = valida("Título", "Autor", "dos mil", "1", "El año debe ser un número entero") { it.anio }
    @Test fun anioDebeEstarEnRango() = valida("Título", "Autor", "1449", "1", "El año debe estar entre 1450 y 2026") { it.anio }
    @Test fun ejemplaresObligatorios() = valida("Título", "Autor", "2020", "", "Los ejemplares son obligatorios") { it.ejemplares }
    @Test fun ejemplaresDebenSerEntero() = valida("Título", "Autor", "2020", "uno", "Los ejemplares deben ser un número entero") { it.ejemplares }
    @Test fun ejemplaresNoPuedenSerNegativos() = valida("Título", "Autor", "2020", "-1", "Los ejemplares no pueden ser negativos") { it.ejemplares }

    @Test
    fun elIdLoAsignaElRepositorio() = runTest {
        val resultado = casoDeUso()("Título", "Autor", "2020", "1")
        assertEquals(1L, resultado.getOrThrow().id)
    }

    @Test
    fun falloDelRepositorioLlegaComoFailure() = runTest {
        val resultado = RegistrarLibroUseCase(FakeLibroRepository(fallarAlRegistrar = true))(
            "Título", "Autor", "2020", "1"
        )
        assertTrue(resultado.isFailure)
        assertFalse(resultado.exceptionOrNull() is LibroInvalidoException)
    }

    private fun valida(
        titulo: String,
        autor: String,
        anio: String,
        ejemplares: String,
        mensaje: String,
        obtenerError: (ErroresDeLibro) -> String?
    ) = runTest {
        val resultado = casoDeUso()(titulo, autor, anio, ejemplares)
        val error = assertIs<LibroInvalidoException>(resultado.exceptionOrNull())
        assertEquals(mensaje, obtenerError(error.errores))
    }

    private fun casoDeUso() = RegistrarLibroUseCase(FakeLibroRepository())
}
