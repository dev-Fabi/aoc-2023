package day08

import Utils
import asInfiniteLoop
import leastCommonMultiply
import nextIndexOf

fun main() {
    val utils = Utils(8)

    fun List<String>.getNodes(): Map<String, Pair<String, String>> {
        val inputRegex = Regex("""(.{3}) = \((.{3}), (.{3})\)""")
        return this.drop(2).associate { line ->
            val (key, left, right) = inputRegex.matchEntire(line)!!.destructured
            key to (left to right)
        }
    }

    fun Map<String, Pair<String, String>>.getStepsToEnd(
        instructions: String,
        startNode: String,
        endCondition: (String) -> Boolean
    ): Int {
        val instructionSequence = instructions.toList().asInfiniteLoop()

        var nextNode = startNode
        var steps = 0
        var instructionIndex = 0
        while (!endCondition(nextNode)) {
            val currentNode = this[nextNode] ?: error("No node definition for $nextNode")

            nextNode = when (val i = instructionSequence.next()) {
                'L' -> currentNode.first
                'R' -> currentNode.second
                else -> error("Invalid instruction $i")
            }

            steps++
            instructionIndex = instructionIndex.nextIndexOf(instructions)
        }

        return steps
    }

    fun part1(input: List<String>): Int {
        return input.getNodes().getStepsToEnd(input.first(), "AAA") { it == "ZZZ" }
    }

    fun part2(input: List<String>): Long {
        val nodes = input.getNodes()
        val counts = nodes.keys.filter { it.endsWith("A") }.map { startNode ->
            nodes.getStepsToEnd(input.first(), startNode) { it.endsWith("Z") }
        }

        return counts.leastCommonMultiply()
    }

    // Test if implementation meets criteria from the description
    check(part1(utils.readLines("test")) == 2)
    check(part1(utils.readLines("test2")) == 6)
    check(part2(utils.readLines("test3")) == 6L)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}