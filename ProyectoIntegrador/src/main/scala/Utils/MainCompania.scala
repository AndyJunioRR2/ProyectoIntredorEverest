package Utils


import InsertAll.CompaniaDAO
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
import models.Compania


// Extiende de IOApp.Simple para manejar efectos IO y recursos de forma segura
object MainCompania extends IOApp.Simple {
  val path2DataFile2 = "src/main/resources/data/compania.csv"

  val dataSource = new File(path2DataFile2)
    .readCsv[List,Compania](rfc.withHeader.withCellSeparator(';'))

  val compania = dataSource.collect {
    case Right(compania) => compania
  }

  /**
   * Punto de entrada principal de la aplicación.
   * Lee temperaturas desde CSV, las inserta en la base de datos,
   * e imprime el número de registros insertados.
   *
   * @return IO[Unit] que representa la secuencia de operaciones
   */
  def run: IO[Unit] =
    CompaniaDAO.insertAll(compania)
      .flatMap(result => IO.println(s"Registros insertados: ${result.size}"))
}

