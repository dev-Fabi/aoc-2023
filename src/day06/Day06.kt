package day06

import Utils

fun main() {
    val utils = Utils(6)

    fun getPossibleDistance(pressTime: Long, totalTime: Long): Long {
        val speed = pressTime
        val travelTime = totalTime - pressTime

        return speed * travelTime
    }

    fun part1(input: List<String>): Long {
        val times = input[0].dropWhile { !it.isDigit() }.split(" ").filter { it.isNotBlank() }.map(String::toLong)
        val distances = input[1].dropWhile { !it.isDigit() }.split(" ").filter { it.isNotBlank() }.map(String::toLong)

        val possibilitiesToWin = times.zip(distances).map { (time, distanceToBeat) ->
            (0..time).mapNotNull { pressTime ->
                getPossibleDistance(pressTime, time).takeIf { it > distanceToBeat }
            }.count().toLong()
        }

        return possibilitiesToWin.reduce { acc, i ->
            acc * i
        }
    }

    fun part2(input: List<String>): Int {
        val time = input[0].filter { it.isDigit() }.toLong()
        val distance = input[1].filter { it.isDigit() }.toLong()

        return (0..time).mapNotNull { pressTime ->
            getPossibleDistance(pressTime, time).takeIf { it > distance }
        }.count()
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}