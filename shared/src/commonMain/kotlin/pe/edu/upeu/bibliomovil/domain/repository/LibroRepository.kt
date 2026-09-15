package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomovil.domain.model.Libro

interface LibroRepository {

    /**
     * Registra un libro en el catálogo de la biblioteca.
     *
     * El repositorio asigna el identificador definitivo del libro.
     */
    suspend fun registrar(libro: Libro): Libro

    /**
     * Recupera todos los libros registrados en el catálogo.
     */
    suspend fun listar(): List<Libro>
}