import java.util.*

/**
 * Main method to start the program.
 */
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
        hexagon.setSatelliteData(data)
    }

    //HexagonalGrid# clearSatelliteData(grid)
    //print("\u001b[0m")
    var checker =0
    var option_choice = ""
    while(option_choice!="q"){
        println("Welcome to HexaBot.")
        println("Enter: \n  " +
                "1: To create a random grid \n  " +
                "2: To view the current grid (If you haven't created a grid, a blank one will be shown)\n  " +
                "X Y: Enter 2 numbers representing [X, Y] to view the neighbours of a hexagon at [X, Y] of the grid \n  " +
                "Column Color: Enter Eg. [6 W] such that first is a column number between 1-7 and the second is a color [W, Y, G, P] for the Hexagon in the grid \n  " +
                "Q/q: To quit ")
        option_choice = readLine()!!.trim()
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
            checker=0
        }
//        else if(option_choice=="3"){
//            if(checker==0){
//                for (hexagon in grid.hexagons)
//                    hexagon.setSatelliteData(data)
//                checker=1
//            }
//
//            printGrid(grid)
//            println("Here is the current grid! \n ")
//
//            println("Where would you like to place a pixel? Enter Row(1-7) and Color(W,G,Y,P) in form 'X Y' where X is the row and Y is a color")
//
            else if(option_choice.matches(("[0-9].[A-Za-z]").toRegex())) {
            //val inp = readLine()!!
            val inpArr = option_choice.split(" ")
            if(inpArr.size<=1)
                println("Try Again!")
            else {
                var inputColumn = -1
                try {
                    inputColumn = Integer.valueOf(inpArr[0])
                }
                catch (exception: Exception){
                    println("Try Again!")
                    continue
                }
                if (inputColumn<1 || inputColumn>7) {
                    println("The row should be between 1 and 7. Try Again!")
                    continue
                }
                val inputColor = inpArr[1].toString().uppercase()
                if(inputColor=="W"||inputColor=="P"||inputColor=="G"||inputColor=="Y") {
                    var row = 10
                    while(row>=0) {
                        var x_coords = 0-row/2 + Integer.valueOf(inpArr[0])
                        if(row%2==0) {
                            if(Integer.valueOf(inpArr[0])==7)
                                x_coords--
                            val hexagon = grid.getByCubeCoordinate(CubeCoordinate.fromCoordinates(x_coords, row)).get()
                            var hexagonToUpdate = hexagon
                            logMsg(x_coords.toString()+" "+row.toString())
                            if(!hexagon.satelliteData.get().isOccupied()) {
                                //println("hi")
                                while(!gravityCheck(grid, hexagonToUpdate)) {
                                    //hexPrintln(hexagonToUpdate)
                                    if(row%2==0){
                                        //println("hi1")
                                        if(!grid.getNeighborByIndex(hexagonToUpdate,4).get().satelliteData.get().isOccupied())
                                            hexagonToUpdate = grid.getNeighborByIndex(hexagonToUpdate,4).get()
                                        else
                                            hexagonToUpdate = grid.getNeighborByIndex(hexagonToUpdate,5).get()
                                    }
                                    else{
                                        //println("hi2")
                                        if(!grid.getNeighborByIndex(hexagonToUpdate,4).get().satelliteData.get().isOccupied())
                                            hexagonToUpdate = grid.getNeighborByIndex(hexagonToUpdate,4).get()
                                        else
                                            hexagonToUpdate = grid.getNeighborByIndex(hexagonToUpdate,5).get()
                                    }
                                    //row++
                                }
                                when (inputColor) {
                                    "W" -> hexagonToUpdate.setSatelliteData(dataW)
                                    "Y" -> hexagonToUpdate.setSatelliteData(dataY)
                                    "P" -> hexagonToUpdate.setSatelliteData(dataP)
                                    "G" -> hexagonToUpdate.setSatelliteData(dataG)
                                }
                                break
                            }
                            else{
                                row--
                            }
                        }
                        else {
                            x_coords--
                            logMsg("Coordinates are: [$x_coords, $row]")
                            val hexagon = grid.getByCubeCoordinate(CubeCoordinate.fromCoordinates(x_coords, row)).get()
                            logMsg("Hexagon found!")
                            var hexagon2 = hexagon
                            //hexPrintln(hexagon2)
                            //row--

                            //hexPrintln(hexagon2)
                            if (!hexagon2.satelliteData.get().isOccupied()) {
                                while(!gravityCheck(grid, hexagon2)){
                                    //hexPrintln(hexagon2)
                                    if(row%2==0){
                                        //println("hi1")
                                        if(!grid.getNeighborByIndex(hexagon2,4).get().satelliteData.get().isOccupied())
                                            hexagon2 = grid.getNeighborByIndex(hexagon2,4).get()
                                        else
                                            hexagon2 = grid.getNeighborByIndex(hexagon2,5).get()
                                    }
                                    else{
                                        //println("hi2")
                                        if(!grid.getNeighborByIndex(hexagon2,4).get().satelliteData.get().isOccupied())
                                            hexagon2 = grid.getNeighborByIndex(hexagon2,4).get()
                                        else
                                            hexagon2 = grid.getNeighborByIndex(hexagon2,5).get()
                                    }
                                    //row++
                                }
                                when (inputColor) {
                                    "W" -> hexagon2.setSatelliteData(dataW)
                                    "Y" -> hexagon2.setSatelliteData(dataY)
                                    "P" -> hexagon2.setSatelliteData(dataP)
                                    "G" -> hexagon2.setSatelliteData(dataG)
                                }
                                break
                            }
                            else{
                                row--
                            }
                        }
                    }
                }
                else{
                    println("Color input is not correct. Please check and Try Again")
                    continue
                }
            }
            printGrid(grid)
        }

        else if(option_choice=="q"||option_choice=="Q"){
            println("Have a great day!")
            break;
        }
        else if(option_choice.matches(("-?[0-9]*.[0-9]*").toRegex())) {
            val coords = option_choice.split(" ")
            logMsg("["+coords.joinToString(",")+"]"+" # of elems: "+coords.size)
            if(coords.size>1 && coords[1] != "") {
                println("You wanted hexagon at (" + coords[0] + ", " + coords[1] + ")")
                if (Integer.valueOf(coords[1]) == (-2 * Integer.valueOf(coords[0]))) {
                    println("This position (" + coords[0] + ", " + coords[1] + ") does not exist! Please look at the grid to view valid positions!")
                } else if (Integer.valueOf(coords[0]) > 6 || Integer.valueOf(coords[0]) < -4 || Integer.valueOf(coords[1]) < 0 || Integer.valueOf(
                        coords[1]
                    ) > 10
                ) {
                    println("This position does not exist! Try again! Please look at the grid to view valid positions!")
                }
                else if(Integer.valueOf(coords[0])>6-(Integer.valueOf(coords[1])/2)||Integer.valueOf(coords[0])<-(Integer.valueOf(coords[1])/2)){
                    println("This position does not exist! Try again! Please look at the grid to view valid positions!")
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
}

/**
 * Prints out log msgs to help user debug. Uncomment the println to see it work
 *
 * @param [s] string to log
 */
fun logMsg(s: String) {
    println(bg(199)+"=> log: $s \u001B[0m \n");
}

/**
 * Checks if the [hexagon] can actually be placed there if gravity is involved
 *
 * @param [grid] , [hexagon]
 *
 * @return boolean
 */
fun gravityCheck(grid: HexagonalGrid<SatelliteDataImpl>, hexagon: Hexagon<SatelliteDataImpl>): Boolean {
    val ids = hexagon.id
    val coords = ids.split(",")
    val x = Integer.valueOf(coords[0])
    val y = Integer.valueOf(coords[1])
    if(y==10){
        return true
    }
    else if(y%2==0){
        return grid.getNeighborByIndex(hexagon,4).get().satelliteData.get().isOccupied()&&grid.getNeighborByIndex(hexagon,5).get().satelliteData.get().isOccupied()
    }
    else if(x==-y/2){
        return grid.getNeighborByIndex(hexagon,5).get().satelliteData.get().isOccupied()
    }
    else if(x==6-y/2){
        return grid.getNeighborByIndex(hexagon,4).get().satelliteData.get().isOccupied()
    }
    else{
        return grid.getNeighborByIndex(hexagon,4).get().satelliteData.get().isOccupied()&&grid.getNeighborByIndex(hexagon,5).get().satelliteData.get().isOccupied()
    }
}

/**
 * Prints all hexagons(pixels) that neighbour(touch) the hexagon selected
 *
 * @param [grid], [hexagon]
 *
 * @return array of all neighbouring hexagons
 */
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

/**
 * Returns the Array of indices of all neighbouring hexagons(pixels)
 *
 * @param [grid] , [hexagon]
 *
 * @return [array] of indices
 */
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

/**
 * Prints the Current Grid
 *
 * @param [grid]
 */
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

/**
 * Prints a hexagon with its current color and its coordinates
 *
 * @param [hexagon]
 */
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