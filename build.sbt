import Dependencies._

ThisBuild / scalaVersion     := "2.13.4"
ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "ru.otus"
ThisBuild / organizationName := "bigdataml"

libraryDependencies  ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.2",
      "org.scalanlp" %% "breeze" % "1.1",
      "org.scalanlp" %% "breeze-natives" % "1.1",
      "org.scalanlp" %% "breeze-viz" % "1.1",
      "com.github.haifengl" %% "smile-scala" % "2.6.0",
      "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.0"
    )
