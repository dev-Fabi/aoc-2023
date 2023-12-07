package day07

import Utils

enum class Type {
    FiveOfAKind, FourOfAKind, FullHouse, ThreeOfAKind, TwoPair, OnePair, HighCard
}

fun main() {
    val utils = Utils(7)

    val letterOrder = listOf('T', 'J', 'Q', 'K', 'A')

    class Hand(
        cardsString: String,
        val bid: Int,
        cardsWithJokerSwapped: String? = null,
    ) : Comparable<Hand> {
        val cards: List<Int> =
            (cardsWithJokerSwapped ?: cardsString).map { card ->
                letterOrder.indexOf(card).let { if (it > -1) it + 10 else card.digitToInt() }
            }

        val cardValues: List<Int> = run {
            if (cardsWithJokerSwapped == null) {
                cards
            } else {
                cards.map { if (it == 11) 1 else it }
            }
        }

        val type = run {
            val grouped = cards.groupBy { it }
            when {
                cards.distinct().count() == 1 -> Type.FiveOfAKind
                grouped.values.any { it.count() == 4 } -> Type.FourOfAKind
                grouped.keys.count() == 2 -> Type.FullHouse
                grouped.values.any { it.count() == 3 } -> Type.ThreeOfAKind
                grouped.values.count { it.count() == 2 } == 2 -> Type.TwoPair
                grouped.values.count { it.count() == 2 } == 1 -> Type.OnePair
                cards.distinct().count() == 5 -> Type.HighCard
                else -> error("Unknown hand: $cards")
            }
        }

        override operator fun compareTo(other: Hand): Int {
            return if (this.type != other.type) {
                other.type.compareTo(this.type)
            } else {
                this.cardValues.forEachIndexed { index, c ->
                    if (c == other.cardValues[index]) return@forEachIndexed
                    return c.compareTo(other.cardValues[index])
                }
                0
            }
        }
    }

    fun part1(input: List<String>): Int {
        val hands = input.map {
            val (cards, bid) = it.split(" ")
            Hand(cards, bid = bid.toInt())
        }

        return hands.sorted().foldIndexed(0) { index, sum, hand ->
            sum + (index + 1) * hand.bid
        }
    }

    fun part2(input: List<String>): Int {
        val hands = input.map { line ->
            val (cards, bid) = line.split(" ")
            listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A').maxOf { swap ->
                Hand(cards, bid.toInt(), cards.map { if (it == 'J') swap else it }.joinToString(""))
            }
        }

        return hands.sorted().foldIndexed(0) { index, sum, hand ->
            sum + (index + 1) * hand.bid
        }
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}