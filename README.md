# ProyectoIntredorEverest
## Tabla de datos del proyecto integrador
| **Nombre del Campo**      | **Tipo de Dato** | **Propósito**                                    | **Observaciones**                                     |
|---------------------------|------------------|------------------------------------------------|----------------------------------------------------|
| `Adult`                  | boolean          | Indica si la película está destinada a un público adulto. |                                                    |
| `belong_to_collection`   | String           | Especifica a qué colección de películas pertenece. | Puede contener URLs y además es un JSON            |
| `budget`                 | Double           | Registra el presupuesto destinado a la película. |                                                    |
| `genres`                 | String           | Clasifica las categorías o géneros de la película. | Puede contener URLs y además es un JSON            |
| `homepage`               | String           | Proporciona el enlace a una página web con información sobre la película. | Puede contener URLs                          |
| `id`                     | String           | Asigna un identificador único a la película.     |                                                    |
| `imdb_id`                | String           | Define el identificador asociado en IMDb.         |                                                    |
| `original_languague`     | String           | Indica el idioma en el que fue realizada originalmente. |                                                    |
| `original_title`         | String           | Registra el título original de la película.      |                                                    |
| `overview`               | String           | Contiene una sinopsis o descripción general.     |                                                    |
| `popularity`             | String           | Mide el nivel de popularidad de la película.     |                                                    |
| `poster_Path`            | String           | Proporciona la ruta del póster de la película.   |                                                    |
| `production_companies`   | String           | Detalla las empresas responsables de la producción. | Es un archivo JSON                                 |
| `production_countries`   | String           | Indica los países donde fue producida la película. | Es un archivo JSON                                 |
| `release_date`           | String           | Marca la fecha en que se lanzó la película.      |                                                    |
| `revenue`                | String           | Registra las ganancias obtenidas por la película. |                                                    |
| `runtime`                | String           | Especifica la duración total de la película.     |                                                    |
| `spoken_languagues`      | String           | Lista los idiomas en los que está doblada.       | Es un archivo JSON                                 |
| `status`                 | String           | Indica el estado actual de la película (ej. lanzada, en producción). |                                                    |
| `tagline`                | String           | Proporciona una frase característica de la película. |                                                    |
| `title`                  | String           | Especifica el título principal de la película.   |                                                    |
| `video`                  | boolean          | Indica si la película cuenta con un video asociado. |                                                    |
| `vote_average`           | Double           | Muestra el promedio de las calificaciones recibidas. |                                                    |
| `vote_count`             | Int              | Indica la cantidad total de votos registrados.   |                                                    |
| `keywords`               | String           | Contiene las palabras clave asociadas a la película. | Es un archivo JSON                                 |
| `cast`                   | String           | Identifica a los actores, sus personajes y su orden en la película. | Es un archivo JSON                                 |
| `crew`                   | String           | Enumera al personal técnico involucrado en la producción. | Es un archivo JSON                                 |
| `ratings`                | String           | Almacena las valoraciones realizadas por los usuarios. | Es un archivo JSON                                 |

## Análisis de datos en columnas numéricas (estadísticas básicas)

