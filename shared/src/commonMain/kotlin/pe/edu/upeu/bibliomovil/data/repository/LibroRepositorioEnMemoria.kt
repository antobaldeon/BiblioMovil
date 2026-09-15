package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.random.Random
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository
import pe.edu.upeu.bibliomovil.domain.model.Libro

class LibroRepositorioEnMemoria : LibroRepository {

    private val mutex = Mutex()
    private val libros = mutableListOf<Libro>()
    private var siguienteId = 1L

    override suspend fun registrar(libro: Libro): Libro {
        simularLatencia()

        return mutex.withLock {
            val libroRegistrado = libro.copy(id = siguienteId++)
            libros.add(libroRegistrado)
            libroRegistrado
        }
    }

    override suspend fun listar(): List<Libro> {
        simularLatencia()

        return mutex.withLock {
            libros.toList()
        }
    }

    private suspend fun simularLatencia() {
        delay(Random.nextLong(300L, 801L))
    }
}