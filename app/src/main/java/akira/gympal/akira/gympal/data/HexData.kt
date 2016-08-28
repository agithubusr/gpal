package akira.gympal.akira.gympal.data


data class HexData(val sName: Int, val vName: Int, var count: Int = 0) {

    fun incr() : Int = ++count
}