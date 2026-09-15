package pe.edu.upeu.bibliomobil.presentation.lector

data class LectorUiState(
    val fase: FaseLector = FaseLector.Cargando,
    val formulario: FormularioLector = FormularioLector(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
)