package snake.logic

class StateStorage (oldState: GameState,
                    var storedSnakePoints: Vector[Point] = Vector[Point](),
                    var storedSnakeSizes: Vector[Int] = Vector[Int](),
                    var storedSnakeDirections: Vector[Direction] = Vector[Direction]()){

  addSnakePoints(oldState)
  addSnakeSize(oldState)
  addSnakeDirection(oldState)

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

}
