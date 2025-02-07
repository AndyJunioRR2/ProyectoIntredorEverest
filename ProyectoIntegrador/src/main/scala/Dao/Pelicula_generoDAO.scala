package Dao

import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.implicits._

import scala.config.Database
import scala.models.Pelicula_genero

object Pelicula_generoDAO {
  def insert(pelicula_genero: Pelicula_genero): ConnectionIO[Int] = {
    sql"""
       INSERT INTO pelicula_genero (id_pelicula, id_genero)
          VALUES (
          ${pelicula_genero.id_pelicula},
           ${pelicula_genero.id_genero})

    """.update.run
  }
  def insertAll(pelicula_genero: List[Pelicula_genero]): IO[List[Int]] = {
    val uniquePelicula_genero = pelicula_genero.distinctBy(_.id_pelicula)

    Database.transactor.use { xa =>
      uniquePelicula_genero.traverse { pelicula_genero =>
        insert(pelicula_genero).transact(xa).attempt.flatMap {
          case Left(err) =>
            IO(println(s"Error al insertar palabras clave ${pelicula_genero.id_pelicula}: ${err.getMessage}")) *> IO.pure(0)
          case Right(inserted) =>
            IO.pure(inserted)
        }
      }
    }
  }

  def getAll: IO[List[Pelicula_genero]] = {
    val query = sql"""
    SELECT * FROM pelicula_genero
  """.query[Pelicula_genero].to[List]

    Database.transactor.use { xa =>
      query.transact(xa)
    }
  }
}


