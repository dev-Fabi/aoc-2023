package day10

import Utils
import kotlin.math.absoluteValue

enum class Direction {
    N, E, S, W
}

sealed class Tile {
    abstract val point: Pair<Int, Int>

    data class Pipe(val connecting: Pair<Direction, Direction>, override val point: Pair<Int, Int>) : Tile() {
        fun getNextTile(previous: Tile, map: Map<Pair<Int, Int>, Tile>): Tile? {
            return listOfNotNull(
                getNeighbour(connecting.first, map),
                getNeighbour(connecting.second, map)
            ).singleOrNull { it != previous }
        }
    }

    data class Start(override val point: Pair<Int, Int>) : Tile()
    data class Ground(override val point: Pair<Int, Int>) : Tile()

    fun getNeighbour(direction: Direction, map: Map<Pair<Int, Int>, Tile>): Tile? {
        val point = when (direction) {
            Direction.N -> this.point.first - 1 to this.point.second
            Direction.E -> this.point.first to this.point.second + 1
            Direction.S -> this.point.first + 1 to this.point.second
            Direction.W -> this.point.first to this.point.second - 1
        }

        return map[point]
    }
}

fun main() {
    val utils = Utils(10)

    fun Char.getConnection(): Pair<Direction, Direction> = when (this) {
        '|' -> Direction.N to Direction.S
        '-' -> Direction.E to Direction.W
        'L' -> Direction.N to Direction.E
        'J' -> Direction.N to Direction.W
        '7' -> Direction.S to Direction.W
        'F' -> Direction.S to Direction.E
        else -> error("Invalid tile")
    }

    fun getMoveDirection(from: Tile, to: Tile): Direction {
        return when {
            from.point.first == to.point.first && from.point.second < to.point.second -> Direction.E
            from.point.first == to.point.first && from.point.second > to.point.second -> Direction.W
            from.point.first < to.point.first && from.point.second == to.point.second -> Direction.S
            from.point.first > to.point.first && from.point.second == to.point.second -> Direction.N
            else -> error("Invalid move")
        }
    }


    fun List<String>.getLongestPath(): List<Tile> {
        val map: Map<Pair<Int, Int>, Tile> = this.flatMapIndexed { row, line ->
            line.mapIndexed { cell, tileChar ->
                val point = row to cell
                val tile = when (tileChar) {
                    '.' -> Tile.Ground(point)
                    'S' -> Tile.Start(point)
                    else -> Tile.Pipe(tileChar.getConnection(), point)
                }

                point to tile
            }
        }.toMap()

        val start = map.values.single { it is Tile.Start }

        val possibleStartTiles = Direction.entries.mapNotNull { direction ->
            val neededConnection = when (direction) {
                Direction.N -> Direction.S
                Direction.E -> Direction.W
                Direction.S -> Direction.N
                Direction.W -> Direction.E
            }
            (start.getNeighbour(direction, map) as? Tile.Pipe)?.takeIf {
                it.connecting.first == neededConnection || it.connecting.second == neededConnection
            }
        }

        return possibleStartTiles.mapNotNull { secondTile ->
            val route = mutableListOf(start)
            var nextTile: Tile? = secondTile
            while (nextTile != null && nextTile is Tile.Pipe) {
                val previous = route.last()
                route.add(nextTile)
                nextTile = nextTile.getNextTile(previous, map)
            }

            return@mapNotNull if (nextTile !is Tile.Start) {
                null
            } else {
                route
            }
        }.maxBy { it.size }
    }

    fun part1(input: List<String>): Int {
        return input.getLongestPath().size / 2
    }

    fun part2(input: List<String>): Int {
        val path = input.getLongestPath()

        return path.zipWithNext().fold(0 to 0) { (sum, d), (from, to) ->
            when (getMoveDirection(from, to)) {
                Direction.N -> sum to d + 1
                Direction.S -> sum to d - 1
                Direction.W -> sum - d to d
                Direction.E -> sum + d to d
            }
        }.first.absoluteValue - (path.size / 2) + 1
    }

    // Test if implementation meets criteria from the description
    check(part1(utils.readLines("test")) == 4)
    check(part1(utils.readLines("test2")) == 8)
    check(part1(utils.readLines("test3")) == 4)
    check(part1(utils.readLines("test4")) == 8)
    check(part2(utils.readLines("test5")) == 4)
    check(part2(utils.readLines("test6")) == 4)
    check(part2(utils.readLines("test7")) == 8)
    check(part2(utils.readLines("test8")) == 10)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}