package infrastructure.input

import scala.io.Source

object InputReader {
  def read(filePath: String): List[String] = {
    val source = Source.fromFile(filePath)
    try
      source.getLines().toList
    finally
      source.close()
  }
}
