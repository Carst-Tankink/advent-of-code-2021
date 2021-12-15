package day15

import util.Helpers.Companion.intValue
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

class Chiton(fileName: String) : Solution<List<Int>, Long>(fileName) {
    override fun parse(line: String): List<Int> = line.map { it.intValue() }

    override fun List<List<Int>>.solve1(): Long {
        val grid: Map<Point, Int> = this.toGrid()
        val initial = Point(0, 0)
        val finalPoint = grid.keys.maxByOrNull { it.x * it.y } ?: initial

        tailrec fun dijkstra(current: Point, unvisited: Set<Point>, tentative: Map<Point, Long>): Long {
            val currentDistance = tentative[current] ?: -1
            return if (current == finalPoint) currentDistance else {
                val unvisitedNeighbours = current.getNeighbours(true).filter { it in unvisited }

                val newTentatives = tentative - unvisitedNeighbours + unvisitedNeighbours.associateWith {
                    val previousDistance = tentative[it] ?: Long.MAX_VALUE
                    val newDistance = currentDistance + grid[it]!!

                    if (newDistance < previousDistance) newDistance else previousDistance
                }

                val left = unvisited - current
                val next = left.minByOrNull { tentative[it] ?: Long.MAX_VALUE }!!
                dijkstra(next, left, newTentatives)
            }
        }

        return dijkstra(
            initial,
            grid.keys,
            mapOf(initial to 0))
    }

    override fun List<List<Int>>.solve2(): Long {
        TODO("Not yet implemented")
    }
}