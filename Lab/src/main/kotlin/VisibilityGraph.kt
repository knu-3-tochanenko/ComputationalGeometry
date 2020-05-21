import java.util.*

object VisibilityGraph {
    //method to determine if two vectors intersect each other
    fun lineOfSight(v1: Vector, v2: Vector): Boolean {
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
    fun getEdges(convexHulls: Array<Array<Point2D>?>): List<Vector> {
        val edges: MutableList<Vector> = ArrayList()
        for (i in convexHulls.indices) {
            var k = 1
            for (j in 0 until convexHulls[i]!!.size) {
                if (k == convexHulls[i]!!.size) {
                    k = 0
                }
                edges.add(Vector(convexHulls[i]!!.get(j), convexHulls[i]!!.get(k)))
                k++
            }
        }
        //returns a List<Vector>
        return edges
    }

    //method to calculate the distance between two points
    fun dist(p1: Point2D?, p2: Point2D): Double {
        //euclidian formula
        return Math.sqrt(Math.pow(p2.x - p1!!.x, 2.0) + Math.pow(p2.y - p1.y, 2.0))
    }

    //method that gets the index of an array at a certain value (works as vertices are unique i.e map/dictionary)
    fun indexOfArray(array: Array<Point2D>, key: Point2D): Int {
        var returnvalue = -1
        for (i in array.indices) {
            //check key and array value
            if (key === array[i]) {
                returnvalue = i
                break
            }
        }
        return returnvalue
    }

    //method to append two arrays
    fun <T> concat(first: Array<T>, second: Array<T>): Array<T> {
        val result = Arrays.copyOf(first, first.size + second.size)
        System.arraycopy(second, 0, result, first.size, second.size)
        return result
    }

    //method that generates the paths on the visibility graph
    fun generatePaths(convexHulls: Array<Array<Point2D>?>, startingPoint: Point2D?, endPoint: Point2D?): List<Graph.Edge> {
        val edges = getEdges(convexHulls) //edges of convex hulls (to check for intersections)
        val lines: MutableList<Vector> = ArrayList() //temp. Vector list
        val paths: MutableList<Graph.Edge> = ArrayList() //list of paths in graph
        var s = 0 //counters
        var intersections = 0
        for (i in convexHulls.indices) { //for each polygon
            for (j in 0 until convexHulls[i]!!.size) { //..for each point in each polygon
                for (k in convexHulls.indices) { //..connected to each polygon
                    for (l in 0 until convexHulls[k]!!.size) { //..to each point in each polygon
                        if (s == 0) {
                            intersections = 0 //reset intersections to 0
                            lines.add(Vector(startingPoint!!, convexHulls[k]!![l])) //add vector from starting point to every other point
                            for (e in edges.indices) { //for every edge on every convex hull
                                if (lineOfSight(lines[lines.size - 1], edges[e]) == true) //if the line intersects any edge
                                    intersections++ //increment intersections (could optimise here)
                            }
                            if (intersections < 1) { //if no intersections
                                //add to path
                                paths.add(Graph.Edge(startingPoint, lines[lines.size - 1].p1, dist(startingPoint, lines[lines.size - 1].p1)))
                            }
                        }
                        if (i != k && s != 0) { //other points (not starting point)
                            intersections = 0
                            lines.add(Vector(convexHulls[i]!!.get(j), convexHulls[k]!!.get(l)))
                            for (e in edges.indices) {
                                if (lineOfSight(lines[lines.size - 1], edges[e]) == true) intersections++
                            }
                            if (intersections < 1) {
                                paths.add(Graph.Edge(lines[lines.size - 1].p0, lines[lines.size - 1].p1, dist(lines[lines.size - 1].p0, lines[lines.size - 1].p1)))
                            }
                        }
                    }
                }
                s++
            }
        } //end point checks
        for (i in convexHulls.indices) {
            for (j in 0 until convexHulls[i]!!.size) {
                intersections = 0
                lines.add(Vector(endPoint!!, convexHulls[i]!![j]))
                for (e in edges.indices) {
                    if (lineOfSight(lines[lines.size - 1], edges[e]) == true) intersections++
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

    @JvmStatic
    fun main(args: Array<String>) {
    }
}