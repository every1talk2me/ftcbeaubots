import java.util.StringTokenizer

fun main(args : Array<String>) {
    println("Hello Hexa!")

    lateinit var builder: HexagonalGridBuilder<out SatelliteDataImpl>

    builder = HexagonalGridBuilder()
    builder.setGridHeight(11)
        .setGridWidth(7)
        .setGridLayout(HexagonalGridLayout.RECTANGULAR)
        .setOrientation(HexagonOrientation.POINTY_TOP)
        .setRadius(10.0)

    val grid = builder.build()

    val data = SatelliteDataImpl()
    data.setOccupied(false)

    val dataW = SatelliteDataImpl()
    dataW.setOccupied(Color.WHITE)

    val dataY = SatelliteDataImpl()
    dataY.setOccupied(Color.YELLOW)

    val dataG = SatelliteDataImpl()
    dataG.setOccupied(Color.GREEN)

    val dataP = SatelliteDataImpl()
    dataP.setOccupied(true)
    dataP.setColor(Color.PURPLE)

    val cArray = arrayOf<Int>(1,2,3,4,5)
    var count = 0
    for (hexagon in grid.hexagons) {
        count = cArray.random()
        if(count%5 == 0)
            hexagon.setSatelliteData(data)
        else if (count%5 == 1)
            hexagon.setSatelliteData(dataW)
        else if (count%5 == 2)
            hexagon.setSatelliteData(dataG)
        else if (count%5 == 3)
            hexagon.setSatelliteData(dataY)
        else
            hexagon.setSatelliteData(dataP)
    }

    /*for (hexagon in grid.hexagons) {
        //System.out.println("X: "+hexagon.getCenterX()+" / Y: "+hexagon.getCenterY());
        //System.out.println("X: "+hexagon.getGridX()+" / Y: "+hexagon.getGridY()+" / Z: "+hexagon.getGridZ()+" / ID: "+hexagon.getId());
        hexPrintln(hexagon)
    }*/

    //printGrid(grid)


    //HexagonalGrid# clearSatelliteData(grid)
    //print("\u001b[0m")

    var option_choice = ""
    while(option_choice!="q"){
        println("Welcome to HexaBot.")
        println("Enter: \n  " +
                "1: To create a random grid \n  " +
                "2: To view the current grid (If you haven't created a grid, one will be made for you)\n  " +
                "3: To start creating a custom grid \n  " +
                "X Y: Enter 2 numbers representing [X, Y] to view the neighbours of a hexagon at [X, Y] of the grid \n  " +
                "Q/q: To quit ")
        option_choice = readLine()!!
        if(option_choice=="2"){
            printGrid(grid)
        }
        else if(option_choice=="1"){
            for (hexagon in grid.hexagons) {
                count = cArray.random()
                if(count%5 == 0)
                    hexagon.setSatelliteData(data)
                else if (count%5 == 1)
                    hexagon.setSatelliteData(dataW)
                else if (count%5 == 2)
                    hexagon.setSatelliteData(dataG)
                else if (count%5 == 3)
                    hexagon.setSatelliteData(dataY)
                else
                    hexagon.setSatelliteData(dataP)
            }
            printGrid(grid)
        }
        else if(option_choice=="3"){
            for (hexagon in grid.hexagons)
                hexagon.setSatelliteData(data)
            printGrid(grid)
            println("Here's a blank grid as this section has not been set up yet :) \n ")
        }
        else if(option_choice=="q"||option_choice=="Q"){
            println("Have a great day!")
            break;
        }
        else if(option_choice.matches(("-?[0-9]*.[0-9]*").toRegex())) {
            val coords = option_choice.split(" ")
            if(coords.size>1) {
                println("You wanted hexagon at (" + coords[0] + ", " + coords[1] + ")")
                if (Integer.valueOf(coords[1]) == (-2 * Integer.valueOf(coords[0]))) {
                    println("This position does not exist")
                } else if (Integer.valueOf(coords[0]) > 6 || Integer.valueOf(coords[0]) < -4 || Integer.valueOf(coords[1]) < 0 || Integer.valueOf(
                        coords[1]
                    ) > 10
                ) {
                    println("This position does not exist! Try again!")
                }
                else if(Integer.valueOf(coords[0])>6-(Integer.valueOf(coords[1])/2)||Integer.valueOf(coords[0])<-(Integer.valueOf(coords[1])/2)){
                    println("This position does not exist! Try again!")
                }
                else {
                    val hexagon = grid.getByCubeCoordinate(
                        CubeCoordinate.fromCoordinates(
                            Integer.valueOf(coords[0]),
                            Integer.valueOf(coords[1])
                        )
                    ).get()
                    val neighborArray: Array<Int> = getNeighborsInArray(grid, hexagon)
                    printNeighborGrid(neighborArray, grid, hexagon)
                }
            }
            else
                println("Oops! Looks like your input doesn't match any of the choices! Try again")
        }
        else{
            println("Oops! Looks like your input doesn't match any of the choices! Try again")
        }
        println("")
    }

    //println("Get Neighbor\n")

//    for (hexagon in grid.hexagons) { //grid.getHexagonsByOffsetRange(0, 0, 0, 1)) {

    //val hexagon = grid.getByCubeCoordinate(CubeCoordinate.fromCoordinates(1, 4)).get()
    //hexPrintln(hexagon)

    //val neighborArray : Array<Int> = getNeighborsInArray(grid, hexagon)
    //printNeighborGrid(neighborArray, grid, hexagon)




    //HexagonalGridCalculator<DefaultSatelliteData> calc = builder.buildCalculatorFor(grid);
    //calc.calculateDistanceBetween(sourceHex, targetHex)

}

