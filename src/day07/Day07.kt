package day07

import println
import readInput
import kotlin.system.measureTimeMillis

private enum class TYPE(val value: Int) {
    FIVE_KIND(7),
    FOUR_KIND(6),
    FULL_HOUSE(5),
    THREE_KIND(4),
    TWO_PAIR(3),
    ONE_PAIR(2),
    HIGH_CARD(1)
}

private data class CamelCard(
    val card: String,
    val type: TYPE,
    val bid: Int
)

fun main() {

    fun getTypePart1(card: String): TYPE {
        val map = mutableMapOf<Char, Int>()
        card.forEach { map[it] = map.getOrDefault(it, 0) + 1 }
        return when(map.size){
            1 -> TYPE.FIVE_KIND
            2 -> if (map.any { it.value == 4 }) TYPE.FOUR_KIND else TYPE.FULL_HOUSE
            3 -> if (map.any { it.value == 3 }) TYPE.THREE_KIND else TYPE.TWO_PAIR
            4 -> TYPE.ONE_PAIR
            5 -> TYPE.HIGH_CARD
            else -> error("Unknown type")
        }
    }

    fun getTypePart2(card: String): TYPE {
        val map = mutableMapOf<Char, Int>()
        card.forEach { map[it] = map.getOrDefault(it, 0) + 1 }
        return when(map.size){
            1 -> TYPE.FIVE_KIND
            2 -> {
                if (map.any { it.value == 4 }) {
                    if (map.containsKey('J')) TYPE.FIVE_KIND else TYPE.FOUR_KIND
                } else {
                    if (map.containsKey('J')) TYPE.FIVE_KIND else TYPE.FULL_HOUSE
                }
            }
            3 -> {
                if (map.any { it.value == 3 }) {
                    if (map.containsKey('J')) TYPE.FOUR_KIND else TYPE.THREE_KIND
                } else {
                    if (map.containsKey('J')) {
                        if (map['J'] == 1) TYPE.FULL_HOUSE else TYPE.FOUR_KIND
                    } else {
                        TYPE.TWO_PAIR
                    }
                }
            }
            4 -> if (map.containsKey('J')) TYPE.THREE_KIND else TYPE.ONE_PAIR
            5 -> if (map.containsKey('J')) TYPE.ONE_PAIR else TYPE.HIGH_CARD
            else -> error("Unknown type")
        }
    }

    fun sortRankOfSameType(cards: List<CamelCard>, part1: Boolean): List<CamelCard> {
        val strengthMap = mapOf(
            'A' to 22, 'K' to 21, 'Q' to 20, 'J' to if (part1) 19 else 9,
            'T' to 18, '9' to 17, '8' to 16, '7' to 15,
            '6' to 14, '5' to 13, '4' to 12, '3' to 11,
            '2' to 10
        )
        return cards.sortedWith(Comparator { o1, o2 ->
            for (i in 0..4) {
                val c1 = strengthMap[o1.card[i]] ?: -1
                val c2 = strengthMap[o2.card[i]] ?: -1
                if (c1 > c2) return@Comparator -1
                if (c1 < c2) return@Comparator 1
            }
            return@Comparator 0
        })
    }

    fun solve(lines: List<String>, part1: Boolean = true): Int {
        val strongestRank = lines.size
        return lines.map {
            val (card, bid) = it.split(" ").map { it.trim() }
            CamelCard(
                card = it,
                type = if (part1) getTypePart1(card) else getTypePart2(card),
                bid = bid.toInt()
            )
        }.groupBy {
            it.type
        }.toSortedMap(compareByDescending { it.value })
            .let {
                var currentRank = 0
                var result = 0
                it.onEachIndexed { index, entry ->
                    if (index == 0) {
                        currentRank = strongestRank
                    }
                    if (entry.value.size == 1) {
                        result += currentRank * entry.value.first().bid
                        currentRank--
                    } else {
                        val sortedList = sortRankOfSameType(entry.value, part1)
                        sortedList.forEach {
                            result += currentRank * it.bid
                            currentRank--
                        }
                    }
                }
                result
            }
    }

    val input = readInput("/day07/Day07")
    measureTimeMillis { solve(input, part1 = true).println() }
    measureTimeMillis { solve(input, part1 = false).println() }
}
