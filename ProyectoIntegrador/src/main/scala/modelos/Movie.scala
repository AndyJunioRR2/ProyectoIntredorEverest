package modelos
case class Movie(
                  adult: Boolean,
                  belongs_to_collection: String,
                  budget: Int,
                  genres: String,
                  homepage: String,
                  id: Int,
                  imdb_id: String,
                  original_language: String,
                  original_title: String,
                  overview: String,
                  popularity: Double,
                  poster_path: String,
                  production_companies: String,
                  production_countries: String,
                  release_date: String,
                  revenue: Double,
                  runtime: Double,
                  spoken_languages: String,
                  status: String,
                  tagline: String,
                  title: String,
                  video: Boolean,
                  vote_average: Double,
                  vote_count: Int,
                  keywords: String,
                  cast: String,
                  crew: String,
                  ratings: String
                )

