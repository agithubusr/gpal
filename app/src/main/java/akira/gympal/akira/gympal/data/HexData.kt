package akira.gympal.akira.gympal.data

import android.content.SharedPreferences


data class HexData(val sName: Int, val vName: Int, var count: Int = 0) {

    fun incr() : Int = ++count

    fun putCount(ed: SharedPreferences.Editor?) {
        ed?.putInt(vName.toString(), count)
    }
}