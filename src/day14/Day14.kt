package day14

import Utils
import toward

enum class Direction {
    N, W, S, E
}

fun main() {
    val utils = Utils(14)

    fun List<MutableList<Char>>.calculateSupportBeamLoad() = reversed().withIndex().sumOf { (rowIndex, row) ->
        row.count { it == 'O' } * (rowIndex + 1)
    }

    fun List<MutableList<Char>>.tilt(direction: Direction) {
        when (direction) {
            Direction.N -> {
                for (r in 1..this.lastIndex) {
                    for (c in 0..this[r].lastIndex) {
                        if (this[r][c] == 'O' && this[r - 1][c] == '.') {
                            val newRow = (r - 1 toward 0).takeWhile { this[it][c] == '.' }.last()
                            this[r][c] = '.'
                            this[newRow][c] = 'O'
                        }
                    }
                }
            }

            Direction.S -> {
                for (r in this.lastIndex - 1 toward 0) {
                    for (c in 0..this[r].lastIndex) {
                        if (this[r][c] == 'O' && this[r + 1][c] == '.') {
                            val newRow = (r + 1 toward this.lastIndex).takeWhile { this[it][c] == '.' }.last()
                            this[r][c] = '.'
                            this[newRow][c] = 'O'
                        }
                    }
                }
            }

            Direction.E -> {
                for (r in 0..this.lastIndex) {
                    for (c in this[r].lastIndex - 1 toward 0) {
                        if (this[r][c] == 'O' && this[r][c + 1] == '.') {
                            val newColumn = (c + 1 toward this[r].lastIndex).takeWhile { this[r][it] == '.' }.last()
                            this[r][c] = '.'
                            this[r][newColumn] = 'O'
                        }
                    }
                }
            }

            Direction.W -> {
                for (r in 0..this.lastIndex) {
                    for (c in 1..this[r].lastIndex) {
                        if (this[r][c] == 'O' && this[r][c - 1] == '.') {
                            val newColumn = (c - 1 toward 0).takeWhile { this[r][it] == '.' }.last()
                            this[r][c] = '.'
                            this[r][newColumn] = 'O'
                        }
                    }
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val map = input.map { it.toMutableList() }
        map.tilt(Direction.N)
        return map.calculateSupportBeamLoad()
    }

    fun part2(input: List<String>): Int {
        var map = input.map { it.toMutableList() }

        val previousRunToMap = mutableMapOf<Int, List<List<Char>>>()
        val previousMapToRun = mutableMapOf<List<List<Char>>, Int>()
        val target = 1_000_000_000
        var cycleCount = 0
        val cyclesToGo: Int
        while (true) {
            if (cycleCount > target) error("How long do you want to wait?")
            Direction.entries.forEach { direction ->
                map.tilt(direction)
            }
            previousRunToMap[cycleCount] = map.map { it.toList() }
            val repeatFrom = previousMapToRun.put(previousRunToMap[cycleCount]!!, cycleCount)
            if (repeatFrom != null) {
                val stillNeeded = target - cycleCount - 1
                val cyclesForRepeat = cycleCount - repeatFrom
                cyclesToGo = stillNeeded % cyclesForRepeat
                break
            }
            cycleCount++
        }

        map = previousRunToMap[cycleCount]!!.map { it.toMutableList() }
        repeat(cyclesToGo) {
            Direction.entries.forEach { direction ->
                map.tilt(direction)
            }
        }

        return map.calculateSupportBeamLoad()
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 136)
    check(part2(testInput) == 64)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}