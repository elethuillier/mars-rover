package modules.rover.plateau

final case class Plateau(maxX: Int, maxY: Int)

object Plateau {
  def fromString(plateau: String): Plateau = {
    val coords = plateau.split(" ").map(_.toInt)
    Plateau(coords(0), coords(1))
  }
}
