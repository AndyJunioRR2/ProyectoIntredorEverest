package Dao

import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.implicits._

import scala.config.Database
import scala.models.Pelicula_keywords

object Pelicula_keywordsDAO {
  def insert(pelicula_keywords: Pelicula_keywords): ConnectionIO[Int] = {
    sql"""
       INSERT INTO pelicula_keywords (id_pelicula, id_keyword)
          VALUES (
          ${pelicula_keywords.id_pelicula},
           ${pelicula_keywords.id_keyword})

    """.update.run
  }
  def insertAll(pelicula_keywords: List[Pelicula_keywords]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      pelicula_keywords.traverse { pelicula_keywords => //  ¡AQUÍ SE QUITÓ distinctBy!
        insert(pelicula_keywords).transact(xa).attempt.flatMap {
          case Left(err) =>
            println(s"Error al insertar palabras clave ${pelicula_keywords.id_pelicula}: ${err.getMessage}") // Imprime el error
            IO.pure(0)
          case Right(inserted) =>
            IO.pure(inserted)
        }
      }
    }
  }

  def getAll: IO[List[Pelicula_keywords]] = {
    val query = sql"""
    SELECT * FROM pelicula_keywords
  """.query[Pelicula_keywords].to[List]

    Database.transactor.use { xa =>
      query.transact(xa)
    }
  }
}


