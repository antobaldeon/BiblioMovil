package pe.edu.upeu.bibliomobil.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LibroTest {
    @Test fun rechazaTituloVacio() { assertFailsWith<IllegalArgumentException> { libro(titulo = "   ") } }
    @Test fun rechazaAnioMenorAlMinimo() { assertFailsWith<IllegalArgumentException> { libro(anio = 1449) } }
    @Test fun rechazaAnioMayorAlMaximo() { assertFailsWith<IllegalArgumentException> { libro(anio = 2027) } }
    @Test fun requiereReposicionConDosEjemplares() = assertTrue(libro(ejemplares = 2).requiereReposicion)
    @Test fun noRequiereReposicionConTresEjemplares() = assertFalse(libro(ejemplares = 3).requiereReposicion)

    private fun libro(titulo: String = "Kotlin", anio: Int = 2020, ejemplares: Int = 3) =
        Libro(0L, titulo, "Autor", anio, ejemplares)
}
