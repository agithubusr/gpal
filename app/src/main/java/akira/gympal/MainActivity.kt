package akira.gympal

import akira.gympal.akira.gympal.data.HexData
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Chronometer
import android.widget.TextView

class MainActivity(var timerOn: Boolean = false, var elapsed: Long = 0)
    : AppCompatActivity() {

    private val hexs = listOf(
        HexData(R.string.one_hex, R.id.one_hex),
        HexData(R.string.two_hex, R.id.two_hex),
        HexData(R.string.three_hex, R.id.three_hex))
    private val atts = listOf(
        HexData(R.string.att, R.id.one_att),
        HexData(R.string.att, R.id.two_att),
        HexData(R.string.att, R.id.three_att))

    private fun handleHex(hex: HexData) {
        val hStr = resources.getString(hex.sName)
        val hTxt = findViewById(hex.vName) as TextView
        hTxt.setOnClickListener { view ->
            hTxt.text = String.format("%s - %s", hStr, hex.incr())
            calcTotals()
        }
    }

    private fun calcTotals() {
        val hexTotal = hexs.fold(0) { total, next -> total + next.count }
        val attTotal = atts.fold(0) { total, next -> total + next.count }
        val hStr = resources.getString(R.string.total_hex)
        val hTxt = findViewById(R.id.total_hex) as TextView
        hTxt.text = String.format("%s - %s", hStr, hexTotal)
        val aStr = resources.getString(R.string.total_att)
        val aTxt = findViewById(R.id.total_att) as TextView
        aTxt.text = String.format("%s - %s", aStr, attTotal)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val pInfo = packageManager.getPackageInfo(packageName, 0)
        val appName = resources.getString(R.string.app_name)
        this.title = String.format("%s %s", appName, pInfo.versionName)

        for (hex in hexs) { handleHex(hex) }
        for (att in atts) { handleHex(att) }

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
}
