package Utils

import cats.effect.{IO, IOApp}
import kantan.csv._
import kantan.csv.ops._
import InsertAll. PeliculaDAO
import kantan.csv.generic._

import java.io.File
import models. Pelicula




// Extiende de IOApp.Simple para manejar efectos IO y recursos de forma segura
object MainPelicula extends IOApp.Simple {
  val path2DataFile2 = "src/main/resources/data/pelicula.csv"

  val dataSource = new File(path2DataFile2)
    .readCsv[List, Pelicula](rfc.withHeader.withCellSeparator(','))

  val pelicula = dataSource.collect {
    case Right(pelicula) => pelicula
  }

  /**
   * Punto de entrada principal de la aplicación.
   * Lee temperaturas desde CSV, las inserta en la base de datos,
   * e imprime el número de registros insertados.
   *
   * @return IO[Unit] que representa la secuencia de operaciones
   */
  def run: IO[Unit] =
    PeliculaDAO.insertAll(pelicula)
      .flatMap(result => IO.println(s"Registros insertados: ${result.size}"))
}
