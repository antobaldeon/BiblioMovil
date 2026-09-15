package pe.edu.upeu.bibliomobil.domain.model

data class Lector(
    val id: Long,
    val nombre: String,
    val correo: String,
    val telefono: String?
) {
    init {
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(correo.isNotBlank()) { "El correo no puede estar vacío" }
        require(telefono == null || telefono.isNotBlank()) {
            "El teléfono no puede estar vacío"
        }
    }
}