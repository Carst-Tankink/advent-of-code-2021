package day5

import util.Point
import util.Solution

data class Line(val start: Point, val end: Point)

class HypothermalVenture(fileName: String) : Solution<Line, Long>(fileName) {
    override fun parse(line: String): Line? {
        val points = line
            .split("->")
            .map {
                val coords = it.trim().split(",").map { p -> p.toLong() }
                Point(coords[0], coords[1])
            }

        return Line(points[0], points[1])
    }

    override fun List<Line>.solve1(): Long {
        TODO("Not yet implemented")
    }

    override fun List<Line>.solve2(): Long {
        TODO("Not yet implemented")
    }

}