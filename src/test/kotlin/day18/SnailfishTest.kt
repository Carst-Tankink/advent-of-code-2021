package day18

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SnailfishTest {
    private val snailfish = Snailfish("/day18/testFile")

    @Test
    internal fun testNavigation() {
        val tree = snailfish.parse("[[3,4],5]")

        val initial = Zipper(tree!!, emptyList())
        val four = initial.left()?.right()

        assertEquals(RegularNumber(4), four?.number)
        assertEquals(tree, four?.up()?.up()?.number)
    }

    @Test
    internal fun testUpdate() {
        val tree = snailfish.parse("[[3,4],5]")
        val initial = Zipper(tree!!, emptyList())
        val four = initial.left()?.right()
        val theAnswer = four?.replaceWith(RegularNumber(42))
        assertEquals("[[3,42],5]", theAnswer?.toNumber().toString())
    }

    @Test
    internal fun testAddition() {
        val left = snailfish.parse("[1,2]")
        val right = snailfish.parse("[[3,4],5]")

        assertEquals("[[1,2],[[3,4],5]]", snailfish.add(left!!, right!!).toString())

    }

    @Test
    internal fun testExplosion() {
        assertEquals("[[[[0,9],2],3],4]", snailfish.explode(snailfish.parse("[[[[[9,8],1],2],3],4]")!!).toString())
        assertEquals("[7,[6,[5,[7,0]]]]", snailfish.explode(snailfish.parse("[7,[6,[5,[4,[3,2]]]]]")!!).toString())
        assertEquals("[[6,[5,[7,0]]],3]", snailfish.explode(snailfish.parse("[[6,[5,[4,[3,2]]]],1]")!!).toString())
        assertEquals(
            "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
            snailfish.explode(snailfish.parse("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]")!!).toString()
        )
        assertEquals(
            "[[3,[2,[8,0]]],[9,[5,[7,0]]]]",
            snailfish.explode(snailfish.parse("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")!!).toString()
        )

    }

}