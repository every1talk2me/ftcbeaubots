fun main(args : Array<String>) {
    println("Hello Hexa!")

    lateinit var builder: HexagonalGridBuilder<out SatelliteDataImpl>

    builder = HexagonalGridBuilder()
    builder.setGridHeight(3)
        .setGridWidth(7)
        .setGridLayout(HexagonalGridLayout.RECTANGULAR)
        .setOrientation(HexagonOrientation.POINTY_TOP)
        .setRadius(10.0)

    val grid = builder.build()

    val data = SatelliteDataImpl()
    data.setOccupied(false)

    for (hexagon in grid.hexagons) {
        hexagon.setSatelliteData(data)
    }

    for (hexagon in grid.hexagons) {
        //System.out.println("X: "+hexagon.getCenterX()+" / Y: "+hexagon.getCenterY());
        //System.out.println("X: "+hexagon.getGridX()+" / Y: "+hexagon.getGridY()+" / Z: "+hexagon.getGridZ()+" / ID: "+hexagon.getId());
        hexPrintln(hexagon)
    }


    //HexagonalGrid# clearSatelliteData(grid)
    println("Get Neighbor\n")

//    for (hexagon in grid.hexagons) { //grid.getHexagonsByOffsetRange(0, 0, 0, 1)) {

    val hexagon = grid.getByCubeCoordinate(CubeCoordinate.fromCoordinates(0, 1)).get()
    hexPrintln(hexagon)

        for(i in 0..5)
        {
            try {
                val hexagon2 = grid.getNeighborByIndex(hexagon, i).get()
                print(" -> " + i + " : ")
                hexPrintln(hexagon2)
            } catch(exception : Exception) {}
        }
//    }


    //HexagonalGridCalculator<DefaultSatelliteData> calc = builder.buildCalculatorFor(grid);
    //calc.calculateDistanceBetween(sourceHex, targetHex)

}

fun hexPrintln(hexagon: Hexagon<SatelliteDataImpl>) {
    println("(X, Y, Z) : ("+hexagon.gridX+", "+hexagon.gridY+", "+hexagon.gridZ+") / ID: "+hexagon.id+" / Data: "+hexagon.satelliteData.get().getColor())
}