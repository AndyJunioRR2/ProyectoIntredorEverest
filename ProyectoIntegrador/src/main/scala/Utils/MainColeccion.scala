package Utils


import InsertAll.ColeccionDAO
import cats.effect.{IO, IOApp}
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import InsertAll.ColeccionDAO
import cats.effect.{IO, IOApp}
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import java.io.File
import models.Coleccion


// Extiende de IOApp.Simple para manejar efectos IO y recursos de forma segura
object MainColeccion extends IOApp.Simple {
  val path2DataFile2 = "src/main/resources/data/coleccion.csv"

  val dataSource = new File(path2DataFile2)
    .readCsv[List,Coleccion](rfc.withHeader.withCellSeparator(','))

  val coleccion = dataSource.collect {
    case Right(coleccion) => coleccion
  }

  /**
   * Punto de entrada principal de la aplicación.
   * Lee temperaturas desde CSV, las inserta en la base de datos,
   * e imprime el número de registros insertados.
   *
   * @return IO[Unit] que representa la secuencia de operaciones
   */
  def run: IO[Unit] =
    ColeccionDAO.insertAll(coleccion)
      .flatMap(result => IO.println(s"Registros insertados: ${result.size}"))
}

