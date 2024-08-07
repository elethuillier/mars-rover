import modules.rover.Rover
import modules.rover.Rover.{Movement, Position}
import modules.rover.plateau.Plateau

trait RoverProgram {
  def executeInstructions(rover: Rover, instructions: String): Position
  def turnLeft(position: Position): Position
  def turnRight(position: Position): Position
  def moveForward(position: Position): Position
}

object RoverProgram {
  final def make(config: Config, plateau: Plateau) = new Live(config, plateau)

  final class Live(config: Config, plateau: Plateau) extends RoverProgram {

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

    override def executeInstructions(rover: Rover, instructions: String): Position =
      instructions.foldLeft(rover.position)(executeInstruction)

    private def executeInstruction(startPosition: Position, instruction: Char): Position = {
      val movement = movements(instruction)
      runMovement(movement, startPosition)
    }

    private def runMovement(movement: Movement, startPosition: Position) = (movement match {
      case Movement.TurnLeft    => turnLeft _
      case Movement.TurnRight   => turnRight _
      case Movement.MoveForward => moveForward _
    })(startPosition)

    override def turnLeft(position: Position): Position = {
      val newDirection = directions(position.direction).left
      position.copy(direction = newDirection)
    }

    override def turnRight(position: Position): Position = {
      val newDirection = directions(position.direction).right
      position.copy(direction = newDirection)
    }

    override def moveForward(position: Position): Position = {
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
