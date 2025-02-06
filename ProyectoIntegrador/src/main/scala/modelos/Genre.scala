package modelos

import play.api.libs.json._
case class Genre(
                  id: Int,
                  name: String
                )
/*
object Genre {
  implicit val genreFormat: Format[Genre] = Json.format[Genre]
}
 */