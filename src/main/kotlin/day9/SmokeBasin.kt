package day9

import util.Helpers.Companion.intValue
import util.Point
import util.Solution

class SmokeBasin(fileName: String) : Solution<List<Int>, Long>(fileName) {
    override fun parse(line: String): List<Int> = line.map { it.intValue() }

    override fun List<List<Int>>.solve1(): Long {
        val heightMap: Map<Point, Int> = createHeightMap(this)

        tailrec fun sumRisk(risks: Long, left: List<Map.Entry<Point, Int>>): Long {
            return if (left.isEmpty()) risks else {
                val next = left[0]
                val localHeight = next.value
                val neighbourHeights = getNeighbours(next.key)
                    .mapNotNull { heightMap[it] }
                val risk = if (neighbourHeights.all { it > localHeight }) localHeight + 1 else 0
                sumRisk(risks + risk, left.drop(1))
            }
        }

        return sumRisk(0, heightMap.entries.toList())
    }

    fun getNeighbours(point: Point): List<Point> = listOf(
        Point(0, -1),
        Point(1, 0),
        Point(0, 1),
        Point(-1, 0)
    ).map { dir -> point + dir }

    private fun createHeightMap(values: List<List<Int>>): Map<Point, Int> =
        values.mapIndexed { y, line ->
            line.mapIndexed() { x, h -> Point(x.toLong(), y.toLong()) to h }
        }.flatten().toMap()

    override fun List<List<Int>>.solve2(): Long {
        TODO("Not yet implemented")
    }
}