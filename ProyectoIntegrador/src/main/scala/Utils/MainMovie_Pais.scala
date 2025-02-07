package Utils

import InsertAll.Movie_PaisDAO
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
import models.Movie_Pais


// Extiende de IOApp.Simple para manejar efectos IO y recursos de forma segura
object MainMovie_Pais extends IOApp.Simple {
  val path2DataFile2 = "src/main/resources/data/movie_pais.csv"

  val dataSource = new File(path2DataFile2)
    .readCsv[List,Movie_Pais](rfc.withHeader.withCellSeparator(','))

  val movie_Pais = dataSource.collect {
    case Right(movie_Pais) => movie_Pais
  }

  /**
   * Punto de entrada principal de la aplicación.
   * Lee temperaturas desde CSV, las inserta en la base de datos,
   * e imprime el número de registros insertados.
   *
   * @return IO[Unit] que representa la secuencia de operaciones
   */
  def run: IO[Unit] =
    Movie_PaisDAO.insertAll(movie_Pais)
      .flatMap(result => IO.println(s"Registros insertados: ${result.size}"))
}

