import library.StdDraw
import java.awt.Color
import java.util.*

class Polygon2D {
    private var vertices: Array<Point2D?>

    constructor() {  // default constructor
        vertices = arrayOfNulls(0)
    }

    constructor(points: Array<Point2D?>) {  // constructor
        vertices = arrayOfNulls(points.size)
        for (i in points.indices) {
            vertices[i] = Point2D(points[i])
        }
    }

    constructor(poly: Polygon2D) {  // copy constructor
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
            if (i < vertices.size - 1) StdDraw.line(vertices[i]!!.x, vertices[i]!!.y, vertices[i + 1]!!.x, vertices[i + 1]!!.y) else StdDraw.line(vertices[i]!!.x, vertices[i]!!.y, vertices[0]!!.x, vertices[0]!!.y)
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
        for (i in vertices.indices) polyAsString += vertices[i].toString() + " "
        return polyAsString
    }

    companion object {
        // TEST CLIENT //
        @JvmStatic
        fun main(args: Array<String>) {
            val shape = arrayOfNulls<Point2D>(4)
            shape[0] = Point2D(0.2, 0.2)
            shape[1] = Point2D(0.2, 0.8)
            shape[2] = Point2D(0.8, 0.8)
            shape[3] = Point2D(0.8, 0.2)
            val pg = Polygon2D(shape)
            StdDraw.setCanvasSize(800, 800)
            StdDraw.setPenRadius(0.005)
            StdDraw.setPenColor(Color.RED)
            pg.draw()
        }
    }
}