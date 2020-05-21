import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

object VisibilityGraph {
    //method to determine if two vectors intersect each other
    private fun lineOfSight(v1: Vector, v2: Vector): Boolean {
        //v1.getP1() is one of the points making the v1 vector
        //v1.getP0() is the other
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
        //returns true or false (boolean)
        return r > 0 && r < 1 && s > 0 && s < 1
    }

    //method to get the edge points of a convex hull
    private fun getEdges(convexHulls: Array<Array<Point2D>?>): List<Vector> {
        val edges: MutableList<Vector> = ArrayList()
        for (i in convexHulls.indices) {
            var k = 1
            for (j in convexHulls[i]!!.indices) {
                if (k == convexHulls[i]!!.size) {
                    k = 0
                }
                edges.add(Vector(convexHulls[i]!![j], convexHulls[i]!![k]))
                k++
            }
        }
        //returns a List<Vector>
        return edges
    }

    //method to calculate the distance between two points
    fun dist(p1: Point2D?, p2: Point2D): Double {
        //euclidian formula
        return sqrt(x = (p2.x - p1!!.x).pow(2.0) + (p2.y - p1.y).pow(2.0))
    }

    //method that generates the paths on the visibility graph
    fun generatePaths(convexHulls: Array<Array<Point2D>?>, startingPoint: Point2D?, endPoint: Point2D?): List<Graph.Edge> {
        val edges = getEdges(convexHulls) //edges of convex hulls (to check for intersections)
        val lines: MutableList<Vector> = ArrayList() //temp. Vector list
        val paths: MutableList<Graph.Edge> = ArrayList() //list of paths in graph
        var s = 0 //counters
        var intersections: Int
        for (i in convexHulls.indices) { //for each polygon
            for (j in convexHulls[i]!!.indices) { //..for each point in each polygon
                for (k in convexHulls.indices) { //..connected to each polygon
                    for (l in convexHulls[k]!!.indices) { //..to each point in each polygon
                        if (s == 0) {
                            intersections = 0 //reset intersections to 0
                            lines.add(Vector(startingPoint!!, convexHulls[k]!![l])) //add vector from starting point to every other point
                            for (e in edges.indices) { //for every edge on every convex hull
                                if (lineOfSight(lines[lines.size - 1], edges[e])) //if the line intersects any edge
                                    intersections++ //increment intersections (could optimise here)
                            }
                            if (intersections < 1) { //if no intersections
                                //add to path
                                paths.add(Graph.Edge(startingPoint, lines[lines.size - 1].p1, dist(startingPoint, lines[lines.size - 1].p1)))
                            }
                        }
                        if (i != k && s != 0) { //other points (not starting point)
                            intersections = 0
                            lines.add(Vector(convexHulls[i]!![j], convexHulls[k]!![l]))
                            for (e in edges.indices) {
                                if (lineOfSight(lines[lines.size - 1], edges[e])) intersections++
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
        } //end point checks
        for (i in convexHulls.indices) {
            for (element in convexHulls[i]!!) {
                intersections = 0
                lines.add(Vector(endPoint!!, element))
                for (e in edges.indices) {
                    if (lineOfSight(lines[lines.size - 1], edges[e])) intersections++
                }
                if (intersections < 1) {
                    paths.add(Graph.Edge(lines[lines.size - 1].p0, lines[lines.size - 1].p1, dist(lines[lines.size - 1].p0, lines[lines.size - 1].p1)))
                }
            }
        }
        for (i in edges.indices) {
            paths.add(Graph.Edge(edges[i].p0, edges[i].p1, dist(edges[i].p0, edges[i].p1)))
        }
        return paths //return list of paths in graph
    }
}