package day3

import util.Solution

class BinaryDiagnostic(fileName: String) : Solution<List<Int>, Long>(fileName) {
    override fun parse(line: String): List<Int> = line.map { it.code - 48 }

    override fun List<List<Int>>.solve1(): Long {
        val mostSignificantBits = transpose(this)
            .map {
                if (it.sum() >= it.size / 2) 1 else 0
            }
        val leastSignificantBits = mostSignificantBits.map { 1 - it }
        return toDecimal(mostSignificantBits) * toDecimal(leastSignificantBits)
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

        return toDecimal(oxygen) * toDecimal(scrubber)

    }

    private fun <T> transpose(input: List<List<T>>): List<List<T>> {
        return if (input.any { it.isEmpty() }) emptyList() else
            listOf(input.map { it[0] }) + transpose(input.map { it.drop(1) })
    }

    private fun toDecimal(bits: List<Int>): Long {
        tailrec fun rec(acc: Long, power: Long, remaining: List<Int>): Long {
            return if (remaining.isEmpty()) acc else {
                rec(acc + power * remaining[0], power * 2, remaining.drop(1))
            }
        }

        return rec(0, 1, bits.reversed())
    }
}