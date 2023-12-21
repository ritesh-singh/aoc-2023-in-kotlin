package day20

import lcm
import println
import readInput
import java.util.*

private class PulsePropagation(input: List<String>) {
    private enum class PulseType { LOW, HIGH }
    private sealed class Module {
        data class FlipFlop(val name: String, val state: Boolean) : Module()
        data class Conjunction(val name: String, val inputs: List<Pair<String, PulseType>>) : Module()
        data object Broadcaster : Module()
        data object UnNamed : Module()
    }

    private val networkMap = hashMapOf<String, List<String>>()
    private val moduleMap = hashMapOf<String, Module>()

    init {
        input.forEach {
            val (from, to) = it.split("->").map { it.trim() }
            val module = when {
                from == "broadcaster" -> Module.Broadcaster
                from.first() == '%' -> Module.FlipFlop(name = from.drop(1), state = false)
                from.first() == '&' -> Module.Conjunction(name = from.drop(1), inputs = listOf())
                else -> Module.UnNamed
            }
            if (module == Module.Broadcaster) {
                moduleMap[from] = module
                networkMap[from] = to.split(",").map { it.trim() }
            } else {
                moduleMap[from.drop(1)] = module
                networkMap[from.drop(1)] = to.split(",").map { it.trim() }
            }
        }
        moduleMap.filter {
            it.value is Module.Conjunction
        }.forEach { (t, _) ->
            networkMap.filter { it.value.contains(t) }.let {
                moduleMap[t] = Module.Conjunction(name = t, inputs = it.keys.map { Pair(it, PulseType.LOW) })
            }
        }
    }

    private fun pressButton(): Pair<Int,Int> {
        val queue = ArrayDeque<Triple<String, PulseType, Module>>()
        var low = 0
        var high = 0
        networkMap["broadcaster"]!!.forEach {
            queue.addLast(Triple("broadcaster", PulseType.LOW, moduleMap[it]!!))
            low++
        }
        low++ // one for the button
        while (queue.isNotEmpty()) {
            val (from, pulse, to) = queue.removeFirst()
            when (to) {
                is Module.FlipFlop -> {
                    if (pulse == PulseType.HIGH) continue
                    val on = (moduleMap[to.name] as Module.FlipFlop).state
                    moduleMap[to.name] = (moduleMap[to.name] as Module.FlipFlop).copy(state = !on)
                    networkMap[to.name]!!.forEach {
                        if (on) {
                            low++
                            queue.addLast(Triple(to.name, PulseType.LOW, moduleMap[it]!!))
                        } else {
                            high++
                            queue.addLast(Triple(to.name, PulseType.HIGH, moduleMap[it]!!))
                        }
                    }
                }

                is Module.Conjunction -> {
                    val conjuction = (moduleMap[to.name] as Module.Conjunction)
                    val inputs = conjuction.inputs.map {
                        if (it.first == from) Pair(it.first, pulse) else it
                    }
                    moduleMap[to.name] = conjuction.copy(inputs = inputs)
                    val allHigh =
                        (moduleMap[to.name] as Module.Conjunction).inputs.all { it.second == PulseType.HIGH }
                    networkMap[to.name]!!.forEach {
                        if (allHigh) {
                            low++
                            moduleMap[it]?.let { queue.addLast(Triple(to.name, PulseType.LOW, it)) }
                        } else {
                            high++
                            moduleMap[it]?.let { queue.addLast(Triple(to.name, PulseType.HIGH, it)) }
                        }
                    }
                }

                Module.Broadcaster -> error("Broadcast happens only once!")
                Module.UnNamed -> continue
            }
        }
        return Pair(low,high)
    }

    fun solvePart1():Long  {
        var low = 0
        var high = 0
        repeat(1000){
            val pair = pressButton()
            low += pair.first
            high += pair.second
        }
        return low.toLong() * high
    }

