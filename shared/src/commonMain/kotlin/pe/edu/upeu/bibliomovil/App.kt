package pe.edu.upeu.bibliomobil

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import pe.edu.upeu.bibliomobil.presentation.libro.LibroScreen
import pe.edu.upeu.bibliomobil.presentation.libro.LibroViewModel

@Composable
@Preview
fun App() {
    KoinContext {
        MaterialTheme {
            LibroScreen(
                viewModel = koinViewModel<LibroViewModel>()
            )
        }
    }
}
