package day02

import Utils

fun main() {
    val utils = Utils(2)

    fun List<String>.createGameList(): List<List<Map<String, Int>>> {
        val cubeRegex = Regex("(\\d+)(red|green|blue)")
        return this.map { game ->
            game.substringAfter(":")
                .replace(" ", "")
                .split(";")
                .map {
                    cubeRegex.findAll(it).associate { match ->
                        val (count, color) = match.destructured
                        color to count.toInt()
                    }
                }
        }
    }

    fun part1(input: List<String>): Int {
        val availableCubes = mapOf(
            "green" to 13,
            "red" to 12,
            "blue" to 14
        )

        return input.createGameList().withIndex()
            .filter { (_, game) ->
                game.all { combinations ->
                    combinations.all { availableCubes[it.key]!! >= it.value }
                }
            }
            .sumOf { it.index + 1 }
    }

    fun part2(input: List<String>): Long {
        return input.createGameList().sumOf { game ->
            val red = game.maxOf { it["red"] ?: 0 }.toLong()
            val green = game.maxOf { it["green"] ?: 0 }.toLong()
            val blue = game.maxOf { it["blue"] ?: 0 }.toLong()

            red * green * blue
        }
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286L)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}