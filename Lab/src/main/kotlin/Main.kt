import library.StdDraw
import java.awt.Color
import java.util.*

fun main(args: Array<String>) {

    //start execution timer
    val startTime = System.currentTimeMillis()

    //set canvas size
    StdDraw.setCanvasSize(750, 600)

    //import shape map
    val map = ShapeMap(args[0])

    //array of point2d arrays of polygons
    val convexHulls: Array<Array<Point2D>?> = arrayOfNulls(map.amountofPolys())

    //find convex hull of every polygon on the map
    for (plygon in map.withIndex()) {
        val polygon = Polygon2D(GrahamScan.findConvexHull(plygon.value!!))
        val points = polygon.asPointsArray()
        map.setPolygon(plygon.index, GrahamScan.PreProcess(plygon.value!!))
        StdDraw.setPenColor(Color.RED)
        plygon.value?.draw() //draw polygons
        StdDraw.setPenColor(Color.LIGHT_GRAY)
        polygon.drawFilled() //draw convex hulls
        StdDraw.setPenColor(Color.GRAY)
        plygon.value?.drawFilled()
        convexHulls[plygon.index] = points
        StdDraw.setPenColor(Color.RED)
        plygon.value?.draw()
    }

    //set start and end points
    val startingPoint = map.sourcePoint()
    val endPoint = map.destinationPoint()

    //generate paths in visibility graph
    var paths: List<Graph.Edge> = ArrayList()
    paths = VisibilityGraph.generatePaths(convexHulls, startingPoint, endPoint)

    //convert list to array
    var graph: Array<Graph.Edge?>? = arrayOfNulls(paths.size)
    graph = paths.toTypedArray()

    //run dijkstras on graph
    val g = Graph(graph)
    g.dijkstra(startingPoint!!)
    g.printPath(endPoint!!)

    //get the optimal route as a list
    var route: List<Point2D> = ArrayList()
    route = g.path

    //print graph paths if verbose mode
    StdDraw.setPenColor(Color.BLACK)
    for (i in paths.indices) {
        StdDraw.line(paths[i].getp0().x, paths[i].getp0().y, paths[i].getp1().x, paths[i].getp1().y)
    }

    //indicate start and end points
    StdDraw.setPenColor(Color.BLACK)
    if (startingPoint != null) {
        StdDraw.filledCircle(startingPoint.x, startingPoint.y, 0.004)
    }
    if (endPoint != null) {
        StdDraw.filledCircle(endPoint.x, endPoint.y, 0.004)
    }
    var totalDist = 0.0

    //draw shortest path
    StdDraw.setPenColor(Color.RED)
    StdDraw.setPenRadius(0.010)
    for (i in 1 until route.size) {
        StdDraw.line(route[i - 1].x, route[i - 1].y, route[i].x, route[i].y)
        totalDist += VisibilityGraph.dist(route[i - 1], route[i])
    }

    //stop timer and display execution time
    val endTime = System.currentTimeMillis()
    val totalTime = endTime - startTime
    println("Execution time (ms) = $totalTime")
    println("Length of path found = $totalDist")
}
