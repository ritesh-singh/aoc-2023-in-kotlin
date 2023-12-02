package day02

import println
import readInput

fun main() {

    data class Cube(
        val red: Int = 0,
        val blue: Int = 0,
        val green: Int = 0
    )

    fun part1(input: List<String>): Int {
        val map = mutableMapOf<Int, List<Cube>>()
        input.forEach {
            val bagAndCubes = it.split(":")
            val game = bagAndCubes[0].substringAfter("Game").trim().toInt()
            val cubes: List<Cube> = bagAndCubes[1].split(";")
                .map { it.trim() }
                .map {
                    val cubes = it.split(",").map { it.trim() }
                    var cube = Cube()
                    cubes.forEach {
                        if (it.contains("blue")) {
                            val num = it.filter { it.isDigit() }.toInt()
                            cube = cube.copy(blue = num)
                        }
                        if (it.contains("red")) {
                            val num = it.filter { it.isDigit() }.toInt()
                            cube = cube.copy(red = num)
                        }
                        if (it.contains("green")) {
                            val num = it.filter { it.isDigit() }.toInt()
                            cube = cube.copy(green = num)
                        }
                    }
                    cube
                }

            map[game] = cubes
        }

        var total = 0

        val RED = 12
        val GREEN = 13
        val BLUE = 14

        map.forEach {
            val result = it.value.all {
                it.red <= RED && it.blue <= BLUE && it.green <= GREEN
            }
            if (result) {
                total += it.key
            }
        }

        return total
    }

    fun part2(input: List<String>): Int {
        val map = mutableMapOf<Int, List<Cube>>()
        input.forEach {
            val bagAndCubes = it.split(":")
            val game = bagAndCubes[0].substringAfter("Game").trim().toInt()
            val cubes: List<Cube> = bagAndCubes[1].split(";")
                .map { it.trim() }
                .map {
                    val cubes = it.split(",").map { it.trim() }
                    var cube = Cube()
                    cubes.forEach {
                        if (it.contains("blue")) {
                            val num = it.filter { it.isDigit() }.toInt()
                            cube = cube.copy(blue = num)
                        }
                        if (it.contains("red")) {
                            val num = it.filter { it.isDigit() }.toInt()
                            cube = cube.copy(red = num)
                        }
                        if (it.contains("green")) {
                            val num = it.filter { it.isDigit() }.toInt()
                            cube = cube.copy(green = num)
                        }
                    }
                    cube
                }

            map[game] = cubes
        }

        var total = 0

        val RED = 12
        val GREEN = 13
        val BLUE = 14

        map.forEach {
            var red = it.value.maxOf { it.red }
            var blue = it.value.maxOf { it.blue }
            var green = it.value.maxOf { it.green }

            total += (red * blue * green)
        }

        return total
    }


    val testInput = readInput("/day02/Day02_test")
    part1(testInput).println()
    part2(testInput).println()

    println("-----------------------------------")

    val input = readInput("/day02/Day02")
    part1(input).println()
    part2(input).println()
}
