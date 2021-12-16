import day15.Chiton
import util.Solution

fun main() {
    val day = 15

    solveDay(day) { s -> Chiton(s) }
}

private fun <I, S> solveDay(day: Int, constructor: (String) -> Solution<I, S>) {
    val dayPrefix = "/day$day/"
    val sample = constructor("${dayPrefix}sample")
    val input = constructor("${dayPrefix}input")

    runSolution("Sample star 1: ") { sample.star1() }
    runSolution("Input star 1: ") { input.star1() }

    runSolution("Sample star 2: ") { sample.star2() }
    runSolution("Input star 2: ") { input.star2() }

}

private fun <S> runSolution(message: String, function: () -> S) {
    val before = System.currentTimeMillis()
    val solution = function()
    val after = System.currentTimeMillis()
    println("$message$solution\nTime: ${after - before}ms")
}