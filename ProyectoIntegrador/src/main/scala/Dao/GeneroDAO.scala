package Dao

import doobie._
import doobie.implicits._
import cats.effect.IO
import cats.implicits._

import config.Database
import models.Genero

object GeneroDAO {
  def insert(genero: Genero): ConnectionIO[Int] = {
    sql"""
          INSERT INTO genero (id_genero, nombre)
          VALUES (
          ${genero.id_genero},
           ${genero.nombre})

    """.update.run
  }
  def insertAll(genero: List[Genero]): IO[List[Int]] = {
    val uniquegenero = genero.distinctBy(_.id_genero)

    Database.transactor.use { xa =>
      uniquegenero.traverse { genero =>
        insert(genero).transact(xa).attempt.flatMap {
          case Left(err) =>
            IO(println(s"Error al insertar palabras clave ${genero.id_genero}: ${err.getMessage}")) *> IO.pure(0)
          case Right(inserted) =>
            IO.pure(inserted)
        }
      }
    }
  }

  def getAll: IO[List[Genero]] = {
    val query = sql"""
    SELECT * FROM genero
  """.query[Genero].to[List]

    Database.transactor.use { xa =>
      query.transact(xa)
    }
  }
}