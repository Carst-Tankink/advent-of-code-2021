package day3

import util.Helpers
import util.Helpers.Companion.intValue
import util.Helpers.Companion.toDecimal
import util.Solution

class BinaryDiagnostic(fileName: String) : Solution<List<Int>, Long>(fileName) {
    override fun parse(line: String): List<Int> = line.map { it.intValue() }

    override fun List<List<Int>>.solve1(): Long {
        val mostSignificantBits = Helpers.transpose(this)
            .map {
                if (it.sum() >= it.size / 2) 1 else 0
            }
        val leastSignificantBits = mostSignificantBits.map { 1 - it }
        return toDecimal(mostSignificantBits, 2) * toDecimal(leastSignificantBits, 2)
    }

    override fun List<List<Int>>.solve2(): Long {
        fun getMostCommonBit(remaining: List<Int>): Int {
            val ones = remaining.count { it == 1 }
            val zeroes = remaining.count { it == 0 }
            return if (ones >= zeroes) 1 else 0
        }

        tailrec fun filterLists(remaining: List<Pair<List<Int>, List<Int>>>, oxygen: Boolean): List<Int> {
            return if (remaining.size == 1) remaining[0].first + remaining[0].second else {
                val msb = getMostCommonBit(remaining.map { it.second[0] })
                val matching = remaining.filter {
                    if (oxygen) it.second[0] == msb else it.second[0] != msb
                }.map { Pair(it.first + it.second[0], it.second.drop(1)) }
                filterLists(matching, oxygen)
            }
        }

        val start = this.map<List<Int>, Pair<List<Int>, List<Int>>> { Pair(emptyList(), it) }
        val oxygen = filterLists(start, true)
        val scrubber = filterLists(start, false)

        return toDecimal(oxygen, 2) * toDecimal(scrubber, 2)

    }
}