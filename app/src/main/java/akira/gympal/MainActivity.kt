package akira.gympal

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

class MainActivity(var cOne: Int = 0, var cTwo: Int = 0, var cThree: Int = 0,
                   var timerOn: Boolean = false, var elapsed: Long = 0)
    : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val pInfo = packageManager.getPackageInfo(packageName, 0)
        val appName = resources.getString(R.string.app_name)
        this.title = String.format("%s %s", appName, pInfo.versionName)

        val oneHexStr = resources.getString(R.string.one_hex)
        val oneHexTxt = findViewById(R.id.one_hex) as TextView
        oneHexTxt.setOnClickListener { view ->
            oneHexTxt.text = String.format("%s - %s", oneHexStr, ++cOne)
        }
        val twoHexStr = resources.getString(R.string.two_hex)
        val twoHexTxt = findViewById(R.id.two_hex) as TextView
        twoHexTxt.setOnClickListener { view ->
            twoHexTxt.text = String.format("%s - %s", twoHexStr, ++cTwo)
        }
        val threeHexStr = resources.getString(R.string.three_hex)
        val threeHexTxt = findViewById(R.id.three_hex) as TextView
        threeHexTxt.setOnClickListener { view ->
            threeHexTxt.text = String.format("%s - %s", threeHexStr, ++cThree)
        }

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
