package day13

import day13.FoldInstruction.FoldDirection.HORIZONTAL
import day13.FoldInstruction.FoldDirection.VERTICAL
import util.*
import util.Helpers.Companion.printGrid

data class FoldInstruction(val coord: Int, val direction: FoldDirection) {
    enum class FoldDirection {
        HORIZONTAL, VERTICAL
    }
}

class TransparentOrigami(fileName: String) : Solution<Either<Point, FoldInstruction>, Int>(fileName) {
    override fun parse(line: String): Either<Point, FoldInstruction>? {
        return when {
            line.isEmpty() -> null
            line.startsWith("fold along") -> {
                val instruction = line.replace("fold along ", "").split("=")
                val direction =
                    if (instruction[0] == "y") HORIZONTAL else VERTICAL
                Right(FoldInstruction(instruction[1].toInt(), direction))
            }
            else -> {
                val points = line.split(",").map { it.toLong() }
                Left(Point(points[0], points[1]))
            }
        }
    }

    private fun foldSheet(points: Set<Point>, instruction: FoldInstruction): Set<Point> {
        val pointSelector =
            { p: Point -> if (instruction.direction == HORIZONTAL) p.y else p.x }

        val (beforeLine, afterLine) = points.partition { pointSelector(it) < instruction.coord }
        val maxInFoldDirection = afterLine.maxOf(pointSelector)
        val folded = afterLine
            .map {
                if (instruction.direction == HORIZONTAL) {
                    Point(it.x, maxInFoldDirection - it.y)
                } else {
                    Point(maxInFoldDirection - it.x, it.y)
                }
            }

        return (beforeLine + folded).toSet()
    }

    override fun List<Either<Point, FoldInstruction>>.solve1(): Int {
        val points = this
            .mapNotNull { it.left }
            .toSet()

        val instructions = this.mapNotNull { it.right }

        return foldSheet(points, instructions[0]).size
    }

    override fun List<Either<Point, FoldInstruction>>.solve2(): Int {
        val sheet = this
        .mapNotNull { it.left }
        .toSet()

        val folds = this.mapNotNull { it.right }
        val folded = folds.fold(sheet) { s, i -> foldSheet(s, i) }
        println("Grid:\n${printGrid(folded)}")

        return -1
    }


}
