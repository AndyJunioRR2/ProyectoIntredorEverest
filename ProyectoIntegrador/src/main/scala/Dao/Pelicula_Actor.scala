package Dao

import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.implicits._

import scala.config.Database
import scala.models.Pelicula_actor

object Pelicula_Actor {
  def insert(pelicula_actor: Pelicula_actor): ConnectionIO[Int] = {
    sql"""
       INSERT INTO pelicula_actor (
      id_pelicula, id_actor,credit_id
    ) VALUES (
      ${pelicula_actor.id_Pelicula},
      ${pelicula_actor.id_Actor},
      ${pelicula_actor.credit_Id}

    )
    """.update.run
  }

  def insertAll(pelicula_actor: List[Pelicula_actor]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      pelicula_actor.traverse { pelicula_actor => //  ¡AQUÍ SE QUITÓ distinctBy!
        insert(pelicula_actor).transact(xa).attempt.flatMap {
          case Left(err) =>
            println(s"Error al insertar palabras clave ${pelicula_actor.id_Pelicula}: ${err.getMessage}") // Imprime el error
            IO.pure(0)
          case Right(inserted) =>
            IO.pure(inserted)
        }
      }
    }
  }

  def getAll: IO[List[Pelicula_actor]] = {
    val query = sql"""
    SELECT * FROM pelicula_actor
  """.query[Pelicula_actor].to[List]

    Database.transactor.use { xa =>
      query.transact(xa)
    }
  }
}

