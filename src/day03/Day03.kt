package day03

import println
import readInput

private val NUMBER_REGEX = "\\d+".toRegex()

private data class Position(val row: Int, val col: Int)

private fun Position.north() = this.copy(row = this.row - 1)
private fun Position.south() = this.copy(row = this.row + 1)
private fun Position.west() = this.copy(col = this.col - 1)
private fun Position.east() = this.copy(col = this.col + 1)
private fun Position.northWest() = this.copy(row = this.row - 1, col = this.col - 1)
private fun Position.northEast() = this.copy(row = this.row - 1, col = this.col + 1)
private fun Position.southWest() = this.copy(row = this.row + 1, col = this.col - 1)
private fun Position.southEast() = this.copy(row = this.row + 1, col = this.col + 1)
private fun Position.eightAdjPositions() =
    listOf(
        this.north(), this.south(), this.west(), this.east(),
        this.northWest(), this.northEast(), this.southWest(), this.southEast(),
    )

private data class Num(val row: Int, val range: IntRange, val num: Int) {
    fun position(): List<Position> {
        return buildList {
            for (i in range) {
                add(Position(row, i))
            }
        }
    }
}

private data class Symbol(val row: Int, val col: Int, val symbol: Char) {
    fun position(): Position = Position(row, col)
}

fun main() {

    fun List<String>.numAndSymSet(): Pair<Set<Symbol>, Set<Num>> {
        val numSet = hashSetOf<Num>()
        val symSet = hashSetOf<Symbol>()

        this.forEachIndexed { lidx, line ->
            NUMBER_REGEX.findAll(line).forEach {
                numSet.add(Num(row = lidx, range = it.range, num = it.value.toInt()))
            }
            line.forEachIndexed { cidx, char ->
                if (!char.isDigit() && char != '.') {
                    symSet.add(Symbol(row = lidx, col = cidx, symbol = char))
                }
            }
        }

        return Pair(symSet, numSet)
    }

    fun part1(lines: List<String>): Int {
        val numAndSymSet = lines.numAndSymSet()

        return numAndSymSet.first.map { sym ->
            numAndSymSet.second.filter { num ->
                sym.position().eightAdjPositions().any { num.position().contains(it) }
            }
        }.flatten().sumOf { it.num }
    }

    fun part2(lines: List<String>): Int {
        val numAndSymSet = lines.numAndSymSet()

        return numAndSymSet.first.associateBy({ sym ->
            sym
        }, { sym ->
            numAndSymSet.second.filter { num ->
                sym.position().eightAdjPositions().any { num.position().contains(it) }
            }
        }).filterKeys {
            it.symbol == '*'
        }.filterValues {
            it.size == 2
        }.map {
            it.value[0].num * it.value[1].num
        }.sum()

    }

    val input = readInput("/day03/Day03")
    part1(input).println()
    part2(input).println()
}
