package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository
import pe.edu.upeu.bibliomovil.domain.model.Libro
import pe.edu.upeu.bibliomovil.domain.usecase.resultadoDe

class ListarLibrosUseCase(
    private val libroRepository: LibroRepository
) {
    suspend operator fun invoke(): Result<List<Libro>> = resultadoDe {
        libroRepository.listar()
    }
}