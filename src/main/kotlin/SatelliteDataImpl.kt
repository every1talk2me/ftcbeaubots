class SatelliteDataImpl : DefaultSatelliteData() {

    private var isOccupied = false

    fun isOccupied(): Boolean {
        return isOccupied
    }

    fun setOccupied(isOccupied: Boolean) {
        if (!isOccupied) setColor(Color.BLACK)
        this.isOccupied = isOccupied
    }

    fun setOccupied(color: Color) {
        this.isOccupied = true
        setColor(color)

    }

    private var color: Color? = null

    fun getColor(): Color? {
        return color
    }

    fun setColor(color: Color?) {
        this.color = color
    }


}