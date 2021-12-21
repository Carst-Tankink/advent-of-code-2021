package day21

import util.Helpers.Companion.intValue
import util.Solution

data class Player(val pos: Int, val score: Int = 0)
data class GameState(
    val player1: Player,
    val player2: Player,
    val turn: Int, val dievalue: Int,
)

class DiracDie(fileName: String) : Solution<Int, Long>(fileName) {
    override fun parse(line: String): Int {
        return line.last().intValue()
    }

    override fun List<Int>.solve1(): Long {
        val player1Start = this[0]
        val player2Start = this[1]

        tailrec fun play(state: GameState): GameState {
            return if (state.player1.score >= 1000 || state.player2.score >= 1000) state else {
                val player1Turn = state.turn % 2 == 0
                val playerToScore = if (player1Turn) state.player1 else state.player2
                val pos = (playerToScore.pos - 1 + state.dievalue) % 10 + 1
                val score = playerToScore.score + pos

                val newPlayerState = Player(pos, score)
                val newDieValue = (state.dievalue + 9) % 10
                val turn = state.turn + 1

                val newGameState = if (player1Turn) GameState(newPlayerState, state.player2, turn, newDieValue) else {
                    GameState(state.player1, newPlayerState, turn, newDieValue)
                }
                play(newGameState)
            }
        }

        val finalState = play(GameState(Player(player1Start), Player(player2Start), 0, 6))
        val losingScore =
            if (finalState.player1.score < finalState.player2.score) finalState.player1.score else finalState.player2.score
        return losingScore * finalState.turn * 3L
    }

    override fun List<Int>.solve2(): Long {
        val player1Start = this[0]
        val player2Start = this[1]

        fun play(state: GameState): Pair<Long, Long> {
            val player1Turn = state.turn % 2 == 0
            val playerToScore = if (player1Turn) state.player1 else state.player2
            val pos = (playerToScore.pos - 1 + state.dievalue) % 10 + 1
            val score = playerToScore.score + pos

            return if (score >= 21) {
                if (player1Turn) Pair(1, 0) else Pair(0, 1)
            } else {
                val newPlayerState = Player(pos, score)
                val turn = state.turn + 1
                (3..9).fold(Pair(0L, 0L)) { (s1, s2), newDieValue ->
                    val newState =
                        if (player1Turn) GameState(newPlayerState, state.player2, turn, newDieValue) else {
                            GameState(state.player1, newPlayerState, turn, newDieValue)
                        }
                    val (p1, p2) = play(newState)
                    Pair(s1 + p1, s2 + p2)
                }
            }
        }


        val (player1Wins, player2Wins) =
            (3..9).fold(Pair(0L, 0L)) { acc, value ->
                val (p1, p2) = play(GameState(Player(player1Start), Player(player2Start), 0, value))
                Pair(acc.first + p1, acc.second + p2)
            }


        return if (player1Wins > player2Wins) player1Wins else player2Wins
    }
}