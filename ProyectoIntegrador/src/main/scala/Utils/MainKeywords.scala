package Utils

import InsertAll.KeywordsDAO
import cats.effect.{IO, IOApp}
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import InsertAll.KeywordsDAO
import cats.effect.{IO, IOApp}
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import java.io.File
import models.Keywords


// Extiende de IOApp.Simple para manejar efectos IO y recursos de forma segura
object MainKeywords extends IOApp.Simple {
  val path2DataFile2 = "src/main/resources/data/Keywords.csv"

  val dataSource = new File(path2DataFile2)
    .readCsv[List,Keywords](rfc.withHeader.withCellSeparator(';'))

  val keywords = dataSource.collect {
    case Right(keywords) => keywords
  }

  /**
   * Punto de entrada principal de la aplicación.
   * Lee temperaturas desde CSV, las inserta en la base de datos,
   * e imprime el número de registros insertados.
   *
   * @return IO[Unit] que representa la secuencia de operaciones
   */
  def run: IO[Unit] =
    KeywordsDAO.insertAll(keywords)
      .flatMap(result => IO.println(s"Registros insertados: ${result.size}"))
}

