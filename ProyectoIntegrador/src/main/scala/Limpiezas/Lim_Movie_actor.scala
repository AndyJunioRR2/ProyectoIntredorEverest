package Limpiezas
import java.io.File
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import play.api.libs.json.Json
import modelos.movie_actor
import Utils.Limpiezaclase
object Lim_Movie_actor extends App{
  val movies = Limpiezaclase.movies()
  implicit val format = Json.format[movie_actor]
  val ActorM = movies.flatMap { movie =>
    try {
      val limpioJson = Limpiezaclase.jsonLimpio(movie.cast).getOrElse("")
      if (limpioJson.startsWith("{")) {
        // Caso donde el JSON es un objeto (una sola colección)
        val json = Json.parse(limpioJson)
        val movieActor = json.as[movie_actor]
        List((movieActor.cast_id, movie.id, movieActor.credit_id))
      } else if (limpioJson.startsWith("[") && limpioJson.endsWith("]")) {
        val j = Json.parse(limpioJson)
        val atrib = j.as[List[movie_actor]]
        atrib.map { movieActor =>
          (movieActor.cast_id,movie.id, movieActor.credit_id)
        }
      }else{
        Nil
      }
    } catch {
      case _: Exception => Nil
    }
  }

  val writer = new File(Limpiezaclase.crearCsv("movie_actor")).asCsvWriter[(Int, Int, String)](rfc.withHeader("id_actor", "id_movie", "credit_id"))
  writer.write(ActorM).close()
}