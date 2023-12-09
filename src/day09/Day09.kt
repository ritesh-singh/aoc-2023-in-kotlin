package day09

import println
import printlnTime
import readInput
import kotlin.system.measureTimeMillis

fun main() {

    fun solve(lines: List<String>, part1: Boolean): Long {
        return lines.map { line ->
            generateSequence(line.split(" ").map { it.trim().toLong() }) {
                it.windowed(2) { it[1] - it[0] }
            }.takeWhile {
                it.any { it != 0L }
            }.map {
                if (part1) it.last() else it.first()
            }.toList().reversed()
                .fold(0L) { acc, l -> if (part1) acc + l else l - acc }
        }.sum()
    }

    val input = readInput("/day09/Day09")
    measureTimeMillis { solve(input, part1 = true).println() }.printlnTime()
    measureTimeMillis { solve(input, part1 = false).println() }.printlnTime()
}
