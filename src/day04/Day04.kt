package day04

import println
import readInput

private data class Game(
    val game: Int,
    val winningNumber: List<Int>,
    val numbers: List<Int>
)

fun main() {

    fun List<String>.parse(): List<Game> {
        return this
            .map {
                it.split(":", "|").map { it.trim() }
            }.map {
                Game(
                    game = it[0].filter { it.isDigit() }.toInt(),
                    winningNumber = it[1].split(" ").filter { it.isNotEmpty() }.map { it.trim() }.map { it.toInt() },
                    numbers = it[2].split(" ").filter { it.isNotEmpty() }.map { it.trim() }.map { it.toInt() },
                )
            }
    }

    fun part1(lines: List<String>): Int {
        return lines.parse()
            .map { game ->
                game.winningNumber
                    .intersect(game.numbers.toSet())
                    .takeIf { it.isNotEmpty() }
                    ?.drop(1)
                    ?.fold(1) { old, _ -> old * 2 } ?: 0

            }.sum()
    }

    fun part2(lines:List<String>):Int {
        return lines.parse()
            .let {
                val copies = it.associate { it.game to 1 }.toMutableMap()
                it.forEach {
                    val winningNum = it.winningNumber.intersect(it.numbers.toSet()).size
                    for (i in it.game + 1..it.game + winningNum) {
                        copies[i] = copies[i]!! + copies[it.game]!!
                    }
                }
                copies.values.sum()
            }
    }

    val input = readInput("/day04/Day04")
    part1(input).println()
    part2(input).println()
}
