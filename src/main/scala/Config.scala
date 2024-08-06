case class Config(
  movements: MovementsConfig,
  directions: DirectionsConfig
)

final case class MovementsConfig(left: String, right: String, move: String)

final case class DirectionsConfig(
  north: DirectionConfig,
  est: DirectionConfig,
  south: DirectionConfig,
  west: DirectionConfig
)

final case class DirectionConfig(left: Char, right: Char, moveX: Int, moveY: Int)
