import day12.PassagePathing
import util.Solution

fun main() {
    val day = 12

    solveDay(day) { s -> PassagePathing(s) }
}

private fun <I, S> solveDay(day: Int, constructor: (String) -> Solution<I, S>) {
    val dayPrefix = "/day$day/"
    val sample = constructor("${dayPrefix}sample")
    val input = constructor("${dayPrefix}input")

    println("Sample star 1: ${sample.star1()}")
    println("Input star 1: ${input.star1()}")

    println("Sample star 2: ${sample.star2()}")
    println("Input star 2: ${input.star2()}")
}