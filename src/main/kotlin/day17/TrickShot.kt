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

    override fun List<Pair<Point, Point>>.solve1(): Long {
        val bottomLeft = this[0].first
        val topRight = this[0].second
        val origin = Point(0, 0)

        fun toward0(x: Long): Long = when {
            (x < 0L) -> x + 1
            (x == 0L) -> 0
            (x > 0L) -> x - 1
            else -> error("Non-exhaustive")
        }

        tailrec fun flyProbe(position: Point, velocity: Point, highestY: Long): Pair<Point, Long> {
            return if (position.isWithin(
                    bottomLeft, topRight
                ) || position.isBeyond(bottomLeft, topRight)
            ) Pair(position, highestY) else {
                val newVelocity = Point(toward0(velocity.x), velocity.y - 1)
                val newPosition = position + velocity
                flyProbe(newPosition, newVelocity, if (newPosition.y > highestY) newPosition.y else highestY)
            }
        }


        return (0..topRight.x).flatMap { x -> (0..-bottomLeft.y).map { flyProbe(origin, Point(x, it), 0) } }
                .filter { it.first.isWithin(bottomLeft, topRight) }
                .maxOf { it.second }
    }

    override fun List<Pair<Point, Point>>.solve2(): Long {
        TODO("Not yet implemented")
    }

    private fun Point.isBeyond(bottomLeft: Point, topRight: Point): Boolean =
        this.x > topRight.x || this.y < bottomLeft.y

    private fun Point.isWithin(bottomLeft: Point, topRight: Point): Boolean =
        bottomLeft.x <= this.x && this.x <= topRight.x &&
            bottomLeft.y <= this.y && this.y <= topRight.y
}
