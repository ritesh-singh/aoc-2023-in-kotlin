package day02

import println
import readInput

private data class Cube(
    val red: Int = 0,
    val blue: Int = 0,
    val green: Int = 0
)

private fun List<String>.parse(): Map<Int, List<Cube>> {
    return this.map { it.split(":").map { it.trim() } }
        .associateBy({
            it[0].substringAfter("Game").trim().toInt()
        }, {
            it[1].split(";")
                .map { cubes -> cubes.split(",").map { it.trim() } }
                .map { cubes ->
                    var cube = Cube()
                    cubes.forEach {
                        when {
                            it.contains("blue") -> cube = cube.copy(blue = it.filter { it.isDigit() }.toInt())
                            it.contains("red") -> cube = cube.copy(red = it.filter { it.isDigit() }.toInt())
                            it.contains("green") -> cube = cube.copy(green = it.filter { it.isDigit() }.toInt())
                            else -> {}
                        }
                    }
                    cube
                }
        })
}

fun main() {

    fun part1(input: List<String>): Int {
        return input.parse()
            .filter {
                it.value.all { it.red <= 12 && it.blue <= 14 && it.green <= 13 }
            }.map { it.key }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.parse()
            .map {
                it.value.maxOf { it.red } * it.value.maxOf { it.blue } * it.value.maxOf { it.green }
            }.sum()
    }

    val input = readInput("/day02/Day02")
    part1(input).println()
    part2(input).println()
}
