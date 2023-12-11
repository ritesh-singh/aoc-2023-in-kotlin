package day11

import Position2D
import com.github.shiguruikai.combinatoricskt.combinations
import down
import left
import println
import readInput
import right
import up
import java.util.ArrayDeque
import kotlin.math.abs
import kotlin.system.measureTimeMillis

private fun Char.isGalaxy() = this == '#'
private fun Char.isEmptySpace() = this == '.'

private class Universe(val lines: List<String>, val times:Int) {

    val galaxies = hashSetOf<Position2D>()
    val rowWithEmptySpace = lines.indices.filter { rowIndex -> lines[rowIndex].all { it.isEmptySpace() } }
        .mapIndexed { index, i -> i + index * times }
    val colWithEmptySpace =
        lines[0].indices.filter { columnIndex -> lines.all { row -> row[columnIndex].isEmptySpace() } }
            .mapIndexed { index, i -> i + index * times }

    init {
        lines.forEachIndexed { rIdx, s ->
            s.forEachIndexed { cIdx, c ->
                if (c.isGalaxy())
                    galaxies.add(Position2D(x = rIdx, y = cIdx))
            }
        }
    }

    fun solve():Long {
        var newGalaxy = galaxies
        colWithEmptySpace.map { col ->
            newGalaxy.map { if (it.y > col) it.copy(y = it.y + times) else it }.also { newGalaxy = it.toHashSet() }
        }
        rowWithEmptySpace.map { row ->
            newGalaxy.map { if (it.x > row) it.copy(x = it.x + times) else it }.also { newGalaxy = it.toHashSet() }
        }


        return newGalaxy.combinations(2).map {
            abs(it[0].x - it[1].x)+ abs(it[0].y - it[1].y).toLong()
        }.sum()
    }
}

fun main() {

    fun part1(lines: List<String>): Long {
        val universe = Universe(lines = lines, times = 1)
        return universe.solve()
    }

    fun part2(lines: List<String>): Long {
        val universe = Universe(lines = lines, times = 1000000 - 1)
        return universe.solve()
    }

    val input = readInput("/day11/Day11")
    measureTimeMillis { part1(input).println() }
    measureTimeMillis { part2(input).println() }
}
