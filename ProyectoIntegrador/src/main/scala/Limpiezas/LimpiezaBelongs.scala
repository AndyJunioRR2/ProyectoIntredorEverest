
package Limpiezas
import java.io.File
import kantan.csv._
import kantan.csv.ops._
import play.api.libs.json.Json
import Utils.Limpiezaclase
import modelos.BelongsCollection

object LimpiezaBelongs extends App {
  val movies = Limpiezaclase.movies()
  implicit val format = Json.format[BelongsCollection]
  val collectionsProcessed = movies.flatMap { movie =>
    try {
      val jsonLimpi = Limpiezaclase.jsonLimpio(movie.belongs_to_collection).getOrElse("[]").trim
      if (jsonLimpi.startsWith("{")) {
        val json = Json.parse(jsonLimpi)
        val belongsCollection = json.as[BelongsCollection]
        List((belongsCollection.id,
            Limpiezaclase.limpiarString(belongsCollection.name.getOrElse("Incomplete")),
            Limpiezaclase.limpiarString(belongsCollection.posterpath.getOrElse("Incomplete")),
            Limpiezaclase.limpiarString(belongsCollection.backdroppath.getOrElse("Incomplete"))))
      } else if (jsonLimpi.startsWith("[") && jsonLimpi.endsWith("]")) {
        val json = Json.parse(jsonLimpi)
        val atrib = json.as[List[BelongsCollection]]
        val unique = atrib.distinctBy(_.id)
        unique.map { belongsCollection =>
          (belongsCollection.id,
            Limpiezaclase.limpiarString(belongsCollection.name.getOrElse("Incomplete")),
            Limpiezaclase.limpiarString(belongsCollection.posterpath.getOrElse("Incomplete")),
            Limpiezaclase.limpiarString(belongsCollection.backdroppath.getOrElse("Incomplete")))
        }
      } else {
        Nil
      }
    } catch {
      case e: Exception =>
        Nil
    }
  }
  val writer = new File(Limpiezaclase.crearCsv("belong_to_collection")).asCsvWriter[(Int, String, String, String)](rfc.withHeader("id", "name", "posterpath", "backdroppath"))
  writer.write(collectionsProcessed).close()
}


