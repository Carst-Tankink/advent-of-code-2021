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

    tailrec fun play(numbersDrawn: List<Int>, numbersToGo: List<Int>, remainingBoards: List<Board>): Pair<List<Int>, Board> {
        val winningBoard = remainingBoards.find { it.wins(numbersDrawn) }
        return if (winningBoard != null) Pair(numbersDrawn, winningBoard)
        else play(listOf(numbersToGo[0]) + numbersDrawn, numbersToGo.drop(1), remainingBoards)
    }
    override fun List<List<Int>>.solve1(): Long {
        val numbersCalled = this[0]
        val boards: List<Board> = makeBoards(this.drop(2))
        val (numbers, winningBoard) = play(emptyList(), numbersCalled, boards)
        return scoreBoard(winningBoard, numbers)
    }

    override fun List<List<Int>>.solve2(): Long {
        tailrec fun playUntilLast(remaining: List<Board>, drawn: List<Int>, numbersToGo: List<Int>): Pair<List<Int>, Board>  {
            return if (remaining.size == 1) play(drawn, numbersToGo, remaining) else {
                val (numbersDrawn, winningBoard) = play(drawn, numbersToGo, remaining)
                playUntilLast(remaining - winningBoard, numbersDrawn, numbersToGo - numbersDrawn)
            }
        }

        val boards: List<Board> = makeBoards(this.drop(2))

        val (numbers, final) = playUntilLast(boards, emptyList(), this[0])

        return scoreBoard(final, numbers)
    }

    private fun scoreBoard(winningBoard: Board, numbers: List<Int>): Long {
        val boardScore = winningBoard.rows.sumOf { row -> row.filterNot { numbers.contains(it) }.sum() }
        return boardScore.toLong() * numbers[0]
    }

    private fun makeBoards(lines: List<List<Int>>): List<Board> {
        tailrec fun build(boards: List<Board>, currentBoard: List<List<Int>>, remaining: List<List<Int>>): List<Board> {
            return if (remaining.isEmpty()) boards + Board(currentBoard) else {
                val next = remaining[0]
                if (next.isEmpty()) build(boards + Board(currentBoard), emptyList(), remaining.drop(1))
                else build(boards, currentBoard + listOf(next), remaining.drop(1))
            }
        }

        return build(emptyList(), emptyList(), lines)
    }
}
