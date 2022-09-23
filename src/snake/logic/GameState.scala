package snake.logic

class GameState (val gridDims: Dimensions,
                 var snake: Snake = new Snake(),
                 var changingDirection: Direction = East(),
                 var applePoint: Point = Point(-10, -10),
                 var emptyPoints: Vector[Point] = Vector[Point]() ) {

  val gameRoom: Int = gridDims.width * gridDims.height
}
