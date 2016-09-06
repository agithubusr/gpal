package akira.gympal

import akira.gympal.akira.gympal.data.HexData
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView

class MainActivity(var timerOn: Boolean = false, var elapsed: Long = 0, var useSavedState: Boolean = false)
    : AppCompatActivity() {

    var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val pInfo = packageManager.getPackageInfo(packageName, 0)
        val appName = resources.getString(R.string.app_name)
        this.title = String.format("%s %s", appName, pInfo.versionName)

        // start off w/ clock stuff as we check if timer was on here; ie; to use saved state
        val now = SystemClock.elapsedRealtime()
        // must do this first, required in init
        prefs = getSharedPreferences(BuildConfig.APPLICATION_ID, 0)
        val savedBase = prefs?.getLong(R.id.timer.toString(), now) ?: now
        useSavedState = savedBase != -1L // set 'magic' var here...

        HexData.calcAll(this)

        val timerStr = resources.getString(R.string.chrono)
        val timerLabel = findViewById(R.id.timer_label) as TextView
        val timer = findViewById(R.id.timer) as Chronometer
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
        // start clock w/ previous value
        if (useSavedState) { // only reset if timer was on previously
            timer.base = savedBase
            timer.start()
            timerLabel.text = String.format("%s %s:", timerStr, "On")
            timerOn = true
        }

        // button
        val button = findViewById(R.id.clear_btn) as Button
        button.setOnClickListener { view ->
            // reset timer
            elapsed = 0
            timer.base = SystemClock.elapsedRealtime()
            timer.stop()
            timerOn = false
            timerLabel.text = String.format("%s %s:", timerStr, "Off")

            HexData.recalcAll()
        }

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop();
        val ed = prefs?.edit()

        // save info
        HexData.putAll(ed)

        val timer = findViewById(R.id.timer) as Chronometer
        // only correct if timer
        if (timerOn) {
            ed?.putLong(R.id.timer.toString(), timer.base)
        } else {
            ed?.putLong(R.id.timer.toString(), -1L) // magic #
        }

        ed?.commit()

        System.out.println("stopping...")
    }
}
