/*
package Limpiezas
import java.io.File
import kantan.csv._
import kantan.csv.ops._
import Utils.Limpiezaclase
object LimpiezaMovie extends App {
  val moviesData = Limpiezaclase.movies()
      val moviee = moviesData.map{ movie =>
        (
      movie.adult,
      movie.budget,
      Limpiezaclase.limpiarString(movie.homepage),
      movie.id,
      Limpiezaclase.limpiarString(movie.imdb_id),
      Limpiezaclase.limpiarString(movie.original_title),
      Limpiezaclase.limpiarString(movie.overview),
      movie.popularity,
      Limpiezaclase.limpiarString(movie.poster_path),
      Limpiezaclase.fechalimpia(movie.release_date),
      movie.revenue,
      movie.runtime,
      Limpiezaclase.limpiarString(movie.status),
      Limpiezaclase.limpiarString(movie.tagline),
      Limpiezaclase.limpiarString(movie.title),
      movie.video,
      movie.vote_average,
      movie.vote_count,
      Limpiezaclase.limpiarString(movie.original_language))
  }

  // Guardar datos limpios en CSV
  val writer = new File(Limpiezaclase.crearCsv("movies")).asCsvWriter[(
    Boolean, Int, String, Int, String, String, String, Double, String, String, Int, Int, String, String, String, Boolean, Double, Int, String
    )](
    rfc.withHeader(
      "adult", "budget", "homepage", "movie_id", "imdb_id", "original_title", "overview",
      "popularity", "poster_path", "release_date", "revenue", "runtime", "status",
      "tagline", "title", "video", "vote_average", "vote_count", "original_language"
    )
  )
}
