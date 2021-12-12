package day12

import util.Solution

class PassagePathing(fileName: String) : Solution<Pair<String, String>, Long>(fileName) {
    override fun parse(line: String): Pair<String, String> {
        val elements = line.split('-')
        return Pair(elements[0], elements[1])
    }

    override fun List<Pair<String, String>>.solve1(): Long {
        val graph: Map<String, List<String>> = this.groupBy({ it.first }) { it.second }

        fun findPaths(node: String): List<List<String>> {
            return if (node == "end") listOf(listOf(node)) else {
                val neighbours = graph[node] ?: emptyList()
                val p = neighbours.flatMap {
                    findPaths(it).map { p -> listOf(node) + p }
                }
                p
            }
        }
        println(findPaths("start"))
        TODO("Not yet implemented")
    }

    override fun List<Pair<String, String>>.solve2(): Long {
        TODO("Not yet implemented")
    }
}