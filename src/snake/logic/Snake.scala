package snake.logic

import scala.collection.mutable.ArrayBuffer


// The snakePoints array consist of the body points and the head
class Snake (var snakePoints: ArrayBuffer[Point] = ArrayBuffer[Point](Point(0,0), Point(1,0), Point(2,0)),
             var changingSnakeLength : Int = 3) {
  var headPoint: Point = snakePoints.last.copy()
  def startOfSnakeBody : Point = snakePoints(this.snakePoints.length-2)
}

