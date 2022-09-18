package snake.logic

import engine.random.RandomGenerator

/** To implement Snake, complete the ``TODOs`` below.
 *
 * If you need additional files,
 * please also put them in the ``snake`` package.
 */
class GameLogic(val random: RandomGenerator,
                val gridDims : Dimensions) {

  val gameRoom: Int = gridDims.width * gridDims.height
  var gameOver: Boolean = false

  var snake : Snake = new Snake()
  var changingDirection: Direction = East()

  def step(): Unit = {

    val currentDirection = changingDirection

    if (!gameOver && snake.snakeLength <= gameRoom) {

      moveSnake(currentDirection)
      checkForEatenApple()
      gameOver = isGameOver

    } else gameOver = true
  }

  // TODO implement me
  def setReverse(r: Boolean): Unit = ()


  def changeDir(d: Direction): Unit = {
    val copyHead = snake.HeadPoint.copy()
    copyHead.movePoint(d)
    if (!(changingDirection == d.opposite) && !(copyHead == snake.snakePoints(snake.snakePoints.length-2))) changingDirection = d
  }

  def getCellType(p : Point): CellType = {
    if (!snake.appleOnBoard && snake.emptyPoints.isEmpty && !isBoardFilled) findEmptyPlaces()
    if (!snake.appleOnBoard && !isBoardFilled) placeApple()

    for (i <- 0 until snake.snakePoints.length-1) {
      if (snake.snakePoints(i).x == p.x && snake.snakePoints(i).y == p.y) p.cell = SnakeBody(1.0f)
    }

    if (snake.HeadPoint.x == p.x && snake.HeadPoint.y == p.y) p.cell = SnakeHead(changingDirection)
    else if (snake.appleOnBoard && snake.applePoint.x == p.x && snake.applePoint.y == p.y) p.cell = Apple()

    p.cell
  }

  def moveSnake(currentDirection: Direction): Unit = {
    if (snake.snakePoints.length < snake.snakeLength) snake.snakePoints += snake.HeadPoint.copy()
    else {
      for (i <- 0 until snake.snakePoints.length - 1) {
        snake.snakePoints(i) = snake.snakePoints(i + 1)
      }
    }
    snake.HeadPoint.movePoint(currentDirection)
    handleBorders(currentDirection)
    snake.snakePoints(snake.snakePoints.length - 1) = snake.HeadPoint.copy()
    snake.emptyPoints = Vector[Point]()

  }

  def checkForEatenApple(): Unit = {
    if (snake.HeadPoint.x == snake.applePoint.x && snake.HeadPoint.y == snake.applePoint.y) {
      snake.snakeLength += 3
      findEmptyPlaces()
      if (snake.snakePoints.length < gameRoom) placeApple()
      snake.appleOnBoard = false
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

  def isBoardFilled: Boolean = {
    if (snake.snakePoints.length >= gameRoom) {
      true
    }
    else false
  }
  def findEmptyPlaces(): Unit = {
    for (y <- 0 until gridDims.height) {
      for (x <- 0 until gridDims.width)
        if (!snake.snakePoints.contains(Point(x,y))) {
          assert(!snake.emptyPoints.contains(Point(x,y)))
          snake.emptyPoints = snake.emptyPoints :+ Point(x, y)
          }
      }
   }

  // TODO: The apple should be placed during the drawing of the board, so the point of the apple should already be calculated.
  def placeApple(): Unit = {
    val randomNumber = random.randomInt(upTo = snake.emptyPoints.length)
    snake.applePoint = snake.emptyPoints(randomNumber).copy()
    snake.appleOnBoard = true

    println("apple placed at: " + snake.applePoint)
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
    Dimensions(width = 10, height = 10)  // you can adjust these values to play on a different sized board

}


