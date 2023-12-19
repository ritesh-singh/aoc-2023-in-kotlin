package day18

import Position2D
import Position2DLong
import down
import left
import println
import readInput
import right
import up
import kotlin.math.abs
import kotlin.system.measureTimeMillis

private class LavaDuctLagoon(val input: List<String>) {

    private val diggedLocation = hashSetOf<Position2D>()

    private fun Position2D.neighbours():List<Position2D> {
        return buildList {
            add(up())
            add(down())
            add(left())
            add(right())
        }
    }

    private fun floodFill(position: Position2D) {
        val queue = ArrayDeque<Position2D>()
        queue.addFirst(position)
        while (queue.isNotEmpty()){
            val loc = queue.removeFirst()
            diggedLocation.add(loc)
            for (adj in loc.neighbours()) {
                if (diggedLocation.contains(adj))
                    continue
                queue.addFirst(adj)
            }
        }
    }

    private fun findStartingPoint(): Position2D {
        val positions = diggedLocation.groupBy { it.y }.toSortedMap().map {
            it.value
        }.first()
        for (pos in positions){
            if (!diggedLocation.contains(Position2D(pos.x, pos.y+1)))
                return Position2D(pos.x, pos.y+1)
        }
        error("Starting point not found")
    }

    fun solvePart1(): Int {
        var lastLocation = Position2D(0, 0)
        input.forEach {
            val (direction, step, color) = it.split(" ").map { it.trim() }
            repeat(step.toInt()){
                when(direction){
                    "L" -> lastLocation = lastLocation.left()
                    "R" -> lastLocation = lastLocation.right()
                    "D" -> lastLocation = lastLocation.down()
                    "U" -> lastLocation = lastLocation.up()
                }
                diggedLocation.add(lastLocation)
            }
        }
        floodFill(findStartingPoint())
        return diggedLocation.size
    }

    private val dirToDigMap = mapOf(0 to "R", 1 to "D", 2 to "L", 3 to "U")

    fun solvePart2():Long{
        val coords = mutableListOf<Position2DLong>()
        var lastLocation = Position2DLong(0,0)
        var perimeter = 0L
        input.forEach {
            val color = it.split(" ").map { it.trim() }.last().substringAfter("(").substringBefore(")")
            val step = color.dropLast(1).drop(1).toLong(16)
            when (dirToDigMap[color.last().digitToInt()]) {
                "L" -> lastLocation = lastLocation.copy(y = lastLocation.y - 1 * step)
                "R" -> lastLocation = lastLocation.copy(y = lastLocation.y + 1 * step)
                "U" -> lastLocation = lastLocation.copy(x = lastLocation.x + 1 * step)
                "D" -> lastLocation = lastLocation.copy(x = lastLocation.x - 1 * step)
            }
            perimeter += step
            coords.add(lastLocation)
        }

        val shoelace = coords.zipWithNext()
            .map {
                Pair(
                    it.first.x * it.second.y,
                    it.first.y * it.second.x
                )
            }.reduce { acc, pair ->
                Pair(acc.first + pair.first, acc.second + pair.second)
            }.let {
                abs(it.first - it.second)
            }.div(2)

        val interiorPoints = shoelace - perimeter / 2 + 1
        return interiorPoints + perimeter
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val lavaDuctLagoon = LavaDuctLagoon(input)
        return lavaDuctLagoon.solvePart1()
    }

    fun part2(input: List<String>): Long {
        val lavaDuctLagoon = LavaDuctLagoon(input)
        return lavaDuctLagoon.solvePart2()
    }


    val testInput = readInput("/day18/Day18_test")
    measureTimeMillis { part1(testInput).println() }
    measureTimeMillis { part2(testInput).println() }

    println("-----")

    val input = readInput("/day18/Day18")
    measureTimeMillis { part1(input).println() }
    measureTimeMillis { part2(input).println() }
}
