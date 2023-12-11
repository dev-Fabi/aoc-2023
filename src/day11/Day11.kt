package day11

import Utils
import kotlin.math.abs

fun main() {
    val utils = Utils(11)

    data class Point(val y: Long, val x: Long)

    fun List<String>.getGalaxies(expansion: Long): List<Point> {
        val rowsToExpand = this.indices.filter { y -> '#' !in this[y] }
        val columnsToExpand = this.indices.filter { x -> this.none { it[x] == '#' } }
        return this.flatMapIndexed { y: Int, row: String ->
            row.withIndex().filter { it.value == '#' }.map { (x, _) ->
                Point(
                    y = y + rowsToExpand.count { it < y } * (expansion - 1),
                    x = x + columnsToExpand.count { it < x } * (expansion - 1)
                )
            }
        }
    }

    fun Pair<Point, Point>.distance(): Long =
        abs(this.first.y - this.second.y) + abs(this.first.x - this.second.x)

    fun List<Point>.allCombinations(): List<Pair<Point, Point>> =
        this.flatMap { a -> this.mapNotNull { b -> if (a == b) null else a to b } }

    fun List<String>.getSumOfDistances(expansion: Long): Long =
        this.getGalaxies(expansion).allCombinations().sumOf { it.distance() } / 2

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(testInput.getSumOfDistances(2) == 374L)
    check(testInput.getSumOfDistances(10) == 1030L)
    check(testInput.getSumOfDistances(100) == 8410L)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + input.getSumOfDistances(2))
    println("\tPart 2: " + input.getSumOfDistances(1_000_000))
}