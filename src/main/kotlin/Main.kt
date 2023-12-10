import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

/**
 * Main method to start the program.
 */
fun main() {
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

    val dataG = SatelliteDataImpl()
    dataG.setOccupied(Color.GREEN)

    val dataP = SatelliteDataImpl()
    dataP.setOccupied(true)
    dataP.setColor(Color.PURPLE)

    val dataY = SatelliteDataImpl()
    dataY.setOccupied(Color.YELLOW)

    val listOfData = arrayListOf<SatelliteDataImpl>(dataW, dataG, dataP, dataY)

    val cArray = arrayOf<Int>(1,2,3,4,5)
    var count: Int
    for (hexagon in grid.hexagons) {
        hexagon.setSatelliteData(data)
    }

    //HexagonalGrid# clearSatelliteData(grid)
    //print("\u001b[0m")
//    var checker =0
    var optionChoice = ""
    while(optionChoice!="q"){
        println("Welcome to HexaBot.")
        println("Enter: \n  " +
                "1: To create a random grid \n  " +
                "2: To view the current grid (If you haven't created a grid, a blank one will be shown)\n  " +
                "3: To Setup the current Backboard Grid from file \n  " +
                "X Y: Enter 2 numbers representing [X, Y] to view the neighbours of a hexagon at [X, Y] of the grid \n  " +
                "Column Color: Enter Eg. [6 W] such that first is a column number between 1-7 and the second is a color [W, Y, G, P] for the Hexagon in the grid \n  " +
                "Q/q: To quit ")

        optionChoice = readLine()!!.trim()

        if(optionChoice=="3"){
            val setupList:ArrayList<BackboardGrid> = readBackboardSetupFile("BackboardGridSetup.txt")
            for(i in setupList.indices) {
                setupHexagonOnBackboard(setupList[i].column, setupList[i].color, grid, listOfData)
                printGrid(grid)
            }
        }


        else if(optionChoice=="2"){
            printGrid(grid)
        }
        else if(optionChoice=="1"){
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
//            checker=0
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
            else if(optionChoice.matches(("[0-9].[A-Za-z]").toRegex())) {
            //val inp = readLine()!!
            if(validateInputOption(optionChoice))
                continue
            else {
                val inpArr = optionChoice.split(" ")
                val inputColumn = Integer.valueOf(inpArr[0])
                val inputColor = inpArr[1].uppercase()
                setupHexagonOnBackboard(inputColumn, inputColor, grid, listOfData)
            }
            printGrid(grid)
        }

        else if(optionChoice=="q"||optionChoice=="Q"){
            println("Have a great day!")
            break
        }
        else if(optionChoice.matches(("-?[0-9]*.[0-9]*").toRegex())) {
            val coords = optionChoice.split(" ")
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


fun setupHexagonOnBackboard(
    inputColumn: Int,
    inputColor: String,
    grid: HexagonalGrid<SatelliteDataImpl>,
    listOfData: ArrayList<SatelliteDataImpl>
) {
        var row = 10
        while(row>=0) {
            var xCoords = 0-row/2 + Integer.valueOf(inputColumn)
            if(row%2==0) {
                if(Integer.valueOf(inputColumn)==7)
                    xCoords--
                val hexagon = grid.getByCubeCoordinate(CubeCoordinate.fromCoordinates(xCoords, row)).get()
                var hexagonToUpdate = hexagon
                logMsg("$xCoords $row")
                if(!hexagon.satelliteData.get().isOccupied()) {
                    while(!gravityCheck(grid, hexagonToUpdate)) {
                        //hexPrintln(hexagonToUpdate)
                        hexagonToUpdate = if(row%2==0){
                            //println("hi1")
                            if(!grid.getNeighborByIndex(hexagonToUpdate,4).get().satelliteData.get().isOccupied())
                                grid.getNeighborByIndex(hexagonToUpdate,4).get()
                            else
                                grid.getNeighborByIndex(hexagonToUpdate,5).get()
                        } else{
                            //println("hi2")
                            if(!grid.getNeighborByIndex(hexagonToUpdate,4).get().satelliteData.get().isOccupied())
                                grid.getNeighborByIndex(hexagonToUpdate,4).get()
                            else
                                grid.getNeighborByIndex(hexagonToUpdate,5).get()
                        }
                        //row++
                    }
                    when (inputColor) {
                        "W" -> hexagonToUpdate.setSatelliteData(listOfData[0])
                        "G" -> hexagonToUpdate.setSatelliteData(listOfData[1])
                        "P" -> hexagonToUpdate.setSatelliteData(listOfData[2])
                        "Y" -> hexagonToUpdate.setSatelliteData(listOfData[3])
                    }
                    break
                }
                else{
                    row--
                }
            }
            else {
                xCoords--
                logMsg("Coordinates are: [$xCoords, $row]")
                val hexagon = grid.getByCubeCoordinate(CubeCoordinate.fromCoordinates(xCoords, row)).get()
                logMsg("Hexagon found!")
                var hexagon2 = hexagon
                //hexPrintln(hexagon2)
                //row--

                //hexPrintln(hexagon2)
                if (!hexagon2.satelliteData.get().isOccupied()) {
                    //hexPrintln(hexagon2)
                    //row++
                    while(!gravityCheck(grid, hexagon2)) {
                        hexagon2 = if (row%2==0) {
                            //println("hi1")
                            if(!grid.getNeighborByIndex(hexagon2,4).get().satelliteData.get().isOccupied())
                                grid.getNeighborByIndex(hexagon2,4).get()
                            else
                                grid.getNeighborByIndex(hexagon2,5).get()
                        } else{
                            //println("hi2")
                            if(!grid.getNeighborByIndex(hexagon2,4).get().satelliteData.get().isOccupied())
                                grid.getNeighborByIndex(hexagon2,4).get()
                            else
                                grid.getNeighborByIndex(hexagon2,5).get()
                        }
                    }
                    when (inputColor) {
                        "W" -> hexagon2.setSatelliteData(listOfData[0])
                        "G" -> hexagon2.setSatelliteData(listOfData[1])
                        "P" -> hexagon2.setSatelliteData(listOfData[2])
                        "Y" -> hexagon2.setSatelliteData(listOfData[3])
                    }
                    break
                }
                else{
                    row--
                }
            }
        }
}

/**
 * Prints out log msgs to help user debug. Uncomment the println to see it work
 *
 * @param [s] string to log
 */
fun logMsg(s: String) {
    println(bg(199)+"=> log: $s \u001B[0m \n")
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
    return if(y==10){
        true
    }
    else if(y%2==0){
        grid.getNeighborByIndex(hexagon,4).get().satelliteData.get().isOccupied()&&grid.getNeighborByIndex(hexagon,5).get().satelliteData.get().isOccupied()
    }
    else if(x==-y/2){
        grid.getNeighborByIndex(hexagon,5).get().satelliteData.get().isOccupied()
    }
    else if(x==6-y/2){
        grid.getNeighborByIndex(hexagon,4).get().satelliteData.get().isOccupied()
    }
    else{
        grid.getNeighborByIndex(hexagon,4).get().satelliteData.get().isOccupied()&&grid.getNeighborByIndex(hexagon,5).get().satelliteData.get().isOccupied()
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
 * @return [Int] of indices
 */
fun getNeighborsInArray(grid: HexagonalGrid<SatelliteDataImpl>, hexagon: Hexagon<SatelliteDataImpl>) : Array<Int> {
    val retArray = arrayOf<Int>(-1, -1, -1, -1, -1, -1)

    for(i in 0..5)
    {
        try {
            grid.getNeighborByIndex(hexagon, i).get()
            when (i) {
                0 -> retArray[3]=i
                1 -> retArray[1]=i
                2 -> retArray[0]=i
                3 -> retArray[2]=i
                4 -> retArray[4]=i
                5 -> retArray[5]=i
            }
        } catch(_: Exception) {
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

/*
 * Prints a hexagon with its current color and its coordinates
 *
 * @param [hexagon]
 */
/*fun hexPrintln(hexagon: Hexagon<SatelliteDataImpl>) {
    println("(X, Y, Z) : ("+hexagon.gridX+", "+hexagon.gridY+", "+hexagon.gridZ+") / ID: "+hexagon.id
            +" / Data: "+hexagon.satelliteData.get().getColor())
}*/

fun bg(n:Int) = "\u001b[48;5;${n}m"
fun colorPrint(hexagon: Hexagon<SatelliteDataImpl>){
    var l = hexagon.id
    if(l.length==3)
        l = " $l "
    else if(l.length==4 && l.startsWith("-"))
        l = "$l "
    else if(l.length==4)
        l = " $l"

    print(bg(hexagon.satelliteData.get().getColor()!!.hexColor)+"["+l+"]\u001B[0m ")
}


fun readBackboardSetupFile(fileName: String): ArrayList<BackboardGrid> {
    val inputStream: InputStream = File(fileName).absoluteFile.inputStream()
    logMsg(inputStream.toString())
    val setupBackboard = arrayListOf<BackboardGrid>()
    File(fileName).forEachLine { logMsg(it)
        for(lineValues in it.split(",")) {
            val trimLineValue = lineValues.trim()
            logMsg("value is: [$trimLineValue]")
            if(validateInputOption(trimLineValue)) {
                val backboard = BackboardGrid(
                    Integer.parseInt(trimLineValue.split(" ")[0].trim()),
                    trimLineValue.split(" ")[1].trim()
                )
                setupBackboard.add(backboard)
            }
        }
    }
    return setupBackboard
}

fun validateInputOption(inputOption: String): Boolean {
    if (!(inputOption.matches(("[0-9].[A-Za-z]").toRegex()))) {
        println("The input does not match the expected type of [Column Color]. Try again!")
        return false
    }
    val inpArr = inputOption.split(" ")

    if(inpArr.size<=1)
        println("The input is a single character, Try Again!")
    else {
        val inputColumn: Int
        try {
            inputColumn = Integer.valueOf(inpArr[0])
        } catch (exception: Exception) {
            println("The first argument is not a number, Try Again!")
            return false
        }
        if (inputColumn < 1 || inputColumn > 7) {
            println("The column should be between 1 and 7. Try Again!")
            return false
        }
        val inputColor = inpArr[1].uppercase()
        if (!(inputColor == "W" || inputColor == "P" || inputColor == "G" || inputColor == "Y")) {
            println("Color input is not correct. Please check and Try Again")
            return false
        }
    }
        return true
}

class BackboardGrid(val column: Int, val color: String) {
    //private var column = 0
    //private var color: String = "B"
}