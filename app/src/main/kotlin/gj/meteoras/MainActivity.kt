package gj.meteoras

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import gj.meteoras.db.Database
import gj.meteoras.net.Meteo
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    var i : Int = 0
    val meteo : Meteo by inject()
    val db : Database by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.log_debug).setOnClickListener {
            val result = runBlocking {
                meteo.places()
            }

            runBlocking {
                db.places().setAll(result)
            }

            Monitor.debug("my debug " + i++)
        }

        findViewById<Button>(R.id.log_info).setOnClickListener {
            val count = runBlocking {
                db.places().countAll()
            }

            Toast.makeText(this, "count=$count", Toast.LENGTH_SHORT).show()

            Monitor.info("my info " + i++)
        }

        findViewById<Button>(R.id.log_error).setOnClickListener {
            Monitor.error("my error" + i++)
        }

        findViewById<Button>(R.id.error).setOnClickListener {
            Monitor.error(IllegalArgumentException("my exception " + i++))
        }

        findViewById<Button>(R.id.crash).setOnClickListener {
            throw RuntimeException("my crash " + i++)
        }

        findViewById<Button>(R.id.event).setOnClickListener {
            Monitor.event("test_event", Bundle().apply {
                putString("property", "value" + i++)
            })
        }
    }
}
