package snake.logic

import engine.random.RandomGenerator

/** To implement Snake, complete the ``TODOs`` below.
 *
 * If you need additional files,
 * please also put them in the ``snake`` package.
 */
class GameLogic(val random: RandomGenerator,
                val gridDims : Dimensions) {


  var gameState: GameState = GameState(gridDims)
  var snake : Snake = gameState.snake
  var gameOver: Boolean = false
  var changingDirection: Direction = East()
  var reverseModeChanging: Boolean = false
  var currentStep : Int = 0

  if (!isBoardFilled) gameState.emptyPoints = getEmptyPoints(snake.snakePoints); gameState.applePoint = getApplePoint(gameState.emptyPoints)
  var stateStorage : StateStorage = new StateStorage(gameState)
  def step(): Unit = {

    currentStep += 1
    println("step: " + currentStep)
    gameState.currentDirection = changingDirection
    val reverseMode: Boolean = reverseModeChanging

    // println("snake points: " + snake.snakePoints)

    if (reverseMode) {
      println("reverse mode on")
      snake.snakePoints = getPreviousSnake
      snake.headPoint = snake.snakePoints(snake.snakePoints.length-1)
      gameState.currentDirection = getPreviousDirection
      gameState.applePoint = getPreviousApple
      gameState.snake.changingLength = getChangingLength
      gameOver = false
    }
    else {
      val testHead: Point = moveHead(snake.headPoint, gameState.currentDirection)
      gameOver = isGameOver(snake.snakePoints, testHead)
    }

    println("game over: " + gameOver)



    if (!gameOver && snake.snakePoints.length <= gameState.gameRoom) {

      if (!reverseMode) {
        snake.headPoint = moveHead(snake.headPoint, gameState.currentDirection)
        snake.snakePoints = moveSnake(snake.snakePoints)
        gameState.emptyPoints = getEmptyPoints(snake.snakePoints)
        checkForEatenApple()
        stateStorage.addSnakePoints(gameState)
        stateStorage.addSnakeSize(gameState)
        stateStorage.addSnakeDirection(gameState)
        stateStorage.addApplePoint(gameState)
        stateStorage.addChangingLength(gameState)

      } else {
        /*println("step: " + currentStep)
        println("number of states: " + stateStorage.storedSnakeSizes.size)
        for (i <- stateStorage.storedSnakePoints.indices) {
          println(stateStorage.storedSnakePoints(i))
        }
        for (i <- stateStorage.storedSnakeSizes.indices){
          println(stateStorage.storedSnakeSizes(i))
        }*/
      }
    }

  }



  def setReverse(r: Boolean): Unit = {
    reverseModeChanging = r
  }

  def getPreviousSnake: Vector[Point] = {
    var sliceIndex: Int = 0

    if (stateStorage.storedSnakeSizes.length > 1 && !gameOver) {
      stateStorage.storedSnakeSizes = stateStorage.storedSnakeSizes.slice(0, stateStorage.storedSnakeSizes.length-1)

      for (i <- stateStorage.storedSnakeSizes.indices) {
        sliceIndex += stateStorage.storedSnakeSizes(i)
      }
      stateStorage.storedSnakePoints = stateStorage.storedSnakePoints.slice(0, sliceIndex)
    }

    var tempVector: Vector[Point] = Vector[Point]()
    for (i <- (stateStorage.storedSnakePoints.length - stateStorage.storedSnakeSizes.last) until stateStorage.storedSnakePoints.length) {
      tempVector = tempVector :+ stateStorage.storedSnakePoints(i)
    }

    println("returned snakeSizes: " + stateStorage.storedSnakeSizes)
    println("returned snakePoints: " + stateStorage.storedSnakePoints)
    tempVector

  }

  def getPreviousDirection : Direction = {
    if (stateStorage.storedSnakeDirections.length > 1 && !gameOver) {
      stateStorage.storedSnakeDirections = stateStorage.storedSnakeDirections.slice(0, stateStorage.storedSnakeDirections.length - 1)
    }
    stateStorage.storedSnakeDirections.last
  }

  def getPreviousApple : Point = {
    if (stateStorage.storedApplePoints.length > 1 && !gameOver) {
      stateStorage.storedApplePoints = stateStorage.storedApplePoints.slice(0, stateStorage.storedApplePoints.length - 1)
    }
   stateStorage.storedApplePoints.last
  }

  def getChangingLength : Int = {
    if (stateStorage.storedChangingLengths.length > 1 && !gameOver) {
      stateStorage.storedChangingLengths = stateStorage.storedChangingLengths.slice(0, stateStorage.storedChangingLengths.length - 1)
    }
    println("returned changing lengths: " + stateStorage.storedChangingLengths.last)
    stateStorage.storedChangingLengths.last
  }

  def changeDir(d: Direction): Unit = {
    val testHead = snake.headPoint.copy()
    testHead.movePoint(d)
    if (isNotDirectionCollision(d, testHead)) changingDirection = d
  }

  def isNotDirectionCollision(d : Direction, testHead : Point) : Boolean = changingDirection != d.opposite && testHead != snake.startOfSnakeBody

  def getCellType(p : Point): CellType = {
    if (snake.snakePoints.contains(p)) p.cell = SnakeBody(1.0f)
    if (snake.snakePoints.last == p) p.cell = SnakeHead(correctDirection)
    else if (gameState.applePoint == p) p.cell = Apple()

    p.cell
  }

  def correctDirection : Direction = if (reverseModeChanging) gameState.currentDirection else changingDirection

  def moveSnake(snakePoints: Vector[Point]): Vector[Point] = {
    var tempVector: Vector[Point] = snakePoints
    if (snakePoints.length < snake.changingLength) tempVector = tempVector :+ snake.headPoint.copy()
    else {
      tempVector = Vector[Point]()
      for (i <- 0 until snakePoints.length - 1) {
        tempVector = tempVector :+ snakePoints(i + 1)
      }
      tempVector = tempVector :+ snake.headPoint.copy()
    }
    tempVector
  }
  def moveHead(headPoint : Point, currDir: Direction): Point = {
    var tempHead = headPoint.copy()
    tempHead.movePoint(currDir)
    tempHead = handleBorders(tempHead, currDir)
    tempHead
  }

  def checkForEatenApple(): Unit = {
    if (snake.headPoint == gameState.applePoint) {
      snake.changingLength += 3
      if (snake.snakePoints.length < gameState.gameRoom) gameState.applePoint = getApplePoint(gameState.emptyPoints)
    }
  }

  def isGameOver(snakePoints : Vector[Point], newHead : Point): Boolean = {
    var testVector : Vector[Point] = snakePoints
    testVector = moveSnake(testVector)
    if (testVector.contains(newHead) && !noCollision(testVector, newHead)) {
      return true
    }
    false
  }

  def noCollision(testVector : Vector[Point], newHead: Point): Boolean = {
    testVector.indexOf(newHead) == testVector.length-1
  }

  def handleBorders(headPoint : Point, currDir: Direction): Point ={
    if      (headPoint.x == gridDims.width && currDir == East())   Point(0, headPoint.y)
    else if (headPoint.x == -1 && currDir == West())               Point(gridDims.width-1, headPoint.y)
    else if (headPoint.y == gridDims.height && currDir == South()) Point(headPoint.x, 0)
    else if (headPoint.y == -1 && currDir == North())              Point(headPoint.x, gridDims.height-1)
    else headPoint
  }

  def isBoardFilled: Boolean = snake.snakePoints.length >= gameState.gameRoom

  def getEmptyPoints(snakePoints : Vector[Point]): Vector[Point] = {
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

  val FramesPerSecond: Int = 2 // change this to increase/decrease speed of game

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
    Dimensions(width = 15, height = 15)  // you can adjust these values to play on a different sized board

}


