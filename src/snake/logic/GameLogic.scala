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

  val gameRoom: Int = gridDims.width * gridDims.height


  //TODO: These can be in the new "Snake" class? Also with the other shit
  var snakePoints: ArrayBuffer[Point] = new ArrayBuffer[Point]
  snakePoints += Point(0,0); snakePoints += Point(1,0); snakePoints += Point(2,0)
  var snakeLength : Int = 3
  var currentHead : Point = snakePoints.last.copy()

  var currentDirection: Direction = East()
  var applePoint : Point = Point(0,0)
  var appleOnBoard: Boolean = false
  var emptySize : Int = gameRoom - snakeLength
  var emptyPlaces: Vector[Point] = Vector[Point]()

  var gameOver: Boolean = false


  // TODO: I have to keep track of the empty cells during the game.
  def step(): Unit = {

    if (!gameOver) {
      if (!appleOnBoard && emptyPlaces.isEmpty && !isBoardFilled) findEmptyPlaces()
      emptySize = gameRoom - snakePoints.length

      if (snakePoints.length < snakeLength) snakePoints += currentHead.copy()
      else {
        for (i <- 0 until snakePoints.length - 1) {
          snakePoints(i) = snakePoints(i + 1)
        }
      }
      currentHead.movePoint(currentDirection)
      if (snakePoints.contains(currentHead)) gameOver = true
      snakePoints(snakePoints.length - 1) = currentHead.copy()
      handleBorders()

      emptyPlaces = Vector[Point]()

      if (currentHead.x == applePoint.x && currentHead.y == applePoint.y) {
        snakeLength += 3
        findEmptyPlaces()
        if (snakePoints.length < gameRoom) placeApple()
        appleOnBoard = false
      }
    }
  }

  // TODO implement me
  def setReverse(r: Boolean): Unit = ()


  def changeDir(d: Direction): Unit = {
    val copyHead = currentHead.copy()
    copyHead.movePoint(d)
    if (!(currentDirection == d.opposite) && !(copyHead == snakePoints(snakePoints.length-2))) currentDirection = d
  }

  def getCellType(p : Point): CellType = {
    if (!appleOnBoard && emptyPlaces.isEmpty && !isBoardFilled) findEmptyPlaces()

    for (i <- 0 until snakePoints.length-1) {
      if (snakePoints(i).x == p.x && snakePoints(i).y == p.y) p.cell = SnakeBody(1.0f)
    }

    if (currentHead.x == p.x && currentHead.y == p.y) p.cell = SnakeHead(currentDirection)
    else if (appleOnBoard && applePoint.x == p.x && applePoint.y == p.y) p.cell = Apple()

    if (!gameOver){
    if (!appleOnBoard && !isBoardFilled) placeApple()
    }
    p.cell
  }

  def handleBorders(): Unit ={
    if (currentHead.x == gridDims.width && currentDirection == East()) currentHead.x = 0
    else if (currentHead.x == -1 && currentDirection == West()) {currentHead.x = gridDims.width-1; snakePoints(snakePoints.length-1) = currentHead.copy()}
    else if (currentHead.y == gridDims.height && currentDirection == South()) currentHead.y = 0
    else if (currentHead.y == -1 && currentDirection == North()) currentHead.y = gridDims.height-1; snakePoints(snakePoints.length-1) = currentHead.copy()
  }

  def isBoardFilled: Boolean = {
    if (snakePoints.length == (gridDims.height * gridDims.width)) {
      true
    }
    else false
  }
  def findEmptyPlaces(): Unit = {
    for (y <- 0 until gridDims.height) {
      for (x <- 0 until gridDims.width)
        if (!snakePoints.contains(Point(x,y))) {
          assert(!emptyPlaces.contains(Point(x,y)))
          emptyPlaces = emptyPlaces :+ Point(x, y)
          }
      }
   }

  // TODO: The apple should be placed during the drawing of the board, so the point of the apple should already be calculated.
  def placeApple(): Unit = {
    val randomNumber = random.randomInt(upTo = emptyPlaces.length)
    applePoint = emptyPlaces(randomNumber).copy()
    appleOnBoard = true

    println("apple placed at: " + applePoint)
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
    Dimensions(width = 6, height = 1)  // you can adjust these values to play on a different sized board

}


