package day09

import Utils

fun main() {
    val utils = Utils(9)

    fun List<Int>.extrapolate(): Int {
        if (this.all { it == 0 }) return 0
        return this.last() + this.zipWithNext { a, b -> b - a }.extrapolate()
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            line.split(" ").map(String::toInt).extrapolate()
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            line.split(" ").map(String::toInt).reversed().extrapolate()
        }
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}