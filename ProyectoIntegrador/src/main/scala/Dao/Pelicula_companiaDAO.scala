package Dao

import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.implicits._

import scala.config.Database
import scala.models.Pelicula_compania

object Pelicula_companiaDAO {
  def insert(pelicula_compania: Pelicula_compania): ConnectionIO[Int] = {
    sql"""
       INSERT INTO pelicula (
      id_pelicula, id_compania
    ) VALUES (
      ${pelicula_compania.id_pelicula},
      ${pelicula_compania.id_compania},
    )
    """.update.run
  }

  def insertAll(pelicula_compania: List[Pelicula_compania]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      pelicula_compania.traverse { pelicula_compania => //  ¡AQUÍ SE QUITÓ distinctBy!
        insert(pelicula_compania).transact(xa).attempt.flatMap {
          case Left(err) =>
            println(s"Error al insertar palabras clave ${pelicula_compania.id_pelicula}: ${err.getMessage}") // Imprime el error
            IO.pure(0)
          case Right(inserted) =>
            IO.pure(inserted)
        }
      }
    }
  }

  def getAll: IO[List[Pelicula_compania]] = {
    val query = sql""" pelicula_compania
    SELECT * FROM pelicula_compania
  """.query[Pelicula_compania].to[List]

    Database.transactor.use { xa =>
      query.transact(xa)
    }
  }
}

