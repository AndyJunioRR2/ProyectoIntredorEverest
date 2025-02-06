package Limpiezas

import java.io.File
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import play.api.libs.json._
import Utils.Limpiezaclase
import modelos.movie_pais

object Lim_Movie_pais extends App {
  implicit val format: Format[movie_pais] = Json.format[movie_pais]

  val movies = Limpiezaclase.movies()

  val pais = movies.flatMap { movie =>
    try {
      val limpiarJson = Limpiezaclase.jsonLimpio(movie.production_countries).getOrElse("")
      if (limpiarJson.startsWith("{")) {
        val j = Json.parse(limpiarJson)
        val country = j.as[movie_pais]
        List((Limpiezaclase.limpiarString(country.iso_3166_1) , movie.id ))

      } else if (limpiarJson.startsWith("[") && limpiarJson.endsWith("]")) {
        val j = Json.parse(limpiarJson)
        val atrib = j.as[List[movie_pais]]
        atrib.distinctBy(_.iso_3166_1).map { movie_country =>
          (Limpiezaclase.limpiarString(movie_country.iso_3166_1) , movie.id )
        }
      } else {
        Nil
      }
    } catch {
      case _: Exception => Nil
    }
  }
  new File(Limpiezaclase.crearCsv("movie_pais"))
    .asCsvWriter[(String, Int)](rfc.withHeader("id_ISO", "id_movie"))
    .write(pais)
    .close()
}
