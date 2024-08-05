import munit.FunSuite

class MarsRoverSpec extends FunSuite {
  test("parsePosition should correctly parse a valid position string") {
    val position = MarsRover.parsePosition("1 2 N")
    assertEquals(position, MarsRover.Position(1, 2, 'N'))
  }

  test("MarsRover should turn left correctly") {
    val position    = MarsRover.Position(1, 2, 'N')
    val newPosition = MarsRover.turnLeft(position)
    assertEquals(newPosition, MarsRover.Position(1, 2, 'W'))
  }

  test("MarsRover should turn right correctly") {
    val position    = MarsRover.Position(1, 2, 'N')
    val newPosition = MarsRover.turnRight(position)
    assertEquals(newPosition, MarsRover.Position(1, 2, 'E'))
  }

  test("MarsRover should move forward correctly") {
    val position    = MarsRover.Position(1, 2, 'N')
    val newPosition = MarsRover.moveForward(position, 5, 5)
    assertEquals(newPosition, MarsRover.Position(1, 3, 'N'))
  }

  test("MarsRover should handle a series of instructions correctly") {
    val position      = MarsRover.Position(1, 2, 'N')
    val finalPosition = MarsRover.executeInstructions(position, "LMLMLMLMM", 5, 5)
    assertEquals(finalPosition, MarsRover.Position(1, 3, 'N'))
  }

  test("MarsRover should not move out of plateau boundaries") {
    val position      = MarsRover.Position(0, 0, 'S')
    val finalPosition = MarsRover.executeInstructions(position, "M", 5, 5)
    assertEquals(finalPosition, MarsRover.Position(0, 0, 'S'))
  }

  test("produce the correct final output for multiple rovers") {
    val plateau = Array(5, 5)
    val instructions = List(
      ("1 2 N", "LMLMLMLMM"),
      ("3 3 E", "MMRMMRMRRM")
    )

    val results = instructions.map { case (start, moves) =>
      val initialPosition = MarsRover.parsePosition(start)
      MarsRover.executeInstructions(initialPosition, moves, plateau(0), plateau(1))
    }

    assertEquals(
      results,
      List(
        MarsRover.Position(1, 3, 'N'),
        MarsRover.Position(5, 1, 'E')
      )
    )
  }
}
