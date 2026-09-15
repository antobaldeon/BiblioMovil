package pe.edu.upeu.bibliomobil.presentation.libro

sealed interface FaseLibro {
    data object Cargando : FaseLibro
    data object SinLibros : FaseLibro
    data class ConLibros(
        val libros: List<LibroUi>
    ) : FaseLibro

    data class Error(
        val mensaje: String
    ) : FaseLibro
}