package modules.rover.plateau

import cats.effect.Sync

final case class Plateau(maxX: Int, maxY: Int)

object Plateau {
  def fromString[F[_]: Sync](plateau: String): F[Plateau] = Sync[F].delay {
    val coords = plateau.split(" ").map(_.toInt)
    Plateau(coords(0), coords(1))
  }
}
