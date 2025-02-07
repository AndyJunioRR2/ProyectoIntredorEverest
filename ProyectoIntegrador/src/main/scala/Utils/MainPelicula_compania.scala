package Utils

import InsertAll.Pelicula_companiaDAO
import cats.effect.{IO, IOApp}
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import InsertAll.Pelicula_companiaDAO
import cats.effect.{IO, IOApp}
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import java.io.File
import models.Pelicula_compania

// Extiende de IOApp.Simple para manejar efectos IO y recursos de forma segura
object MainPelicula_compania extends IOApp.Simple {
  val path2DataFile2 = "src/main/resources/data/pelicula_compania.csv"

  val dataSource = new File(path2DataFile2)
    .readCsv[List,Pelicula_compania](rfc.withHeader.withCellSeparator(','))

  val pelicula_compania = dataSource.collect {
    case Right(pelicula_compania) => pelicula_compania
  }

  /**
   * Punto de entrada principal de la aplicación.
   * Lee temperaturas desde CSV, las inserta en la base de datos,
   * e imprime el número de registros insertados.
   *
   * @return IO[Unit] que representa la secuencia de operaciones
   */
  def run: IO[Unit] =
    Pelicula_companiaDAO.insertAll(pelicula_compania)
      .flatMap(result => IO.println(s"Registros insertados: ${result.size}"))
}

