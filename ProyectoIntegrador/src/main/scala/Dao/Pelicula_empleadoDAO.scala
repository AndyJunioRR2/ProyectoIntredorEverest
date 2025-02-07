package Dao

import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.implicits._

import scala.config.Database
import scala.models.Pelicula_empleado

object Pelicula_empleadoDAO {
  def insert(pelicula_empleado: Pelicula_empleado): ConnectionIO[Int] = {
    sql"""
    INSERT INTO pelicula_empleado(
      id_empleado, id_pelicula, credit_id, departamento, trabajo
    ) VALUES (
      ${pelicula_empleado.id_Empleado},
      ${pelicula_empleado.id_Pelicula},
      ${pelicula_empleado.credit_id},
      ${pelicula_empleado.departamento},
      ${pelicula_empleado.trabajo}
    )
  """.update.run
  }

  def insertAll(pelicula_empleado: List[Pelicula_empleado]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      pelicula_empleado.traverse { pelicula_empleado => //  ¡AQUÍ SE QUITÓ distinctBy!
        insert(pelicula_empleado).transact(xa).attempt.flatMap {
          case Left(err) =>
            println(s"Error al insertar palabras clave ${pelicula_empleado.id_Pelicula}: ${err.getMessage}") // Imprime el error
            IO.pure(0)
          case Right(inserted) =>
            IO.pure(inserted)
        }
      }
    }
  }

  def getAll: IO[List[Pelicula_empleado]] = {
    val query = sql"""
    SELECT * FROM pelicula_empleado
  """.query[Pelicula_empleado].to[List]

    Database.transactor.use { xa =>
      query.transact(xa)
    }
  }
}

