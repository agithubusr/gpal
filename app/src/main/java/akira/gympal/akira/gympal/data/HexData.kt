package akira.gympal.akira.gympal.data

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.widget.TextView


data class HexData(val sName: Int, val vName: Int, val isAttempt: Boolean = false, var count: Int = 0) {

    var view: TextView? = null
    private var text: String? = null

    fun incr() : Int = ++count

    fun putCount(ed: SharedPreferences.Editor?) {
        ed?.putInt(vName.toString(), count)
    }

    fun init(activity: AppCompatActivity) {
        view = activity.findViewById(vName) as TextView
        text = activity.resources.getString(sName)
    }

    fun setText() {
        view?.text = String.format("%s - %s", text, count)
    }
    fun reset() {
        count = 0 // set this so totals are calculated property
        setText()
    }
}