import day0.Day0Placeholder
import util.Solution

fun main(args: Array<String>) {
    val day = "0"

    solveDay(day) { s -> Day0Placeholder(s) }
}

private fun <I, S> solveDay(day: String, constructor: (String) -> Solution<I, S>) {
    val dayPrefix = "/day$day/"
    val sample = constructor("${dayPrefix}sample")
    val input = constructor("${dayPrefix}input")

    println("Sample star 1: ${sample.star1()}")
    println("Sample star 2: ${sample.star2()}")

    println("Input star 1: ${input.star1()}")
    println("Input star 2: ${input.star2()}")
}