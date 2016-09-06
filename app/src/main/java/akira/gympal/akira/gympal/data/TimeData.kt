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
class TimeData(val timerStr: String, val timerLabel: TextView, val timer: Chronometer, val timerBtn: Button,
               val prefs: SharedPreferences) {

    var useSavedState: Boolean = false;
    var timerOn: Boolean = false;
    var elapsed: Long = 0

    init {
        timerLabel.setOnClickListener { view ->
            val now = SystemClock.elapsedRealtime()
            if (timerOn) {
                elapsed = now - timer.base
                timer.stop()
                timerLabel.text = String.format("%s %s:", timerStr, "Off")
                timerOn = false
            } else {
                timer.base = now - elapsed
                timer.start()
                timerLabel.text = String.format("%s %s:", timerStr, "On")
                timerOn = true
            }
        }
        val now = SystemClock.elapsedRealtime()
        val savedBase = prefs.getLong(R.id.timer.toString(), now) ?: now
        useSavedState = savedBase != -1L // set 'magic' var here...
        if (useSavedState) { // only reset if timer was on previously
            println("savedBase... " + savedBase)
            timer.base = savedBase
            timer.start()
            timerLabel.text = String.format("%s %s:", timerStr, "On")
            timerOn = true
        }

        // button
        timerBtn.setOnClickListener { view ->
            // reset timer
            elapsed = 0
            timer.base = SystemClock.elapsedRealtime()
            timer.stop()
            timerOn = false
            timerLabel.text = String.format("%s %s:", timerStr, "Off")

            HexData.recalcAll()
        }
    }

    fun stop() {
        val ed = prefs.edit()
        // only correct if timer was on
        if (timerOn) {
            val baseToSave = timer.base
            println("Saving... " + baseToSave)
            ed.putLong(R.id.timer.toString(), timer.base)
        } else {
            ed.putLong(R.id.timer.toString(), -1L) // magic #
        }
        ed.commit()
    }

}