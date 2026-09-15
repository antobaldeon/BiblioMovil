package pe.edu.upeu.bibliomovil

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform