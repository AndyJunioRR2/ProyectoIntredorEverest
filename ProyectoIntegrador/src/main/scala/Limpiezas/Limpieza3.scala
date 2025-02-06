package Limpiezas

import kantan.csv._
import kantan.csv.ops._
import modelos._
import play.api.libs.json._
import java.io.File

object Limpieza3 extends App {

  val path2DataFile = "data/limpios3.csv"

  // Leer el archivo CSV y parsearlo en una lista de objetos Movie
  val dataSource = new File(path2DataFile)
    .readCsv[List, Movie](rfc.withHeader.withCellSeparator(';'))

  // Filtrar solo las filas correctamente parseadas a Movie
  val movies = dataSource.collect { case Right(movie) => movie }

  /**
   * Limpia cadenas JSON corrigiendo errores comunes de formato.
   * @param jsonOriginal Cadena JSON original
   * @return JSON corregido y limpio
   */
  def limpiarJson(jsonOriginal: String): String = {
    if (jsonOriginal == null || jsonOriginal.trim.isEmpty) {
      return "\"Incompleto\""
    }

    var jsonLimpio = jsonOriginal.trim

    // Correcciones básicas en el formato
    // Correcciones básicas en el formato
    jsonLimpio = jsonLimpio
      .replaceAll("""\[""", "[") // Reemplaza comillas escapadas antes de corchetes de apertura
      .replaceAll("""\]""", "]") // Reemplaza comillas escapadas antes de corchetes de cierre
      .replaceAll("''", "'") // Reemplaza dobles comillas simples por una sola
      .replaceAll("'", "\"") // Reemplaza comillas simples por dobles comillas para JSON válido
      .replaceAll("None", "null") // Sustituye la representación de None de Python por null de JSON
      .replaceAll("True", "true") // Convierte valores booleanos de Python a formato JSON
      .replaceAll("False", "false") // Convierte valores booleanos de Python a formato JSON
      .replaceAll("""\\""", "") // Elimina barras invertidas innecesarias


    // Correcciones de balance de llaves y corchetes
    val balanced = jsonLimpio.count(_ == '{') == jsonLimpio.count(_ == '}') &&
      jsonLimpio.count(_ == '[') == jsonLimpio.count(_ == ']')

    if (!balanced) {
      jsonLimpio = balancearParentesis(jsonLimpio)
    }

    jsonLimpio
  }

  /**
   * Balancea llaves y corchetes en un JSON desbalanceado
   * @param json Cadena JSON posiblemente mal formada
   * @return JSON con llaves y corchetes balanceados
   */
  def balancearParentesis(json: String): String = {
    var result = json
    val faltanLlaves = result.count(_ == '{') - result.count(_ == '}')
    val faltanCorchetes = result.count(_ == '[') - result.count(_ == ']')

    if (faltanLlaves > 0) {
      result = result + ("}" * faltanLlaves)
    } else if (faltanLlaves < 0) {
      result = ("{" * -faltanLlaves) + result
    }

    if (faltanCorchetes > 0) {
      result = result + ("]" * faltanCorchetes)
    } else if (faltanCorchetes < 0) {
      result = ("[" * -faltanCorchetes) + result
    }

    result
  }

  /**
   * Convierte un String JSON en una Lista de Objetos del Tipo Especificado
   * @param jsonString Cadena JSON a parsear
   * @tparam T Tipo de datos esperado en el JSON
   * @return Lista de objetos parseados
   */
  def parseJson[T](jsonString: String)(implicit reads: Reads[T]): List[T] = {
    try {
      Json.parse(jsonString).validate[List[T]] match {
        case JsSuccess(value, _) => value
        case JsError(errors) =>
          println(s"Error parsing JSON: $errors")
          List.empty[T]
      }
    } catch {
      case _: Exception =>
        println(s"Error al parsear: $jsonString")
        List.empty[T]
    }
  }

  /**
   * Guarda una lista de datos en un archivo CSV
   * @param data Datos a escribir en el CSV
   * @param filePath Ruta del archivo CSV de salida
   * @param headers Encabezados del CSV
   */
  def guardarCsv[T: HeaderEncoder](data: List[T], filePath: String, headers: Seq[String]): Unit = {
    val outputFile = new File(filePath)
    outputFile.writeCsv(data, rfc.withHeader(headers: _*).withCellSeparator(','))
    println(s"Datos guardados en $filePath")
  }

  // Aplicar limpieza a los campos JSON dentro de cada objeto Movie
  val moviesLimpios = movies.map { movie =>
    movie.copy(
      ratings = limpiarJson(movie.ratings),
      genres = limpiarJson(movie.genres),
      production_companies = limpiarJson(movie.production_companies),
      production_countries = limpiarJson(movie.production_countries),
      keywords = limpiarJson(movie.keywords),
      crew = limpiarJson(movie.crew)
    )
  }

  // Procesar y guardar cada campo JSON en archivos CSV separados
  val ratings = moviesLimpios.flatMap(movie => parseJson[Rating](movie.ratings))
  guardarCsv(ratings, "data/ratings.csv", Seq("user_id", "movie_id", "ratings", "timestamp"))

  val genres = moviesLimpios.flatMap(movie => parseJson[Genre](movie.genres))
  guardarCsv(genres, "data/genre.csv", Seq("id", "name"))

  val companies = moviesLimpios.flatMap(movie => parseJson[ProducCompany](movie.production_companies))
  guardarCsv(companies, "data/production_companies.csv", Seq("name", "id"))

  val countries = moviesLimpios.flatMap(movie => parseJson[ProducCountry](movie.production_countries))
  guardarCsv(countries, "data/production_countries.csv", Seq("iso_3166_1", "name"))

  val keywords = moviesLimpios.flatMap(movie => parseJson[KeyWord](movie.keywords))
  guardarCsv(keywords, "data/keywords.csv", Seq("id", "name"))

  val crew = moviesLimpios.flatMap(movie => parseJson[CrewMember](movie.crew))
  guardarCsv(crew, "data/crew.csv", Seq("id", "name", "department", "job", "gender", "profile_path"))

  println("Proceso de limpieza, deserialización y guardado completado.")
}