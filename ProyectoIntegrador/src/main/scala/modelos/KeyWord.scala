package modelos

import play.api.libs.json._
case class KeyWord(id: Int,
                   name: Option[String]
                  )
object KeyWord {
  implicit val keyWordFormat: Format[KeyWord] = Json.format[KeyWord]
}

