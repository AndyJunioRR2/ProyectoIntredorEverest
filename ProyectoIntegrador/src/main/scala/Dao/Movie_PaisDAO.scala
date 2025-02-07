package Dao

import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.implicits._

import scala.config.Database
import scala.models.Movie_Pais

object Movie_PaisDAO {
  def insert(movie_Pais: Movie_Pais): ConnectionIO[Int] = {
    sql"""
     INSERT INTO movie_pais (id_ISO,id_pelicula)
     VALUES (${movie_Pais.id_ISO}, ${movie_Pais.id_pelicula})
  """.update.run
  }

  def insertAll(movie_Pais: List[Movie_Pais]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      movie_Pais.traverse { movie_Pais => //  ¡AQUÍ SE QUITÓ distinctBy!
        insert(movie_Pais).transact(xa).attempt.flatMap {
          case Left(err) =>
            println(s"Error al insertar palabras clave ${movie_Pais.id_pelicula}: ${err.getMessage}") // Imprime el error
            IO.pure(0)
          case Right(inserted) =>
            IO.pure(inserted)
        }
      }
    }
  }

  def getAll: IO[List[Movie_Pais]] = {
    val query = sql"""
    SELECT * FROM  movie_pais
  """.query[Movie_Pais].to[List]

    Database.transactor.use { xa =>
      query.transact(xa)
    }
  }
}