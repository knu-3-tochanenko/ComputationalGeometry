import library.StdDraw
import java.awt.Color

class PathVisualize {
    companion object {

        private var startingPoint: Point2D? = null
        private var endPoint: Point2D? = null
        private var totalDist: Double = 0.0

        private lateinit var paths: List<Graph.Edge>
        private lateinit var route: List<Point2D>
        private lateinit var map: ShapeMap
        private lateinit var convexHulls: Array<Array<Point2D>?>


        fun visualize(fileName: String, verbose: Boolean = false, timer: Boolean = false) {
            //start execution timer
            val startTime = System.currentTimeMillis()

            //set canvas size
            StdDraw.setCanvasSize(750, 600)

            map = ShapeMap(fileName)
            convexHulls = arrayOfNulls(map.amountOfPolys())

            drawPolygons()

            startingPoint = map.sourcePoint()
            endPoint = map.destinationPoint()

            paths = VisibilityGraph.generatePaths(convexHulls, startingPoint, endPoint)

            val graph: Array<Graph.Edge?>?
            graph = paths.toTypedArray()

            val g = Graph(graph)
            g.dijkstra(startingPoint!!)
            g.printPath(endPoint!!)
            route = g.path

            drawLines(verbose)

            val endTime = System.currentTimeMillis()
            val totalTime = endTime - startTime

            if (timer)
                println("Execution time (ms) = $totalTime")
            println("Length of path found = $totalDist")
        }

        private fun drawPolygons() {
            for (polygon_ in map.withIndex()) {
                val polygon = Polygon2D(GrahamScan.findConvexHull(polygon_.value!!))
                val points = polygon.asPointsArray()

                map.setPolygon(polygon_.index, GrahamScan.preProcess(polygon_.value!!))

                StdDraw.setPenColor(Color.BLUE)
                polygon_.value?.draw()

                StdDraw.setPenColor(Color.LIGHT_GRAY)
                polygon.drawFilled()

                StdDraw.setPenColor(Color.GRAY)
                polygon_.value?.drawFilled()
                convexHulls[polygon_.index] = points

                StdDraw.setPenColor(Color.BLUE)
                polygon_.value?.draw()
            }
        }

        private fun drawLines(verbose: Boolean) {
            if (verbose) {
                StdDraw.setPenColor(Color.GRAY)
                for (i in paths.indices) {
                    StdDraw.line(
                            paths[i].getP0().x,
                            paths[i].getP0().y,
                            paths[i].getP1().x,
                            paths[i].getP1().y
                    )
                }
            }

            StdDraw.setPenColor(Color.BLACK)
            StdDraw.filledCircle(startingPoint!!.x, startingPoint!!.y, 0.004)
            StdDraw.filledCircle(endPoint!!.x, endPoint!!.y, 0.004)
            totalDist = 0.0

            StdDraw.setPenColor(Color.BLUE)
            StdDraw.setPenRadius(0.010)
            for (i in 1 until route.size) {
                StdDraw.line(route[i - 1].x, route[i - 1].y, route[i].x, route[i].y)
                totalDist += VisibilityGraph.dist(route[i - 1], route[i])
            }
        }
    }
}