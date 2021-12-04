package day4

import util.Helpers
import util.Solution

data class Board(val rows: List<List<Int>>) {
    val columns: List<List<Int>> = Helpers.transpose(rows)

    fun wins(drawn: List<Int>): Boolean =
        rows.any { drawn.containsAll(it) } || columns.any { drawn.containsAll(it) }
}

class GiantSquid(fileName: String) : Solution<List<Int>, Long>(fileName) {

    override fun parse(line: String): List<Int> {
        return when {
            line.isEmpty() -> emptyList()
            line.contains(",") -> line.split(",").map { it.toInt() }
            else -> line.split(" ").filterNot { it.isEmpty() }.map { it.toInt() }
        }
    }

    override fun List<List<Int>>.solve1(): Long {

        val numbersCalled = this[0]
        val boards: List<Board> = makeBoard(this.drop(2))

        tailrec fun play(numbersDrawn: List<Int>, numbersToGo: List<Int>): Pair<List<Int>, Board> {
            val winningBoard = boards.find { it.wins(numbersDrawn) }
            return if (winningBoard != null) Pair(numbersDrawn, winningBoard)
            else play(listOf(numbersToGo[0]) + numbersDrawn, numbersToGo.drop(1))
        }

        val (numbers, winningBoard) = play(emptyList(), numbersCalled)
        val boardScore = winningBoard.rows.sumOf { row -> row.filterNot { numbers.contains(it) }.sum() }

        return boardScore.toLong() * numbers[0]
    }

    private fun makeBoard(lines: List<List<Int>>): List<Board> {
        tailrec fun build(boards: List<Board>, currentBoard: List<List<Int>>, remaining: List<List<Int>>): List<Board> {
            return if (remaining.isEmpty()) boards + Board(currentBoard) else {
                val next = remaining[0]
                if (next.isEmpty()) build(boards + Board(currentBoard), emptyList(), remaining.drop(1))
                else build(boards, currentBoard + listOf(next), remaining.drop(1))
            }
        }

        return build(emptyList(), emptyList(), lines)
    }

    override fun List<List<Int>>.solve2(): Long {
        TODO("Not yet implemented")
    }
}
