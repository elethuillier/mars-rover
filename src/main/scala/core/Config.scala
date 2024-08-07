import modules.rover.Rover.Movement

case class Config(
  movements: MovementsConfig,
  directions: DirectionsConfig
)

final case class MovementsConfig(left: Movement, right: Movement, move: Movement)

final case class DirectionsConfig(
  north: DirectionConfig,
  est: DirectionConfig,
  south: DirectionConfig,
  west: DirectionConfig
)

final case class DirectionConfig(left: Char, right: Char, moveX: Int, moveY: Int)
