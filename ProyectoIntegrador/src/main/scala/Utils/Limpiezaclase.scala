package Utils
import java.io.File
import kantan.csv._
import kantan.csv.ops._
import play.api.libs.json._
import modelos.Movie

object Limpiezaclase extends App {

  def jsonLimpio(jsonStr: String): Option[String] = {
    try {
      val fixedJsonStr = jsonStr
        .trim
        .replaceAll("""\[""", "[") // Reemplaza comillas escapadas antes de corchetes de apertura
        .replaceAll("""\]""", "]") // Reemplaza comillas escapadas antes de corchetes de cierre
        .replaceAll("''", "'") // Reemplaza dobles comillas simples por una sola
        .replaceAll("'", "\"") // Reemplaza comillas simples por dobles comillas para JSON válido
        .replaceAll("None", "null") // Sustituye la representación de None de Python por null de JSON
        .replaceAll("True", "true") // Convierte valores booleanos de Python a formato JSON
        .replaceAll("False", "false") // Convierte valores booleanos de Python a formato JSON
        .replaceAll("""\\""", "") // Elimina barras invertidas innecesarias
      // Intenta parsear el JSON
      val json = Json.parse(fixedJsonStr)
      Some(Json.stringify(json))
    } catch {
      case _: Throwable => None
    }
  }

  def fechalimpia(value: String): String = {
    val cleanValue = Limpiezaclase.limpiarString(value).trim // Elimina espacios en blanco
    val datePattern = """\d{4}-\d{2}-\d{2}""".r // Expresión regular para fechas en formato YYYY-MM-DD
    val formattedDate = datePattern.findFirstIn(cleanValue) match {
      case Some(date) => date
      case None => "0000-01-01" // Valor por defecto si la fecha no es válida
    }
    formattedDate.replaceAll("\"", "") // Elimina comillas innecesarias
  }

  def limpiarString(value: String): String = {
    // Verifica si el valor es nulo o está vacío después de eliminar espacios en blanco
    if (value == null || value.trim.isEmpty)
      "Incompleto" // Si es nulo o vacío, retorna "Incomplete"
    else
      value.trim.replaceAll("\\s+", " ") // Elimina espacios innecesarios y reemplaza múltiples espacios por uno solo
  }

  def movies(): List[Movie] = {
    val pathDataFile = "data/pi_movies_complete.csv"
    new File(pathDataFile).readCsv[List, Movie](rfc.withHeader(true).withCellSeparator(';'))
      .collect {
        case Right(movie) => movie
      }.toList
  }

  def crearCsv(name: String): String = {
    s"data/${name}.csv"
  }
}


