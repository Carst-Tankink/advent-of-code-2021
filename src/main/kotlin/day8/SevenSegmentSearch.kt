package day8

import util.Solution

data class Display(val patterns: List<Set<Char>>, val outputs: List<Set<Char>> )
class SevenSegmentSearch(fileName: String) : Solution<Display, Long>(fileName) {
    override fun parse(line: String): Display? {
        val patternsOutput = line
            .split(" | ")
            .map { observation -> observation.split(" ")
                .map{it.toSet()} }

        return Display(patternsOutput[0], patternsOutput[1])
    }

    override fun List<Display>.solve1(): Long =
        this.sumOf { display -> display.outputs.count { it.size in setOf(2, 3, 4, 7) }.toLong() }


    override fun List<Display>.solve2(): Long {
        TODO("Not yet implemented")
    }
}