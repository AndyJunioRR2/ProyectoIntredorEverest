
package Limpiezas

import java.io.File
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import play.api.libs.json._
import Utils.Limpiezaclase
import modelos.Language_Dubbling

object Lim_Language_Dub extends App {
  implicit val format: Format[Language_Dubbling] = Json.format[Language_Dubbling]

  val movies = Limpiezaclase.movies()

  val languageDubblingProcessed = movies.flatMap { movie =>
    try {
      val limpiarJson = Limpiezaclase.jsonLimpio(movie.spoken_languages).getOrElse("")
      if (limpiarJson.startsWith("{")) {
        val json = Json.parse(limpiarJson)
        val LanguageDubb = json.as[Language_Dubbling]
        List((movie.id, Limpiezaclase.limpiarString(LanguageDubb.iso6391)))

      } else if (limpiarJson.startsWith("[") && limpiarJson.endsWith("]")) {
        val json = Json.parse(limpiarJson)
        val atributos = json.as[List[Language_Dubbling]]
        atributos.distinctBy(_.iso6391).map { Language_Dubb =>
          (movie.id, Limpiezaclase.limpiarString(Language_Dubb.iso6391))
        }
      } else {
        Nil
      }
    } catch {
      case _: Exception => Nil
    }
  }

  new File(Limpiezaclase.crearCsv("movie_Dubbling"))
    .asCsvWriter[(Int, String)](rfc.withHeader("id_movie", "id_ISO"))
    .write(languageDubblingProcessed)
    .close()
}
