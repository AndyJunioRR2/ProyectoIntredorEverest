package Limpiezas

import kantan.csv._
import kantan.csv.ops._
import modelos._
import play.api.libs.json._

import java.io.File


object prueba extends App {

  val path2DataFile = "data/limpios.csv"

  // Configurar lectura del CSV
  val dataSource = new File(path2DataFile)
    .readCsv[List, Movie](rfc.withHeader.withCellSeparator(';'))

  // Filtrar solo las filas correctamente parseadas a Movie
  val movies = dataSource.collect { case Right(movie) => movie }

  // Inicializar contadores de errores por cada columna
  var erroresPorColumna = Map(
    "genres" -> 0,
    "productionCompanies" -> 0,
    "spokenLanguages" -> 0,
    "keywords" -> 0,
    "cast" -> 0,
    "crew" -> 0,
    "rating" -> 0,
    "belongsToCollection" -> 0
  )

  /**
   * Limpia cadenas JSON corrigiendo errores comunes de formato.
   */
  def limpiarJson(jsonOriginal: String): String = {
    if (jsonOriginal == null || jsonOriginal.trim.isEmpty) {
      return "[]"
    }

    var jsonLimpio = jsonOriginal.trim

    // Paso 1: Correcciones básicas
    jsonLimpio = jsonLimpio
      .replaceAll("""\[""", "[")
      .replaceAll(""""\]""", "]")
      .replaceAll("''", "'")
      .replaceAll("'", "\"")
      .replaceAll("None", "null")
      .replaceAll("True", "true")
      .replaceAll("False", "false")
      .replaceAll("""\\""", "")

    // Paso 2: Asegurar que los objetos y arrays estén bien formados
    if (!jsonLimpio.startsWith("[") && !jsonLimpio.startsWith("{")) {
      if (jsonLimpio.contains(":")) {
        if (!jsonLimpio.startsWith("{")) jsonLimpio = "{" + jsonLimpio
        if (!jsonLimpio.endsWith("}")) jsonLimpio = jsonLimpio + "}"
      } else {
        if (!jsonLimpio.startsWith("[")) jsonLimpio = "[" + jsonLimpio
        if (!jsonLimpio.endsWith("]")) jsonLimpio = jsonLimpio + "]"
      }
    }

    // Paso 3: Correcciones específicas para diferentes estructuras JSON
    jsonLimpio = if (jsonLimpio.contains("\"id\":") && jsonLimpio.contains("\"name\":")) {
      if (!jsonLimpio.startsWith("[")) "[" + jsonLimpio + "]" else jsonLimpio
    } else if (jsonLimpio.contains("\"userId\":") && jsonLimpio.contains("\"rating\":")) {
      if (!jsonLimpio.startsWith("[")) "[" + jsonLimpio + "]" else jsonLimpio
    } else if (jsonLimpio.contains("\"cast_id\":") || jsonLimpio.contains("\"credit_id\":")) {
      if (!jsonLimpio.startsWith("[")) "[" + jsonLimpio + "]" else jsonLimpio
    } else {
      jsonLimpio
    }

    // Paso 4: Limpieza final
    jsonLimpio = jsonLimpio
      .replaceAll(",\\s*\\]", "]")  // Eliminar coma antes de cierre de array
      .replaceAll(",\\s*\\}", "}")  // Eliminar coma antes de cierre de objeto
      .replaceAll("\"\"", "\"")  // Eliminar dobles comillas
      .replaceAll("(?<=[a-zA-Z0-9])\"(?=[a-zA-Z0-9])", "'")  // Corregir comillas en medio de palabras

    // Paso 5: Validación y balanceo de paréntesis **dentro de la misma función**
    val faltanLlaves = jsonLimpio.count(_ == '{') - jsonLimpio.count(_ == '}')
    val faltanCorchetes = jsonLimpio.count(_ == '[') - jsonLimpio.count(_ == ']')

    // Balanceamos correctamente las llaves y corchetes al final
    if (faltanLlaves > 0) jsonLimpio += "}" * faltanLlaves
    if (faltanLlaves < 0) jsonLimpio = "{" * -faltanLlaves + jsonLimpio
    if (faltanCorchetes > 0) jsonLimpio += "]" * faltanCorchetes
    if (faltanCorchetes < 0) jsonLimpio = "[" * -faltanCorchetes + jsonLimpio

    jsonLimpio
  }

