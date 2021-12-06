package day5

import util.Point
import util.Solution

data class Line(val start: Point, val end: Point) {
    fun isHorizontal(): Boolean = start.y == end.y
    fun isVertical(): Boolean = start.x == end.x

    private val xCover: Set<Long> = (if (start.x <= end.x) (start.x..end.x) else (start.x downTo end.x)).toSet()
    private val yCover: Set<Long> = (if (start.y <= end.y) (start.y..end.y) else (start.y downTo end.y)).toSet()

    fun cover(): Set<Point> = when {
        isHorizontal() -> xCover.map { Point(it, start.y) }
        isVertical() -> yCover.map { Point(start.x, it) }
        else -> xCover.zip(yCover).map { Point(it.first, it.second) }
    }.toSet()

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

    private tailrec fun overlap(points: Set<Point>, remaining: List<Line>): Set<Point> {
        return if (remaining.isEmpty()) points else {
            val head = remaining[0]
            val tail = remaining.drop(1)
            val newPoints = tail.flatMap { it.overlapping(head) }.toSet()
            overlap(points + newPoints, tail)
        }
    }

    override fun List<Line>.solve1(): Long {
        val straightLines = this.filter { it.isVertical() || it.isHorizontal() }
        return overlap(emptySet(), straightLines).count().toLong()
    }

    override fun List<Line>.solve2(): Long {
        return overlap(emptySet(), this).count().toLong()
    }
}