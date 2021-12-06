package day6

import util.Solution

class Lanternfish(fileName: String) : Solution<List<Int>, Long>(fileName) {
    override fun parse(line: String): List<Int> {
        return line.split(",").map { it.toInt() }
    }

    tailrec fun procreate(days: Int, fish: List<Long>, finalDay: Int): List<Long> {
        return if (days == finalDay) fish else {
            val birthing = fish[0]
            val newFish = (fish
                .drop(1)
                + birthing)
                .mapIndexed { index, i ->  when(index) {
                    6 -> i + birthing
                    else -> i
                } }
            procreate(days + 1, newFish, finalDay)
        }
    }
    override fun List<List<Int>>.solve1(): Long {
        val initialFish = (0..8).map { idx -> this[0].count { idx == it }.toLong() }
        return procreate(0, initialFish, 80).sum()
    }

    override fun List<List<Int>>.solve2(): Long {
        val initialFish = (0..8).map { idx -> this[0].count { idx == it }.toLong() }
        return procreate(0, initialFish, 256).sum()

    }
}