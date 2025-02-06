package modelos

import play.api.libs.json._
case class ProducCountry(
                          iso_3166_1: String,
                          name: String
                        )

object ProducCountry {
  implicit val producCountryFormat: Format[ProducCountry] = Json.format[ProducCountry]
}