Cargar los datos del archivo CSV especificando el delimitador en este caso ;
```python
# Cargar datos desde el archivo CSV
data = pd.read_csv(r"C:\Users\adalb\OneDrive\Documents\pi_movies_small.csv", delimiter=";")
```
Se seleccciona las columnas numericas
```python
# Seleccionar columnas numéricas
columnas_num = data.select_dtypes(include=["number"])
```
Se calculan las estadisticas basicas (minimos,maximos,promedios, desviaciones estandar y correlaciones.

```python
# Calcular estadísticas básicas
stats = columnas_num.describe()
cantidad_columnas = len(columnas_num.columns)
valores_min = columnas_num.min()
valores_max = columnas_num.max()
valores_prom = columnas_num.mean()
valores_std = columnas_num.std()
# Calcular correlaciones entre columnas numéricas
correlaciones = columnas_num.corr()
```
![image](https://github.com/user-attachments/assets/83ecf290-b053-4bfc-95d7-e10a6a20f82a)
![image](https://github.com/user-attachments/assets/63791d8e-4f92-45b3-aed5-db6eb88f6e89)

Análisis de datos en columnas tipo texto (algunas col. - distribución de frecuencia). OJO: no considerar columnas en formato JSON
Se usa codificación UTF-8 para manejar caracteres especiales como tildes y ñ como al igual Si encuentra líneas con errores en el CSV (como columnas mal formateadas), las ignora y muestra una advertencia en lugar de generar un error.
Si el CSV tiene problemas estructurales, este código evitará que el programa falle abrupta
```python
data = pd.read_csv(archivo_path, delimiter=";", encoding="utf-8", on_bad_lines="warn")
```
Se excluyen las columnas que sean JSON y se selecciona el texto de los no JSON para posterior realizar la distribucion de frecuencias
```python
 columnas_json = ['spoken_languages','production_countries','production_companies','belongs_to_collection','genres',
     'keywords', 'cast', 'crew', 'ratings'] 
    
    # Seleccionar columnas de texto excluyendo las que son JSON
    columnas_texto = [col for col in data.select_dtypes(include=['object']).columns if col not in columnas_json]
    
    # Análisis de distribución de frecuencia para columnas seleccionadas
    for col in columnas_texto:
        print(f"\nDistribución de frecuencias para la columna '{col}':")
        print(data[col].value_counts().head(5)) 
   
```
## Consultar sobre librería play-json (trabajo json en scala) y hacer:
  - Usar cualquier JSON pequeño para aprender play-json
  - Usar en algunas columnas JSON para obtener datos.

### Proceso
Se importa la libreria play-son para trabajar con datos JSON de gual manera al Json se lo representa en un case class

```scala
import play.api.libs.json._

case class Pelicula(titulo: String, generos: List[String], presupuesto: Int)
   Formato implícito para serialización/deserialización de JSON
 implicit val formatoPelicula: Format[Pelicula] = Json.format[Pelicula]
```
```scala
// 1. Parsear la cadena JSON
  val jsonParseado = Json.parse(cadenaJson)

  // 2. Convertir el JSON en una instancia de Pelicula
  val pelicula = jsonParseado.as[Pelicula]
  println("Deserializado desde JSON:")
  println(pelicula)
```
Luego se convierte en una instancia JSON
```scala
val jsonDeVuelta = Json.toJson(pelicula)
  println("\nConvertido de vuelta a JSON:")
  println(Json.prettyPrint(jsonDeVuelta))
  // Simulación de filas con una columna "generos" en formato JSON
  val filas = List(
    """{"nombre":"Acción"}""",
    """{"nombre":"Drama"}""",
    """{"nombre":"Acción"}""",
    """{"nombre":"Comedia"}""",
    """{"nombre":"Drama"}"""
  ).map(Json.parse) // Parsear cada fila a un objeto JSON
```
## Limpieza

### Excepciones para controlar que el JSON esté bien formado 

- Función para reemplazar caracteres o errores de formato en mi JSON
  
  ![Imagen 1](https://github.com/user-attachments/assets/17c394f1-1993-4d89-b9b7-eaf2ba85496f)

- Función para controlar espacios en blanco. Filtra solo las filas correctamente parseadas (Right), filtra solo los valores que no sean vacíos
  
  ![Imagen 2](https://github.com/user-attachments/assets/09f24d0f-169b-414c-9e76-44bac5720730)

- Método para controlar el formato de fechas y asegurar que sea el correcto, específico para la columna `release_date

  ![Imagen 3](https://github.com/user-attachments/assets/212a0988-d7c9-432d-8769-830bcb9d8288)

- Método para controlar cadenas--------------------------------------------------------------
  
  ![Imagen 4](https://github.com/user-attachments/assets/fa63c252-c012-4611-9cf5-a7ddd2d4ead9)

- Case class (Ejemplo: Spoken Language)------------------------------------------------------
  
  ![Imagen 5](https://github.com/user-attachments/assets/7bc197f1-28e5-43d3-a18b-43b70195ec32)

- Expresión regular diseñada para coincidir con cadenas que representen un objeto JSON o un arreglo JSON
  
  ![Imagen 6](https://github.com/user-attachments/assets/3d51d658-3f67-455b-a114-d499373d948c)

- Caso `Movies_genre`: Combina todos los elementos de las colecciones resultantes en una sola colección
  
  ![Imagen 7](https://github.com/user-attachments/assets/3bfb26c8-88d2-4ed9-a7ff-3708b9b9c89b)

## Trabajo de Inserción

- Película_empleado Dao---------------------------------------------------------------------------
  
  ![Imagen 8](https://github.com/user-attachments/assets/90c3ba5b-e059-455b-bb94-59b4d46c285b)

- Main--------------------------------------------------------------------------------------------
  ![Imagen 9](https://github.com/user-attachments/assets/4bba2776-7bcb-4e79-98e0-169286a1155a)

- Case Class---------------------------------------------------------------------------------------
  ![Imagen 10](https://github.com/user-attachments/assets/fd3a1b12-2677-4ca5-be59-83ec45470fd8)

- Ingreso de datos a la base de datos-----------------------------------------------------------
  ![Imagen 11](https://github.com/user-attachments/assets/cf1ec689-aa80-4d31-b303-b9d11c115247)





