package day5

import util.Point
import util.Solution

data class Line(val start: Point, val end: Point) {
    fun isHorizontal(): Boolean = start.y == end.y
    fun isVertical(): Boolean = start.x == end.x

    fun cover(): Set<Point> = when {
        isHorizontal() && start.x < end.x -> (start.x..end.x).map { Point(it, start.y) }.toSet()
        isHorizontal() && start.x > end.x -> (start.x downTo end.x).map { Point(it, start.y) }.toSet()
        isVertical() && start.y < end.y -> (start.y..end.y).map { Point(start.x, it) }.toSet()
        isVertical() && start.y > end.y -> (start.y downTo end.y).map { Point(start.x, it) }.toSet()
        else -> error("Not implement for diagonals")
    }

    fun overlapping(next: Line): Set<Point> = next.cover().intersect(this.cover())
}

class HypothermalVenture(fileName: String) : Solution<Line, Long>(fileName) {
    override fun parse(line: String): Line {
        val points = line
            .split("->")
            .map {
                val coords = it.trim().split(",").map { p -> p.toLong() }
                Point(coords[0], coords[1])
            }
        return Line(points[0], points[1])
    }

    override fun List<Line>.solve1(): Long {
        tailrec fun overlap(points: Set<Point>, remaining: List<Line>): Set<Point> {
            return if (remaining.isEmpty()) points else {
                val head = remaining[0]
                val tail = remaining.drop(1)
                val newPoints = tail.flatMap { it.overlapping(head) }.toSet()
                overlap(points + newPoints, tail)
            }
        }

        val straightLines = this.filter { it.isVertical() || it.isHorizontal() }

        return overlap(emptySet(), straightLines).count().toLong()
    }

    override fun List<Line>.solve2(): Long {
        TODO("Not yet implemented")
    }

}