package modelos
case class Pelicula(
                           adult: Boolean,
                           budget: Long,
                           homepage: String,
                           id: Int,
                           imdbId: String,
                           originalTitle: String,
                           overview: String,
                           popularity: Double,
                           posterPath: String,
                           releaseDate: String,
                           revenue: Long,
                           runtime: Int,
                           status: String,
                           tagline: String,
                           title: String,
                           video: Boolean,
                           voteAverage: Double,
                           voteCount: Int,
                           originalLanguage: String
                   )