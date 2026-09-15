package pe.edu.upeu.bibliomobil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.inicio.InicioScreen
import pe.edu.upeu.bibliomobil.presentation.lector.LectorScreen
import pe.edu.upeu.bibliomobil.presentation.lector.LectorViewModel
import pe.edu.upeu.bibliomobil.presentation.libro.LibroScreen
import pe.edu.upeu.bibliomobil.presentation.libro.LibroViewModel
import pe.edu.upeu.bibliomobil.presentation.navigation.Screen
import pe.edu.upeu.bibliomobil.presentation.theme.BiblioMobilTheme

private data class Destino(
    val screen: Screen,
    val titulo: String,
    val icono: ImageVector
)

private val DESTINOS = listOf(
    Destino(Screen.Inicio, "Inicio", Icons.Default.Home),
    Destino(Screen.Libros, "Libros", Icons.AutoMirrored.Filled.MenuBook),
    Destino(Screen.Lectores, "Lectores", Icons.Default.Person),
    Destino(Screen.Prestamos, "Préstamos", Icons.Default.Bookmark)
)

private val ScreenSaver = Saver<Screen, String>(
    save = { it.clave },
    restore = { clave ->
        when (clave) {
            Screen.Inicio.clave -> Screen.Inicio
            Screen.Libros.clave -> Screen.Libros
            Screen.Lectores.clave -> Screen.Lectores
            Screen.Prestamos.clave -> Screen.Prestamos
            else -> Screen.Inicio
        }
    }
)

@Composable
@Preview
fun App() {
    KoinContext {
        BiblioMobilApp()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BiblioMobilApp() {
    var modoOscuro by rememberSaveable { mutableStateOf(false) }
    var pantallaActual by rememberSaveable(stateSaver = ScreenSaver) {
        mutableStateOf<Screen>(Screen.Inicio)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val destinoActual = DESTINOS.first { it.screen == pantallaActual }

    BiblioMobilTheme(modoOscuro = modoOscuro) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Column(
                        modifier = Modifier.fillMaxHeight().padding(vertical = 12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "BiblioMobil",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.headlineSmall
                            )

                            DESTINOS.forEach { destino ->
                                NavigationDrawerItem(
                                    label = { Text(destino.titulo) },
                                    selected = destino.screen == pantallaActual,
                                    onClick = {
                                        pantallaActual = destino.screen
                                        scope.launch { drawerState.close() }
                                    },
                                    icon = { Icon(destino.icono, null) },
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                            }
                        }

                        Column(modifier = Modifier.fillMaxWidth()) {
                            NavigationDrawerItem(
                                label = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Modo oscuro")
                                        Switch(
                                            checked = modoOscuro,
                                            onCheckedChange = { modoOscuro = it }
                                        )
                                    }
                                },
                                selected = modoOscuro,
                                onClick = { modoOscuro = !modoOscuro },
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                        }
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(destinoActual.titulo) },
                        navigationIcon = {
                            IconButton(
                                onClick = { scope.launch { drawerState.open() } }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Abrir menú"
                                )
                            }
                        }
                    )
                }
            ) { paddingValues ->
                when (pantallaActual) {
                    Screen.Inicio -> InicioScreen(
                        onNavegar = { pantallaActual = it },
                        modifier = Modifier.padding(paddingValues)
                    )

                    Screen.Libros -> LibroScreen(
                        viewModel = koinViewModel<LibroViewModel>(),
                        modifier = Modifier.padding(paddingValues)
                    )

                    Screen.Lectores -> LectorScreen(
                        viewModel = koinViewModel<LectorViewModel>(),
                        modifier = Modifier.padding(paddingValues)
                    )

                    Screen.Prestamos -> EstadoVacio(
                        icono = Icons.Default.Bookmark,
                        titulo = "Préstamos en construcción",
                        descripcion = "Este módulo estará disponible próximamente.",
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}
