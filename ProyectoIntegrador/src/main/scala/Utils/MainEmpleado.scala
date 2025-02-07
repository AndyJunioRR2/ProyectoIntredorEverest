package Utils

import InsertAll.EmpleadoDAO
import cats.effect.{IO, IOApp}
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import InsertAll.EmpleadoDAO
import cats.effect.{IO, IOApp}
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import java.io.File
import models.Empleado


// Extiende de IOApp.Simple para manejar efectos IO y recursos de forma segura
object MainEmpleado extends IOApp.Simple {
  val path2DataFile2 = "src/main/resources/data/empleado.csv"

  val dataSource = new File(path2DataFile2)
    .readCsv[List,Empleado](rfc.withHeader.withCellSeparator(';'))

  val empleado = dataSource.collect {
    case Right(empleado) => empleado
  }

  /**
   * Punto de entrada principal de la aplicación.
   * Lee temperaturas desde CSV, las inserta en la base de datos,
   * e imprime el número de registros insertados.
   *
   * @return IO[Unit] que representa la secuencia de operaciones
   */
  def run: IO[Unit] =
    EmpleadoDAO.insertAll(empleado)
      .flatMap(result => IO.println(s"Registros insertados: ${result.size}"))
}

