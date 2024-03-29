package day12

import util.Solution

class PassagePathing(fileName: String) : Solution<Pair<String, String>, Int>(fileName) {
    override fun parse(line: String): Pair<String, String> {
        val elements = line.split('-')
        return Pair(elements[0], elements[1])
    }

    private val graph: Map<String, List<String>> = let {
        val returnEdges = data.map { Pair(it.second, it.first) }
        (data + returnEdges).groupBy({ it.first }) { it.second }
    }

    override fun List<Pair<String, String>>.solve1(): Int {
        return explorePaths().size
    }

    private fun explorePaths(allowedSmall: String? = null): Set<List<String>> {
        fun findPaths(node: String, visited: Set<String>, allowedSmall: String?): Set<List<String>> {
            return if (node == "end") setOf(listOf(node)) else {
                val newVisited = if (node.all { it.isUpperCase() } || node == allowedSmall) visited else visited + node
                graph[node]?.filter { it !in visited }
                    ?.flatMap {
                        findPaths(
                            it,
                            newVisited,
                            if (node == allowedSmall) null else allowedSmall
                        ).map { p -> listOf(node) + p }
                    }
                    ?.toSet()
                    ?: emptySet()
            }
        }

        return findPaths("start", setOf(), allowedSmall)
    }

    override fun List<Pair<String, String>>.solve2(): Int {
        return graph.keys
            .filterNot { it in setOf("start", "end") }
            .filter { it.all { c -> c.isLowerCase() } }
            .fold<String, Set<List<String>>>(emptySet()) { acc, c -> acc + explorePaths(c) }.size
    }

}