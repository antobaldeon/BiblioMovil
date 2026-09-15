package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Lector

interface LectorRepository {

    /**
     * Registra un lector en la cartera de lectores de la biblioteca.
     *
     * El repositorio asigna el identificador definitivo del lector.
     */
    suspend fun registrar(lector: Lector): Lector

    /**
     * Recupera todos los lectores registrados en la biblioteca.
     */
    suspend fun listar(): List<Lector>
}