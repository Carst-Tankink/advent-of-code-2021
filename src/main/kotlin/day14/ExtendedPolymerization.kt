package day14

import util.Either
import util.Left
import util.Right
import util.Solution

class ExtendedPolymerization(fileName: String) : Solution<Either<String, Pair<String, String>>, Long>(fileName) {
    override fun parse(line: String): Either<String, Pair<String, String>>? {
        return when {
            line.isEmpty() -> null
            line.contains(" -> ") -> {
                val split = line.split(" -> ")
                Right(Pair(split[0], split[1]))
            }
            else -> Left(line)
        }
    }

    private fun <T> Map<T, Long>.updateCounts(entry: T, amount: Long): Map<T, Long> {
        val existingValue = this[entry] ?: 0
        return this - entry + (entry to (existingValue + amount))
    }

    private fun List<Either<String, Pair<String, String>>>.polymerize(steps: Int): Long {
        val rules = this.mapNotNull { it.right }.toMap()

        fun pairInsert(
            polymer: Map<String, Long>,
            elements: Map<String, Long>
        ): Pair<Map<String, Long>, Map<String, Long>> {
            return polymer.entries.fold(Pair(emptyMap(), elements)) { acc, entry ->
                val key = entry.key
                val produced = rules[key]!!
                val amount = entry.value

                val newElements = acc.second.updateCounts(produced, amount)

                val newPairs = acc.first
                    .updateCounts(key[0] + produced, amount)
                    .updateCounts(produced + key[1], amount)

                Pair(newPairs, newElements)
            }
        }

        tailrec fun applySteps(
            step: Int,
            polymerPairs: Map<String, Long>,
            elements: Map<String, Long>,
            finalStep: Int
        ): Pair<Map<String, Long>, Map<String, Long>> {
            return if (step == finalStep) Pair(polymerPairs, elements) else {
                val (newPairs, newElements) = pairInsert(polymerPairs, elements)
                applySteps(step + 1, newPairs, newElements, finalStep)
            }
        }

        val template = this.mapNotNull { it.left }.first()
        val startElements = template.groupBy { it.toString() }.mapValues { it.value.size.toLong() }
        val polymerPairs = template.windowed(2).groupBy { it }.mapValues { it.value.size.toLong() }

        val elements = applySteps(0, polymerPairs, startElements, steps).second

        return elements.maxOf { it.value } - elements.minOf { it.value }
    }

    override fun List<Either<String, Pair<String, String>>>.solve1(): Long {
        return polymerize(10)
    }

    override fun List<Either<String, Pair<String, String>>>.solve2(): Long {
        return polymerize(40)
    }
}