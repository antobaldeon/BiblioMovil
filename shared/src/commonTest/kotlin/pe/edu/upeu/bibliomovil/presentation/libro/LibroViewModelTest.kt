package pe.edu.upeu.bibliomobil.presentation.libro

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase
import pe.edu.upeu.bibliomobil.testutil.FakeLibroRepository

@OptIn(ExperimentalCoroutinesApi::class)
class LibroViewModelTest {
    private lateinit var dispatcher: TestDispatcher

    @BeforeTest
    fun prepararMain() {
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun restaurarMain() {
        Dispatchers.resetMain()
    }

    @Test
    fun arrancaEnSinLibros() {
        val viewModel = crearViewModel(FakeLibroRepository())
        assertEquals(FaseLibro.SinLibros, viewModel.uiState.value.fase)
    }

    @Test
    fun muestraLineaSecundariaExacta() {
        val viewModel = crearViewModel(
            FakeLibroRepository(listOf(Libro(1L, "Kotlin", "Autor", 1998, 3)))
        )

        val fase = assertIs<FaseLibro.ConLibros>(viewModel.uiState.value.fase)
        assertEquals("1998 · 3 ejemplares", fase.libros.single().lineaSecundaria)
    }

    @Test
    fun pasaAErrorSiElRepositorioFalla() {
        val viewModel = crearViewModel(FakeLibroRepository(fallarAlListar = true))
        val fase = assertIs<FaseLibro.Error>(viewModel.uiState.value.fase)
        assertEquals("No se pudo cargar el catálogo", fase.mensaje)
    }

    @Test
    fun erroresDeValidacionSeGuardanEnElFormulario() {
        val viewModel = crearViewModel(FakeLibroRepository())
        viewModel.registrar()

        assertEquals("El título es obligatorio", viewModel.uiState.value.formulario.errorTitulo)
        assertEquals(FaseLibro.SinLibros, viewModel.uiState.value.fase)
    }

    @Test
    fun registrarLimpiaFormularioYRecargaCatalogo() {
        val viewModel = crearViewModel(FakeLibroRepository())
        viewModel.onTituloChange("Kotlin")
        viewModel.onAutorChange("JetBrains")
        viewModel.onAnioChange("2020")
        viewModel.onEjemplaresChange("3")
        viewModel.registrar()

        assertEquals(FormularioLibro(), viewModel.uiState.value.formulario)
        assertEquals("Libro \"Kotlin\" registrado correctamente", viewModel.uiState.value.mensajeExito)
        assertIs<FaseLibro.ConLibros>(viewModel.uiState.value.fase)
    }

    private fun crearViewModel(repositorio: FakeLibroRepository): LibroViewModel =
        LibroViewModel(
            registrarLibro = RegistrarLibroUseCase(repositorio),
            listarLibros = ListarLibrosUseCase(repositorio)
        )
}
