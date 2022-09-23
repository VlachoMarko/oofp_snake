package snake.logic

class StateStorage (oldState: GameState){

  var storedSnakePoints: Vector[Point] = Vector[Point]()
  var storedSnakeSizes: Vector[Int] = Vector[Int]()

  addSnakePoints(oldState)
  addSnakeSize(oldState)

  def addSnakePoints(oldState: GameState): Unit = {
    for (i <- oldState.snake.snakePoints.indices) {
    this.storedSnakePoints = storedSnakePoints :+ oldState.snake.snakePoints(i)
    }
  }

  def addSnakeSize(oldState: GameState): Unit = {
    this.storedSnakeSizes = storedSnakeSizes :+ oldState.snake.snakePoints.length
  }

}
