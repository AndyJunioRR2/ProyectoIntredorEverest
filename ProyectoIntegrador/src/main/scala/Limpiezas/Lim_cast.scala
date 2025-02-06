package Limpiezas
import java.io.File
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import play.api.libs.json._
import Utils.Limpiezaclase
import modelos.CastMember
object Lim_cast extends App {
  implicit val format: Format[CastMember] = Json.format[CastMember]
  val movies = Limpiezaclase.movies()
  val cast = movies.flatMap { movie =>
    try {
      val limpiarJson = Limpiezaclase.jsonLimpio(movie.cast).getOrElse("")
      if (limpiarJson.startsWith("{")) {
        val json = Json.parse(limpiarJson)
        val movieCast = json.as[CastMember]
        List((movieCast.castid, movieCast.name, movieCast.gender, movieCast.profilepath))
      } else if (limpiarJson.startsWith("[") && limpiarJson.endsWith("]")) {
        val j = Json.parse(limpiarJson)
        j.as[List[CastMember]].map { movieCas =>
          (movieCas.castid, movieCas.name, movieCas.gender, movieCas.profilepath)
        }
      } else {
        Nil
      }
    } catch {
      case _: Exception => Nil
    }
  }
  new File(Limpiezaclase.crearCsv("movie_cast"))
    .asCsvWriter[(Int, String, Int, String)](rfc.withHeader("id_actor", "nombre", "genero", "ruta_de_perfil"))
    .write(cast)
    .close()
}
