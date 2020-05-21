import Point2D.Companion.turningDirection
import Vector.Companion.removeColinear
import library.StdDraw
import java.awt.Color
import java.util.*

object GrahamScan {
    private fun mergeSort(a: Array<Vector?>) {
        val tmp = arrayOfNulls<Vector?>(a.size)
        mergeSort(a, tmp, 0, a.size - 1)
    }

    private fun mergeSort(a: Array<Vector?>, tmp: Array<Vector?>, left: Int, right: Int) {
        if (left < right) {
            val center = (left + right) / 2
            mergeSort(a, tmp, left, center)
            mergeSort(a, tmp, center + 1, right)
            merge(a, tmp, left, center + 1, right)
        }
    }

    private fun merge(a: Array<Vector?>, tmp: Array<Vector?>, left_: Int, right_: Int, rightEnd_: Int) {
        var left = left_
        var right = right_
        var rightEnd = rightEnd_
        val leftEnd = right - 1
        var k = left
        val num = rightEnd - left + 1
        while (left <= leftEnd && right <= rightEnd) if (a[left]!! <= a[right]!!) tmp[k++] = a[left++] else tmp[k++] = a[right++]
        while (left <= leftEnd) // Copy rest of first half
            tmp[k++] = a[left++]
        while (right <= rightEnd) // Copy rest of right half
            tmp[k++] = a[right++]

        // Copy tmp back
        var i = 0
        while (i < num) {
            a[rightEnd] = tmp[rightEnd]
            i++
            rightEnd--
        }
    }

    //method to check if a point is within a quad
    private fun pointIn(compPoint: Point2D, points: Array<Array<Point2D?>>): Boolean {
        var b = false
        for (i in points.indices) {
            for (j in points[i].indices) {
                var k = j + 1
                if (k == points[i].size) {
                    k = 0
                }
                if (points[i][j]!!.y < compPoint.y && points[i][k]!!.y >= compPoint.y || points[i][k]!!.y < compPoint.y && points[i][j]!!.y >= compPoint.y) {
                    if (points[i][j]!!.x + (compPoint.y - points[i][j]!!.y) / (points[i][k]!!.y - points[i][j]!!.y) * (points[i][k]!!.x - points[i][j]!!.x) < compPoint.x) {
                        b = !b
                    }
                }
            }
        }
        return b
    }

    //method to preprocess data
    fun preProcess(polygon_: Polygon2D): Polygon2D {
        var polygon = polygon_
        val points: Array<Point2D?>
        points = polygon.asPointsArrayNull()
        //declare points
        val maxX = points[0]!!.x
        val maxY = points[0]!!.y
        val minX = points[0]!!.x
        val minY = points[0]!!.y
        var maxXp = points[0]
        var maxYp = points[0]
        var minXp = points[0]
        var minYp = points[0]

        //check if there is a higher/lower point
        for (i in 1 until points.size) {
            if (points[i]!!.x > maxX) {
                maxXp = points[i]
            }
            if (points[i]!!.y > maxY) {
                maxYp = points[i]
            }
            if (points[i]!!.x < minX) {
                minXp = points[i]
            }
            if (points[i]!!.y < minY) {
                minYp = points[i]
            }
        }
        //create quad with points
        val quad = Array(2) { arrayOfNulls<Point2D>(2) }
        quad[1][1] = maxXp
        quad[0][1] = maxYp
        quad[1][0] = minXp
        quad[0][0] = minYp

        //check if any points are within quad
        val p: MutableList<Point2D?> = ArrayList()
        for (i in points.indices) {
            if (!pointIn(points[i]!!, quad)) {
                p.add(points[i])
            }
        }
        if (!p.contains(minXp)) {
            p.add(maxXp)
        }
        if (!p.contains(minXp)) {
            p.add(maxYp)
        }
        if (!p.contains(minXp)) {
            p.add(minXp)
        }
        if (!p.contains(minXp)) {
            p.add(minYp)
        }
        //convert to polygon and return
        val newPoints: Array<Point2D?>?
        newPoints = p.toTypedArray()
        polygon = Polygon2D(newPoints)
        return polygon
    }

    fun findConvexHull(polygon_: Polygon2D): Polygon2D {
        var polygon = polygon_
        val points: Array<Point2D?>
        points = polygon.asPointsArrayNull()
        val lowest = lowestPoint(points) //get lowest hull point
        val vecs = arrayOfNulls<Vector>(points.size)
        for (i in points.indices) {
            vecs[i] = Vector(lowest!!, points[i]!!)
        }

        //sort by polar angle
        mergeSort(vecs)
        val hull = HullStack<Point2D>() //create hull stack
        hull.push(vecs[0]!!.p1) //add two initial points to hull
        hull.push(vecs[1]!!.p1)
        for (i in 2 until points.size) {
            while (turningDirection(hull.sneakyPeek()!!, hull.peek()!!, vecs[i]!!.p1) == -1.0) { //check if right/left turn
                hull.pop() //pop from stack
            }
            hull.push(vecs[i]!!.p1) //push onto stack
        }
        val size = hull.size()
        val hullPoints = arrayOfNulls<Point2D>(size)
        for (i in 0 until size) {
            hullPoints[i] = hull.pop()
        }
        val testArray = arrayOfNulls<Vector>(hullPoints.size)
        for (i in hullPoints.indices) {
            testArray[i] = Vector(hullPoints[i]!!, hullPoints[(i + 1) % hullPoints.size]!!)
            //StdDraw.line(testArray[i].getP0().getX(), testArray[i].getP0().getY(), testArray[i].getP1().getX(), testArray[i].getP1().getY());
        }
        StdDraw.setPenColor(Color.RED)
        val test = removeColinear(testArray) //remove collinear points
        val h = arrayOfNulls<Point2D>(test.size)
        for (i in test.indices) {
            h[i] = test[i]
            StdDraw.setPenColor(Color.BLACK)
            StdDraw.filledCircle(h[i]!!.x, h[i]!!.y, 0.004)
        }
        //polygon = new Polygon2D(hullPoints);
        polygon = Polygon2D(test)
        return polygon
    }

    //method to get lowest hull point
    private fun lowestPoint(points: Array<Point2D?>): Point2D? {
        var lowest = points[0]
        for (i in 0 until points.size - 1) {
            if (points[i]!!.y <= lowest!!.y) {
                if (points[i]!!.y == lowest.y && points[i]!!.x < lowest.x) {
                    lowest = points[i]
                } else if (points[i]!!.y != lowest.y) {
                    lowest = points[i]
                }
            }
        }
        swapLowest(points, lowest)
        return lowest
    }

    //helper method for lowestPoints()
    private fun swapLowest(points: Array<Point2D?>, lowest: Point2D?) {
        var temp: Point2D?
        for (i in 0 until points.size - 1) {
            if (points[i] == lowest) {
                temp = points[0]
                points[0] = lowest
                points[i] = temp
            }
        }
    }
}