data class Position2D(
    val x: Int,
    val y: Int
)

fun Position2D.up() = copy(x = x - 1)
fun Position2D.down() = copy(x = x + 1)
fun Position2D.left() = copy(y = y - 1)
fun Position2D.right() = copy(y = y + 1)