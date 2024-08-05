import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      println("Usage: sbt run <input_file>")
      sys.exit(1)
    }
    val inputFile    = args(0)
    val input        = readInput(inputFile)
    val plateau      = parsePlateau(input.head)
    val instructions = input.tail.grouped(2).toList
    processInstructions(plateau, instructions)
  }

  def readInput(filePath: String): List[String] = {
    val source = Source.fromFile(filePath)
    try
      source.getLines().toList
    finally
      source.close()
  }

  def parsePlateau(line: String): (Int, Int) = {
    val coords = line.split(" ").map(_.toInt)
    (coords(0), coords(1))
  }

  def processInstructions(plateau: (Int, Int), instructions: List[List[String]]): Unit =
    instructions.foreach {
      case List(start, moves) =>
        val initialPosition = MarsRover.parsePosition(start)
        val finalPosition   = MarsRover.executeInstructions(initialPosition, moves, plateau._1, plateau._2)
        println(s"${finalPosition.x} ${finalPosition.y} ${finalPosition.direction}")
      case _ =>
        println("Invalid instruction set")
    }
}
