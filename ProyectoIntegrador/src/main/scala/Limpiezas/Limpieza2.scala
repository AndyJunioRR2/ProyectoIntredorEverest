package Limpiezas

import java.io.FileWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.util.Try
import scala.util.matching.Regex

object Limpieza2 extends App {
  val inputFilePath = "data/limpios2.csv"
  val outputFilePath = "data/limpios3.csv"

  // Expresión regular para detectar JSON válido
  val jsonPattern: Regex = """^(\{.\}|\[.\])$""".r

  // Función para parsear fechas
  def parseDate(dateStr: String): String = {
    if (Option(dateStr).forall(_.trim.isEmpty) || dateStr == "No Data") "No Data"
    else {
      val cleanDateStr = dateStr.trim.replaceAll("[^0-9-]", "")
      if (cleanDateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
        Try(LocalDate.parse(cleanDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString).getOrElse("No Data")
      } else "No Data"
    }
  }

  // Leer encabezados
  val csvReader = java.nio.file.Files.newBufferedReader(java.nio.file.Paths.get(inputFilePath))
  val headers: List[String] = csvReader.readLine().split(";").map(_.trim).toList
  csvReader.close()

  // Leer filas y mapearlas a Map[String, String]
  val rows: List[Map[String, String]] = scala.io.Source.fromFile(inputFilePath)
    .getLines()
    .drop(1)
    .map { line =>
      val values = line.split(";").map(_.trim)
      headers.zipAll(values, "", "No Data").toMap
    }
    .toList

  // Limpieza de datos
  val cleanedRows: List[Map[String, String]] = rows
    .filter(row => row.getOrElse("title", "").trim.nonEmpty) // Filtrar filas sin título
    .map { row =>
      row.map { case (column, value) =>
        val cleanedValue = column match {
          case "release_date" => parseDate(value)
          case _ if jsonPattern.matches(value) => value // Mantener JSON sin cambios
          case _ =>
            val trimmedValue = value.trim
              .stripPrefix("\"") // Quitar comillas dobles al inicio
              .stripSuffix("\"") // Quitar comillas dobles al final
            trimmedValue.replaceAll("[^A-Za-z0-9 /.,:{}'\"\\[\\]]", "").trim

        }
        column -> cleanedValue
      }
    }
    .distinctBy(_.getOrElse("id", "")) // Eliminar duplicados por "id"

  // Escribir los datos limpios en el archivo de salida
  val writer = new FileWriter(outputFilePath)
  try {
    writer.write(headers.mkString(";") + "\n")
    cleanedRows.foreach { row =>
      writer.write(headers.map(header => row.getOrElse(header, "No Data")).mkString(";") + "\n")
    }
  } finally {
    writer.close()
  }

  println(s"Archivo limpio guardado en: $outputFilePath")
}