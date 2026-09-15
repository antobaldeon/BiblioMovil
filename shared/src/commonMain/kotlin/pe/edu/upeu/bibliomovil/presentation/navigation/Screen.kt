package pe.edu.upeu.bibliomobil.presentation.navigation

sealed class Screen(
    val clave: String
) {
    data object Inicio : Screen("inicio")
    data object Libros : Screen("libros")
    data object Lectores : Screen("lectores")
    data object Prestamos : Screen("prestamos")
}
