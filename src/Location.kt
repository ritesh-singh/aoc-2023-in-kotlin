data class Position2D(
    val x: Int,
    val y: Int
)


data class Position2DLong(
    val x: Long,
    val y: Long
)

fun Position2D.up() = copy(x = x - 1)
fun Position2D.down() = copy(x = x + 1)
fun Position2D.left() = copy(y = y - 1)
fun Position2D.right() = copy(y = y + 1)

fun Position2D.north() = copy(x = x - 1)
fun Position2D.south() = copy(x = x + 1)
fun Position2D.east() = copy(y = y + 1)
fun Position2D.west() = copy(y = y - 1)


/**
 *
 *      N
 *   W      E
 *      S
 *
 */
enum class Direction {
    N, S, W, E
}

enum class DirectionLRUP {
    L, R, U, D
}