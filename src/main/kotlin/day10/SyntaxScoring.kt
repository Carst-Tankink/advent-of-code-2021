package day10

import util.Solution

class SyntaxScoring(fileName: String) : Solution<String, Long>(fileName) {
    override fun parse(line: String): String = line

    override fun List<String>.solve1(): Long {
        return this.mapNotNull { s -> findIllegal(s) }
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
        tailrec fun parse(open: List<Char>, left: String): Char? {
            return if (left.isEmpty()) null else {
                val next = left[0]
                return when {
                    (next in PAIRS.keys) -> parse(listOf(next) + open, left.drop(1))
                    (next == PAIRS[open[0]]) -> parse(open.drop(1), left.drop(1))
                    else -> next
                }
            }
        }

        return parse(emptyList(), s)
    }

    override fun List<String>.solve2(): Long {
        TODO("Not yet implemented")
    }

    companion object {
        val PAIRS = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
    }
}