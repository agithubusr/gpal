package akira.gympal.akira.gympal.data

import akira.gympal.R
import android.content.SharedPreferences
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView

/**
 * Created by amatsuo on 2016-09-05.
 */
class TimeData(val activity: AppCompatActivity, val prefs: SharedPreferences, var useSavedState: Boolean = false, var timerOn: Boolean = false, var elapsed: Long = 0) {
    var timerStr: String?
    var timerLabel: TextView?
    var timer: Chronometer?

    init {
        timerStr = activity.resources.getString(R.string.chrono)
        timerLabel = activity.findViewById(R.id.timer_label) as TextView
        timer = activity.findViewById(R.id.timer) as Chronometer
        val sTimer = timer!!

        timerLabel?.setOnClickListener { view ->
            val now = SystemClock.elapsedRealtime()
            if (timerOn) {
                elapsed = now - sTimer.base
                sTimer.stop()
                timerLabel?.text = String.format("%s %s:", timerStr, "Off")
                timerOn = false
            } else {
                sTimer.base = now - elapsed
                sTimer.start()
                timerLabel?.text = String.format("%s %s:", timerStr, "On")
                timerOn = true
            }
        }
        val now = SystemClock.elapsedRealtime()
        val savedBase = prefs.getLong(R.id.timer.toString(), now) ?: now
        useSavedState = savedBase != -1L // set 'magic' var here...
        if (useSavedState) { // only reset if timer was on previously
            println("savedBase... " + savedBase)
            sTimer.base = savedBase
            sTimer.start()
            timerLabel?.text = String.format("%s %s:", timerStr, "On")
            timerOn = true
        }

        // button
        val button = activity.findViewById(R.id.clear_btn) as Button
        button.setOnClickListener { view ->
            // reset timer
            elapsed = 0
            sTimer.base = SystemClock.elapsedRealtime()
            sTimer.stop()
            timerOn = false
            timerLabel?.text = String.format("%s %s:", timerStr, "Off")

            HexData.recalcAll()
        }
    }

    fun stop() {
        val ed = prefs!!.edit()
        val sTimer = timer!!
        // only correct if timer was on
        if (timerOn) {
            val baseToSave = sTimer.base
            println("Saving... " + baseToSave)
            ed.putLong(R.id.timer.toString(), sTimer.base)
        } else {
            ed.putLong(R.id.timer.toString(), -1L) // magic #
        }
        ed.commit()
    }

}