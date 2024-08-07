import infrastructure.input.InputReader
import modules.rover.plateau.Plateau
import pureconfig._
import pureconfig.generic.auto._

object Main {
  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      println("Usage: sbt run <input_file>")
      sys.exit(1)
    }

    val config: Config = ConfigSource.default.loadOrThrow[Config]

    val input        = InputReader.read(args(0))
    val plateau      = Plateau.fromString(input.head)
    val instructions = input.tail.grouped(2).toList

    val program = RoverProgram.make(config, plateau)

    InstructionProcessor.processInstructions(instructions, program)
  }
}
