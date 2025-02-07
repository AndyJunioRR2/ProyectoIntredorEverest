package Dao

import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.implicits._

import scala.config.Database
import scala.models.Empleado


object EmpleadoDAO {
  def insert(empleado: Empleado): ConnectionIO[Int] = {
    sql"""
      INSERT INTO empleado (
      id_empleado, nombre, departamento, job, genero, ruta_del_perfil
    ) VALUES (
      ${empleado.id_empleado},
      ${empleado.nombre},
      ${empleado.departamento},
      ${empleado.job},
      ${empleado.genero},
      ${empleado.ruta_del_perfil}
    )

    """.update.run
  }
  def insertAll(empleado: List[Empleado]): IO[List[Int]] = {

    Database.transactor.use { xa =>
      empleado.traverse { empleado => //  ¡AQUÍ SE QUITÓ distinctBy!
        insert(empleado).transact(xa).attempt.flatMap {
          case Left(err) =>
            println(s"Error al insertar palabras clave ${empleado.id_empleado}: ${err.getMessage}") // Imprime el error
            IO.pure(0)
          case Right(inserted) =>
            IO.pure(inserted)
        }
      }
    }
  }

  def getAll: IO[List[Empleado]] = {
    val query = sql"""
    SELECT * FROM empleado
  """.query[Empleado].to[List]

    Database.transactor.use { xa =>
      query.transact(xa)
    }
  }
}


