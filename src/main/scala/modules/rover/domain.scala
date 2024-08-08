package modules.rover

import cats.effect.Sync
import enumeratum.values.{StringEnum, StringEnumEntry}
import pureconfig.ConfigConvert.viaStringOpt
import pureconfig.ConfigReader

final case class Rover(position: Rover.Position)

object Rover {
  final case class Position(x: Int, y: Int, direction: Char)

  object Position {
    def fromString[F[_]: Sync](position: String): F[Position] = Sync[F].delay {
      val parts = position.split(" ")
      Position(parts(0).toInt, parts(1).toInt, parts(2).head)
    }
  }

  sealed abstract class Movement(val value: String) extends StringEnumEntry

  object Movement extends StringEnum[Movement] {
    override final val values: IndexedSeq[Movement] = findValues
    final case object TurnLeft    extends Movement("turnLeft")
    final case object TurnRight   extends Movement("turnRight")
    final case object MoveForward extends Movement("moveForward")

    implicit final def configReader: ConfigReader[Movement] =
      ConfigReader.fromCursor(viaStringOpt[Movement](Movement.withValueOpt, _.value).from)
  }
}
