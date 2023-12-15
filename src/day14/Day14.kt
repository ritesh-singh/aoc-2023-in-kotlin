package day14

import Direction
import println
import readInput
import kotlin.system.measureTimeMillis

class Parabolic(val lines: List<String>) {
    private val grid = Array(lines.size) { CharArray(lines[0].length) }

    init {
        lines.forEachIndexed { rIdx, s ->
            s.forEachIndexed { cIdx, c ->
                grid[rIdx][cIdx] = c
            }
        }
    }

    private fun rollNorthOrSouth(direction: Direction) {
        assert(direction == Direction.N || direction == Direction.S)
        grid[0].indices.map { columnIndex ->
            val columValues = grid.map { row -> row[columnIndex] }
            val blockers = columValues.withIndex().filter { it.value == '#' }
            columValues.joinToString("").split("#").map {
                if (direction == Direction.N) it.toCharArray().sortedDescending()
                else it.toCharArray().sorted()
            }.flatten().let {
                val list = it.toMutableList()
                blockers.forEach {(index,_) -> list.add(index,'#') }
                grid.indices.forEachIndexed { index, _ -> grid[index][columnIndex] = list[index] }
            }
        }
    }

    private fun rollWestOrEast(direction: Direction) {
        assert(direction == Direction.W || direction == Direction.E)
        grid.mapIndexed { index, charArr ->
            val blockers = charArr.withIndex().filter { it.value == '#' }
            charArr.joinToString("").split("#").map {
                if (direction == Direction.W) it.toCharArray().sortedDescending()
                else it.toCharArray().sorted()
            }.flatten().let {
                val list = it.toMutableList()
                blockers.forEach { (index, _) -> list.add(index, '#') }
                grid[index] = list.toCharArray()
            }
        }
    }

    private data class State(val grid:Array<CharArray>) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as State

            if (!grid.contentDeepEquals(other.grid)) return false

            return true
        }
        override fun hashCode(): Int { return grid.contentDeepHashCode() }
    }

    fun totalLoadPart1(): Int {
        rollNorthOrSouth(direction = Direction.N)
        return grid.mapIndexed { index, chars ->
            chars.count { it == 'O' } * (grid[0].size-index)
        }.sum()
    }

    fun totalLoadPart2(): Int {
        val visited = mutableSetOf<State>()
        visited.add(State(grid = grid.map { it.clone() }.toTypedArray()))
        var cycleEndIdx = 0
        while (true){
            cycleEndIdx++
            rollNorthOrSouth(Direction.N)
            rollWestOrEast(Direction.W)
            rollNorthOrSouth(Direction.S)
            rollWestOrEast(Direction.E)
            if (visited.contains(State(grid = grid))) break
            visited.add(State(grid = grid.map { it.clone() }.toTypedArray()))
        }

        val cycleStartIdx = visited.indexOf(State(grid = grid))

        return visited.filterIndexed { index, state ->
            index == cycleStartIdx + (1000000000 - cycleStartIdx) % (cycleEndIdx - cycleStartIdx)
        }.map {
            it.grid.mapIndexed { index, chars -> chars.count { it == 'O' } * (grid[0].size - index) }
        }.flatten().sum()
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val parabolic = Parabolic(lines = input)
        return parabolic.totalLoadPart1()
    }

    fun part2(input: List<String>): Int {
        val parabolic = Parabolic(lines = input)
        return parabolic.totalLoadPart2()
    }

    val input = readInput("/day14/Day14")
    measureTimeMillis { part1(input).println() }
    measureTimeMillis { part2(input).println() }
}
