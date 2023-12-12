package day12

import Utils

fun main() {
    val utils = Utils(12)

    class ConditionRecord(val pattern: String, val groups: List<Int>)

    fun List<String>.getConditionRecords(): List<ConditionRecord> = this.map { record ->
        val (pattern, groups) = record.split(" ")
        ConditionRecord(pattern, groups.split(",").map(String::toInt))
    }

    fun ConditionRecord.possiblePermutations(): Long {
        val groupRegex = Regex("(#+)")
        val unknownFields = this.pattern.count { it == '?' }
        val permutationCount = 1 shl unknownFields // = 2^unknownFields
        val bitPattern = BooleanArray(this.pattern.length)

        this.pattern.forEachIndexed { index, c ->
            if (c == '#') bitPattern[index] = true
        }

        return (0..<permutationCount).count { permutation ->
            var currentField = 1
            for ((i, c) in this.pattern.withIndex()) {
                if (c == '?') {
                    bitPattern[i] = (permutation and currentField) > 0
                    currentField = currentField shl 1
                }
            }
            val pattern = bitPattern.joinToString("") { if (it) "#" else "." }
            val groups = groupRegex.findAll(pattern).map { it.value.length }.toList()

            groups == this.groups
        }.toLong()
    }

    fun part1(input: List<String>): Long {
        return input
            .getConditionRecords()
            .also {
                val maxUnknownFields = it.maxOf { it.pattern.count { it == '?' } }
                println("Max number of ?: $maxUnknownFields (${1 shl maxUnknownFields} permutations)")
            }
            .sumOf { it.possiblePermutations() }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 21L)
    //check(part2(testInput) == 525152L)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}