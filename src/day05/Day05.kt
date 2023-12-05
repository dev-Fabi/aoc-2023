package day05

import Utils
import kotlin.math.max
import kotlin.math.min

fun main() {
    val utils = Utils(5)

    class Mapping(private val sourceStart: Long, private val destinationStart: Long, length: Long) {
        val sourceRange = sourceStart..(sourceStart + length)

        fun getDestinationFor(source: Long): Long = destinationStart + source - sourceStart
    }

    fun String.createMappings(): List<List<Mapping>> {
        return substringAfter("\n\n").split("\n\n").map { section ->
            section.split("\n").drop(1).map { line ->
                val (destination, source, range) = line.split(" ").map(String::toLong)
                Mapping(source, destination, range)
            }
        }
    }


    fun String.getLocationsFromSeeds(seedNumbers: List<Long>): List<Long> {
        val mappings = this.createMappings()

        return seedNumbers.map { seed ->
            mappings.fold(seed) { acc, map ->
                map.firstOrNull { it.sourceRange.contains(acc) }?.getDestinationFor(acc) ?: acc
            }
        }
    }

    fun getDestinationRange(mappings: List<Mapping>, sourceRange: LongRange): List<LongRange> {
        val mappedSourceRanges = mutableListOf<LongRange>()
        val destinationRanges = mappings.mapNotNull { map ->
            val start = max(map.sourceRange.first, sourceRange.first)
            val end = min(map.sourceRange.last, sourceRange.last)
            if (start <= end) {
                mappedSourceRanges += start..end
                map.getDestinationFor(start)..map.getDestinationFor(end)
            } else {
                null
            }
        }
        val cuts = buildList {
            add(sourceRange.first)
            add(sourceRange.last)
            mappedSourceRanges.forEach {
                add(it.first)
                add(it.last)
            }
        }.sorted()

        val unmappedSourceRanges = cuts.chunked(2).mapNotNull { (first, second) ->
            if (second <= first) return@mapNotNull null
            if (second == cuts.last()) first..second else first..<second
        }
        return destinationRanges + unmappedSourceRanges
    }


    fun String.getLocationsFromSeeds(seedRanges: List<LongRange>): List<LongRange> {
        val mappings = this.createMappings()

        return seedRanges.flatMap { seeds ->
            mappings.fold(listOf(seeds)) { acc, map ->
                acc.flatMap { getDestinationRange(map, it) }
            }
        }
    }

    fun part1(input: String): Long {
        val seedNumbers = input
            .substringBefore("\n").substringAfter("seeds: ")
            .split(" ")
            .map(String::toLong)

        return input.getLocationsFromSeeds(seedNumbers).min()
    }

    fun part2(input: String): Long {
        val seedRanges = input
            .substringBefore("\n").substringAfter("seeds: ")
            .split(" ")
            .map(String::toLong)
            .chunked(2)
            .map { it.first()..<(it.first() + it.last()) }

        return input.getLocationsFromSeeds(seedRanges).minOf { it.first }
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readFile("test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    // Solve puzzle and print result
    val input = utils.readFile()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}