import cats.effect.{ExitCode, IO, IOApp}
import infrastructure.input.InputReader
import modules.rover.plateau.Plateau
import pureconfig._
import pureconfig.generic.auto._

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    if (args.length != 1) {
      IO(println("Usage: sbt run <input_file>")) *> IO.pure(ExitCode.Error)
    } else {
      (for {
        config      <- IO(ConfigSource.default.loadOrThrow[Config])
        input       <- InputReader.read[IO](args.head)
        plateau     <- Plateau.fromString[IO](input.head)
        instructions = input.tail.grouped(2).toList
        program      = RoverProgram.make[IO](config, plateau)
        _           <- InstructionProcessor.processInstructions[IO](instructions, program)
      } yield ExitCode.Success).handleErrorWith { error =>
        IO(println(s"An error occurred: ${error.getMessage}")) *> IO.pure(ExitCode.Error)
      }
    }
}
