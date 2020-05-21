import java.util.*

class Vector(
        val p0: Point2D,
        val p1: Point2D
) : Comparable<Vector> {

    fun calcArea(b: Vector): Double {
        val a = this
        return (a.p0.x * (a.p1.y - b.p1.y)
                + a.p1.x * (b.p1.y - a.p0.y)
                + b.p1.x * (a.p0.y - a.p1.y)
                )
    }

    override fun compareTo(other: Vector): Int {
        val a = this
        if (a === other) return 0
        val a1 = a.p1.x - a.p0.x
        val a2 = other.p1.x - a.p0.x
        val b1 = a.p1.y - other.p0.y
        val b2 = other.p1.y - other.p0.y
        val cross = a1 * b2 - b1 * a2
        return when {
            cross == 0.0 -> 0 // Collinear
            cross > 0 -> 1
            cross < 0 -> -1
            else -> 0
        }
    }

    override fun toString(): String {
        return """
            
            Edge Point 1: (${p0.x},${p0.y})	Edge Point 2: (${p1.x},${p1.y})
            """.trimIndent()
    }

    companion object {
        @JvmStatic
        fun removeCollinear(v: Array<Vector?>): Array<Point2D?> {
            val hullPoints: MutableList<Point2D> = ArrayList()
            for (i in v.indices) {
                val area = v[i]!!.calcArea(v[(i + 1) % v.size]!!)
                if (area >= 0.0001) {
                    hullPoints.add(v[i]!!.p1)
                }
            }

            val hull: Array<Point2D?>
            hull = hullPoints.toTypedArray()
            return hull
        }

        @JvmStatic
        fun main(args: Array<String>) {
        }
    }

}