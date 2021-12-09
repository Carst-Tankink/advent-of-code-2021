package day9

import util.Helpers.Companion.intValue
import util.Point
import util.Solution

class SmokeBasin(fileName: String) : Solution<List<Int>, Long>(fileName) {
    override fun parse(line: String): List<Int> = line.map { it.intValue() }

    private val heightMap: Map<Point, Int> = createHeightMap()
    override fun List<List<Int>>.solve1(): Long {
        return lowPoints().sumOf { heightMap[it]!!.toLong() + 1 }
    }

    override fun List<List<Int>>.solve2(): Long {
        tailrec fun createBasin(basin: Set<Point>): Set<Point> {
            val neighbours = basin.flatMap { it.getNeighbours() }
                .filterNot { it in basin }
                .filterNot { (heightMap[it] ?: 9) == 9 }

            return if (neighbours.isEmpty()) basin else createBasin(basin + neighbours)
        }

        tailrec fun findBasins(basins: Set<Set<Point>>, left: List<Point>): Set<Set<Point>> {
            return if (left.isEmpty()) basins else findBasins(basins + setOf(createBasin(setOf(left[0]))), left.drop(1))

        }

        val lowPoints = lowPoints().toList()
        return findBasins(emptySet(), lowPoints).map { basin -> basin.size }
            .sortedDescending()
            .take(3)
            .fold(1) { acc, i -> acc * i }
    }


    private tailrec fun lowPoints(
        risks: Set<Point> = setOf(),
        left: List<Point> = heightMap.keys.toList()
    ): Set<Point> {
        return if (left.isEmpty()) risks else {
            val next = left[0]
            val localHeight = heightMap[next]!!
            val neighbourHeights = next.getNeighbours()
                .mapNotNull { heightMap[it] }
            val lowPoint = if (neighbourHeights.all { it > localHeight }) setOf(next) else emptySet()
            lowPoints(risks + lowPoint, left.drop(1))
        }
    }

    private fun createHeightMap(): Map<Point, Int> =
        data.mapIndexed { y, line ->
            line.mapIndexed() { x, h -> Point(x.toLong(), y.toLong()) to h }
        }.flatten().toMap()
}