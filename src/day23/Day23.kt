package day23

import Position2D
import down
import left
import println
import readInput
import right
import up

private class LongWalk(val input: List<String>) {
    val graph = hashMapOf<Position2D, List<Position2D>>()

    init {
        for (r in input.indices){
            for(c in input[0].indices){
                val pos = Position2D(r,c)
                if (input[r][c] == '#') continue
                graph[pos] = pos.allPositions()
            }
        }
    }

    private val starPos = Position2D(0, 1)
    private val endPos = Position2D(input.lastIndex, input[0].lastIndex - 1)

    private fun Position2D.isValid() = x in input.indices && y in input[0].indices

    private fun Position2D.canMove() = input[x][y] != '#'

    private fun Position2D.allPositions() = buildList {
        when (input[x][y]) {
            '>' -> if (right().isValid() && right().canMove() && input[right().x][right().y] != '<') add(right())
            '<' -> if (left().isValid() && left().canMove() && input[left().x][left().y] != '>') add(left())
            '^' -> if (up().isValid() && up().canMove() && input[up().x][up().y] != 'v') add(up())
            'v' -> if (down().isValid() && down().canMove() && input[down().x][down().y] != '^') add(down())
            else -> {
                if (right().isValid() && right().canMove() && input[right().x][right().y] != '<') add(right())
                if (left().isValid() && left().canMove() && input[left().x][left().y] != '>') add(left())
                if (up().isValid() && up().canMove() && input[up().x][up().y] != 'v') add(up())
                if (down().isValid() && down().canMove() && input[down().x][down().y] != '^') add(down())
            }
        }
    }

    private fun longestPath(): Int {
        fun dfs(pos: Position2D, visited: HashSet<Position2D>, dist: Int, distList: MutableList<Int>) {
            if (pos == endPos) {
                distList.add(dist)
                return
            }

            if (visited.contains(pos)) return

            visited.add(pos)

            for (adj in graph[pos]!!) {
                if (visited.contains(adj)) continue
                dfs(adj, visited, dist + 1, distList)
            }

            visited.remove(pos)
        }

        val list = mutableListOf<Int>()
        dfs(starPos, hashSetOf(), 0, list)

        return list.max()
    }

    fun solve(): Int {
        return longestPath()
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val longWalk = LongWalk(input = input)
        return longWalk.solve()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("/day23/Day23_test")
    part1(testInput).println()
//    part2(testInput).println()
//
//    println("--------------------------------------------")
//
    val input = readInput("/day23/Day23")
    part1(input).println()
//    part2(input).println()
}
