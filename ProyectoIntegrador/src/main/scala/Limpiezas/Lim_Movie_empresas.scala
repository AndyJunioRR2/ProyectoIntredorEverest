package Limpiezas
import java.io.File
import kantan.csv._
import kantan.csv.ops._
import play.api.libs.json.Json
import Utils.Limpiezaclase
import modelos.movie_empresas
object Lim_Movie_empresas extends App{
  val movies = Limpiezaclase.movies()
  implicit val format = Json.format[movie_empresas]
  val movieEmpresas = movies.flatMap { movie =>
    try {
      val jsonl = Limpiezaclase.jsonLimpio(movie.production_companies).getOrElse("")
      if (jsonl.startsWith("{")) {
        val j = Json.parse(jsonl)
        val movieEmpresas = j.as[movie_empresas]
        List((movieEmpresas.id, movie.id))
      } else if (jsonl.startsWith("[") && jsonl.endsWith("]")) {
        val j = Json.parse(jsonl)
        j.as[List[movie_empresas]].map { movie_empres => (movie_empres.id, movie.id)
        }
      }else{
        Nil
      }
    } catch {
      case _: Exception => Nil
    }
  }
  val writer = new File(Limpiezaclase.crearCsv("movie_empresas")).asCsvWriter[(Int, Int)](rfc.withHeader("id_ISO", "id_movie"))
  writer.write(movieEmpresas).close()
}
