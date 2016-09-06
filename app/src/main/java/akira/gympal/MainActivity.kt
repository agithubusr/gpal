package akira.gympal

import akira.gympal.akira.gympal.data.HexData
import akira.gympal.akira.gympal.data.TimeData
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

class MainActivity() : AppCompatActivity() {

    var prefs: SharedPreferences? = null
    var time: TimeData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val pInfo = packageManager.getPackageInfo(packageName, 0)
        val appName = resources.getString(R.string.app_name)
        this.title = String.format("%s %s", appName, pInfo.versionName)

        prefs = getSharedPreferences(BuildConfig.APPLICATION_ID, 0)
        time = TimeData(this, prefs!!) // must be created first as hexData uses useSaved field
        HexData.calcAll(this)

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
        time?.stop()

        ed?.commit()

        System.out.println("stopping...")
    }
}
