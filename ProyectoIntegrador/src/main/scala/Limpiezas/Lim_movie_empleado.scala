
package Limpiezas
import java.io.File
import kantan.csv._
import kantan.csv.ops._
import play.api.libs.json.Json
import Utils.Limpiezaclase
import modelos.movie_empleado

object Lim_movie_empleado extends App {
  val movies = Limpiezaclase.movies()
  implicit val format = Json.format[movie_empleado]
  val movieEmple= movies.flatMap { movie =>
    try {
      val cleanJson = Limpiezaclase.jsonLimpio(movie.crew).getOrElse("")
      if (cleanJson.startsWith("{")) {
        val json = Json.parse(cleanJson)
        val movieEmple = json.as[movie_empleado]
        List(
          (
            movieEmple.staff_id,
            movie.id,
            Limpiezaclase.limpiarString(movieEmple.credit_id),
            Limpiezaclase.limpiarString(movieEmple.department),
            Limpiezaclase.limpiarString(movieEmple.job)
          )
        )
      } else if (cleanJson.startsWith("[") && cleanJson.endsWith("]")) {
        val json = Json.parse(cleanJson)
        val atributos = json.as[List[movie_empleado]]
        atributos.map { movieEmple =>
          (
            movieEmple.staff_id,
            movie.id,
            Limpiezaclase.limpiarString(movieEmple.credit_id),
            Limpiezaclase.limpiarString(movieEmple.department),
            Limpiezaclase.limpiarString(movieEmple.job)
          )
        }
      }else{
        Nil
      }
    } catch {
      case _: Exception => Nil
    }
  }
  val writer = new File(Limpiezaclase.crearCsv("movies_empleado")).asCsvWriter[(Int, Int, String, String, String)](rfc.withHeader("id_empleado", "id_pelicula", "credit_id", "departamento", "trabajo"))
  writer.write(movieEmple).close()
}


