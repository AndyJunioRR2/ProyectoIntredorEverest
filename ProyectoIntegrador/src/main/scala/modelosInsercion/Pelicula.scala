package modelosInsercion

case class Pelicula(
                     adult: Option[Boolean],
                     presupuesto: Option[BigDecimal],
                     pagina_web: Option[String],
                     id_pelicula: Int,
                     imdb_id: Option[String],
                     titulo_original: Option[String],
                     resumen: Option[String],
                     popularity: Option[String],
                     ruta_del_poster: Option[String],
                     fecha_de_lanzamiento: Option[String],
                     ingresos: Option[BigDecimal],
                     duracion: Option[Int],
                     estado: Option[String],
                     eslogan: Option[String],
                     titulo: String,
                     video: Option[Boolean],
                     promedio_votos: Option[BigDecimal],
                     numero_votos: Option[Int],
                     lenguaje_original: Option[String]
                   )

