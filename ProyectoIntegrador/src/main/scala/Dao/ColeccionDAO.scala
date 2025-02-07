package Dao

import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.implicits._

import scala.config.Database
import scala.models.Coleccion

object ColeccionDAO {
  def insert(coleccion: Coleccion): ConnectionIO[Int] = {
    sql"""
      INSERT INTO coleccion (
        id_coleccion,nombre,ruta_del_poster,ruta_del_fondo
      ) VALUES (
        ${coleccion.id_coleccion}, ${coleccion.nombre},
        ${coleccion.ruta_del_Poster},${coleccion.ruta_del_fondo}

      )
    """.update.run
  }

  def insertAll(coleccion: List[Coleccion]): IO[List[Int]] = {
    val uniquecoleccion = coleccion.distinctBy(_.id_coleccion)

    Database.transactor.use { xa =>
      uniquecoleccion.traverse { coleccion =>
        println(s"Inserting pelicula: ${coleccion}") // Ver los datos que se están insertando
        insert(coleccion).transact(xa).attempt.flatMap {
          case Left(err) =>
            IO(println(s"Error al insertar película ${coleccion.id_coleccion}: ${err.getMessage}")) *> IO.pure(0)
          case Right(inserted) =>
            IO.pure(inserted)
        }
      }
    }
  }

  def getAll: IO[List[Coleccion]] = {
    val query = sql"""
      SELECT * FROM coleccion
    """.query[Coleccion].to[List]

    Database.transactor.use { xa =>
      query.transact(xa)
    }
  }
}