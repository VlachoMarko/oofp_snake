package snake.logic

import engine.random.RandomGenerator
import snake.logic.Point.getCell

/** To implement Snake, complete the ``TODOs`` below.
 *
 * If you need additional files,
 * please also put them in the ``snake`` package.
 */
class GameLogic(val random: RandomGenerator,
                val gridDims : Dimensions) {

  var currentHead : Point = Point(0 , 0)
  var currentDirection: Direction = East()

  def gameOver: Boolean = false

  // TODO implement me
  def step(): Unit = {
    currentHead.movePoint(currentDirection)

    if (currentHead.x == GameLogic.DefaultGridDims.width && currentDirection == East()) currentHead.x = 0
    else if (currentHead.x == -1 && currentDirection == West()) currentHead.x = 24
    else if (currentHead.y == GameLogic.DefaultGridDims.height && currentDirection == South()) currentHead.y = 0
    else if (currentHead.y == -1 && currentDirection == North()) currentHead.y = 24
  }

  // TODO implement me
  def setReverse(r: Boolean): Unit = ()

  // TODO implement me
  def changeDir(d: Direction): Unit = {
    currentDirection = d
  }

  // TODO implement me
  def getCellType(p : Point): CellType = {

    if (currentHead.x == p.x && currentHead.y == p.y) p.cell = SnakeHead(currentDirection)

    getCell(p)
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


