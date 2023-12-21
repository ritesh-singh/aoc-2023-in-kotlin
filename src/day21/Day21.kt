package day21

import Position2D
import down
import left
import println
import readInput
import right
import up
import java.util.ArrayDeque
import kotlin.properties.Delegates

private class StepCounter(input: List<String>) {

    private val grid = Array(input.size) { CharArray(input[0].length) }

    private fun Array<CharArray>.printGrid(){
        for(r in indices){
            for(c in this[0].indices){
                print(grid[r][c])
            }
            kotlin.io.println()
        }
        kotlin.io.println()
    }

    private var sR by Delegates.notNull<Int>()
    private var sC by Delegates.notNull<Int>()

    init {
        for(r in input.indices){
            for (c in input[0].indices){
                grid[r][c] = input[r][c]
                if (input[r][c] == 'S'){
                    sR = r
                    sC = c
                    grid[r][c] = '.'
                }
            }
        }
    }

    private fun Position2D.isValid() = x in grid.indices && y in grid[0].indices
    private fun Position2D.adjacentPositions(): List<Position2D> {
        return buildList {
            if (up().isValid()) add(up())
            if (down().isValid()) add(down())
            if (left().isValid()) add(left())
            if (right().isValid()) add(right())
        }
    }

    private fun Position2D.canMove(): Boolean {
        if (grid[x][y] == '.') return true
        return false
    }

    private fun List<Position2D>.gardenPlots(): List<Position2D> {
        val currentLocation = mutableListOf<Position2D>()
        for (pos in this) {
            val movePositions = pos.adjacentPositions().filter { it.canMove() }
            if (movePositions.isEmpty()){
                grid[pos.x][pos.y] = '.'
                continue
            }
            for (adj in movePositions) {
                grid[adj.x][adj.y] = 'O'
                grid[pos.x][pos.y] = '.'
                currentLocation.add(adj)
            }
        }
        return currentLocation
    }

    fun solvePart1():Int {
        var currentLocation = listOf(Position2D(sR,sC))
        repeat(64){
            currentLocation = currentLocation.gardenPlots()
        }
        return grid.map {
            it.count { it == 'O' }
        }.sum()
    }

    fun solvePart2():Int {
        return 0
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val stepCounter = StepCounter(input)
        return stepCounter.solvePart1()
    }

    val input = readInput("/day21/Day21")
    part1(input).println()
}
