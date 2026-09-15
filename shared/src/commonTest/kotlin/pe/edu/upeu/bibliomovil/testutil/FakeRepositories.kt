package pe.edu.upeu.bibliomobil.testutil

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class FakeLibroRepository(
    librosIniciales: List<Libro> = emptyList(),
    var fallarAlRegistrar: Boolean = false,
    var fallarAlListar: Boolean = false
) : LibroRepository {
    private val libros = librosIniciales.toMutableList()
    private var siguienteId = (libros.maxOfOrNull { it.id } ?: 0L) + 1L

    override suspend fun registrar(libro: Libro): Libro {
        check(!fallarAlRegistrar) { "Fallo al registrar libro" }
        return libro.copy(id = siguienteId++).also(libros::add)
    }

    override suspend fun listar(): List<Libro> {
        check(!fallarAlListar) { "Fallo al listar libros" }
        return libros.toList()
    }
}

class FakeLectorRepository(
    var fallarAlRegistrar: Boolean = false,
    var fallarAlListar: Boolean = false
) : LectorRepository {
    private val lectores = mutableListOf<Lector>()
    private var siguienteId = 1L

    override suspend fun registrar(lector: Lector): Lector {
        check(!fallarAlRegistrar) { "Fallo al registrar lector" }
        return lector.copy(id = siguienteId++).also(lectores::add)
    }

    override suspend fun listar(): List<Lector> {
        check(!fallarAlListar) { "Fallo al listar lectores" }
        return lectores.toList()
    }
}
