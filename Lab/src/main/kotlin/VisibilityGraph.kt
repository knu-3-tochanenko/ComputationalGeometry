import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

object VisibilityGraph {

    private fun lineOfSight(v1: Vector, v2: Vector): Boolean {
        val denominator = (v1.p1.x - v1.p0.x) * (v2.p1.y - v2.p0.y) - (v1.p1.y - v1.p0.y) * (v2.p1.x - v2.p0.x)
        if (denominator == 0.0) {
            return false
        }

        val numerator1 = (v1.p0.y - v2.p0.y) * (v2.p1.x - v2.p0.x) - (v1.p0.x - v2.p0.x) * (v2.p1.y - v2.p0.y)
        val numerator2 = (v1.p0.y - v2.p0.y) * (v1.p1.x - v1.p0.x) - (v1.p0.x - v2.p0.x) * (v1.p1.y - v1.p0.y)

        if (numerator1 == 0.0 || numerator2 == 0.0) {
            return false
        }
        val r = numerator1 / denominator
        val s = numerator2 / denominator

        return r > 0 && r < 1 && s > 0 && s < 1
    }

    private fun getEdges(convexHulls: Array<Array<Point2D>?>): List<Vector> {
        val edges: MutableList<Vector> = ArrayList()

        for (i in convexHulls.indices) {
            var k = 1

            for (j in convexHulls[i]!!.indices) {
                if (k == convexHulls[i]!!.size)
                    k = 0
                edges.add(Vector(convexHulls[i]!![j], convexHulls[i]!![k]))
                k++
            }
        }

        return edges
    }

    fun dist(p1: Point2D?, p2: Point2D): Double {
        return sqrt(x = (p2.x - p1!!.x).pow(2.0) + (p2.y - p1.y).pow(2.0))
    }

    fun generatePaths(
            convexHulls: Array<Array<Point2D>?>,
            startingPoint: Point2D?,
            endPoint: Point2D?
    ): List<Graph.Edge> {
        val edges = getEdges(convexHulls)
        val lines: MutableList<Vector> = ArrayList()
        val paths: MutableList<Graph.Edge> = ArrayList()
        var s = 0
        var intersections: Int

        for (i in convexHulls.indices) { // .. each polygon
            for (j in convexHulls[i]!!.indices) { //.. each point in each polygon
                for (k in convexHulls.indices) { //.. connected to each polygon
                    for (l in convexHulls[k]!!.indices) { //.. each point in each polygon
                        if (s == 0) {
                            intersections = 0
                            lines.add(Vector(startingPoint!!, convexHulls[k]!![l]))

                            // Every edge on every convex hull
                            for (e in edges.indices) {
                                if (lineOfSight(lines[lines.size - 1], edges[e]))
                                    intersections++
                            }
                            if (intersections < 1) {
                                paths.add(Graph.Edge(
                                        startingPoint,
                                        lines[lines.size - 1].p1,
                                        dist(startingPoint, lines[lines.size - 1].p1)
                                ))
                            }
                        }

                        if (i != k && s != 0) {
                            intersections = 0
                            lines.add(Vector(convexHulls[i]!![j], convexHulls[k]!![l]))
                            for (e in edges.indices) {
                                if (lineOfSight(lines[lines.size - 1], edges[e]))
                                    intersections++
                            }
                            if (intersections < 1) {
                                paths.add(Graph.Edge(
                                        lines[lines.size - 1].p0,
                                        lines[lines.size - 1].p1,
                                        dist(lines[lines.size - 1].p0, lines[lines.size - 1].p1)
                                ))
                            }
                        }
                    }
                }
                s++
            }
        }

        for (i in convexHulls.indices) {
            for (element in convexHulls[i]!!) {
                intersections = 0
                lines.add(Vector(endPoint!!, element))
                for (e in edges.indices) {
                    if (lineOfSight(lines[lines.size - 1], edges[e]))
                        intersections++
                }
                if (intersections < 1) {
                    paths.add(Graph.Edge(
                            lines[lines.size - 1].p0,
                            lines[lines.size - 1].p1,
                            dist(lines[lines.size - 1].p0, lines[lines.size - 1].p1)
                    ))
                }
            }
        }

        for (i in edges.indices) {
            paths.add(Graph.Edge(edges[i].p0, edges[i].p1, dist(edges[i].p0, edges[i].p1)))
        }

        return paths
    }
}