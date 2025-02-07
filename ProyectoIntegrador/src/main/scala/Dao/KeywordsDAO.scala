package Dao

import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.implicits._

import config.Database
import models.Keywords

object KeywordsDAO {
  def insert(keywords: Keywords): ConnectionIO[Int] = {
    sql"""
       INSERT INTO keywords (id_keyword, words)
          VALUES (
          ${keywords.id_keyword},
           ${keywords.words})

    """.update.run
  }
  def insertAll(keywords: List[Keywords]): IO[List[Int]] = {
    val uniqueKeywords = keywords.distinctBy(_.id_keyword)

    Database.transactor.use { xa =>
      uniqueKeywords.traverse { keyword =>
        insert(keyword).transact(xa).attempt.flatMap {
          case Left(err) =>
            IO(println(s"Error al insertar palabras clave ${keyword.id_keyword}: ${err.getMessage}")) *> IO.pure(0)
          case Right(inserted) =>
            IO.pure(inserted)
        }
      }
    }
  }

  def getAll: IO[List[Keywords]] = {
    val query = sql"""
    SELECT * FROM keywords
  """.query[Keywords].to[List]

    Database.transactor.use { xa =>
      query.transact(xa)
    }
  }
}


