package util

sealed class Either<L, R>(val left: L?, val right: R?)
class Left<L, R>(value: L): Either<L, R>(left = value, right = null)
class Right<L, R>(value: R): Either<L, R>(left = null, right = value)

data class Point(val x: Long, val y: Long) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    fun getNeighbours(cardinal: Boolean): List<Point> {
        val nonCardinal = if (cardinal) emptyList() else listOf(
            Point(-1, -1), Point(1, -1),
            Point(-1, 1), Point(1, 1)
        )
        return (nonCardinal + listOf(
            Point(0, -1),
            Point(1, 0),
            Point(0, 1),
            Point(-1, 0)
        )).map { dir -> this + dir }
    }
}

typealias Grid<T> = Map<Point, T>

class Helpers {
    companion object {
        fun Char.intValue() = this.code - 48

        fun <T> transpose(input: List<List<T>>): List<List<T>> {
            return if (input.any { it.isEmpty() }) emptyList() else
                listOf(input.map { it[0] }) + transpose(input.map { it.drop(1) })
        }

        fun toDecimal(digits: List<Int>, base: Int): Long {
            tailrec fun rec(acc: Long, power: Long, remaining: List<Int>): Long {
                return if (remaining.isEmpty()) acc else {
                    rec(acc + power * remaining[0], power * base, remaining.drop(1))
                }
            }

            return rec(0, 1, digits.reversed())
        }

        fun <T> List<List<T>>.toGrid(): Grid<T> {
            return this.mapIndexed { y, line ->
                line.mapIndexed { x, t ->
                    Point(x.toLong(), y.toLong()) to t
                }
            }
                .flatten()
                .toMap()
        }
    }


}
