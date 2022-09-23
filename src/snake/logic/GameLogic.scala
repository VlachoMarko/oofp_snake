package snake.logic

import engine.random.RandomGenerator
import scala.collection.mutable.ArrayBuffer

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

  if (!isBoardFilled) gameState.emptyPoints = getEmptyPoints(snake.snakePoints); gameState.applePoint = getApplePoint(gameState.emptyPoints)
  def step(): Unit = {

    val currentDirection = gameState.changingDirection

    if (!gameOver && snake.changingSnakeLength <= gameState.gameRoom) {
      snake.snakePoints = moveSnake(snake.snakePoints)
      snake.headPoint = moveHead(snake.headPoint, currentDirection)
      gameState.emptyPoints = getEmptyPoints(snake.snakePoints)
      checkForEatenApple()
      gameOver = isGameOver(snake.snakePoints, snake.headPoint)

    } else gameOver = true
  }

  // TODO implement me
  def setReverse(r: Boolean): Unit = ()


  def changeDir(d: Direction): Unit = {
    val testHead = snake.headPoint.copy()
    testHead.movePoint(d)
    if (isNotDirectionCollision(d, testHead)) gameState.changingDirection = d
  }

  def isNotDirectionCollision(d : Direction, testHead : Point) : Boolean = gameState.changingDirection != d.opposite && testHead != snake.startOfSnakeBody

  def getCellType(p : Point): CellType = {
    if (snake.snakePoints.contains(p)) p.cell = SnakeBody(1.0f)
    if (snake.snakePoints.last == p) p.cell = SnakeHead(gameState.changingDirection)
    else if (gameState.applePoint == p) p.cell = Apple()

    p.cell
  }

  def moveSnake(snakePoints: ArrayBuffer[Point]): ArrayBuffer[Point] = {
    if (snakePoints.length < snake.changingSnakeLength) snakePoints += snake.headPoint.copy()
    else {
      for (i <- 0 until snakePoints.length - 1) {
        snakePoints(i) = snakePoints(i + 1)
      }
    }
    snakePoints
  }
  def moveHead(headPoint : Point, currDir: Direction): Point = {
    var tempHead = headPoint
    tempHead.movePoint(currDir)
    tempHead = handleBorders(tempHead, currDir)

    snake.snakePoints(snake.snakePoints.length - 1) = tempHead.copy()
    tempHead
  }

  def checkForEatenApple(): Unit = {
    if (snake.headPoint == gameState.applePoint) {
      snake.changingSnakeLength += 3
      if (snake.snakePoints.length < gameState.gameRoom) gameState.applePoint = getApplePoint(gameState.emptyPoints)
    }
  }

  def isGameOver(snakePoints : ArrayBuffer[Point], newHead : Point): Boolean = {
    if (snakePoints.contains(newHead)) {
      if (isCollision(snakePoints, newHead)) {
        return true
      }
    }
    false
  }

  def isCollision(snakePoints : ArrayBuffer[Point], newHead: Point): Boolean = {
    snakePoints.indexOf(newHead) != 0 && snakePoints.indexOf(newHead) != snakePoints.length - 1
  }

  def handleBorders(headPoint : Point, currDir: Direction): Point ={
    if      (headPoint.x == gridDims.width && currDir == East())   Point(0, headPoint.y)
    else if (headPoint.x == -1 && currDir == West())               Point(gridDims.width-1, headPoint.y)
    else if (headPoint.y == gridDims.height && currDir == South()) Point(headPoint.x, 0)
    else if (headPoint.y == -1 && currDir == North())              Point(headPoint.x, gridDims.height-1)
    else headPoint
  }

  def isBoardFilled: Boolean = snake.snakePoints.length >= gameState.gameRoom

  def getEmptyPoints(snakePoints : ArrayBuffer[Point]): Vector[Point] = {
    var emptyPoints : Vector[Point] = Vector[Point]()
    for (y <- 0 until gridDims.height) {
      for (x <- 0 until gridDims.width)
        if (!snakePoints.contains(Point(x,y))) {
          emptyPoints = emptyPoints :+ Point(x, y)
          }
      }
    emptyPoints
   }

  def getApplePoint(emptyPoints : Vector[Point]): Point = {
    var applePoint : Point = Point(-10, -10)
    if (emptyPoints.nonEmpty){
      val randomNumber = random.randomInt(emptyPoints.length)
      applePoint = emptyPoints(randomNumber).copy()
      applePoint
    }
    else applePoint
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


