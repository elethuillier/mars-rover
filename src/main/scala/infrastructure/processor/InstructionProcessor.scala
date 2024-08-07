import modules.rover.Rover
import modules.rover.Rover.Position

object InstructionProcessor {
  def processInstructions(instructions: List[List[String]], program: RoverProgram): Unit =
    instructions.foreach {
      case List(start, moves) =>
        val rover         = Rover(Position.fromString(start))
        val finalPosition = program.executeInstructions(rover, moves)
        println(s"${finalPosition.x} ${finalPosition.y} ${finalPosition.direction}")

      case _ => throw new IllegalArgumentException("Invalid instruction set")
    }
}
