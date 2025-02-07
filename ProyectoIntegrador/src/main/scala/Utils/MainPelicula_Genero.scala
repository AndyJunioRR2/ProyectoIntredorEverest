package Utils


import InsertAll.Pelicula_generoDAO
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
import models.Pelicula_genero


// Extiende de IOApp.Simple para manejar efectos IO y recursos de forma segura
object MainPelicula_Genero extends IOApp.Simple {
  val path2DataFile2 = "src/main/resources/data/pelicula_genero.csv"

  val dataSource = new File(path2DataFile2)
    .readCsv[List,Pelicula_genero](rfc.withHeader.withCellSeparator(','))

  val pelicula_genero = dataSource.collect {
    case Right(pelicula_genero) => pelicula_genero
  }

  /**
   * Punto de entrada principal de la aplicación.
   * Lee temperaturas desde CSV, las inserta en la base de datos,
   * e imprime el número de registros insertados.
   *
   * @return IO[Unit] que representa la secuencia de operaciones
   */
  def run: IO[Unit] =
    Pelicula_generoDAO.insertAll(pelicula_genero)
      .flatMap(result => IO.println(s"Registros insertados: ${result.size}"))
}

