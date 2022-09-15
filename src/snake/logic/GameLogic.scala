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

  var currentHead : Point = Point(2,0)

  //TODO: These can be under "currentHead" or in the new "Snake" class? Also with the apple shit..
  var bodyPoints: ArrayBuffer[Point] = new ArrayBuffer[Point]
  bodyPoints += Point(0,0); bodyPoints += Point(1,0)
  var bodyLength : Int = 2

  var currentDirection: Direction = East()
  var applePoint : Point = Point(0,0)
  var appleOnBoard: Boolean = false
  var emptySize : Int = gridDims.width * gridDims.height - (bodyLength + 1)
  var emptyPlaces: Vector[Point] = Vector[Point]()


  def gameOver: Boolean = false

  // TODO: The apple should be placed during the drawing of the board, so the point of the apple should already be calculated.
  // TODO: Also, I have to keep track of the empty cells during the game somehow.
  def step(): Unit = {

    emptySize = gridDims.width * gridDims.height - (bodyLength + 1)

    if (appleOnBoard && currentHead.x == applePoint.x && currentHead.y == applePoint.y) {
      emptySize -= 3;
      bodyLength += 3
      emptyPlaces = Vector[Point]()
      appleOnBoard = false
    }

    if (appleOnBoard) {
      if (bodyPoints.length < bodyLength) bodyPoints += currentHead.copy()
        else {
          for (i <- 0 until bodyLength - 1) {
            bodyPoints(i) = bodyPoints(i + 1)
          }
          bodyPoints(bodyLength - 1) = currentHead.copy()
        }
        currentHead.movePoint(currentDirection)
        disableBorders()
      }
  }

  // TODO implement me
  def setReverse(r: Boolean): Unit = ()


  def changeDir(d: Direction): Unit = {
    currentDirection = d
  }

  def getCellType(p : Point): CellType = {

    for (i <- bodyPoints.indices) {
      if (bodyPoints(i).x == p.x && bodyPoints(i).y == p.y) p.cell = SnakeBody(1.0f)
    }

    if (currentHead.x == p.x && currentHead.y == p.y) p.cell = SnakeHead(currentDirection)
    else if (appleOnBoard && applePoint.x == p.x && applePoint.y == p.y) p.cell = Apple()
    else if (p.cell == Empty() && emptyPlaces.length < emptySize) emptyPlaces = emptyPlaces :+ p

    if (!appleOnBoard && (emptyPlaces.length == emptySize)) placeApple()

    p.cell
  }

  def disableBorders(): Unit ={
    if (currentHead.x == gridDims.width && currentDirection == East()) currentHead.x = 0
    else if (currentHead.x == -1 && currentDirection == West()) currentHead.x = gridDims.width-1
    else if (currentHead.y == gridDims.height && currentDirection == South()) currentHead.y = 0
    else if (currentHead.y == -1 && currentDirection == North()) currentHead.y = gridDims.height-1
  }

  def placeApple(): Unit = {
    val randomNumber = random.randomInt(upTo = emptySize)
    applePoint = emptyPlaces(randomNumber).copy()
    appleOnBoard = true

    println("apple placed at: " + applePoint)
  }
}

/** GameLogic companion object */
object GameLogic {

  val FramesPerSecond: Int = 5 // change this to increase/decrease speed of game

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
    Dimensions(width = 25, height = 25)  // you can adjust these values to play on a different sized board

}


