package day12

import util.Solution

class PassagePathing(fileName: String) : Solution<Pair<String, String>, Long>(fileName) {
    override fun parse(line: String): Pair<String, String> {
        val elements = line.split('-')
        return Pair(elements[0], elements[1])
    }

    override fun List<Pair<String, String>>.solve1(): Long {
        val paths = explorePaths()
        return paths.size.toLong()
    }

    private fun List<Pair<String, String>>.explorePaths(allowedSmall: String? = null): Set<List<String>> {
        val returnEdges = this.map { Pair(it.second, it.first) }
        val graph: Map<String, List<String>> = (this + returnEdges).groupBy({ it.first }) { it.second }

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

    override fun List<Pair<String, String>>.solve2(): Long {
        TODO("Not yet implemented")
    }
}