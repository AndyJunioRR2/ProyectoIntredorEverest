package Utils

import InsertAll.Pelicula_keywordsDAO
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
import models.Pelicula_keywords


// Extiende de IOApp.Simple para manejar efectos IO y recursos de forma segura
object MainPelicula_keywords extends IOApp.Simple {
  val path2DataFile2 = "src/main/resources/data/pelicula_keywords.csv"

  val dataSource = new File(path2DataFile2)
    .readCsv[List,Pelicula_keywords](rfc.withHeader.withCellSeparator(','))

  val pelicula_keywords = dataSource.collect {
    case Right(pelicula_keywords) => pelicula_keywords
  }

  /**
   * Punto de entrada principal de la aplicación.
   * Lee temperaturas desde CSV, las inserta en la base de datos,
   * e imprime el número de registros insertados.
   *
   * @return IO[Unit] que representa la secuencia de operaciones
   */
  def run: IO[Unit] =
    Pelicula_keywordsDAO.insertAll(pelicula_keywords)
      .flatMap(result => IO.println(s"Registros insertados: ${result.size}"))
}

