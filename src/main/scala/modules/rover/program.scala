import cats.syntax.all._
import cats.effect.Sync
import modules.rover.Rover
import modules.rover.Rover.{Movement, Position}
import modules.rover.plateau.Plateau

trait RoverProgram[F[_]] {
  def executeInstructions(rover: Rover, instructions: String): F[Position]
  def turnLeft(position: Position): F[Position]
  def turnRight(position: Position): F[Position]
  def moveForward(position: Position): F[Position]
}

object RoverProgram {
  final def make[F[_]: Sync](config: Config, plateau: Plateau): RoverProgram[F] = new Live(config, plateau)

  private[this] final class Live[F[_]: Sync](config: Config, plateau: Plateau) extends RoverProgram[F] {

    private val movements: Map[Char, Movement] = Map(
      'L' -> config.movements.left,
      'R' -> config.movements.right,
      'M' -> config.movements.move
    )

    private val directions: Map[Char, DirectionConfig] = Map(
      'N' -> config.directions.north,
      'E' -> config.directions.est,
      'S' -> config.directions.south,
      'W' -> config.directions.west
    )

    override def executeInstructions(rover: Rover, instructions: String): F[Position] =
      instructions.foldLeft(Sync[F].pure(rover.position)) { (position, instruction) =>
        position.flatMap(executeInstruction(_, instruction))
      }

    private def executeInstruction(startPosition: Position, instruction: Char): F[Position] = {
      val movement = movements(instruction)
      runMovement(movement, startPosition)
    }

    private def runMovement(movement: Movement, startPosition: Position): F[Position] = (movement match {
      case Movement.TurnLeft    => turnLeft _
      case Movement.TurnRight   => turnRight _
      case Movement.MoveForward => moveForward _
    })(startPosition)

    override def turnLeft(position: Position): F[Position] = Sync[F].delay {
      val newDirection = directions(position.direction).left
      position.copy(direction = newDirection)
    }

    override def turnRight(position: Position): F[Position] = Sync[F].delay {
      val newDirection = directions(position.direction).right
      position.copy(direction = newDirection)
    }

    override def moveForward(position: Position): F[Position] = Sync[F].delay {
      val (moveX, moveY) = {
        val config = directions(position.direction)
        (config.moveX, config.moveY)
      }

      val newX = Math.min(Math.max(position.x + moveX, 0), plateau.maxX)
      val newY = Math.min(Math.max(position.y + moveY, 0), plateau.maxY)

      position.copy(x = newX, y = newY)
    }
  }
}