  // Función auxiliar para balancear paréntesis
  def balancearParentesis(json: String): String = {
    val stack = scala.collection.mutable.Stack[Char]()
    val resultado = new StringBuilder()

    for (char <- json) {
      char match {
        case '{' | '[' =>
          stack.push(char)
          resultado.append(char)
        case '}' =>
          if (stack.nonEmpty && stack.top == '{') stack.pop() else resultado.insert(0, '{')
          resultado.append(char)
        case ']' =>
          if (stack.nonEmpty && stack.top == '[') stack.pop() else resultado.insert(0, '[')
          resultado.append(char)
        case _ => resultado.append(char)
      }
    }

    // Cerrar los elementos restantes en la pila
    while (stack.nonEmpty) {
      stack.pop() match {
        case '{' => resultado.append('}')
        case '[' => resultado.append(']')
      }
    }

    resultado.toString()
  }


  /**
   * Convierte un String JSON en una Lista de Objetos del Tipo Especificado
   */
  def parseJson[T](jsonString: String)(implicit reads: Reads[T]): Option[List[T]] = {
    try {
      Json.parse(jsonString).validate[List[T]] match {
        case JsSuccess(value, _) => Some(value)
        case JsError(errors) =>
         // println(s"Error parsing JSON: $errors")
          None
      }
    } catch {
      case _: Exception =>
       // println(s"Error al parsear: $jsonString")
        None
    }
  }

  // Procesar y contar errores de cada campo JSON
  movies.foreach { movie =>
    // Contar errores por columna
    if (parseJson[Genre](limpiarJson(movie.genres)).isEmpty) erroresPorColumna = erroresPorColumna.updated("genres", erroresPorColumna("genres") + 1)
    if (parseJson[ProducCompany](limpiarJson(movie.production_companies)).isEmpty) erroresPorColumna = erroresPorColumna.updated("productionCompanies", erroresPorColumna("productionCompanies") + 1)
    if (parseJson[SpokenLanguage](limpiarJson(movie.spoken_languages)).isEmpty) erroresPorColumna = erroresPorColumna.updated("spokenLanguages", erroresPorColumna("spokenLanguages") + 1)
    if (parseJson[KeyWord](limpiarJson(movie.keywords)).isEmpty) erroresPorColumna = erroresPorColumna.updated("keywords", erroresPorColumna("keywords") + 1)
    if (parseJson[CastMember](limpiarJson(movie.cast)).isEmpty) erroresPorColumna = erroresPorColumna.updated("cast", erroresPorColumna("cast") + 1)
    if (parseJson[CrewMember](limpiarJson(movie.crew)).isEmpty) erroresPorColumna = erroresPorColumna.updated("crew", erroresPorColumna("crew") + 1)
    if (parseJson[Rating](limpiarJson(movie.ratings)).isEmpty) erroresPorColumna = erroresPorColumna.updated("rating", erroresPorColumna("rating") + 1)
    if (parseJson[BelongsCollection](limpiarJson(movie.belongs_to_collection)).isEmpty) erroresPorColumna = erroresPorColumna.updated("belongsToCollection", erroresPorColumna("belongsToCollection") + 1)
  }

  // Mostrar el número de errores por cada columna
  println("\n🔍 Errores por columna JSON:")
  erroresPorColumna.foreach { case (columna, errores) =>
    println(s"  - $columna: $errores errores")
  }

  println("Proceso de análisis completado.")
}
