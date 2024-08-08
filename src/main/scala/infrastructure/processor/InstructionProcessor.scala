import cats.effect.Sync
import cats.syntax.all._
import modules.rover.Rover
import modules.rover.Rover.Position

object InstructionProcessor {
  def processInstructions[F[_]: Sync](instructions: List[List[String]], program: RoverProgram[F]): F[Unit] =
    instructions.traverse_ {
      case List(start, moves) =>
        for {
          position      <- Position.fromString[F](start)
          finalPosition <- program.executeInstructions(Rover(position), moves)
          _             <- Sync[F].delay(println(s"${finalPosition.x} ${finalPosition.y} ${finalPosition.direction}"))
        } yield ()

      case _ => Sync[F].raiseError[Unit](new IllegalArgumentException("Invalid instruction set"))
    }
}
