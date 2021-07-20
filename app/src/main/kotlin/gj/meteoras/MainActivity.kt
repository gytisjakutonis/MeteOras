package gj.meteoras

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var i: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.log_debug).setOnClickListener {
            Monitor.debug("my debug " + i++)
        }

        findViewById<Button>(R.id.log_info).setOnClickListener {
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
