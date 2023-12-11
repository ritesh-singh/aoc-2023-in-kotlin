package day10


import println
import readInput
import java.util.ArrayDeque
import kotlin.properties.Delegates
import kotlin.system.measureTimeMillis

private class Island(val lines: List<String>) {
    val rowSize = lines.size
    val colSize = lines[0].length

    val grid = Array(rowSize) { CharArray(colSize) }

    var sRow by Delegates.notNull<Int>()
    var sCol by Delegates.notNull<Int>()

    init {
        for (row in 0 until rowSize) {
            for (col in 0 until colSize) {
                if (lines[row][col] == 'S'){
                    sRow = row
                    sCol = col
                }
                grid[row][col] = lines[row][col]
            }
        }
    }

    fun longestPath(): Int {
        val queue = ArrayDeque<Pair<Int, Int>>()
        val visited = hashSetOf<Pair<Int, Int>>()

        queue.add(Pair(sRow, sCol))
        visited.add(Pair(sRow, sCol))

        while (queue.isNotEmpty()) {
            val (row, col) = queue.removeFirst()
            // up
            if (row > 0 && !visited.contains(Pair(row - 1, col)) && grid[row][col] in "S|JL" && grid[row - 1][col] in "|7F") {
                visited.add(Pair(row - 1, col))
                queue.add(Pair(row - 1, col))
            }

            // down
            if (row < rowSize-1 && !visited.contains(Pair(row + 1, col)) && grid[row][col] in "S|7F" && grid[row+1][col] in "|JL"){
                visited.add(Pair(row + 1, col))
                queue.add(Pair(row + 1, col))
            }
            // left
            if (col > 0 && !visited.contains(Pair(row, col-1)) && grid[row][col] in "S-J7" && grid[row][col-1] in "-LF"){
                visited.add(Pair(row, col-1))
                queue.add(Pair(row, col-1))
            }
            // right
            if (col < colSize - 1 && !visited.contains(Pair(row, col+1))&& grid[row][col] in "S-LF" && grid[row][col+1] in "-J7") {
                visited.add(Pair(row, col+1))
                queue.add(Pair(row, col+1))
            }
        }
        return visited.size / 2
    }
}

fun main() {

    fun part1(lines: List<String>): Int {
        val island = Island(lines = lines)
        return island.longestPath()
    }

    fun part2(lines: List<String>): Int {
        return 0
    }

    val testInput = readInput("/day10/Day10_test")
    measureTimeMillis { part1(testInput).println() }
//    measureTimeMillis { part2(testInput).println() }.printlnTime()

    println("-----------------------------------------------------")

    val input = readInput("/day10/Day10")
    measureTimeMillis { part1(input).println() }
//    measureTimeMillis { part2(input).println() }.printlnTime()
}
