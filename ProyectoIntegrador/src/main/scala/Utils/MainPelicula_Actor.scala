package Utils

import InsertAll.Pelicula_Actor
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
import models.Pelicula_actor


// Extiende de IOApp.Simple para manejar efectos IO y recursos de forma segura
object MainPelicula_Actor extends IOApp.Simple {
  val path2DataFile2 = "src/main/resources/data/pelicula_actor.csv"

  val dataSource = new File(path2DataFile2)
    .readCsv[List,Pelicula_actor](rfc.withHeader.withCellSeparator(','))

  val pelicula_actor = dataSource.collect {
    case Right(pelicula_actor) => pelicula_actor
  }

  /**
   * Punto de entrada principal de la aplicación.
   * Lee temperaturas desde CSV, las inserta en la base de datos,
   * e imprime el número de registros insertados.
   *
   * @return IO[Unit] que representa la secuencia de operaciones
   */
  def run: IO[Unit] =
    Pelicula_Actor.insertAll(pelicula_actor)
      .flatMap(result => IO.println(s"Registros insertados: ${result.size}"))
}

