package Utils

import InsertAll.Pelicula_IdiomaDAO
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
import models.Pelicula_Idioma

// Extiende de IOApp.Simple para manejar efectos IO y recursos de forma segura
object MainPelicula_Idioma extends IOApp.Simple {
  val path2DataFile2 = "src/main/resources/data/pelicula_idioma.csv"

  val dataSource = new File(path2DataFile2)
    .readCsv[List,Pelicula_Idioma](rfc.withHeader.withCellSeparator(','))

  val pelicula_Idioma = dataSource.collect {
    case Right(pelicula_Idioma) => pelicula_Idioma
  }

  /**
   * Punto de entrada principal de la aplicación.
   * Lee temperaturas desde CSV, las inserta en la base de datos,
   * e imprime el número de registros insertados.
   *
   * @return IO[Unit] que representa la secuencia de operaciones
   */
  def run: IO[Unit] =
    Pelicula_IdiomaDAO.insertAll(pelicula_Idioma)
      .flatMap(result => IO.println(s"Registros insertados: ${result.size}"))
}

