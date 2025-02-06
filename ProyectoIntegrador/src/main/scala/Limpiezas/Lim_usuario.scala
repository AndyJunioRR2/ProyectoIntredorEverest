package Limpiezas
import java.io.File
import kantan.csv._
import kantan.csv.ops._
import play.api.libs.json.Json
import Utils.Limpiezaclase
import modelos.Usuario

object Lim_usuario extends App{
  val movies = Limpiezaclase.movies()
  implicit val format = Json.format[Usuario]
  val Usua = movies.flatMap { movie =>
    try {
      val cleanJson = Limpiezaclase.jsonLimpio(movie.ratings).getOrElse("Incorrecto")
      if (cleanJson.startsWith("{")) {
        val json = Json.parse(cleanJson)
        val use = json.as[Usuario]
        List(
          (
            use.id
            )
        )
      } else if (cleanJson.startsWith("[") && cleanJson.endsWith("]")) {
        val json = Json.parse(cleanJson)
        val atributos = json.as[List[Usuario]]
        atributos.map { user =>
          (
            user.id)
        }
      }else{
        Nil
      }
    } catch {
      case _: Exception => Nil
    }
  }

  val writer = new File(Limpiezaclase.crearCsv("usuario")).asCsvWriter[(Int)](rfc.withHeader("movie_id"))
  writer.write(Usua).close()
}