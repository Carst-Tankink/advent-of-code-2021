package day17

import util.Point
import util.Solution

class TrickShot(fileName: String) : Solution<Pair<Point, Point>, Long>(fileName) {
    override fun parse(line: String): Pair<Point, Point>? {
        val pattern = """target area: x=(-?\d+)\.\.(-?\d+), y=(-?\d+)\.\.(-?\d+)""".toRegex()
        val groups = pattern.find(line)?.groups ?: error("No match")

        val bottomLeft = Point(groups[1]!!.value.toLong(), groups[3]!!.value.toLong())
        val topRight = Point(groups[2]!!.value.toLong(), groups[4]!!.value.toLong())

        return Pair(bottomLeft, topRight)
    }
    val bottomLeft = data[0].first
    val topRight = data[0].second

    private fun toward0(x: Long): Long = when {
        (x < 0L) -> x + 1
        (x == 0L) -> 0
        (x > 0L) -> x - 1
        else -> error("Non-exhaustive")
    }

    private tailrec fun flyProbe(velocity: Point, position: Point = Point(0, 0), highestY: Long = 0): Pair<Point, Long> {

        return if (position.isWithin(
                bottomLeft, topRight
            ) || position.isBeyond(bottomLeft, topRight)
        ) Pair(position, highestY) else {
            val newVelocity = Point(toward0(velocity.x), velocity.y - 1)
            val newPosition = position + velocity
            flyProbe(newVelocity, newPosition, if (newPosition.y > highestY) newPosition.y else highestY)
        }
    }

    override fun List<Pair<Point, Point>>.solve1(): Long {

        return (0..topRight.x).flatMap { x -> (0..-bottomLeft.y).map { flyProbe(Point(x, it)) } }
            .filter { it.first.isWithin(bottomLeft, topRight) }
            .maxOf { it.second }
    }

    override fun List<Pair<Point, Point>>.solve2(): Long {
        return (0..topRight.x).flatMap { x -> (bottomLeft.y..-bottomLeft.y).map { flyProbe(Point(x, it)) } }
            .count { it.first.isWithin(bottomLeft, topRight) }
            .toLong()
    }

    private fun Point.isBeyond(bottomLeft: Point, topRight: Point): Boolean =
        this.x > topRight.x || this.y < bottomLeft.y

    private fun Point.isWithin(bottomLeft: Point, topRight: Point): Boolean =
        bottomLeft.x <= this.x && this.x <= topRight.x &&
            bottomLeft.y <= this.y && this.y <= topRight.y
}
