package day13

import Utils

sealed interface MirrorAxis {
    val index: Int

    data class Horizontal(override val index: Int) : MirrorAxis
    data class Vertical(override val index: Int) : MirrorAxis
}

fun main() {
    val utils = Utils(13)

    /** Returns how many items are different **/
    fun List<Char>.difference(other: List<Char>): Int = this.withIndex().count { (index, c) ->
        c != other[index]
    }

    fun List<MirrorAxis>.summarySum() = sumOf {
        when (it) {
            is MirrorAxis.Horizontal -> 100 * (it.index)
            is MirrorAxis.Vertical -> it.index
        }
    }

    fun part1(input: String): Int {
        val patterns = input.split("\n\n")

        val mirrorAxis = patterns.map { note ->
            val map = note.lines().map(String::toList) // [row][column]

            for (r in 1..map.lastIndex) {
                var canBeMirrorAxis = false
                for (c in 1..(map.size - r)) {
                    val upper = (r - c).takeIf { it >= 0 } ?: break
                    val lower = r + c - 1

                    if (map[upper] != map[lower]) {
                        canBeMirrorAxis = false
                        break
                    } else {
                        canBeMirrorAxis = true
                    }
                }
                if (canBeMirrorAxis) return@map MirrorAxis.Horizontal(r)
            }

            for (r in 1..map[0].lastIndex) {
                var canBeMirrorAxis = false
                for (c in 1..(map[0].size - r)) {
                    val left = (r - c).takeIf { it >= 0 } ?: break
                    val right = r + c - 1

                    val leftList = map.indices.map { map[it][left] }
                    val rightList = map.indices.map { map[it][right] }
                    if (leftList != rightList) {
                        canBeMirrorAxis = false
                        break
                    } else {
                        canBeMirrorAxis = true
                    }
                }
                if (canBeMirrorAxis) return@map MirrorAxis.Vertical(r)
            }

            error("No axis found for note \n$note")
        }

        return mirrorAxis.summarySum()
    }

    fun part2(input: String): Int {
        val patterns = input.split("\n\n")

        val mirrorAxis = patterns.map { note ->
            val map = note.lines().map(String::toList) // [row][column]

            for (r in 1..map.lastIndex) {
                var canBeMirrorAxis = false
                var differenceFound = false
                for (c in 1..(map.size - r)) {
                    val upper = (r - c).takeIf { it >= 0 } ?: break
                    val lower = r + c - 1

                    if (map[upper] != map[lower]) {
                        if (map[upper].difference(map[lower]) == 1 && !differenceFound) {
                            differenceFound = true
                            canBeMirrorAxis = true
                        } else {
                            canBeMirrorAxis = false
                            break
                        }
                    } else {
                        canBeMirrorAxis = true
                    }
                }
                if (canBeMirrorAxis && differenceFound) return@map MirrorAxis.Horizontal(r)
            }

            for (r in 1..map[0].lastIndex) {
                var canBeMirrorAxis = false
                var differenceFound = false
                for (c in 1..(map[0].size - r)) {
                    val left = (r - c).takeIf { it >= 0 } ?: break
                    val right = r + c - 1

                    val leftList = map.indices.map { map[it][left] }
                    val rightList = map.indices.map { map[it][right] }
                    if (leftList != rightList) {
                        if (leftList.difference(rightList) == 1 && !differenceFound) {
                            differenceFound = true
                            canBeMirrorAxis = true
                        } else {
                            canBeMirrorAxis = false
                            break
                        }
                    } else {
                        canBeMirrorAxis = true
                    }
                }
                if (canBeMirrorAxis && differenceFound) return@map MirrorAxis.Vertical(r)
            }

            error("No axis with difference found for note \n$note")
        }

        return mirrorAxis.summarySum()
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readFile("test")
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    // Solve puzzle and print result
    val input = utils.readFile()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}