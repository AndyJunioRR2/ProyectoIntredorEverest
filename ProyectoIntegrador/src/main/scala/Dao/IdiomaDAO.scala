package Dao

import doobie._
import doobie.implicits._
import cats.effect.IO
import cats.implicits._

import config.Database
import models.Idioma

object IdiomaDAO {
  def insert(idioma: Idioma): ConnectionIO[Int] = {
    sql"""
      INSERT INTO idioma (
        id_ISO,nombre
      ) VALUES (
        ${idioma.id_ISO}, ${idioma.nombre}

      )
    """.update.run
  }

  def insertAll(idioma: List[Idioma]): IO[List[Int]] = {
    val uniquedioma = idioma.distinctBy(_.id_ISO)

    Database.transactor.use { xa =>
      uniquedioma.traverse { idioma =>
        println(s"Inserting pelicula: ${idioma}") // Ver los datos que se están insertando
        insert(idioma).transact(xa).attempt.flatMap {
          case Left(err) =>
            IO(println(s"Error al insertar película ${idioma.id_ISO}: ${err.getMessage}")) *> IO.pure(0)
          case Right(inserted) =>
            IO.pure(inserted)
        }
      }
    }
  }

  def getAll: IO[List[Idioma]] = {
    val query = sql"""
      SELECT * FROM idioma
    """.query[Idioma].to[List]

    Database.transactor.use { xa =>
      query.transact(xa)
    }
  }
}