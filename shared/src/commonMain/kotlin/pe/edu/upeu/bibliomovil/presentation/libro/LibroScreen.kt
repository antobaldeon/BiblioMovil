package pe.edu.upeu.bibliomobil.presentation.libro

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
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
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
fun LibroScreen(
    viewModel: LibroViewModel,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            FormularioLibro(
                formulario = estado.formulario,
                registrando = estado.registrando,
                onTituloChange = viewModel::onTituloChange,
                onAutorChange = viewModel::onAutorChange,
                onAnioChange = viewModel::onAnioChange,
                onEjemplaresChange = viewModel::onEjemplaresChange,
                onRegistrar = viewModel::registrar
            )
        }

        estado.mensajeExito?.let { mensaje ->
            item {
                MensajeExito(mensaje = mensaje)
            }
        }

        when (val fase = estado.fase) {
            FaseLibro.Cargando -> {
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
                        text = "Cargando catálogo…",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            FaseLibro.SinLibros -> {
                item {
                    EstadoVacio(
                        icono = Icons.Default.MenuBook,
                        titulo = "No hay libros registrados",
                        descripcion = "Registra el primer libro del catálogo."
                    )
                }
            }

            is FaseLibro.ConLibros -> {
                item {
                    Text(
                        text = if (fase.libros.size == 1) {
                            "1 libro"
                        } else {
                            "${fase.libros.size} libros"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(
                    items = fase.libros,
                    key = { libro -> libro.id }
                ) { libro ->
                    TarjetaLibro(libro)
                }
            }

            is FaseLibro.Error -> {
                item {
                    EstadoVacio(
                        icono = Icons.Default.ErrorOutline,
                        titulo = fase.mensaje,
                        descripcion = "Intenta cargar el catálogo nuevamente.",
                        esError = true,
                        textoAccion = "Reintentar",
                        onAccion = viewModel::cargarLibros
                    )
                }
            }
        }
    }
}

@Composable
private fun FormularioLibro(
    formulario: FormularioLibro,
    registrando: Boolean,
    onTituloChange: (String) -> Unit,
    onAutorChange: (String) -> Unit,
    onAnioChange: (String) -> Unit,
    onEjemplaresChange: (String) -> Unit,
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
                text = "Registrar libro",
                style = MaterialTheme.typography.titleLarge
            )

            ValidatedTextField(
                value = formulario.titulo,
                onValueChange = onTituloChange,
                label = "Título",
                error = formulario.errorTitulo
            )

            ValidatedTextField(
                value = formulario.autor,
                onValueChange = onAutorChange,
                label = "Autor",
                error = formulario.errorAutor
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ValidatedTextField(
                    value = formulario.anio,
                    onValueChange = onAnioChange,
                    label = "Año",
                    error = formulario.errorAnio,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )

                ValidatedTextField(
                    value = formulario.ejemplares,
                    onValueChange = onEjemplaresChange,
                    label = "Ejemplares",
                    error = formulario.errorEjemplares,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
            }

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
private fun TarjetaLibro(
    libro: LibroUi
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = libro.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = libro.autor,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = libro.lineaSecundaria,
                style = MaterialTheme.typography.bodySmall
            )

            if (libro.requiereReposicion) {
                AssistChip(
                    onClick = {},
                    label = {
                        Text("Pocos ejemplares")
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        labelColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                )
            }
        }
    }
}