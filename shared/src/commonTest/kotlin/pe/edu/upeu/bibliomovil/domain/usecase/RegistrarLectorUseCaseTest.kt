package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import pe.edu.upeu.bibliomobil.testutil.FakeLectorRepository

class RegistrarLectorUseCaseTest {
    @Test
    fun rechazaCorreoInvalido() = runTest {
        val resultado = casoDeUso()("Ana", "correo-invalido", "")
        val error = assertIs<LectorInvalidoException>(resultado.exceptionOrNull())
        assertEquals("El correo no tiene un formato válido", error.errores.correo)
    }

    @Test
    fun rechazaTelefonoCorto() = runTest {
        val resultado = casoDeUso()("Ana", "ana@upeu.edu.pe", "12345")
        val error = assertIs<LectorInvalidoException>(resultado.exceptionOrNull())
        assertEquals("El teléfono debe tener entre 6 y 9 dígitos", error.errores.telefono)
    }

    @Test
    fun telefonoEnBlancoSeGuardaComoNull() = runTest {
        val resultado = casoDeUso()("Ana", "ana@upeu.edu.pe", "   ")
        assertNull(resultado.getOrThrow().telefono)
    }

    @Test
    fun rechazaNombreObligatorio() = runTest {
        val resultado = casoDeUso()("", "ana@upeu.edu.pe", "")
        val error = assertIs<LectorInvalidoException>(resultado.exceptionOrNull())
        assertEquals("El nombre es obligatorio", error.errores.nombre)
    }

    private fun casoDeUso() = RegistrarLectorUseCase(FakeLectorRepository())
}
