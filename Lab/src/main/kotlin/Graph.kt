import java.util.*

class Graph(edges: Array<Edge?>?) {

    private val graph: MutableMap<Point2D, Vertex>
    var path: MutableList<Point2D> = ArrayList() //returns all possible paths

    fun dijkstra(startName: Point2D) { //dijkstra algorithm
        val source = graph[startName] //set source
        val q: NavigableSet<Vertex?> = TreeSet() //create treeset
        for (v in graph.values) { //init variables
            v.previous = if (v === source) source else null
            if (v == source)
                v.dist = 0.0
            else
                v.dist = Double.MAX_VALUE
            q.add(v) //add to q
        }
        dijkstra(q)
    }

    private fun dijkstra(q: NavigableSet<Vertex?>) {
        var u: Vertex?
        var v: Vertex
        while (!q.isEmpty()) { //for every vertex
            u = q.pollFirst() // vertex with smallest dist
            if (u!!.dist == Double.MAX_VALUE) break
            for ((key, value) in u.neighbours) {
                v = key!!
                val alternateDist = u.dist + value
                if (alternateDist < v.dist) { //id dist is shorter
                    q.remove(v) //remove and add
                    v.dist = alternateDist
                    v.previous = u
                    q.add(v)
                }
            }
        }
    }

    fun printPath(endName: Point2D) { //graph functions that call vertex methods
        graph[endName]!!.printPath(path)
        println()
    }

    class Edge(
            val v1: Point2D, val v2: Point2D,
            val dist: Double
    ) {

        override fun toString(): String {
            return "($v1,$v2)\n"
        }

        fun getp0(): Point2D { //getters for points
            return v1
        }

        fun getp1(): Point2D {
            return v2
        }

    }

    class Vertex(
            private val point: Point2D
    ) : Comparable<Vertex> {
        val neighbours: MutableMap<Vertex?, Double> = HashMap()

        //getter
        var dist = Double.MAX_VALUE //set initial dist to infinity
        var previous: Vertex? = null

        //prints path and adds to list
        fun printPath(path: MutableList<Point2D>) {
            when {
                this === previous -> {
                    print(point)
                    path.add(point) //path is list in graph
                }
                previous == null -> {
                    print("$point(unreached)")
                }
                else -> {
                    path.add(point) //recursive call
                    previous!!.printPath(path)
                    print(" -> $point")
                }
            }
        }

        override fun compareTo(other: Vertex): Int //compare distances
        {
            return if (dist == other.dist) point.compareTo(other.point) else dist.compareTo(other.dist)
        }

        override fun toString(): String {
            return "($point, $dist)"
        }

    }

    init { //edges array
        graph = HashMap(edges!!.size)
        for (e in edges) {
            if (!graph.containsKey(e!!.v1)) graph[e.v1] = Vertex(e.v1) //checks if vertex already exists
            if (!graph.containsKey(e.v2)) graph[e.v2] = Vertex(e.v2)
        }
        for (e in edges) {
            graph[e!!.v1]!!.neighbours[graph[e.v2]] = e.dist
            graph[e.v2]!!.neighbours[graph[e.v1]] = e.dist
        }
    }
}