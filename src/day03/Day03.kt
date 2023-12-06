package day03

import Utils

fun main() {
    val utils = Utils(3)

    data class Num(val number: Int, val row: Int, val startColumn: Int, val length: Int)

    fun List<String>.getNumbers(): List<Num> {
        val numbers = mutableListOf<Num>()
        this.withIndex().forEach { (rowIndex, row) ->
            var nextColumn = 0
            do {
                val nextNumberChars =
                    row.withIndex().drop(nextColumn).dropWhile { !it.value.isDigit() }.takeWhile { it.value.isDigit() }
                if (nextNumberChars.isEmpty()) break

                val num = Num(
                    number = nextNumberChars.joinToString("") { it.value.toString() }.toInt(),
                    row = rowIndex,
                    startColumn = nextNumberChars[0].index,
                    length = nextNumberChars.size,
                )

                nextColumn = num.startColumn + num.length
                numbers.add(num)
            } while (nextColumn <= row.lastIndex)
        }

        return numbers
    }

    fun part1(input: List<String>): Int {
        val map = input.map { it.toList() }

        fun symbolNextToNumber(row: Int, startColumn: Int, length: Int): Boolean {
            ((row - 1).coerceAtLeast(0)..(row + 1).coerceAtMost(map.lastIndex)).forEach { rowIndex ->
                ((startColumn - 1).coerceAtLeast(0)..(startColumn + length).coerceAtMost(map[0].lastIndex)).forEach { columnIndex ->
                    if (map[rowIndex][columnIndex].let { !it.isDigit() && it != '.' }) return true
                }
            }
            return false
        }

        return input.getNumbers().filter {
            symbolNextToNumber(it.row, it.startColumn, it.length)
        }.sumOf { it.number }
    }

    fun part2(input: List<String>): Long {
        val numbers = input.getNumbers()

        return input.withIndex().sumOf { (rowIndex, row) ->
            row.withIndex()
                .filter { it.value == '*' }
                .map { column ->
                    numbers.filter { it.row in (rowIndex - 1)..(rowIndex + 1) && column.index in (it.startColumn - 1)..(it.startColumn + it.length) }
                }
                .filter { it.size == 2 }
                .sumOf { it[0].number * it[1].number }.toLong()
        }
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835L)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}