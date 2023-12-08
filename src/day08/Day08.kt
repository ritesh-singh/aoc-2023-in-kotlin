package day08

import lcm
import println
import printlnTime
import readInput
import kotlin.system.measureTimeMillis

fun main() {

    fun List<String>.networkMap() = this.drop(2).associate {
        val (node, rs) = it.split("=").map { it.trim() }
        node to rs.replace("(", "").replace(")", "")
            .split(",").map { it.trim() }
    }

    fun part1(lines: List<String>): Int {
        return lines.networkMap().let { networkMap ->
            val instruction = lines[0]
            var steps = 0
            var currentPos = "AAA"
            while (currentPos != "ZZZ") {
                val dir = instruction[(steps % instruction.length)]
                currentPos = if (dir == 'L') {
                    networkMap[currentPos]!![0]
                } else {
                    networkMap[currentPos]!![1]
                }
                steps++
            }
            steps
        }
    }

    fun part2(lines: List<String>): Long {
        return lines.networkMap().let { networkMap ->
            networkMap.keys.filter { it.endsWith("A") }
                .map {
                    val instruction = lines[0]
                    var currPos = it
                    var steps = 0L
                    while (!currPos.endsWith("Z")) {
                        val dir = instruction[((steps % instruction.length).toInt())]
                        currPos = if (dir == 'L') {
                            networkMap[currPos]!![0]
                        } else {
                            networkMap[currPos]!![1]
                        }
                        steps++
                    }
                    steps
                }.reduce(::lcm)
        }
    }

    val input = readInput("/day08/Day08")
    measureTimeMillis { part1(input).println() }.printlnTime()
    measureTimeMillis { part2(input).println() }.printlnTime()
}
