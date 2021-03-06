import java.io.BufferedReader
import java.io.FileReader
import java.util.*
import kotlin.system.exitProcess

class MapFileReader(
        fileName: String
) {
    private val lines = ArrayList<String>()

    fun parsePolygonData(): ArrayList<Polygon2D>? {
        if (lines.size < 7)
            return null
        if (lines[3] != "POLYGON")
            return null

        val polygons = ArrayList<Polygon2D>()
        var tokens: Array<String>

        for (i in 3 until lines.size) {
            tokens = lines[i].split(",").toTypedArray()

            if (tokens[0] == "END")
                break
            else if (tokens[0] == "POLYGON") {
                val pg = Polygon2D()
                polygons.add(pg)
            } else {
                if (tokens.size != 2)
                    return null
                val pg = polygons[polygons.size - 1]
                val x = tokens[0].toDouble()
                val y = tokens[1].toDouble()
                pg.addPoint(Point2D(x, y))
            }
        }
        return polygons
    }


    val sourcePoint: Point2D?
        get() {
            if (lines.size < 7)
                return null
            if (lines[0] != "SRC-DST")
                return null
            val tokens = lines[1].split(",").toTypedArray()
            if (tokens.size != 2) return null
            val x = tokens[0].toDouble()
            val y = tokens[1].toDouble()
            return Point2D(x, y)
        }


    val destinationPoint: Point2D?
        get() {
            if (lines.size < 7)
                return null
            if (lines[0] != "SRC-DST")
                return null
            val tokens = lines[2].split(",").toTypedArray()
            if (tokens.size != 2) return null
            val x = tokens[0].toDouble()
            val y = tokens[1].toDouble()
            return Point2D(x, y)
        }

    init {
        try {
            val inputFile = FileReader(fileName)
            val bufferReader = BufferedReader(inputFile)
            var line: String
            while (true) {
                line = bufferReader.readLine()
                lines.add(line)
                if (line == "END")
                    break
            }
            bufferReader.close()
        } catch (e: Exception) {
            println("Error reading input map data file $fileName")
            exitProcess(1)
        }
    }
}