package day7

import util.Solution
import kotlin.math.absoluteValue

class TreacheryOfWhales(fileName: String) : Solution<List<Long>, Long>(fileName) {
    override fun parse(line: String): List<Long> = line.split(",").map { it.toLong() }

    override fun List<List<Long>>.solve1(): Long {
        val input = this[0]
        val stop = input.maxOrNull() ?: 0

        return (0..stop).map { position ->
            input.sumOf { (it - position).absoluteValue }
        }.minOrNull() ?: -1
    }

    override fun List<List<Long>>.solve2(): Long {
        TODO("Not yet implemented")
    }
}