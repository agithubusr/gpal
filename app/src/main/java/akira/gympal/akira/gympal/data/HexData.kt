package akira.gympal.akira.gympal.data

import akira.gympal.MainActivity
import akira.gympal.R
import android.content.SharedPreferences
import android.widget.TextView


class HexData(val sName: Int, val vName: Int, var count: Int = 0) {

    companion object {
        private val all  =  listOf(
            HexData(R.string.one_hex, R.id.one_hex),
            HexData(R.string.two_hex, R.id.two_hex),
            HexData(R.string.three_hex, R.id.three_hex),
            HexData(R.string.four_hex, R.id.four_hex),
            HexData(R.string.five_hex, R.id.five_hex),
            HexData(R.string.six_hex, R.id.six_hex))

        fun calcAll(mainActivity: MainActivity) {
            all.forEach { hex ->  hex.prep(mainActivity) }
            // hack-ish way of getting a reference to a HexData
            all.first().calcTotals()
        }
        fun recalcAll() {
            all.forEach { hex -> hex.reset() }
            all.first().calcTotals()
        }
        fun putAll(ed: SharedPreferences.Editor?) {
            for (hex in all) { hex.putCount(ed) }
        }
    }

    private var view: TextView? = null
    private var text: String? = null
    private var activity: MainActivity? = null


    fun incr() : Int = ++count

    fun putCount(ed: SharedPreferences.Editor?) {
        ed?.putInt(vName.toString(), count)
    }

    fun prep(activity: MainActivity) {
        println("In prep...")

        this.activity = activity
        view = activity.findViewById(vName) as TextView
        text = activity.resources.getString(sName)
        view?.setOnClickListener { view ->
            incr()
            setText()
            calcTotals()
        }
        // set count from prefs if possible
        if (activity.time!!.useSavedState) {
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

    fun calcTotals() {
        val activity = this.activity!!
        val hexTotal = all.fold(0) { total, next -> total + next.count }
        val hStr = activity.resources.getString(R.string.total_hex)
        val hTxt = activity.findViewById(R.id.total_hex) as TextView
        setText(hTxt, hStr, hexTotal)
    }
}