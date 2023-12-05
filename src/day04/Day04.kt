package day04

import Utils
import kotlin.math.pow

fun main() {
    val utils = Utils(4)

    class Card(val number: Int, winningNumbers: Set<Int>, numbers: Set<Int>) {
        val matchingNumbers = (winningNumbers intersect numbers).size
    }

    fun List<String>.getCards(): List<Card> {
        return this.mapIndexed { index, card ->
            val (winningNumbers, numbers) = card.substringAfter(": ").split(" | ")
            Card(
                number = index + 1,
                winningNumbers.split(" ").mapNotNull { it.toIntOrNull() }.toSet(),
                numbers.split(" ").mapNotNull { it.toIntOrNull() }.toSet()
            )
        }
    }

    fun part1(input: List<String>): Int {
        return input.getCards().sumOf {
            if (it.matchingNumbers > 0) {
                2.0.pow(it.matchingNumbers.toDouble() - 1)
            } else {
                0.0
            }
        }.toInt()
    }

    fun part2(input: List<String>): Long {
        val initialCards = input.getCards()
        var nextRoundCards = initialCards
        var cardSum = initialCards.size.toLong()

        do {
            val currentCards = nextRoundCards
            nextRoundCards = buildList {
                currentCards.forEach { card ->
                    (card.number..<(card.number + card.matchingNumbers)).forEach {
                        this.add(initialCards[it])
                    }
                }
            }
            cardSum += nextRoundCards.size
        } while (nextRoundCards.isNotEmpty())
        return cardSum
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30L)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}