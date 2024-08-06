import pureconfig._
import pureconfig.generic.auto._

object MarsRover {

  case class Position(x: Int, y: Int, direction: Char)

  val config: Config = ConfigSource.default.loadOrThrow[Config]

  def parsePosition(position: String): Position = {
    val parts = position.split(" ")
    Position(parts(0).toInt, parts(1).toInt, parts(2).head)
  }

  def executeInstructions(position: Position, instructions: String, maxX: Int, maxY: Int): Position =
    instructions.foldLeft(position) { (currentPosition, instruction) =>
      val action = instruction match {
        case 'L' => config.movements.left
        case 'R' => config.movements.right
        case 'M' => config.movements.move
        case _   => throw new IllegalArgumentException(s"Invalid instruction: $instruction")
      }

      action match {
        case "turnLeft"    => turnLeft(currentPosition)
        case "turnRight"   => turnRight(currentPosition)
        case "moveForward" => moveForward(currentPosition, maxX, maxY)
        case _             => throw new IllegalArgumentException(s"Invalid action: $action")
      }
    }

  def turnLeft(position: Position): Position = {
    val newDirection = position.direction match {
      case 'N' => config.directions.north.left
      case 'E' => config.directions.est.left
      case 'S' => config.directions.south.left
      case 'W' => config.directions.west.left
      case _   => throw new IllegalArgumentException(s"Invalid direction: ${position.direction}")
    }
    position.copy(direction = newDirection)
  }

  def turnRight(position: Position): Position = {
    val newDirection = position.direction match {
      case 'N' => config.directions.north.right
      case 'E' => config.directions.est.right
      case 'S' => config.directions.south.right
      case 'W' => config.directions.west.right
      case _   => throw new IllegalArgumentException(s"Invalid direction: ${position.direction}")
    }
    position.copy(direction = newDirection)
  }

  def moveForward(position: Position, maxX: Int, maxY: Int): Position = {
    val (moveX, moveY) = position.direction match {
      case 'N' => (config.directions.north.moveX, config.directions.north.moveY)
      case 'E' => (config.directions.est.moveX, config.directions.est.moveY)
      case 'S' => (config.directions.south.moveX, config.directions.south.moveY)
      case 'W' => (config.directions.west.moveX, config.directions.west.moveY)
      case _   => throw new IllegalArgumentException(s"Invalid direction: ${position.direction}")
    }

    val newX = Math.min(Math.max(position.x + moveX, 0), maxX)
    val newY = Math.min(Math.max(position.y + moveY, 0), maxY)
    position.copy(x = newX, y = newY)
  }
}
