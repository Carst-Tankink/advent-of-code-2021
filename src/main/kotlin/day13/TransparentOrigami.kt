package day13

import day13.FoldInstruction.FoldDirection.HORIZONTAL
import day13.FoldInstruction.FoldDirection.VERTICAL
import util.*

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

    private fun foldPoints(points: Set<Point>, instruction: FoldInstruction): Set<Point> {
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

        val result = (beforeLine + folded).toSet()
        return result
    }

    override fun List<Either<Point, FoldInstruction>>.solve1(): Int {
        val points = this
            .mapNotNull { it.left }
            .toSet()

        val instructions = this.mapNotNull { it.right }

        return foldPoints(points, instructions[0]).size
    }

    override fun List<Either<Point, FoldInstruction>>.solve2(): Int {
        val points = this
        .mapNotNull { it.left }
        .toSet()

        val instructions = this.mapNotNull { it.right }
        val finalGrid = instructions.fold(points) { grid, i -> foldPoints(grid, i) }


        println("Grid:\n${printGrid(finalGrid)}")

        return -1
    }

    private fun printGrid(filledPoints: Set<Point>): String {
        return (0..(filledPoints.map { it.y }.maxOrNull() ?: 0)).map { y ->
            (0..(filledPoints.map { it.x }.maxOrNull() ?: 0)).map { x ->
                if (Point(x, y) in filledPoints) '#' else ' '
            }.joinToString("")
        }.joinToString("\n")
    }
}
