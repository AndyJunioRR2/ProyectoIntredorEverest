package modelosInsercion

case class Actor(
                  id_actor: BigDecimal,  // DECIMAL(15,2) -> Se usa BigDecimal en Scala
                  nombre: String,
                  genero: Int,
                  ruta_de_perfil: Option[String] // Puede ser NULL, por eso usamos Option

                )

// Puede ser NULL

