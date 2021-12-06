package day2

import util.Point
import util.Solution

data class Submarine(val pos: Point, val aim: Long)

class Dive(fileName: String) : Solution<Point, Long>(fileName) {
    override fun parse(line: String): Point? {
        val elements = line.split(" ")
        val speed = elements[1].toLong()
        return when (elements[0]) {
            "forward" -> Point(speed, 0)
            "down" -> Point(0, speed)
            "up" -> Point(0, -speed)
            else -> Point(0, 0)
        }
    }

    override fun List<Point>.solve1(): Long {
        val pos = fold(Point(0, 0)) { acc, position -> acc + position }
        return pos.x * pos.y
    }

    override fun List<Point>.solve2(): Long {
        val finalPoint = fold(Submarine(Point(0, 0), 0)) { acc, position ->
            val change = Point(position.x, acc.aim * position.x)

            Submarine(acc.pos + change, acc.aim + position.y)
        }.pos

        return finalPoint.x * finalPoint.y
    }

}