package modelos

import play.api.libs.json._
case class ProducCompany(
                          name : String,
                          id: Int
                        )
object ProducCompany {
  implicit val producCompanyFormat: Format[ProducCompany] = Json.format[ProducCompany]
}