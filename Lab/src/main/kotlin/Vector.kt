import java.util.*

class Vector(
        val p0: Point2D,
        val p1: Point2D
) : Comparable<Vector> {

    //method to calculate area for removing collinear points
    fun calcArea(b: Vector): Double {
        val a = this
        return a.p0.x * (a.p1.y - b.p1.y) + a.p1.x * (b.p1.y - a.p0.y) + b.p1.x * (a.p0.y - a.p1.y)
    }

    override fun compareTo(b: Vector): Int {
        val a = this
        if (a === b) return 0
        val a1 = a.p1.x - a.p0.x
        val a2 = b.p1.x - a.p0.x
        val b1 = a.p1.y - b.p0.y
        val b2 = b.p1.y - b.p0.y
        val cross = a1 * b2 - b1 * a2 //cross product
        return when {
            cross == 0.0 -> 0 //collinear
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
        //method to remove collinear points
        @JvmStatic
        fun removeColinear(v: Array<Vector?>): Array<Point2D?> {
            val hullPoints: MutableList<Point2D> = ArrayList() //create hull points
            for (i in v.indices) {
                val area = v[i]!!.calcArea(v[(i + 1) % v.size]!!) //calculate area under 3 points
                if (area < 0.0001) { //if less than threshold
                } else {
                    hullPoints.add(v[i]!!.p1) //add to hull points
                }
            }
            //convert to Point2D array
            val hull: Array<Point2D?>
            hull = hullPoints.toTypedArray()
            return hull
        }

        @JvmStatic
        fun main(args: Array<String>) {
        }
    }

}