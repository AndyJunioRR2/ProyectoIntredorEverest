package Limpiezas
import java.io.File
import kantan.csv._
import kantan.csv.ops._
import play.api.libs.json.Json
import Utils.Limpiezaclase
import modelos.SpokenLanguage

object LimpiezaSpoken extends App{
  implicit val format = Json.format[SpokenLanguage]
  val movies = Limpiezaclase.movies()

  val languageProcessed = movies.flatMap { movie =>
    try {
      val jsonlimpi = Limpiezaclase.jsonLimpio(movie.spoken_languages).getOrElse("")
      if (jsonlimpi.startsWith("{")) {
        // Json una coleccion
        val json = Json.parse(jsonlimpi)
        val spokenLanguage = json.as[SpokenLanguage]
        List(
          (

            Limpiezaclase.limpiarString(spokenLanguage.iso6391),
            Limpiezaclase.limpiarString(spokenLanguage.name.getOrElse("Incomplete"))
          )
        )
      } else if (jsonlimpi.startsWith("[") && jsonlimpi.endsWith("]")) {
        val json = Json.parse(jsonlimpi)
        val atributos = json.as[List[SpokenLanguage]]
        val unique = atributos.distinctBy(_.iso6391)
        unique.map { spoken =>
          (
            Limpiezaclase.limpiarString(spoken.iso6391),
            Limpiezaclase.limpiarString(spoken.name.getOrElse("Incomplete"))
          )
        }
      }else{
        Nil
      }
    } catch {
      case _: Exception => Nil
    }
  }

  val writer = new File(Limpiezaclase.crearCsv("spoken_languages")).asCsvWriter[(String, String)](rfc.withHeader("id", "name"))
  writer.write(languageProcessed).close()
}