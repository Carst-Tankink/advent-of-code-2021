package util

data class Point(val x: Long, val y: Long) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

class Helpers {
    companion object {
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
    }
}