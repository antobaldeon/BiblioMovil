package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import pe.edu.upeu.bibliomobil.domain.model.Libro

class LibroRepositorioEnMemoriaTest {
    @Test
    fun asignaIdsCorrelativos() = runTest {
        val repositorio = LibroRepositorioEnMemoria()
        val primero = repositorio.registrar(libro("Primero"))
        val segundo = repositorio.registrar(libro("Segundo"))

        assertEquals(1L, primero.id)
        assertEquals(2L, segundo.id)
    }

    @Test
    fun listaEnOrdenDeRegistro() = runTest {
        val repositorio = LibroRepositorioEnMemoria()
        repositorio.registrar(libro("Primero"))
        repositorio.registrar(libro("Segundo"))

        assertEquals(listOf("Primero", "Segundo"), repositorio.listar().map { it.titulo })
    }

    private fun libro(titulo: String) = Libro(0L, titulo, "Autor", 2020, 1)
}
