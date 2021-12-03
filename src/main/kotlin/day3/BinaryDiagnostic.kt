package day3

import util.Solution

class BinaryDiagnostic(fileName: String) : Solution<List<Int>, Long>(fileName) {
    override fun parse(line: String): List<Int> = line.map { it.code - 48 }

    override fun List<List<Int>>.solve1(): Long {
        val mostSignificantBits = transpose(this)
            .map {
                if (it.sum() > it.size / 2) 1 else 0
            }
        val leastSignificantBits = mostSignificantBits.map { 1 - it }
        return toDecimal(mostSignificantBits) * toDecimal(leastSignificantBits)
    }

    override fun List<List<Int>>.solve2(): Long {
        TODO("Not yet implemented")
    }

    fun <T> transpose(input: List<List<T>>): List<List<T>> {
        return if (input.any { it.isEmpty() }) emptyList() else
            listOf(input.map { it[0] }) + transpose(input.map { it.drop(1) })
    }

    fun toDecimal(bits: List<Int>): Long {
        tailrec fun rec(acc: Long, power: Long, remaining: List<Int>): Long {
            return if (remaining.isEmpty()) acc else {
                rec(acc + power * remaining[0], power * 2, remaining.drop(1))
            }
        }

        return rec(0, 1, bits.reversed())
    }
}