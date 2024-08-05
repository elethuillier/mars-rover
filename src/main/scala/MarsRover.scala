object MarsRover {
  case class Position(x: Int, y: Int, direction: Char)

  def parsePosition(position: String): Position = {
    val parts = position.split(" ")
    Position(parts(0).toInt, parts(1).toInt, parts(2).head)
  }

  def executeInstructions(position: Position, instructions: String, maxX: Int, maxY: Int): Position =
    instructions.foldLeft(position) { (currentPosition, instruction) =>
      instruction match {
        case 'L' => turnLeft(currentPosition)
        case 'R' => turnRight(currentPosition)
        case 'M' => moveForward(currentPosition, maxX, maxY)
      }
    }

  def turnLeft(position: Position): Position = {
    val newDirection = position.direction match {
      case 'N' => 'W'
      case 'W' => 'S'
      case 'S' => 'E'
      case 'E' => 'N'
    }
    position.copy(direction = newDirection)
  }

  def turnRight(position: Position): Position = {
    val newDirection = position.direction match {
      case 'N' => 'E'
      case 'E' => 'S'
      case 'S' => 'W'
      case 'W' => 'N'
    }
    position.copy(direction = newDirection)
  }

  def moveForward(position: Position, maxX: Int, maxY: Int): Position = {
    val (newX, newY) = position.direction match {
      case 'N' => (position.x, Math.min(position.y + 1, maxY))
      case 'E' => (Math.min(position.x + 1, maxX), position.y)
      case 'S' => (position.x, Math.max(position.y - 1, 0))
      case 'W' => (Math.max(position.x - 1, 0), position.y)
    }
    position.copy(x = newX, y = newY)
  }
}
