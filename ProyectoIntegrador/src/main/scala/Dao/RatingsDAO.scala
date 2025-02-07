package Dao

import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.implicits._

import scala.config.Database
import scala.models.Pelicula

object RatingsDAO {
  def insert(pelicula: Pelicula): ConnectionIO[Int] = {
    sql"""
       INSERT INTO pelicula (
      id_pelicula, imdb_id, titulo, titulo_original, fecha_de_lanzamiento, duracion,
      presupuesto, ingresos, pagina_web, popularity, eslogan, lenguaje_original,
      resumen, ruta_del_poster, adult, numero_votos, promedio_votos, video, estado
    ) VALUES (
      ${pelicula.id_pelicula},
      ${pelicula.imdb_id},
      ${pelicula.titulo},
      ${pelicula.titulo_original},
      ${pelicula.fecha_de_lanzamiento},
      ${pelicula.duracion},
      ${pelicula.presupuesto},
      ${pelicula.ingresos},
      ${pelicula.pagina_web},
      ${pelicula.popularity},
      ${pelicula.eslogan},
      ${pelicula.lenguaje_original},
      ${pelicula.resumen},
      ${pelicula.ruta_del_poster},
      ${pelicula.adult},
      ${pelicula.numero_votos},
      ${pelicula.promedio_votos},
      ${pelicula.video},
      ${pelicula.estado}
    )
    """.update.run
  }

  def insertAll(pelicula: List[Pelicula]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      pelicula.traverse { pelicula => //  ¡AQUÍ SE QUITÓ distinctBy!
        insert(pelicula).transact(xa).attempt.flatMap {
          case Left(err) =>
            println(s"Error al insertar palabras clave ${pelicula.id_pelicula}: ${err.getMessage}") // Imprime el error
            IO.pure(0)
          case Right(inserted) =>
            IO.pure(inserted)
        }
      }
    }
  }

  def getAll: IO[List[Pelicula]] = {
    val query = sql"""
    SELECT * FROM pelicula
  """.query[Pelicula].to[List]

    Database.transactor.use { xa =>
      query.transact(xa)
    }
  }
}

