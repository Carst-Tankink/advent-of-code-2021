package day10

import util.Solution

class SyntaxScoring(fileName: String) : Solution<String, Long>(fileName) {
    override fun parse(line: String): String = line

    override fun List<String>.solve1(): Long {
        return this
            .mapNotNull { findIllegal(it) }
            .sumOf { scoreIllegal(it) }
    }

    private fun scoreIllegal(c: Char): Long =
        when (c) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> 0
        }

    private fun findIllegal(s: String): Char? {
        tailrec fun rec(open: List<Char>, left: String): Char? {
            return if (left.isEmpty()) null else {
                val next = left[0]
                return when {
                    (next in PAIRS.keys) -> rec(listOf(next) + open, left.drop(1))
                    (next == PAIRS[open[0]]) -> rec(open.drop(1), left.drop(1))
                    else -> next
                }
            }
        }

        return rec(emptyList(), s)
    }

    override fun List<String>.solve2(): Long {
        val scores = this.filter { findIllegal(it) == null }
            .map { complete(it) }
            .map { scoreCompletion(it) }
            .sorted()
        return scores[scores.size / 2]
    }

    private fun scoreCompletion(line: String): Long {
        val scoringTable: Map<Char, Long> = mapOf(
            ')' to 1,
            ']' to 2,
            '}' to 3,
            '>' to 4
        )

        return line.fold(0) { acc, c -> acc * 5 + (scoringTable[c] ?: 0) }
    }

    private fun complete(s: String): String {
        tailrec fun rec(open: List<Char>, left: String): String {
            return if (left.isEmpty())
                open.mapNotNull { PAIRS[it] }.joinToString("")
            else {
                val next = left[0]
                return when {
                    (next in PAIRS.keys) -> rec(listOf(next) + open, left.drop(1))
                    (next == PAIRS[open[0]]) -> rec(open.drop(1), left.drop(1))
                    else -> error("There should not be a corrupt character")
                }
            }
        }
        return rec(emptyList(), s)
    }

    companion object {
        val PAIRS = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
    }
}