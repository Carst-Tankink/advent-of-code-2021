package day15

import util.*
import util.Helpers.Companion.intValue
import util.Helpers.Companion.toGrid

class Chiton(fileName: String) : Solution<List<Int>, Long>(fileName) {
    override fun parse(line: String): List<Int> = line.map { it.intValue() }

    override fun List<List<Int>>.solve1(): Long {
        val grid: Grid<Long> = this.toGrid().mapValues { it.value.toLong() }
        val finalPoint = Point(grid.keys.maxOf { it.x }, grid.keys.maxOf { it.y })
        return shortestPath(finalPoint, grid)
    }

    override fun List<List<Int>>.solve2(): Long {
        val grid = this.toGrid()

        val width = grid.width()
        val height = grid.height()
        val maxX = width * 5
        val maxY = height * 5
        val extendedGrid = (0 until maxY).flatMap { y ->
            (0 until maxX).map { x ->
                val offsetX = x % width
                val gridX = x / width

                val offsetY = y % height
                val gridY = y / height

                val amplification = (gridX + gridY)

                val extendedValue = (grid[Point(offsetX, offsetY)]!! + amplification - 1) % 9 + 1

                Point(x, y) to extendedValue
            }
        }.toMap()

        return shortestPath(Point(maxX - 1, maxY - 1), extendedGrid)
    }

    private fun shortestPath(finalPoint: Point, grid: Grid<Long>): Long {

        tailrec fun dijkstra(
            unvisited: MutableSet<Point>,
            tentative: MutableMap<Point, Long>,
            done: MutableSet<Point>
        ): Long {
            if (unvisited.size % 1000 == 0) println("Unvisited ${unvisited.size}")
            val current = unvisited
                .minByOrNull { tentative[it]!! }!!
            val currentDistance = tentative[current] ?: -1
            return if (current == finalPoint) currentDistance else {
                val unvisitedNeighbours = current
                    .getNeighbours(true)
                    .filterNot { it in done }
                    .filter { it in grid.keys }

                unvisitedNeighbours.forEach {
                    val previousDistance = tentative[it] ?: Long.MAX_VALUE
                    val newDistance = currentDistance + grid[it]!!

                    tentative[it] = if (newDistance < previousDistance) newDistance else previousDistance
                }

                unvisited.addAll(unvisitedNeighbours)
                unvisited.remove(current)
                done.add(current)

                dijkstra(unvisited, tentative, done)
            }
        }

        return dijkstra(
            setOf(Point(0, 0)).toMutableSet(),
            mapOf(Point(0, 0) to 0L).toMutableMap(),
            emptySet<Point>().toMutableSet()
        )
    }
}