package modelos

case class CastMember(
                       castid: Int,
                       name: String,
                       gender: Int,
                       profilepath: String
                     )
/*
object CastMember {
  implicit val castMemberFormat: Format[CastMember] = Json.format[CastMember]
}
 */