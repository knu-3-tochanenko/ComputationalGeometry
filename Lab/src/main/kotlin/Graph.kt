import java.util.*

class Graph(edges: Array<Edge?>?) {

    private val graph: MutableMap<Point2D, Vertex>
    var path: MutableList<Point2D> = ArrayList()

    fun dijkstra(startName: Point2D) {
        val source = graph[startName]
        val q: NavigableSet<Vertex?> = TreeSet()

        for (v in graph.values) {
            v.previous = if (v === source) source else null
            if (v == source)
                v.dist = 0.0
            else
                v.dist = Double.MAX_VALUE
            q.add(v)
        }

        dijkstra(q)
    }

    private fun dijkstra(q: NavigableSet<Vertex?>) {
        var u: Vertex?
        var v: Vertex

        while (!q.isEmpty()) {
            u = q.pollFirst()
            if (u!!.dist == Double.MAX_VALUE)
                break

            for ((key, value) in u.neighbours) {
                v = key!!
                val alternateDist = u.dist + value
                if (alternateDist < v.dist) {
                    q.remove(v)
                    v.dist = alternateDist
                    v.previous = u
                    q.add(v)
                }
            }
        }
    }

    fun printPath(endName: Point2D) {
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

        fun getP0(): Point2D {
            return v1
        }

        fun getP1(): Point2D {
            return v2
        }

    }

    class Vertex(
            private val point: Point2D
    ) : Comparable<Vertex> {

        val neighbours: MutableMap<Vertex?, Double> = HashMap()
        var dist = Double.MAX_VALUE
        var previous: Vertex? = null


        fun printPath(path: MutableList<Point2D>) {
            when {
                this === previous -> {
                    print(point)
                    path.add(point)
                }
                previous == null -> {
                    print("$point(unreached)")
                }
                else -> {
                    path.add(point)
                    previous!!.printPath(path)
                    print(" -> $point")
                }
            }
        }

        override fun compareTo(other: Vertex): Int {
            return if (dist == other.dist)
                point.compareTo(other.point)
            else dist.compareTo(other.dist)
        }

        override fun toString(): String {
            return "($point, $dist)"
        }

    }

    init {
        graph = HashMap(edges!!.size)
        for (e in edges) {
            if (!graph.containsKey(e!!.v1)) graph[e.v1] = Vertex(e.v1)
            if (!graph.containsKey(e.v2)) graph[e.v2] = Vertex(e.v2)
        }
        for (e in edges) {
            graph[e!!.v1]!!.neighbours[graph[e.v2]] = e.dist
            graph[e.v2]!!.neighbours[graph[e.v1]] = e.dist
        }
    }
}