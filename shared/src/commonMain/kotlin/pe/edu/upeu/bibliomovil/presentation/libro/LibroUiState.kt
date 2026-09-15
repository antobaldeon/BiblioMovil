package pe.edu.upeu.bibliomobil.presentation.libro

data class LibroUiState(
    val fase: FaseLibro = FaseLibro.Cargando,
    val formulario: FormularioLibro = FormularioLibro(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
)