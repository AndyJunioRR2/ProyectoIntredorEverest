package Limpiezas

import kantan.csv._
import kantan.csv.ops._

import java.io.File
import scala.io.Source

object Limpieza extends App {
  val inputFilePath = "data/pi_movies_complete.csv"
  val outputFilePath = "data/limpios2.csv"

  // Leer los headers
  val source = Source.fromFile(inputFilePath)
  val headers = source.getLines().next().split(",").map(_.trim).toList
  source.close()

  val inputFile = new File(inputFilePath)

  val Data = inputFile
    .asCsvReader[List[String]](rfc.withHeader).collect { case Right(fila) => fila }
    .filter(_.forall(_.nonEmpty))
    .toList
    .distinct

  // Preparar los datos con los headers
  val allData = headers :: Data

  // Escribir al archivo de salida
  val outputFile = new File(outputFilePath)
  outputFile.writeCsv[List[String]](allData, rfc)

  println(s"Archivo limpio guardado en: $outputFilePath")
}
