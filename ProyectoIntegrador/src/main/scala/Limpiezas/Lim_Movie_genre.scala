package Limpiezas
import java.io.File
import kantan.csv._
import kantan.csv.ops._
import play.api.libs.json.Json
import Utils.Limpiezaclase
import modelos.movie_genre
object Lim_Movie_genre extends App{
  implicit val format = Json.format[movie_genre]
  val movies = Limpiezaclase.movies()
  val movieGenre = movies.flatMap { movie =>
    try {
      val jsonlim = Limpiezaclase.jsonLimpio(movie.genres).getOrElse("")
      if (jsonlim.startsWith("{")) {
        val json = Json.parse(jsonlim)
        val movieGenre = json.as[movie_genre]
        List(
          (movie.id, movieGenre.id)
        )
      } else if (jsonlim.startsWith("[") && jsonlim.endsWith("]")) {
        val json = Json.parse(jsonlim)
        val atrib = json.as[List[movie_genre]]
        atrib.map { genre =>
          (movie.id, genre.id)
        }
      }else{
        Nil
      }
    } catch {
      case _: Exception => Nil
    }
  }
  val writer = new File(Limpiezaclase.crearCsv("movie_genre")).asCsvWriter[(Int, Int)](rfc.withHeader("movie_id", "genre_id"))
  writer.write(movieGenre).close()
}
