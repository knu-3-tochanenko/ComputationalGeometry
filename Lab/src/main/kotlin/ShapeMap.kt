import java.util.*

class ShapeMap
internal constructor(fileName: String?) : Iterable<Polygon2D?> {
    private var srcPoint: Point2D? = null
    private var destPoint: Point2D? = null
    private var polygons: ArrayList<Polygon2D>

    init {
        val mapReader = MapFileReader(fileName!!)
        srcPoint = mapReader.sourcePoint
        destPoint = mapReader.destinationPoint
        polygons = mapReader.parsePolygonData()!!
    }

    fun amountOfPolys(): Int {
        return polygons.size
    }

    fun sourcePoint(): Point2D? {
        return srcPoint
    }

    fun destinationPoint(): Point2D? {
        return destPoint
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
}