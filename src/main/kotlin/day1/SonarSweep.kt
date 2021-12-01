package day1

import util.Solution

class SonarSweep(fileName: String) : Solution<Long, Int>(fileName) {
    override fun parse(line: String): Long? = line.toLong()

    override fun List<Long>.solve1(): Int {
        return this.fold(Pair(0, Long.MAX_VALUE)) { (acc, prev), current ->
             Pair(if (prev < current) acc + 1 else acc, current)
        }.first
    }

    override fun List<Long>.solve2(): Int {
        return this.drop(3).fold(Pair(0, this.take(3))) { (acc, prevWindow), current ->
            val nextWindow: List<Long> = prevWindow.drop(1) + current
            Pair(if (prevWindow.sum() < nextWindow.sum()) acc + 1 else acc, nextWindow)
        }.first
    }
}