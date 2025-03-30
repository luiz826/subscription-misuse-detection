package com.example.scio

import com.spotify.scio._
import com.spotify.scio.io._
import com.spotify.scio.values._
import spray.json._
import org.apache.beam.sdk.options.StreamingOptions
// If you need more imports, add them

case class LogEvent(
  timestamp: String,
  user_id: String,
  action: String,
  ip_address: String,
  device_id: String
)

object LogEventProtocol extends DefaultJsonProtocol {
  implicit val logEventFormat = jsonFormat5(LogEvent)
}

object MisusePipeline {
  def main(cmdlineArgs: Array[String]): Unit = {
    import LogEventProtocol._
    val (sc, args) = ContextAndArgs(cmdlineArgs)

    // Input/Output default paths
    val inputPath  = args.getOrElse("input", "data_samples/logs_simulated.json")
    val outputPath = args.getOrElse("output", "data_samples/scio_output")

    // 1) Read lines
    val lines = sc.textFile(inputPath)

    // 2) Parse JSON
    val logs = lines.map(line => line.parseJson.convertTo[LogEvent])

    // 3) Filter out incomplete or invalid logs
    val validLogs = logs.filter(l => l.user_id.nonEmpty && l.ip_address.nonEmpty)

    // 4) Write output to local text
    validLogs
      .map(_.toJson.prettyPrint)
      .saveAsTextFile(outputPath)

    sc.run().waitUntilFinish()
  }
}
