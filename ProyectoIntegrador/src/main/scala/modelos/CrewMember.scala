package modelos

import play.api.libs.json._
case class CrewMember(
                       credit_id: String,
                       department: String,
                       gender: Int,
                       id: Int,
                       job: String,
                       name: String,
                       profile_path: Option[String]
                     )
object CrewMember {
  implicit val crewMemberFormat: Format[CrewMember] = Json.format[CrewMember]
}