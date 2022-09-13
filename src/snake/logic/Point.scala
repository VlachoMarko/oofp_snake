package snake.logic

// you can alter this file!

case class Point(x : Int, y : Int, var cell: CellType = Empty()) {
}

object Point {

  def setCell(x: Int, y: Int, cellType: CellType): Point = {
    val newP = Point(x, y)
    newP.cell = cellType
    newP
  }

  def getCell(p: Point): CellType = {
    p.cell
  }
}

