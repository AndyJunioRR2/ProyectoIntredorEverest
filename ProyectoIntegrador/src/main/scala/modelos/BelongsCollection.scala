package modelos

case class BelongsCollection(
                              id: Int,
                              name: Option[String],
                              posterpath: Option[String],
                              backdroppath: Option[String]
                            )
/*
object BelongsCollection {
  implicit val belongstocollectionFormat: Format[BelongsCollection] = Json.format[BelongsCollection]
}

 */