    fun solvePart2():Long{
        // https://mermaid.live/edit#pako:eNptlE2PmzAQhv9K5HM2SkoSNqzU0157ak8VF4wxBLBjDEEsUf57Z_B0kJpe0KNhPPPOh_0Q-U0VIhGlz1y1-fX5kVrpb5nKs34o_Obt7ftmbF9tef8fm321GTjru_C7Yuzr1Fod0DD6MrUuDzgzzi6107CgzME3YNMxYrAq-DaSEY95tWDbpNa0CzrHOA2p7YOcpmXEYHW5oG4ZUdkUjimd2iagkYxLQYp9CWtAX3K_CGe5Wh3jODOWKFKvVcz_ovaQuOFshBI1yAU7QOe54tnxHAmxJYReMWL7CCfw1aE2qxkx2xBSuGZFSCGrFwSRtlq7E9DD3Nqgt4Jp5pQYgrWW-zs5RhmmaSUjpmhrljOGllioYgwi25ERe1auwSrFEQglyNEdWwklbKoLInXHiA55zyMkRA19kK5BQ7OuEWEHEWbJM25HVkaIA-hqTkFYgVUHkRY2VdEVmaAPIe6A20cDwE4GDb1dETR0JAc0GMlWQgtl1orlEBpoVB80TI4RqyDEa06IFRNiYkfLtSJef0K8b4TYdUIcbG_WThq-F_m6OzKkaFdEX-1ZOqGBlTOrlbBWYitM4U12VfDQPVK72aRiqApTpCIBVIXO7u2QitQ-wTW7D7efXzYXyeDvxVbcncqG4vOawRNp_hoLdR1u_kd4O5cndCtcZkXyEJNIDvtodzic3-NoH0XH8-UYb8WXSL5FuziKL5f94XI8wvf9uRXz7QZB97v4fD7Gp2N02ceH8-kULeF-Lz8x4_MP4Zm5_Q
        val rxInputMod = "df" // conjuction module
        val dfInputMod = listOf("xl", "ln", "xp", "gp") // all are conjuction modules

        // For rx to get low pulse, df should get high pulse as input
        // For df to get high pulse (xl, ln, xp,gp) - all inputs should get high pulse

        val cycleLength = mutableMapOf("xl" to 0, "ln" to 0, "xp" to 0, "gp" to 0)
        var pressCount = 0

        while (true){
            pressCount++
            val queue = ArrayDeque<Triple<String, PulseType, Module>>()
            networkMap["broadcaster"]!!.forEach { queue.addLast(Triple("broadcaster", PulseType.LOW, moduleMap[it]!!)) }
            while (queue.isNotEmpty()) {
                val (from, pulse, to) = queue.removeFirst()
                when (to) {
                    is Module.FlipFlop -> {
                        if (dfInputMod.contains(from) && to.name == rxInputMod && pulse == PulseType.HIGH) {
                            cycleLength[from] = pressCount
                        }
                        if (pulse == PulseType.HIGH) continue
                        val on = (moduleMap[to.name] as Module.FlipFlop).state
                        moduleMap[to.name] = (moduleMap[to.name] as Module.FlipFlop).copy(state = !on)
                        networkMap[to.name]!!.forEach {
                            if (on) {
                                queue.addLast(Triple(to.name, PulseType.LOW, moduleMap[it]!!))
                            } else {
                                queue.addLast(Triple(to.name, PulseType.HIGH, moduleMap[it]!!))
                            }
                        }
                    }
                    is Module.Conjunction -> {
                        if (dfInputMod.contains(from) && to.name == rxInputMod && pulse == PulseType.HIGH) {
                            cycleLength[from] = pressCount
                        }
                        val conjuction = (moduleMap[to.name] as Module.Conjunction)
                        val inputs = conjuction.inputs.map {
                            if (it.first == from) Pair(it.first, pulse) else it
                        }
                        moduleMap[to.name] = conjuction.copy(inputs = inputs)
                        val allHigh =
                            (moduleMap[to.name] as Module.Conjunction).inputs.all { it.second == PulseType.HIGH }
                        networkMap[to.name]!!.forEach {
                            if (allHigh) {
                                moduleMap[it]?.let { queue.addLast(Triple(to.name, PulseType.LOW, it)) }
                            } else {
                                moduleMap[it]?.let { queue.addLast(Triple(to.name, PulseType.HIGH, it)) }
                            }
                        }
                    }
                    Module.Broadcaster -> error("Broadcast happens only once!")
                    Module.UnNamed -> continue
                }
            }
            if (cycleLength.all { it.value != 0 }) {
                break
            }
        }
        return cycleLength.map {
            it.value.toLong()
        }.reduce(::lcm)
    }

}


fun main() {
    fun part1(input: List<String>): Long {
        val pulsePropagation = PulsePropagation(input)
        return pulsePropagation.solvePart1()
    }

    fun part2(input: List<String>): Long {
        val pulsePropagation = PulsePropagation(input)
        return pulsePropagation.solvePart2()
    }

    val input = readInput("/day20/Day20")
    part1(input).println()
    part2(input).println()
}
