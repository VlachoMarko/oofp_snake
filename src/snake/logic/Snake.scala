package snake.logic

import scala.collection.mutable.ArrayBuffer


// The snakePoints array consist of the body points and the head
class Snake (var snakePoints: ArrayBuffer[Point] = ArrayBuffer[Point](Point(0,0), Point(1,0), Point(2,0)),
             var snakeLength : Int = 3,
             var applePoint : Point = Point(-10,-10),
             var appleOnBoard : Boolean = false,
             var emptyPoints: Vector[Point] = Vector[Point]()
            ) {
  var HeadPoint: Point = snakePoints.last.copy()
}

