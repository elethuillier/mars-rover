import modules.rover.Rover
import modules.rover.Rover.Position
import modules.rover.plateau.Plateau
import munit.FunSuite
import pureconfig._
import pureconfig.generic.auto._

class MarsRoverSpec extends FunSuite {
  private val config: Config = ConfigSource.default.loadOrThrow[Config]
  private val plateau        = Plateau(5, 5)
  private val program        = RoverProgram.make(config, plateau)

  test("parsePosition should correctly parse a valid position string") {
    val position = Position.fromString("1 2 N")
    assertEquals(position, Position(1, 2, 'N'))
  }

  test("MarsRover should turn left correctly") {
    val position    = Position(1, 2, 'N')
    val newPosition = program.turnLeft(position)
    assertEquals(newPosition, Position(1, 2, 'W'))
  }

  test("MarsRover should turn right correctly") {
    val position    = Position(1, 2, 'N')
    val newPosition = program.turnRight(position)
    assertEquals(newPosition, Position(1, 2, 'E'))
  }

  test("MarsRover should move forward correctly") {
    val position    = Position(1, 2, 'N')
    val newPosition = program.moveForward(position)
    assertEquals(newPosition, Position(1, 3, 'N'))
  }

  test("MarsRover should handle a series of instructions correctly") {
    val rover         = Rover(Position(1, 2, 'N'))
    val finalPosition = program.executeInstructions(rover, "LMLMLMLMM")
    assertEquals(finalPosition, Position(1, 3, 'N'))
  }

  test("MarsRover should not move out of plateau boundaries") {
    val position      = Rover(Position(0, 0, 'S'))
    val finalPosition = program.executeInstructions(position, "M")
    assertEquals(finalPosition, Position(0, 0, 'S'))
  }

  test("produce the correct final output for multiple rovers") {
    val instructions = List(
      ("1 2 N", "LMLMLMLMM"),
      ("3 3 E", "MMRMMRMRRM")
    )

    val results = instructions.map { case (start, moves) =>
      val rover = Rover(Position.fromString(start))
      program.executeInstructions(rover, moves)
    }

    assertEquals(
      results,
      List(
        Position(1, 3, 'N'),
        Position(5, 1, 'E')
      )
    )
  }
}
