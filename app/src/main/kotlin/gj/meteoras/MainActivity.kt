package gj.meteoras

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import timber.log.Timber
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {
    var i: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.log_debug).setOnClickListener {
            Timber.d("my debug " + i++)
        }

        findViewById<Button>(R.id.log_info).setOnClickListener {
            Timber.i("my info " + i++)
        }

        findViewById<Button>(R.id.log_error).setOnClickListener {
            Timber.e("my error" + i++)
        }

        findViewById<Button>(R.id.error).setOnClickListener {
            Timber.e(IllegalArgumentException("my exception " + i++))
        }

        findViewById<Button>(R.id.crash).setOnClickListener {
            throw RuntimeException("my crash " + i++)
        }
    }
}
