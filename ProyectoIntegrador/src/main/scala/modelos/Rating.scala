package modelos

import play.api.libs.json._
case class Rating(userId: Int,
                  rating: Double,
                  timestamp: Long
                 )
object Rating {
  implicit val ratingFormat: Format[Rating] = Json.format[Rating]
}