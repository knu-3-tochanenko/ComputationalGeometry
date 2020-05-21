import java.util.*

class Vector //constructor
(//2D vector
        val p0: Point2D, //getters
        val p1: Point2D) : Comparable<Vector> {

    //method to calculate area for removing collinear points
    fun calcArea(B: Vector): Double {
        val A = this
        return A.p0.x * (A.p1.y - B.p1.y) + A.p1.x * (B.p1.y - A.p0.y) + B.p1.x * (A.p0.y - A.p1.y)
    }

    //compareto method
    override fun compareTo(B: Vector): Int {
        val A = this
        if (A === B) return 0
        val a1 = A.p1.x - A.p0.x
        val a2 = B.p1.x - A.p0.x
        val b1 = A.p1.y - B.p0.y
        val b2 = B.p1.y - B.p0.y
        val cross = a1 * b2 - b1 * a2 //cross product
        if (cross == 0.0) return 0 //collinear
        else if (cross > 0) return 1 else if (cross < 0) return -1
        return 0
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
            var hull = arrayOfNulls<Point2D>(hullPoints.size)
            hull = hullPoints.toTypedArray()
            return hull
        }

        @JvmStatic
        fun main(args: Array<String>) {
        }
    }

}