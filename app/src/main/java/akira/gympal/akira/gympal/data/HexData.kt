package akira.gympal.akira.gympal.data

import akira.gympal.MainActivity
import akira.gympal.R
import android.content.SharedPreferences
import android.widget.TextView


data class HexData(val sName: Int, val vName: Int, val isAttempt: Boolean = false, var count: Int = 0) {

    var view: TextView? = null
    private var text: String? = null
    private var activity: MainActivity? = null

    fun incr() : Int = ++count

    fun putCount(ed: SharedPreferences.Editor?) {
        ed?.putInt(vName.toString(), count)
    }

    fun init(activity: MainActivity) {
        this.activity = activity
        view = activity.findViewById(vName) as TextView
        text = activity.resources.getString(sName)
        view?.setOnClickListener { view ->
            incr()
            setText()
            calcTotals()
        }
        // set count from prefs if possible
        if (activity.useSavedState) {
            val prevCount = activity.prefs?.getInt(vName.toString(), count) ?: count
            count = prevCount // set this so totals are calculated property
        }
        setText()
    }

    fun setText() {
        setText(view!!, text!!, count)
    }

    fun setText(view: TextView, text: String, count: Int) {
        view.text = String.format("%s - %s", text, count)
    }

    fun reset() {
        count = 0 // set this so totals are calculated property
        setText()
    }

    /**
     * Only calc what we need.
     */
    fun calcTotals() {
        val activity = this.activity!!
        if (!isAttempt) { // regular hex
            val hexTotal = activity.hexs.filter { !it.isAttempt }.fold(0) { total, next -> total + next.count }
            val hStr = activity.resources.getString(R.string.total_hex)
            val hTxt = activity.findViewById(R.id.total_hex) as TextView
            setText(hTxt, hStr, hexTotal)
        } else {
            val attTotal = activity.hexs.filter { it.isAttempt }.fold(0) { total, next -> total + next.count }
            val aStr = activity.resources.getString(R.string.total_att)
            val aTxt = activity.findViewById(R.id.total_att) as TextView
            setText(aTxt, aStr, attTotal)
        }
    }
}