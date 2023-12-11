package day10

import println
import printlnTime
import readInput
import java.util.ArrayDeque
import kotlin.system.measureTimeMillis

private class Area(lines: List<String>) {
    private data class Position(val row: Int, val col: Int) {
        fun up() = copy(row = row - 1)
        fun down() = copy(row = row + 1)
        fun left() = copy(col = col - 1)
        fun right() = copy(col = col + 1)
    }

    private enum class PType { V_P, H_P, L_P, J_P, SEVEN_P, F_P, S_P }

    private enum class Direction { L, R, U, D }

    private val connectionMap = mapOf(
        // valid source to destination, when moving up
        Direction.U to Pair(
            setOf(PType.S_P, PType.V_P, PType.J_P, PType.L_P),
            setOf(PType.V_P, PType.SEVEN_P, PType.F_P)
        ),
        Direction.D to Pair(
            setOf(PType.S_P, PType.V_P, PType.SEVEN_P, PType.F_P),
            setOf(PType.V_P, PType.J_P, PType.L_P)
        ),
        Direction.L to Pair(
            setOf(PType.S_P, PType.H_P, PType.J_P, PType.SEVEN_P),
            setOf(PType.H_P, PType.L_P, PType.F_P)
        ),
        Direction.R to Pair(
            setOf(PType.S_P, PType.H_P, PType.L_P, PType.F_P),
            setOf(PType.H_P, PType.J_P, PType.SEVEN_P)
        )
    )

    private fun type(c: Char): PType {
        return when (c) {
            '|' -> PType.V_P
            '-' -> PType.H_P
            'L' -> PType.L_P
            'J' -> PType.J_P
            '7' -> PType.SEVEN_P
            'F' -> PType.F_P
            'S' -> PType.S_P
            else -> error("Unsupported type")
        }
    }

    private data class Pipe(val position: Position, val type: PType)

    private fun Pipe.adjacentPipes(): Map<Direction, Pipe> {
        return listOf(
            Pair(Direction.U, pipes.find { it.position == position.up() }),
            Pair(Direction.D, pipes.find { it.position == position.down() }),
            Pair(Direction.L, pipes.find { it.position == position.left() }),
            Pair(Direction.R, pipes.find { it.position == position.right() })
        ).filter {
            it.second != null
        }.associateBy({
            it.first
        }, {
            it.second!!
        })
    }

    private val pipes = hashSetOf<Pipe>()

    private lateinit var sourcePipe: Pipe

    init {
        lines.forEachIndexed { row, line ->
            line.forEachIndexed { col, c ->
                if (c == '.') return@forEachIndexed
                if (c == 'S') {
                    sourcePipe = Pipe(position = Position(row = row, col = col), type = type(c))
                }
                pipes.add(Pipe(position = Position(row = row, col = col), type = type(c)))
            }
        }
    }

    fun longestStep(): Int {
        val queue = ArrayDeque<Pair<Pipe, Int>>()
        val visited = hashSetOf<Pipe>()

        val allSteps = mutableSetOf<Int>()

        queue.add(Pair(sourcePipe, 0))
        visited.add(sourcePipe)

        while (queue.isNotEmpty()) {
            val (current, steps) = queue.removeFirst()
            allSteps.add(steps)

            val nextIter:(Pair<Set<PType>,Set<PType>>, Pipe) -> Unit = { pair, adj ->
                if (pair.first.contains(current.type) && pair.second.contains(adj.type)) {
                    visited.add(adj)
                    queue.add(Pair(adj, steps + 1))
                }
            }

            for ((direction, adj) in current.adjacentPipes()) {
                if (visited.contains(adj)) continue
                when (direction) {
                    Direction.U -> nextIter(connectionMap[Direction.U]!!,adj)
                    Direction.D -> nextIter(connectionMap[Direction.D]!!,adj)
                    Direction.L -> nextIter(connectionMap[Direction.L]!!,adj)
                    Direction.R -> nextIter(connectionMap[Direction.R]!!,adj)
                }
            }
        }
        return allSteps.max()
    }
}

fun main() {

    fun part1(lines: List<String>): Int {
        val area = Area(lines)
        return area.longestStep()
    }

    fun part2(lines: List<String>): Int {
        return 0
    }

    val testInput = readInput("/day10/Day10_test")
    measureTimeMillis { part1(testInput).println() }
//    measureTimeMillis { part2(testInput).println() }.printlnTime()

    println("-----------------------------------------------------")

    val input = readInput("/day10/Day10")
    measureTimeMillis { part1(input).println() }.printlnTime()
//    measureTimeMillis { part2(input).println() }.printlnTime()
}
