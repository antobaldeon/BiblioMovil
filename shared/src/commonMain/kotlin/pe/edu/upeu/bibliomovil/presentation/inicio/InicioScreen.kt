package pe.edu.upeu.bibliomobil.presentation.inicio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pe.edu.upeu.bibliomobil.presentation.navigation.Screen

private data class AccesoRapido(
    val titulo: String,
    val descripcion: String,
    val icono: ImageVector,
    val destino: Screen
)

@Composable
fun InicioScreen(
    onNavegar: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val accesos = listOf(
        AccesoRapido("Registrar libros", "Administra el catálogo", Icons.AutoMirrored.Filled.MenuBook, Screen.Libros),
        AccesoRapido("Registrar lectores", "Administra la cartera de lectores", Icons.Default.Person, Screen.Lectores),
        AccesoRapido("Revisar préstamos", "Consulta los préstamos", Icons.Default.Bookmark, Screen.Prestamos)
    )

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(Icons.Default.LocalLibrary, null, tint = MaterialTheme.colorScheme.primary)
        Text("BiblioMobil", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Tu biblioteca, siempre organizada.", style = MaterialTheme.typography.bodyLarge)
        Text(
            text = "Qué puedes hacer",
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        accesos.forEach { acceso ->
            Card(Modifier.fillMaxWidth().clickable { onNavegar(acceso.destino) }) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(acceso.icono, null, tint = MaterialTheme.colorScheme.primary)
                    Text(acceso.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(acceso.descripcion, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
