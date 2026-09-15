package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.LectorInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase

class LectorViewModel(
    private val registrarLector: RegistrarLectorUseCase,
    private val listarLectores: ListarLectoresUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LectorUiState())
    val uiState: StateFlow<LectorUiState> = _uiState.asStateFlow()

    init {
        cargarLectores()
    }

    fun cargarLectores() {
        viewModelScope.launch {
            cargarLectoresInterno()
        }
    }

    fun onNombreChange(valor: String) {
        _uiState.update { estado ->
            estado.copy(
                formulario = estado.formulario.copy(
                    nombre = valor,
                    errorNombre = null
                ),
                mensajeExito = null
            )
        }
    }

    fun onCorreoChange(valor: String) {
        _uiState.update { estado ->
            estado.copy(
                formulario = estado.formulario.copy(
                    correo = valor,
                    errorCorreo = null
                ),
                mensajeExito = null
            )
        }
    }

    fun onTelefonoChange(valor: String) {
        _uiState.update { estado ->
            estado.copy(
                formulario = estado.formulario.copy(
                    telefono = valor,
                    errorTelefono = null
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

            registrarLector(
                nombre = formulario.nombre,
                correo = formulario.correo,
                telefono = formulario.telefono
            ).onSuccess { lector ->
                _uiState.update {
                    it.copy(
                        formulario = FormularioLector(),
                        registrando = false,
                        mensajeExito = "Lector \"${lector.nombre}\" registrado correctamente"
                    )
                }

                cargarLectoresInterno()
            }.onFailure { error ->
                when (error) {
                    is LectorInvalidoException -> {
                        _uiState.update { estado ->
                            estado.copy(
                                registrando = false,
                                formulario = estado.formulario.copy(
                                    errorNombre = error.errores.nombre,
                                    errorCorreo = error.errores.correo,
                                    errorTelefono = error.errores.telefono
                                )
                            )
                        }
                    }

                    else -> {
                        _uiState.update {
                            it.copy(
                                registrando = false,
                                fase = FaseLector.Error(
                                    error.message
                                        ?: "No se pudo cargar la cartera de lectores"
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun cargarLectoresInterno() {
        _uiState.update {
            it.copy(fase = FaseLector.Cargando)
        }

        listarLectores()
            .onSuccess { lectores ->
                _uiState.update { estado ->
                    estado.copy(
                        fase = if (lectores.isEmpty()) {
                            FaseLector.SinLectores
                        } else {
                            FaseLector.ConLectores(
                                lectores.map { lector -> lector.aUi() }
                            )
                        }
                    )
                }
            }
            .onFailure {
                _uiState.update { estado ->
                    estado.copy(
                        fase = FaseLector.Error(
                            "No se pudo cargar la cartera de lectores"
                        )
                    )
                }
            }
    }
}