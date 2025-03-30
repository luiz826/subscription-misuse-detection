name := "scio-misuse-pipeline"
version := "0.1"
scalaVersion := "2.12.15"

libraryDependencies ++= Seq(
  "com.spotify" %% "scio-core" % "0.12.0",
  "com.spotify" %% "scio-avro" % "0.12.0",
  "io.spray"    %% "spray-json" % "1.3.6"
)
