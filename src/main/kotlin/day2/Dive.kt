package day2

import util.Solution

data class Position(val x: Long, val y: Long) {
    operator fun plus(other: Position): Position {
        return Position(x + other.x, y + other.y)
    }
}

data class Submarine(val pos: Position, val aim: Long)

class Dive(fileName: String) : Solution<Position, Long>(fileName) {
    override fun parse(line: String): Position? {
        val elements = line.split(" ")
        val speed = elements[1].toLong()
        return when (elements[0]) {
            "forward" -> Position(speed, 0)
            "down" -> Position(0, speed)
            "up" -> Position(0, -speed)
            else -> Position(0, 0)
        }
    }

    override fun List<Position>.solve1(): Long {
        val pos = fold(Position(0, 0)) { acc, position -> acc + position }
        return pos.x * pos.y
    }

    override fun List<Position>.solve2(): Long {
        val finalPosition = fold(Submarine(Position(0, 0), 0)) { acc, position ->
            val change = Position(position.x, acc.aim * position.x)

            Submarine(acc.pos + change, acc.aim + position.y)
        }.pos

        return finalPosition.x * finalPosition.y
    }

}