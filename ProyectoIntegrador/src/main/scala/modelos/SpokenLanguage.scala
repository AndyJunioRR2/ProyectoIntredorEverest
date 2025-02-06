package modelos

case class SpokenLanguage(
                           iso6391: String,
                           name: Option[String]
                         )
/*
object SpokenLanguage {
  implicit val spokenLanguageFormat: Format[SpokenLanguage] = Json.format[SpokenLanguage]
}

 */