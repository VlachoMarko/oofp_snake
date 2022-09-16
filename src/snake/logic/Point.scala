package snake.logic

import snake.logic.Point.getMovements

// you can alter this file!


case class Point(var x : Int, var y : Int) {
  var cell: CellType = Empty()

  def movePoint(d: Direction): Unit = {
    var movements: (Int, Int) = getMovements(d)
    this.x +=  movements._1
    this.y += movements._2
  }

}

object Point {


  def getMovements(d: Direction): (Int, Int) =
    d match {
      case East() => (1, 0)
      case West() => (-1, 0)
      case North() => (0, -1)
      case South() => (0, 1)
    }

}