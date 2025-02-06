package Limpiezas
import java.io.File
import kantan.csv._
import kantan.csv.ops._
import play.api.libs.json.Json
import Utils.Limpiezaclase
import modelos.movie_keyword
object Lim_Movies_keyword extends App{
  val movies = Limpiezaclase.movies()
  implicit val format = Json.format[movie_keyword]

  val movieKeywordProcessed = movies.flatMap { movie =>
    try {
      val jsonlim = Limpiezaclase.jsonLimpio(movie.keywords).getOrElse("")
      if (jsonlim.startsWith("{")) {
        val j = Json.parse(jsonlim)
        val movieKeys = j.as[movie_keyword]
        List((movieKeys.id, movie.id))
      } else if (jsonlim.startsWith("[") && jsonlim.endsWith("]")) {
        val json = Json.parse(jsonlim)
        val atrib = json.as[List[movie_keyword]]
        atrib.map { key => (key.id, movie.id)
        }
      }else{
        Nil
      }
    } catch {
      case _: Exception => Nil
    }
  }
  val writer = new File(Limpiezaclase.crearCsv("movie_keyword")).asCsvWriter[(Int, Int)](rfc.withHeader("id_keywords", "id_movie"))
  writer.write(movieKeywordProcessed).close()
}