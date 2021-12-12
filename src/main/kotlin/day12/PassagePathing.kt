package day12

import util.Solution

class PassagePathing(fileName: String) : Solution<Pair<String, String>, Long>(fileName) {
    override fun parse(line: String): Pair<String, String> {
        val elements = line.split('-')
        return Pair(elements[0], elements[1])
    }

    override fun List<Pair<String, String>>.solve1(): Long {
        val extraEdges = this.map { Pair(it.second, it.first) }
        val graph: Map<String, List<String>> = (this + extraEdges).groupBy({ it.first }) { it.second }

        fun findPaths(node: String, visited: Set<String>): List<List<String>> {
            return if (node == "end") listOf(listOf(node)) else
                graph[node]?.filter { it.all { c -> c.isUpperCase() || it !in visited } }
                    ?.flatMap { findPaths(it, visited + node).map { p -> listOf(node) + p } }
                    ?: emptyList()
        }

        return findPaths("start", setOf()).size.toLong()
    }

    override fun List<Pair<String, String>>.solve2(): Long {
        TODO("Not yet implemented")
    }
}