fun printNeighborGrid(neighborArray: Array<Int>, grid: HexagonalGrid<SatelliteDataImpl>, hexagon: Hexagon<SatelliteDataImpl>) {
    //println(neighborArray.contentToString())
    var count = 0
    print("   ")
    for(i in neighborArray){
        if(i==-1)
            print("[     ] ")
        else{
            val hexagon2 = grid.getNeighborByIndex(hexagon,i).get()
            colorPrint(hexagon2)
        }
        if(count==1)
            println("")
        if(count==3){
            println("")
            print("   ")
        }
        if(count==2)
            colorPrint(hexagon)
        count++
    }
}


fun getNeighborsInArray(grid: HexagonalGrid<SatelliteDataImpl>, hexagon: Hexagon<SatelliteDataImpl>) : Array<Int> {
    val retArray = arrayOf<Int>(-1, -1, -1, -1, -1, -1)

    for(i in 0..5)
    {
        try {
            val hexagon2 = grid.getNeighborByIndex(hexagon, i).get()
            when (i) {
                0 -> retArray[3]=i
                1 -> retArray[1]=i
                2 -> retArray[0]=i
                3 -> retArray[2]=i
                4 -> retArray[4]=i
                5 -> retArray[5]=i
            }
        } catch(exception : Exception) {
        }
    }

    return retArray
}

fun printGrid(grid: HexagonalGrid<SatelliteDataImpl>) {
    var i =1
    var row=1

    for(hexagon in grid.hexagons) {

        if(i==1){
            print("   ")
        }
        colorPrint(hexagon)
        if(i%13==0||(i-6)%13==0) {
            println("")
            row+=1
            if(row%2==1)
                print("   ")
        }
        i+=1
    }
}

fun hexPrintln(hexagon: Hexagon<SatelliteDataImpl>) {
    println("(X, Y, Z) : ("+hexagon.gridX+", "+hexagon.gridY+", "+hexagon.gridZ+") / ID: "+hexagon.id
            +" / Data: "+hexagon.satelliteData.get().getColor())
}
fun bg(n:Int) = "\u001b[48;5;${n}m"
fun colorPrint(hexagon: Hexagon<SatelliteDataImpl>){
    var l = hexagon.id
    if(l.length==3)
        l = " "+l+" "
    else if(l.length==4 && l.startsWith("-"))
        l = l+" "
    else if(l.length==4)
        l = " "+l

    print(bg(hexagon.satelliteData.get().getColor()!!.hexColor)+"["+l+"]\u001B[0m ")
}