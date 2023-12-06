package day06

import println
import readInput
import kotlin.system.measureTimeMillis

fun main() {

    fun part1(lines: List<String>): Int {
        return lines.map { s ->
            s.substringAfter(":")
                .trim()
                .split(" ")
                .filter { it.toIntOrNull() != null }
                .map { it.toInt() }
        }.let {
            it[0].zip(it[1])
        }.map {
            fun startRace(sTime: Int): Int {
                val time = it.first - sTime
                return time * sTime
            }

            var count = 0
            var sTime = 0
            while (sTime <= it.first) {
                val distance = startRace(sTime)
                sTime++

                if (it.second < distance)
                    count++
            }
            count
        }.reduce { acc, i -> acc * i }
    }

    fun part2(lines: List<String>): Long {
        return lines.map { s ->
            s.substringAfter("Time:").trim().filter {
                it.isDigit()
            }.toLong()
        }.let {
            fun startRace(sTime: Long): Long {
                val time = it[0] - sTime
                return time * sTime
            }

            var count = 0L
            var sTime = 0L
            while (sTime <= it[0]) {
                val distance = startRace(sTime)
                sTime++

                if (it[1] < distance)
                    count++
            }
            count
        }
    }


    val input = readInput("/day06/Day06")
    part1(input).println()
    measureTimeMillis {
        part2(input).println()
    }.println()

}
