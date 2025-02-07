package Dao

import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.implicits._

import scala.config.Database
import scala.models.Actor

object ActorDAO {
  def insert(actor: Actor): ConnectionIO[Int] = {
    sql"""
       INSERT INTO actor (
      id_actor, nombre,genero,ruta_de_perfil
    ) VALUES (
      ${actor.id_actor},
      ${actor.nombre},
      ${actor.genero},
      ${actor.ruta_de_perfil}

    )
    """.update.run
  }

  def insertAll(actor: List[Actor]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      actor.traverse { actor => //  ¡AQUÍ SE QUITÓ distinctBy!
        insert(actor).transact(xa).attempt.flatMap {
          case Left(err) =>
            println(s"Error al insertar palabras clave ${actor.id_actor}: ${err.getMessage}") // Imprime el error
            IO.pure(0)
          case Right(inserted) =>
            IO.pure(inserted)
        }
      }
    }
  }

  def getAll: IO[List[Actor]] = {
    val query = sql"""
    SELECT * FROM actor
  """.query[Actor].to[List]

    Database.transactor.use { xa =>
      query.transact(xa)
    }
  }
}

