package day0

import util.Solution

class Day0Placeholder(fileName: String) : Solution<Long, Long>(fileName) {
    override fun parse(line: String): Long? = line.toLong()

    override fun List<Long>.solve1(): Long {
        return this.get(0)
    }

    override fun List<Long>.solve2(): Long {
        return this.get(0) * 2
    }
}