import library.StdDraw
import java.util.*

class Polygon2D {
    private var vertices: Array<Point2D?>

    constructor() {
        vertices = arrayOfNulls(0)
    }

    constructor(points: Array<Point2D?>) {
        vertices = arrayOfNulls(points.size)

        for (i in points.indices) {
            vertices[i] = Point2D(points[i])
        }
    }

    constructor(poly: Polygon2D) {
        vertices = arrayOfNulls(poly.vertices.size)

        for (i in poly.vertices.indices) {
            vertices[i] = Point2D(poly.vertices[i])
        }
    }

    fun addPoint(p: Point2D?) {
        vertices = vertices.copyOf(vertices.size + 1)
        vertices[vertices.size - 1] = Point2D(p)
    }

    fun size(): Int {
        return vertices.size
    }

    fun asPointsArray(): Array<Point2D> {
        return Arrays.copyOf(vertices, vertices.size)
    }

    fun asPointsArrayNull(): Array<Point2D?> {
        return vertices.copyOf(vertices.size)
    }

    fun draw() {
        for (i in vertices.indices) {
            if (i < vertices.size - 1)
                StdDraw.line(
                        vertices[i]!!.x,
                        vertices[i]!!.y,
                        vertices[i + 1]!!.x,
                        vertices[i + 1]!!.y
                )
            else
                StdDraw.line(
                        vertices[i]!!.x,
                        vertices[i]!!.y,
                        vertices[0]!!.x,
                        vertices[0]!!.y
                )
        }
    }

    fun drawFilled() {
        val x = DoubleArray(vertices.size)
        val y = DoubleArray(vertices.size)
        for (i in vertices.indices) {
            x[i] = vertices[i]!!.x
            y[i] = vertices[i]!!.y
        }
        StdDraw.filledPolygon(x, y)
    }

    override fun toString(): String {
        var polyAsString = ""
        for (i in vertices.indices)
            polyAsString += vertices[i].toString() + " "
        return polyAsString
    }
}