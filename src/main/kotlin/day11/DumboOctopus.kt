package day11

import util.Grid
import util.Helpers.Companion.intValue
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

class DumboOctopus(fileName: String) : Solution<List<Int>, Long>(fileName) {
    override fun parse(line: String): List<Int> = line.map { it.intValue() }

    override fun List<List<Int>>.solve1(): Long {
        val grid = this.toGrid()
        tailrec fun flash(g: Grid<Int>, flashed: Set<Point>): Pair<Set<Point>, Grid<Int>> {
            val toFlash = g
                .filterValues { it > 9 }
                .filterKeys { it !in flashed }

            return if (toFlash.isEmpty()) Pair(flashed, g) else {
                val nextFlash = toFlash.keys.first()
                val neighbours = nextFlash.getNeighbours(cardinal = false)
                flash(g.mapValues { if (it.key in neighbours) it.value + 1 else it.value }, flashed + nextFlash)
            }
        }

        fun runStep(g: Grid<Int>): Pair<Long, Grid<Int>> {
            val increased = g.mapValues { it.value + 1 }
            val (flashed, newGrid) = flash(increased, emptySet())
            return Pair(flashed.size.toLong(), newGrid.mapValues { if (it.value > 9) 0 else it.value })
        }

        tailrec fun runSteps(currentStep: Int, flashed: Long, g: Grid<Int>): Long {
            return if (currentStep == 100) flashed else {
                val (flashStep: Long, newGrid: Grid<Int>) = runStep(g)
                runSteps(currentStep + 1, flashed + flashStep, newGrid)
            }
        }

        return runSteps(0, 0, grid)
    }

    override fun List<List<Int>>.solve2(): Long {
        TODO("Not yet implemented")
    }
}