import cats.effect.SyncIO
import cats.syntax.all._
import modules.rover.Rover
import modules.rover.Rover.Position
import modules.rover.plateau.Plateau
import munit.CatsEffectSuite
import pureconfig._
import pureconfig.generic.auto._

class MarsRoverSpec extends CatsEffectSuite {
  private val config: Config = ConfigSource.default.loadOrThrow[Config]
  private val plateau        = Plateau(5, 5)
  private val program        = RoverProgram.make[SyncIO](config, plateau)

  test("parsePosition should correctly parse a valid position string") {
    val position = Position.fromString[SyncIO]("1 2 N")
    assertSyncIO(position, Position(1, 2, 'N'))
  }

  test("MarsRover should turn left correctly") {
    val position    = Position(1, 2, 'N')
    val newPosition = program.turnLeft(position)
    assertSyncIO(newPosition, Position(1, 2, 'W'))
  }

  test("MarsRover should turn right correctly") {
    val position    = Position(1, 2, 'N')
    val newPosition = program.turnRight(position)
    assertSyncIO(newPosition, Position(1, 2, 'E'))
  }

  test("MarsRover should move forward correctly") {
    val position    = Position(1, 2, 'N')
    val newPosition = program.moveForward(position)
    assertSyncIO(newPosition, Position(1, 3, 'N'))
  }

  test("MarsRover should handle a series of instructions correctly") {
    val rover         = Rover(Position(1, 2, 'N'))
    val finalPosition = program.executeInstructions(rover, "LMLMLMLMM")
    assertSyncIO(finalPosition, Position(1, 3, 'N'))
  }

  test("MarsRover should not move out of plateau boundaries") {
    val position      = Rover(Position(0, 0, 'S'))
    val finalPosition = program.executeInstructions(position, "M")
    assertSyncIO(finalPosition, Position(0, 0, 'S'))
  }

  test("produce the correct final output for multiple rovers") {
    val instructions = List(
      ("1 2 N", "LMLMLMLMM"),
      ("3 3 E", "MMRMMRMRRM")
    )

    val results = instructions.traverse { case (start, moves) =>
      for {
        startPosition <- Position.fromString[SyncIO](start)
        newPosition   <- program.executeInstructions(Rover(startPosition), moves)
      } yield newPosition
    }

    assertSyncIO(
      results,
      List(
        Position(1, 3, 'N'),
        Position(5, 1, 'E')
      )
    )
  }
}
