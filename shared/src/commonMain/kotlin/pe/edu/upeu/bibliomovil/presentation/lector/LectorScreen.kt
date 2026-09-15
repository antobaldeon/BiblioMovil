package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.components.MensajeExito
import pe.edu.upeu.bibliomobil.presentation.components.ValidatedTextField

@Composable
fun LectorScreen(
    viewModel: LectorViewModel,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            FormularioLector(
                formulario = estado.formulario,
                registrando = estado.registrando,
                onNombreChange = viewModel::onNombreChange,
                onCorreoChange = viewModel::onCorreoChange,
                onTelefonoChange = viewModel::onTelefonoChange,
                onRegistrar = viewModel::registrar
            )
        }

        estado.mensajeExito?.let { mensaje ->
            item {
                MensajeExito(mensaje = mensaje)
            }
        }

        when (val fase = estado.fase) {
            FaseLector.Cargando -> {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                item {
                    Text(
                        text = "Cargando cartera de lectores…",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            FaseLector.SinLectores -> {
                item {
                    EstadoVacio(
                        icono = Icons.Default.Person,
                        titulo = "No hay lectores registrados",
                        descripcion = "Registra el primer lector de la biblioteca."
                    )
                }
            }

            is FaseLector.ConLectores -> {
                item {
                    Text(
                        text = if (fase.lectores.size == 1) {
                            "1 lector"
                        } else {
                            "${fase.lectores.size} lectores"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(
                    items = fase.lectores,
                    key = { lector -> lector.id }
                ) { lector ->
                    TarjetaLector(lector)
                }
            }

            is FaseLector.Error -> {
                item {
                    EstadoVacio(
                        icono = Icons.Default.ErrorOutline,
                        titulo = fase.mensaje,
                        descripcion = "Intenta cargar la cartera nuevamente.",
                        esError = true,
                        textoAccion = "Reintentar",
                        onAccion = viewModel::cargarLectores
                    )
                }
            }
        }
    }
}

@Composable
private fun FormularioLector(
    formulario: FormularioLector,
    registrando: Boolean,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onRegistrar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Registrar lector",
                style = MaterialTheme.typography.titleLarge
            )

            ValidatedTextField(
                value = formulario.nombre,
                onValueChange = onNombreChange,
                label = "Nombre",
                error = formulario.errorNombre
            )

            ValidatedTextField(
                value = formulario.correo,
                onValueChange = onCorreoChange,
                label = "Correo",
                error = formulario.errorCorreo,
                keyboardType = KeyboardType.Email
            )

            ValidatedTextField(
                value = formulario.telefono,
                onValueChange = onTelefonoChange,
                label = "Teléfono (opcional)",
                error = formulario.errorTelefono,
                keyboardType = KeyboardType.Phone
            )

            Button(
                onClick = onRegistrar,
                enabled = !registrando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (registrando) {
                        "Registrando…"
                    } else {
                        "Registrar"
                    }
                )
            }
        }
    }
}

@Composable
private fun TarjetaLector(
    lector: LectorUi
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = lector.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = lector.correo,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = lector.telefono,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}