package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomovil.domain.usecase.LibroInvalidoException
import pe.edu.upeu.bibliomovil.domain.usecase.RegistrarLibroUseCase

class LibroViewModel(
    private val registrarLibro: RegistrarLibroUseCase,
    private val listarLibros: ListarLibrosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibroUiState())
    val uiState: StateFlow<LibroUiState> = _uiState.asStateFlow()

    init {
        cargarLibros()
    }

    fun cargarLibros() {
        viewModelScope.launch {
            cargarLibrosInterno()
        }
    }

    fun onTituloChange(valor: String) {
        _uiState.update { estado ->
            estado.copy(
                formulario = estado.formulario.copy(
                    titulo = valor,
                    errorTitulo = null
                ),
                mensajeExito = null
            )
        }
    }

    fun onAutorChange(valor: String) {
        _uiState.update { estado ->
            estado.copy(
                formulario = estado.formulario.copy(
                    autor = valor,
                    errorAutor = null
                ),
                mensajeExito = null
            )
        }
    }

    fun onAnioChange(valor: String) {
        _uiState.update { estado ->
            estado.copy(
                formulario = estado.formulario.copy(
                    anio = valor,
                    errorAnio = null
                ),
                mensajeExito = null
            )
        }
    }

    fun onEjemplaresChange(valor: String) {
        _uiState.update { estado ->
            estado.copy(
                formulario = estado.formulario.copy(
                    ejemplares = valor,
                    errorEjemplares = null
                ),
                mensajeExito = null
            )
        }
    }

    fun registrar() {
        if (_uiState.value.registrando) return

        viewModelScope.launch {
            val formulario = _uiState.value.formulario

            _uiState.update {
                it.copy(
                    registrando = true,
                    mensajeExito = null
                )
            }

            registrarLibro(
                titulo = formulario.titulo,
                autor = formulario.autor,
                anio = formulario.anio,
                ejemplares = formulario.ejemplares
            ).onSuccess { libro ->
                _uiState.update {
                    it.copy(
                        formulario = FormularioLibro(),
                        registrando = false,
                        mensajeExito = "Libro \"${libro.titulo}\" registrado correctamente"
                    )
                }

                cargarLibrosInterno()
            }.onFailure { error ->
                when (error) {
                    is LibroInvalidoException -> {
                        _uiState.update { estado ->
                            estado.copy(
                                registrando = false,
                                formulario = estado.formulario.copy(
                                    errorTitulo = error.errores.titulo,
                                    errorAutor = error.errores.autor,
                                    errorAnio = error.errores.anio,
                                    errorEjemplares = error.errores.ejemplares
                                )
                            )
                        }
                    }

                    else -> {
                        _uiState.update {
                            it.copy(
                                registrando = false,
                                fase = FaseLibro.Error(
                                    error.message ?: "No se pudo cargar el catálogo"
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun cargarLibrosInterno() {
        _uiState.update {
            it.copy(fase = FaseLibro.Cargando)
        }

        listarLibros()
            .onSuccess { libros ->
                _uiState.update { estado ->
                    estado.copy(
                        fase = if (libros.isEmpty()) {
                            FaseLibro.SinLibros
                        } else {
                            FaseLibro.ConLibros(libros.map { libro -> libro.aUi() })
                        }
                    )
                }
            }
            .onFailure {
                _uiState.update { estado ->
                    estado.copy(
                        fase = FaseLibro.Error("No se pudo cargar el catálogo")
                    )
                }
            }
    }
}