package day20

import util.Grid
import util.Helpers
import util.Helpers.Companion.toGrid
import util.Point
import util.Solution

class TrenchMap(fileName: String) : Solution<List<Boolean>, Long>(fileName) {
    override fun parse(line: String): List<Boolean>? {
        return if (line.isEmpty()) null else line.map { it == '#' }
    }

    private fun enhance(grid: Grid<Boolean>, second: Boolean, algorithm: List<Boolean>): Grid<Boolean> {
        val minX = grid.minOf { it.key.x }
        val maxX = grid.maxOf { it.key.x }
        val minY = grid.minOf { it.key.y }
        val maxY = grid.maxOf { it.key.y }
        val points = (minY - 1..maxY + 1).flatMap { y ->
            (minX - 1..maxX + 1).map { x -> Point(x, y) }
        }

        return points.associateWith {
            val bits = it.getAllNeighbours()
                .map { n -> grid[n] ?: second }
                .map { b -> if (b) 1 else 0 }
            algorithm[Helpers.toDecimal(bits, 2).toInt()]
        }
    }

    override fun List<List<Boolean>>.solve1(): Long {
        val algorithm = this[0]

        val enhanced = enhance(this.drop(1).toGrid(), false, algorithm)
        val enhancedTwice = enhance(enhanced, algorithm[0], algorithm)
        return enhancedTwice.filter { it.value }.size.toLong()
    }


    override fun List<List<Boolean>>.solve2(): Long {
        val algorithm = this[0]
        tailrec fun applySteps(step: Int, grid: Grid<Boolean>): Grid<Boolean> {
            return if (step == 25) grid else {
                val enhanced = enhance(grid, false, algorithm)
                val enhancedTwice = enhance(enhanced, algorithm[0], algorithm)
                applySteps(step + 1, enhancedTwice)
            }
        }

        return applySteps(0, this.drop(1).toGrid()).filter { it.value }.size.toLong()
    }
}