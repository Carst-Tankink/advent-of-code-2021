package day16

import day16.OperatorPacket.Operation.*
import util.Helpers.Companion.toDecimal
import util.Solution

sealed class Packet(val version: Long, val children: List<Packet>)
class OperatorPacket(version: Long, children: List<Packet>, val operation: Operation) : Packet(version, children) {
    enum class Operation {
        SUM,
        PRODUCT,
        MINIMUM,
        MAXIMUM,
        GREATER_THAN,
        LESS_THAN,
        EQUAL_TO
    }
}

class DigitPacket(version: Long, val value: Long) : Packet(version, children = emptyList())

class PacketDecoder(fileName: String) : Solution<List<Int>, Long>(fileName) {
    override fun parse(line: String): List<Int> {
        return line
            .flatMap {
                when (it) {
                    '0' -> listOf(0, 0, 0, 0)
                    '1' -> listOf(0, 0, 0, 1)
                    '2' -> listOf(0, 0, 1, 0)
                    '3' -> listOf(0, 0, 1, 1)
                    '4' -> listOf(0, 1, 0, 0)
                    '5' -> listOf(0, 1, 0, 1)
                    '6' -> listOf(0, 1, 1, 0)
                    '7' -> listOf(0, 1, 1, 1)
                    '8' -> listOf(1, 0, 0, 0)
                    '9' -> listOf(1, 0, 0, 1)
                    'A' -> listOf(1, 0, 1, 0)
                    'B' -> listOf(1, 0, 1, 1)
                    'C' -> listOf(1, 1, 0, 0)
                    'D' -> listOf(1, 1, 0, 1)
                    'E' -> listOf(1, 1, 1, 0)
                    'F' -> listOf(1, 1, 1, 1)
                    else -> error("Not a hexcode $it")
                }
            }
    }


    private fun fromBinary(input: List<Int>) = toDecimal(input, 2)
    private fun <T> List<T>.splitAt(n: Int): Pair<List<T>, List<T>> = Pair(this.take(n), this.drop(n))

    private fun parseAsDigit(body: List<Int>): Pair<Long, List<Int>> {
        tailrec fun consume(string: List<Int>, acc: List<Int>): Pair<Long, List<Int>> {
            val (head, tail) = string.splitAt(1)
            val accumulated = acc + tail.take(4)
            val remaining = tail.drop(4)
            return when (head[0]) {
                0 -> Pair(fromBinary(accumulated), remaining)
                1 -> {
                    consume(remaining, accumulated)
                }
                else -> error("Not binary $string")
            }
        }

        return consume(body, emptyList())
    }


    private fun parseAsOperator(body: List<Int>): Pair<List<Packet>, List<Int>> {
        tailrec fun parseSubPacketsLength(acc: List<Packet>, string: List<Int>): List<Packet> {
            return if (string.isEmpty()) acc else {
                val (parsedNextPacket, remaining) = readPacket(string)
                parseSubPacketsLength(acc + parsedNextPacket, remaining)
            }
        }

        tailrec fun parseSubPacketsCount(
            acc: List<Packet>,
            string: List<Int>,
            subPacketCount: Long
        ): Pair<List<Packet>, List<Int>> {
            return if (acc.size.toLong() == subPacketCount) Pair(acc, string) else {
                val (packet, remaining) = readPacket(string)
                parseSubPacketsCount(acc + packet, remaining, subPacketCount)
            }
        }

        val lengthType = body[0]
        val following = body.drop(1)
        return when (lengthType) {
            0 -> {
                val (lengthBits, tail) = following.splitAt(15)
                val subPacketLength = fromBinary(lengthBits)
                val (subPackageBits, remaining) = tail.splitAt(subPacketLength.toInt())
                Pair(parseSubPacketsLength(emptyList(), subPackageBits), remaining)
            }
            1 -> {
                val (countBits, tail) = following.splitAt(11)
                val subPacketCount = fromBinary(countBits)
                val (subPackets, remaining) = parseSubPacketsCount(emptyList(), tail, subPacketCount)
                Pair(subPackets, remaining)
            }
            else -> error("Not binary $body")
        }
    }

    private fun readPacket(input: List<Int>): Pair<Packet, List<Int>> {
        val header = input.take(6)
        val version = fromBinary(header.take(3))
        val typeId = fromBinary(header.takeLast(3))
        val body = input.drop(6)

        return when (typeId) {
            4L -> {
                val (value, remaining) = parseAsDigit(body)
                Pair(DigitPacket(version, value), remaining)
            }
            else -> {
                val operation = when (typeId) {
                    0L -> SUM
                    1L -> PRODUCT
                    2L -> MINIMUM
                    3L -> MAXIMUM
                    5L -> GREATER_THAN
                    6L -> LESS_THAN
                    7L -> EQUAL_TO
                    else -> error("Invalid type $typeId")
                }
                val (children, remaining) = parseAsOperator(body)
                Pair(OperatorPacket(version, children, operation), remaining)
            }
        }
    }

    override fun List<List<Int>>.solve1(): Long {
        tailrec fun sumVersions(queue: List<Packet>, acc: Long = 0): Long {
            return if (queue.isEmpty()) acc else {
                val first = queue[0]
                sumVersions(queue.drop(1) + first.children, acc + first.version)
            }
        }

        return sumVersions(listOf(readPacket(this[0]).first))
    }

    override fun List<List<Int>>.solve2(): Long {
        val packet = readPacket(this[0]).first

        return interpretPacket(packet)
    }

    private fun interpretPacket(packet: Packet): Long {
        return when (packet) {
            is DigitPacket -> packet.value
            is OperatorPacket -> when (packet.operation) {
                SUM -> packet.children.sumOf { interpretPacket(it) }
                PRODUCT -> packet.children.fold(1) { acc, p -> acc * interpretPacket(p) }
                MINIMUM -> packet.children.minOf { interpretPacket(it) }
                MAXIMUM -> packet.children.maxOf { interpretPacket(it) }
                GREATER_THAN -> {
                    return test(packet) { v1, v2 -> v1 > v2 }
                }
                LESS_THAN -> {
                    return test(packet) { v1, v2 -> v1 < v2 }
                }
                EQUAL_TO -> {
                    return test(packet) { v1, v2 -> v1 == v2 }
                }
            }
        }

    }
    private fun test(packet: Packet, testValue: (v1: Long, v2: Long) -> Boolean): Long {
        val child1 = interpretPacket(packet.children[0])
        val child2 = interpretPacket(packet.children[1])

        return if (testValue(child1, child2)) 1 else 0
    }
}