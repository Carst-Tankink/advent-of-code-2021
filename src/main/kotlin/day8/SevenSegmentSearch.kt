package day8

import util.Helpers.Companion.toDecimal
import util.Solution

data class Display(val patterns: Set<Set<Char>>, val outputs: List<Set<Char>>)
class SevenSegmentSearch(fileName: String) : Solution<Display, Long>(fileName) {
    override fun parse(line: String): Display? {
        val patternsOutput = line
            .split(" | ")
            .map { observation ->
                observation.split(" ")
                    .map { it.toSet() }
            }

        return Display(patternsOutput[0].toSet(), patternsOutput[1])
    }

    override fun List<Display>.solve1(): Long =
        this.sumOf { display -> display.outputs.count { it.size in setOf(2, 3, 4, 7) }.toLong() }


    override fun List<Display>.solve2(): Long {
        val lastToNextCheck: Map<Int?, Pair<(Set<Char>, Map<Set<Char>, Int>) -> Boolean, Int>> = mapOf(
            null to Pair({ pattern, _ -> pattern.size == 7 }, 8),
            1 to Pair({ pattern, _ -> pattern.size == 4 }, 4),
            2 to Pair(
                { pattern, identified -> pattern.size == 6 && identified.entries.find { it.value == 5 }!!.key.all { c -> c in pattern } },
                6
            ),
            3 to Pair(
                { pattern, identified -> pattern.size == 6 && identified.entries.find { it.value == 3 }!!.key.all { c -> c in pattern } },
                9
            ),
            4 to Pair({ pattern, _ -> pattern.size == 3 }, 7),
            5 to Pair({ pattern, _ -> pattern.size == 5 }, 2),
            6 to Pair({ pattern, _ -> pattern.size == 6}, 0),
            7 to Pair(
                { pattern, identified -> pattern.size == 5 && identified.entries.find { it.value == 7 }!!.key.all { c -> c in pattern } },
                3
            ),
            8 to Pair({ pattern, _ -> pattern.size == 2 }, 1),
            9 to Pair(
                { pattern, identified ->
                    val nine = identified.entries.find { it.value == 9 }!!.key
                    pattern.size == 5 && pattern.all { c -> c in nine }
                },
                5
            )
        )

        tailrec fun identify(
            identified: Map<Set<Char>, Int>,
            unidentified: Set<Set<Char>>,
            last: Int?
        ): Map<Set<Char>, Int> {
            return if (unidentified.isEmpty()) identified else {
                val (predicate, target) = lastToNextCheck[last] ?: error("No mapping found $last, so far $identified ")
                val nextMap =
                    (unidentified.find { pattern -> predicate(pattern, identified) } ?: error("No match")) to target

                identify(identified + nextMap, unidentified - setOf(nextMap.first), nextMap.second)
            }
        }
        return this.sumOf { display ->
            val mapping = identify(emptyMap(), display.patterns, null)
            toDecimal(display.outputs.map { d -> mapping[d]!! }, 10)
        }
    }
}