import java.util.*

class ShapeMap : Iterable<Polygon2D?> {
    private var srcPoint // source point on map
            : Point2D? = null
    private var destPoint // destination point on map
            : Point2D? = null
    private var polygons // polygon data
            : ArrayList<Polygon2D>

    // constructor
    internal constructor() {
        polygons = ArrayList()
    }

    // constructor
    internal constructor(src: Point2D?, dest: Point2D?) {
        polygons = ArrayList()
        srcPoint = Point2D(src)
        destPoint = Point2D(dest)
    }

    // constructor
    internal constructor(pgs: ArrayList<Polygon2D?>, src: Point2D?, dest: Point2D?) {
        // defensive copy
        polygons = ArrayList()
        for (i in pgs.indices) {
            polygons.add(Polygon2D(pgs[i]!!))
        }
        srcPoint = Point2D(src)
        destPoint = Point2D(dest)
    }

    // constructor - from input file data
    internal constructor(fileName: String?) {
        val mapReader = MapFileReader(fileName)
        srcPoint = mapReader.sourcePoint
        destPoint = mapReader.destinationPoint
        polygons = mapReader.parsePolygonData()
    }

    fun addPolygon(pg: Polygon2D) {
        polygons.add(pg)
    }

    fun amountofPolys(): Int {
        return polygons.size
    }

    fun addPolygon(points: Array<Point2D?>) {
        val pg = Polygon2D()
        for (i in points.indices) {
            pg.addPoint(points[i])
        }
        addPolygon(pg)
    }

    fun sourcePoint(): Point2D? {
        return srcPoint
    }

    fun destinationPoint(): Point2D? {
        return destPoint
    }

    fun getPolygon(index: Int): Polygon2D {
        return polygons[index]
    }

    fun setPolygon(index: Int, polygon2D: Polygon2D) {
        polygons[index] = polygon2D
    }

    override fun iterator(): MutableIterator<Polygon2D?> {
        return object : MutableIterator<Polygon2D?> {
            private var currentIndex = 0
            override fun hasNext(): Boolean {
                return currentIndex < polygons.size
            }

            override fun next(): Polygon2D {
                return polygons[currentIndex++]
            }

            override fun remove() {
                throw UnsupportedOperationException()
            }
        }
    }

    fun draw() {
        for (i in polygons.indices) {
            polygons[i].draw()
        }
    }

    fun drawFilled() {
        for (i in polygons.indices) {
            polygons[i].drawFilled()
        }
    }
}