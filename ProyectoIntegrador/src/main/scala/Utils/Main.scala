package Utils


import InsertAll.IdiomaDAO
import cats.effect.{IO, IOApp}
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import InsertAll.IdiomaDAO
import cats.effect.{IO, IOApp}
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import java.io.File
import models.Idioma


// Extiende de IOApp.Simple para manejar efectos IO y recursos de forma segura
object Main extends IOApp.Simple {
  val path2DataFile2 = "src/main/resources/data/idioma.csv"

  val dataSource = new File(path2DataFile2)
    .readCsv[List, Idioma](rfc.withHeader.withCellSeparator(';'))

  val idioma = dataSource.collect {
    case Right(idioma) => idioma
  }

  /**
   * Punto de entrada principal de la aplicación.
   * Lee temperaturas desde CSV, las inserta en la base de datos,
   * e imprime el número de registros insertados.
   *
   * @return IO[Unit] que representa la secuencia de operaciones
   */
  def run: IO[Unit] =
    IdiomaDAO.insertAll(idioma)
      .flatMap(result => IO.println(s"Registros insertados: ${result.size}"))
}



