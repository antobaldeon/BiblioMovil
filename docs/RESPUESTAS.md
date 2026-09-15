# RESPUESTAS

## 1. Cambio a backend REST

Cuando llegue el backend REST, cambiarán las implementaciones 
de `data`, por ejemplo `LibroRepositorioEnMemoria` y `LectorRepositorioEnMemoria`, 
que serán reemplazadas por repositorios que consuman la API. También cambiará 
`di/AppModule.kt` para registrar esas nuevas implementaciones. Se mantienen intactos 
los modelos, interfaces de repositorio, casos de uso, ViewModels y pantallas, porque 
las capas internas no dependen de detalles externos; `data` depende de `domain`, no 
al revés.

## 2. Parámetros String en RegistrarLibroUseCase

Si `RegistrarLibroUseCase` recibiera `anio` y `ejemplares` 
como `Int`, se perdería la posibilidad de diferenciar un campo 
vacío de un valor no numérico y no se podrían mostrar los 
mensajes exactos “El año es obligatorio” o “El año debe ser un 
número entero”. La pantalla o ViewModel tendría que convertir el texto antes de 
llamar al caso de uso, mezclando validación de negocio con presentación y duplicando reglas
fuera del dominio.

## 3. Repositorio registrado como factory

Si `LibroRepository` estuviera registrado como `factory`, Koin crearía una nueva instancia cada vez que un caso de uso la solicite. Como el repositorio en memoria guarda su lista dentro de la instancia, al registrar un libro y luego listar desde otra instancia, el usuario vería el catálogo vacío o cambios que desaparecen. Debe ser `single` para que todos los casos de uso compartan el mismo catálogo durante la ejecución de la aplicación.