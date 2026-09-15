package pe.edu.upeu.bibliomobil.di

import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertSame
import org.koin.core.component.get
import org.koin.dsl.koinApplication
import pe.edu.upeu.bibliomobil.data.repository.LibroRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase

class AppModuleTest {
    @Test
    fun resuelveRepositorioDeLibrosEnMemoria() = conKoin { koin ->
        assertIs<LibroRepositorioEnMemoria>(koin.get<LibroRepository>())
    }

    @Test
    fun repositorioDeLibrosEsUnico() = conKoin { koin ->
        assertSame(koin.get<LibroRepository>(), koin.get<LibroRepository>())
    }

    @Test
    fun resuelveLosCuatroCasosDeUso() = conKoin { koin ->
        koin.get<RegistrarLibroUseCase>()
        koin.get<ListarLibrosUseCase>()
        koin.get<RegistrarLectorUseCase>()
        koin.get<ListarLectoresUseCase>()
    }

    private fun conKoin(bloque: (org.koin.core.Koin) -> Unit) {
        val aplicacion = koinApplication {
            modules(dataModule, domainModule, presentationModule)
        }
        try {
            bloque(aplicacion.koin)
        } finally {
            aplicacion.close()
        }
    }
}
