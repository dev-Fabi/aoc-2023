package day01

import Utils

fun main() {
    val utils = Utils(1)

    fun part1(input: List<String>): Long {
        return input.sumOf { line ->
            val firstNumber = line.first(Char::isDigit).digitToInt()
            val secondNumber = line.last(Char::isDigit).digitToInt()
            firstNumber * 10L + secondNumber
        }
    }

    fun part2(input: List<String>): Long {
        val digits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        return input.sumOf { line ->
            line.indices.mapNotNull { i ->
                digits.indexOfFirst { line.drop(i).startsWith(it) }.takeIf { it > -1 }?.let { it + 1 }
                    ?: line[i].digitToIntOrNull()
            }.let { it.first() * 10L + it.last() }
        }
    }

    // Test if implementation meets criteria from the description
    check(part1(utils.readLines("test")) == 142L)
    check(part2(utils.readLines("test2")) == 281L)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}