package Dao

import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.implicits._

import scala.config.Database
import scala.models.Pelicula_Idioma

object Pelicula_IdiomaDAO {
  def insert(pelicula_Idioma: Pelicula_Idioma): ConnectionIO[Int] = {
    sql"""
   INSERT INTO pelicula_idioma (
     id_ISO, id_pelicula
   ) VALUES (
     ${pelicula_Idioma.id_ISO},
     ${pelicula_Idioma.id_pelicula}
   )
""".update.run
  }

  def insertAll(pelicula_Idioma: List[Pelicula_Idioma]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      pelicula_Idioma.traverse { pelicula_Idioma => //  ¡AQUÍ SE QUITÓ distinctBy!
        insert(pelicula_Idioma).transact(xa).attempt.flatMap {
          case Left(err) =>
            println(s"Error al insertar palabras clave ${pelicula_Idioma.id_pelicula}: ${err.getMessage}") // Imprime el error
            IO.pure(0)
          case Right(inserted) =>
            IO.pure(inserted)
        }
      }
    }
  }

  def getAll: IO[List[Pelicula_Idioma]] = {
    val query = sql"""
    SELECT * FROM pelicula_idioma
  """.query[Pelicula_Idioma].to[List]

    Database.transactor.use { xa =>
      query.transact(xa)
    }
  }
}

