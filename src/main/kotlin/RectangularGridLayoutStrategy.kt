
class RectangularGridLayoutStrategy : GridLayoutStrategy() {

    override fun fetchGridCoordinates(builder: HexagonalGridBuilder<out SatelliteData>): Iterable<CubeCoordinate> {
//        excludeElems ArrayList<ArrayList<int, int>> = ArrayList<ArrayList>{ArrayList(0,0)}
        val coords = ArrayList<CubeCoordinate>( builder.getGridHeight() * builder.getGridWidth())
        for (y in 0 until builder.getGridHeight()) {
            for (x in 0 until builder.getGridWidth()) {
                val gridX = CoordinateConverter.convertOffsetCoordinatesToCubeX(x, y, builder.getOrientation())
                val gridZ = CoordinateConverter.convertOffsetCoordinatesToCubeZ(x, y, builder.getOrientation())
                if(gridZ == -gridX*2)
                    continue
                else
                    coords.add(CubeCoordinate.fromCoordinates(gridX, gridZ))
            }
        }
        return coords
    }

    override fun checkParameters(gridHeight: Int, gridWidth: Int): Boolean {
        return checkCommonCase(gridHeight, gridWidth)
    }

    override fun getName(): String {
        return "RECTANGULAR"
    }
}
