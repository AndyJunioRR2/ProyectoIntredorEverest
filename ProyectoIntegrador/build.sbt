ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.12"
lazy val root = (project in file("."))
  .settings(
    name := "ProyectoIntegrador",
    libraryDependencies ++= Seq(
      "io.reactivex" %% "rxscala" % "0.27.0",
      "com.lihaoyi" %% "scalarx" % "0.4.1",
      "com.nrinaudo" %% "kantan.csv" % "0.6.1",
      "com.nrinaudo" %% "kantan.csv-generic" % "0.6.1",
      "com.typesafe.play" %% "play-json" % "2.9.2",
      "org.tpolecat" %% "doobie-core" % "1.0.0-RC5",
      "org.tpolecat" %% "doobie-hikari" % "1.0.0-RC5",
      "com.typesafe" % "config" % "1.4.2",
      "org.knowm.xchart" % "xchart" % "3.8.0",
      "com.mysql" % "mysql-connector-j" % "8.0.31",
      "ch.qos.logback" % "logback-classic"%"1.2.3")
  )
