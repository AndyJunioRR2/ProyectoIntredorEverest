package Limpiezas
import java.io.File
import kantan.csv._
import kantan.csv.ops._
import play.api.libs.json.Json
import Utils.Limpiezaclase
import modelos.movie_compania
object Lim_Movie_compania extends App{
  val movies = Limpiezaclase.movies()
  implicit val format = Json.format[movie_compania]
  val movieCompanias = movies.flatMap { movie =>
    try {
      val jsonlim = Limpiezaclase.jsonLimpio(movie.production_companies).getOrElse("")
      if (jsonlim.startsWith("{")) {
        val j = Json.parse(jsonlim)
        val moviecompania = j.as[movie_compania]
        List((movie.id, moviecompania.id))
      } else if (jsonlim.startsWith("[") && jsonlim.endsWith("]")) {
        val j = Json.parse(jsonlim)
        j.as[List[movie_compania]].map { movie_compania => (movie.id, movie_compania.id)
        }
      }else{
        Nil
      }
    } catch {
      case _: Exception => Nil
    }
  }
  val writer = new File(Limpiezaclase.crearCsv("movie_companies")).asCsvWriter[(Int, Int)](rfc.withHeader("id_movie", "id_compania"))
  writer.write(movieCompanias).close()
}
