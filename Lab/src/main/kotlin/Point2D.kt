class Point2D : Comparable<Point2D> {
    var x: Double
    var y: Double

    constructor(x: Double, y: Double) {
        this.x = x
        this.y = y
    }

    constructor(p: Point2D?) {
        if (p == null)
            println("Point2D(): null point!")
        x = p!!.x
        y = p.y
    }

    override fun compareTo(other: Point2D): Int {
        val p1 = this
        return when {
            p1.x + p1.y > other.x + other.y -> -1
            p1.x + p1.y < other.x + other.y -> 1
            else -> 0
        }
    }

    override fun toString(): String {
        return "($x,$y)\n"
    }

    companion object {
        @JvmStatic
        fun turningDirection(p0: Point2D, p1: Point2D, p2: Point2D): Double {
            val a1 = p1.x - p0.x
            val a2 = p2.x - p0.x
            val b1 = p1.y - p0.y
            val b2 = p2.y - p0.y
            val z = a1 * b2 - b1 * a2

            return when {
                z < 0 -> 1.0
                z > 0 -> -1.0
                else -> 0.0
            }
        }
    }
}