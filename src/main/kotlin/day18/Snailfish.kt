package day18

import util.Helpers.Companion.intValue
import util.Solution

sealed class SnailFishNumber(var parent: PairNumber?) {
}

data class PairNumber(val left: SnailFishNumber, val right: SnailFishNumber) : SnailFishNumber(null) {
    override fun toString(): String {
        return "[$left,$right]"
    }
}

data class RegularNumber(val value: Int) : SnailFishNumber(null) {
    override fun toString(): String {
        return value.toString()
    }
}

sealed interface Crumb
data class LeftCrumb(val toRight: SnailFishNumber) : Crumb
data class RightCrumb(val toLeft: SnailFishNumber) : Crumb

data class Zipper(val number: SnailFishNumber, val directions: List<Crumb>) {
    fun left(): Zipper? {
        return when (number) {
            is PairNumber -> Zipper(number.left, listOf(LeftCrumb(number.right)) + directions)
            else -> null
        }
    }

    fun right(): Zipper? {
        return when (number) {
            is PairNumber -> Zipper(number.right, listOf(RightCrumb(number.left)) + directions)
            else -> null
        }
    }

    fun up(): Zipper? {
        return if (directions.isEmpty()) null else {
            val tail = directions.drop(1)
            when (val head = directions[0]) {
                is LeftCrumb -> Zipper(PairNumber(number, head.toRight), tail)
                is RightCrumb -> Zipper(PairNumber(head.toLeft, number), tail)
            }
        }
    }

    fun replaceWith(newNumber: SnailFishNumber): Zipper {
        return Zipper(number = newNumber, directions = directions)
    }

    fun root(): Zipper {
        val parent = up()
        return parent?.root() ?: this
    }

    fun goto(instructions: List<Crumb>): Zipper {
        return instructions.reversed().fold(root()) { loc, ins ->
            when (ins) {
                is RightCrumb -> loc.right()!!
                is LeftCrumb -> loc.left()!!
            }
        }
    }


    fun toNumber(): SnailFishNumber {
        return root().number
    }

}


class Snailfish(fileName: String) : Solution<SnailFishNumber, Int>(fileName) {
    override fun parse(line: String): SnailFishNumber? {
        fun parseNumber(remainder: String): Pair<SnailFishNumber, String> {
            val nextChar = remainder[0]
            val tail = remainder.drop(1)
            return when (nextChar) {
                '[' -> {
                    val (firstChild, next) = parseNumber(tail)
                    val (secondChild, finalTail) = parseNumber(next.drop(1))

                    val node = PairNumber(firstChild, secondChild)
                    firstChild.parent = node
                    secondChild.parent = node
                    Pair(node, finalTail.drop(1))
                }
                else ->
                    Pair(RegularNumber(nextChar.intValue()), tail)
            }
        }
        return if (line.isEmpty()) null else {
            parseNumber(line).first
        }
    }

    override fun List<SnailFishNumber>.solve1(): Int {
        println("Parsed tree: ${this[0]}")
        TODO("Not yet implemented")
    }

    override fun List<SnailFishNumber>.solve2(): Int {
        TODO("Not yet implemented")
    }

    fun add(left: SnailFishNumber, right: SnailFishNumber): SnailFishNumber {
        val addedNumber = PairNumber(left, right)
        // TODO: Reduction
        return addedNumber
    }


    fun explode(s: SnailFishNumber): SnailFishNumber {
        tailrec fun findExplodingPair(current: List<Zipper>): Zipper {
            val head = current[0]
            return if (head.directions.size == 4 && head.number is PairNumber) head else {
                val tail = current.drop(1)
                findExplodingPair(listOfNotNull(head.left(), head.right()) + tail)
            }
        }

        fun addExplodedValue(receiver: Zipper, explodedValue: RegularNumber): Zipper {
            val value = receiver.number as RegularNumber
            return receiver.replaceWith(RegularNumber(value.value + explodedValue.value))
        }


        val explodingPair = findExplodingPair(listOf(Zipper(s, emptyList())))
        val leftReceiver = findLeftSibling(explodingPair)

        val rightReceiver = findRightSibling(explodingPair)
        println("Exploding $explodingPair into:\n$leftReceiver \n$rightReceiver")

        val updatedLeft = if (leftReceiver == null) explodingPair else addExplodedValue(
            leftReceiver,
            explodingPair.left()!!.number as RegularNumber
        )

        val updatedRight = if (rightReceiver == null) updatedLeft else addExplodedValue(
            updatedLeft.goto(rightReceiver.directions),
            explodingPair.right()!!.number as RegularNumber
        )

        val finalValue = updatedRight.goto(explodingPair.directions).replaceWith(RegularNumber(0))
        return finalValue.toNumber()
    }


    private fun findRightSibling(explodingPair: Zipper) =
        findSibling(explodingPair, { it is RightCrumb }, { it.right()!! }) { it.left()!! }

    private fun findLeftSibling(explodingPair: Zipper) =
        findSibling(explodingPair, { it is LeftCrumb }, { it.left()!! }) { it.right()!! }


    private tailrec fun findSibling(
        focus: Zipper,
        check: (Crumb) -> Boolean,
        firstTurn: (Zipper) -> Zipper,
        descendDirection: (Zipper) -> Zipper
    ): Zipper? {
        val above = focus.up()
        return when {
            (above == null) -> null
            check(focus.directions[0]) -> findSibling(above, check, firstTurn, descendDirection)
            else -> descendToBottom(firstTurn(above), descendDirection)
        }
    }

    private tailrec fun descendToBottom(node: Zipper, direction: (Zipper) -> Zipper): Zipper? {
        return when (node.number) {
            is RegularNumber -> node
            is PairNumber -> descendToBottom(direction(node), direction)
        }

    }
}


