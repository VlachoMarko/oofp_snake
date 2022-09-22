package snake.logic

import engine.random.RandomGenerator

/** To implement Snake, complete the ``TODOs`` below.
 *
 * If you need additional files,
 * please also put them in the ``snake`` package.
 */
class GameLogic(val random: RandomGenerator,
                val gridDims : Dimensions) {

  var gameState: GameState = new GameState(gridDims)
  var snake : Snake = gameState.snake
  var gameOver: Boolean = false

  if (snake.snakePoints.length < gameState.gameRoom) findEmptyPlaces(); placeApple()
  var stepsTaken: Int = 0
  def step(): Unit = {
    stepsTaken += 1
    println("step: " + stepsTaken)

    val currentDirection = gameState.changingDirection

    if (!gameOver && snake.changingSnakeLength <= gameState.gameRoom) {
      moveSnake(currentDirection)
      findEmptyPlaces()
      checkForEatenApple()
      gameOver = isGameOver
    } else gameOver = true
  }

  // TODO implement me
  def setReverse(r: Boolean): Unit = ()


  def changeDir(d: Direction): Unit = {
    val newTestHead = snake.HeadPoint.copy()
    newTestHead.movePoint(d)
    if (!(gameState.changingDirection == d.opposite) && !(newTestHead == snake.startOfSnakeBody)) gameState.changingDirection = d
  }

  def getCellType(p : Point): CellType = {
    if (snake.snakePoints.contains(p)) p.cell = SnakeBody(1.0f)
    if (snake.HeadPoint == p) p.cell = SnakeHead(gameState.changingDirection)
    else if (gameState.applePoint == p) p.cell = Apple()

    p.cell
  }

  def moveSnake(currentDirection: Direction): Unit = {
    if (snake.snakePoints.length < snake.changingSnakeLength) snake.snakePoints += snake.HeadPoint.copy()
    else {
      for (i <- 0 until snake.snakePoints.length - 1) {
        snake.snakePoints(i) = snake.snakePoints(i + 1)
      }
    }
    snake.HeadPoint.movePoint(currentDirection)
    handleBorders(currentDirection)
    snake.snakePoints(snake.snakePoints.length - 1) = snake.HeadPoint.copy()
    gameState.emptyPoints = Vector[Point]()
  }

  def checkForEatenApple(): Unit = {
    if (snake.HeadPoint == gameState.applePoint) {
      snake.changingSnakeLength += 3
      if (snake.snakePoints.length < gameState.gameRoom) placeApple()
    }
  }

  def isGameOver: Boolean = {
    if (snake.snakePoints.contains(snake.HeadPoint)) {
      if (snake.snakePoints.indexOf(snake.HeadPoint) != 0 && snake.snakePoints.indexOf(snake.HeadPoint) != snake.snakePoints.length - 1) {
        println("index of failure: " + snake.snakePoints.indexOf(snake.HeadPoint))
        return true
      }
    }
    false
  }

  def handleBorders(currentDirection: Direction): Unit ={
    if (snake.HeadPoint.x == gridDims.width && currentDirection == East()) snake.HeadPoint.x = 0
    else if (snake.HeadPoint.x == -1 && currentDirection == West()) {snake.HeadPoint.x = gridDims.width-1; snake.snakePoints(snake.snakePoints.length-1) = snake.HeadPoint.copy()}
    else if (snake.HeadPoint.y == gridDims.height && currentDirection == South()) snake.HeadPoint.y = 0
    else if (snake.HeadPoint.y == -1 && currentDirection == North()) snake.HeadPoint.y = gridDims.height-1; snake.snakePoints(snake.snakePoints.length-1) = snake.HeadPoint.copy()
  }

  def isBoardFilled: Boolean = snake.snakePoints.length >= gameState.gameRoom

  def findEmptyPlaces(): Unit = {
    for (y <- 0 until gridDims.height) {
      for (x <- 0 until gridDims.width)
        if (!snake.snakePoints.contains(Point(x,y))) {
          // assert(!gameState.emptyPoints.contains(Point(x,y)))
          gameState.emptyPoints = gameState.emptyPoints :+ Point(x, y)
          }
      }
   }

  def placeApple(): Unit = {
    if (gameState.emptyPoints.nonEmpty){
      val randomNumber = random.randomInt(gameState.emptyPoints.length)
      gameState.applePoint = gameState.emptyPoints(randomNumber).copy()
    }


    // println("apple placed at: " + gameState.applePoint)
  }
}


/** GameLogic companion object */
object GameLogic {

  val FramesPerSecond: Int = 1 // change this to increase/decrease speed of game

  val DrawSizeFactor = 1.0 // increase this to make the game bigger (for high-res screens)
  // or decrease to make game smaller

  // These are the dimensions used when playing the game.
  // When testing the game, other dimensions are passed to
  // the constructor of GameLogic.
  //
  // DO NOT USE the variable DefaultGridDims in your code!
  //
  // Doing so will cause tests which have different dimensions to FAIL!
  //
  // In your code only use gridDims.width and gridDims.height
  // do NOT use DefaultGridDims.width and DefaultGridDims.height
  val DefaultGridDims
    : Dimensions =
    Dimensions(width = 3, height = 1)  // you can adjust these values to play on a different sized board

}


