package Dao

import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.implicits._

import scala.config.Database
import scala.models.Compania


object CompaniaDAO {
  def insert(compania: Compania): ConnectionIO[Int] = {
    sql"""
       INSERT INTO compania (nombre,id_compania)
          VALUES (
          ${compania.nombre},
           ${compania.id_compania})

    """.update.run
  }
  def insertAll(compania: List[Compania]): IO[List[Int]] = {

    Database.transactor.use { xa =>
      compania.traverse { compania => //  ¡AQUÍ SE QUITÓ distinctBy!
        insert(compania).transact(xa).attempt.flatMap {
          case Left(err) =>
            println(s"Error al insertar palabras clave ${compania.id_compania}: ${err.getMessage}") // Imprime el error
            IO.pure(0)
          case Right(inserted) =>
            IO.pure(inserted)
        }
      }
    }
  }

  def getAll: IO[List[Compania]] = {
    val query = sql"""
    SELECT * FROM compania
  """.query[Compania].to[List]

    Database.transactor.use { xa =>
      query.transact(xa)
    }
  }
}


