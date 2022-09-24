package snake.logic

class StateStorage (oldState: GameState,
                    var storedSnakePoints: Vector[Point] = Vector[Point](),
                    var storedSnakeSizes: Vector[Int] = Vector[Int](),
                    var storedSnakeDirections: Vector[Direction] = Vector[Direction](),
                    var storedApplePoints: Vector[Point] = Vector[Point](),
                    var storedChangingLengths: Vector[Int] = Vector[Int]()){

  addSnakePoints(oldState)
  addSnakeSize(oldState)
  addSnakeDirection(oldState)
  addApplePoint(oldState)
  addChangingLength(oldState)

  def addSnakePoints(oldState: GameState): Unit = {
    for (i <- oldState.snake.snakePoints.indices) {
    this.storedSnakePoints = storedSnakePoints :+ oldState.snake.snakePoints(i)
    }
  }

  def addSnakeSize(oldState: GameState): Unit = {
    this.storedSnakeSizes = storedSnakeSizes :+ oldState.snake.snakePoints.length
  }

  def addSnakeDirection(oldState: GameState): Unit = {
    this.storedSnakeDirections = storedSnakeDirections :+ oldState.currentDirection
  }

  def addApplePoint(oldState: GameState): Unit = {
    this.storedApplePoints = storedApplePoints :+ oldState.applePoint
  }

  def addChangingLength(oldState: GameState): Unit = {
    this.storedChangingLengths = storedChangingLengths :+ oldState.snake.changingLength
  }

}
