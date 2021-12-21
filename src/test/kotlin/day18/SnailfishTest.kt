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

        val leftComplex = snailfish.parse("[[[[4,3],4],4],[7,[[8,4],9]]]")
        val rightComplex = snailfish.parse("[1,1]")
        assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", snailfish.add(leftComplex!!, rightComplex!!).toString())

    }

    @Test
    internal fun testExplosion() {
        val s = snailfish.findExplodingPair(listOf(Zipper(snailfish.parse("[[[[[9,8],1],2],3],4]")!!)))
        assertEquals("[[[[0,9],2],3],4]", snailfish.explode(s!!).toString())
        val s1 = snailfish.findExplodingPair(listOf(Zipper(snailfish.parse("[7,[6,[5,[4,[3,2]]]]]")!!)))
        assertEquals("[7,[6,[5,[7,0]]]]", snailfish.explode(s1!!).toString())
        val s2 = snailfish.findExplodingPair(listOf(Zipper(snailfish.parse("[[6,[5,[4,[3,2]]]],1]")!!)))
        assertEquals("[[6,[5,[7,0]]],3]", snailfish.explode(s2!!).toString())
        val s3 = snailfish.findExplodingPair(listOf(Zipper(snailfish.parse("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]")!!)))
        assertEquals(
            "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
            snailfish.explode(s3!!).toString()
        )
        val s4 = snailfish.findExplodingPair(listOf(Zipper(snailfish.parse("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")!!)))
        assertEquals(
            "[[3,[2,[8,0]]],[9,[5,[7,0]]]]",
            snailfish.explode(s4!!).toString()
        )
    }

    @Test
    internal fun testSplit() {
        assertEquals("[5,5]", snailfish.split(Zipper(RegularNumber(10))).toString())
        assertEquals("[5,6]", snailfish.split(Zipper(RegularNumber(11))).toString())
        assertEquals("[6,6]", snailfish.split(Zipper(RegularNumber(12))).toString())
    }
}