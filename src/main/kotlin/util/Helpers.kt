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
    }
}