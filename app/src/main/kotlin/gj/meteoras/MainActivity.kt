package gj.meteoras

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import gj.meteoras.ui.places.PlacesAdapter
import gj.meteoras.ui.places.PlacesViewModel
import gj.meteoras.ui.places.PlacesViewState
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.ExperimentalTime

class MainActivity : AppCompatActivity() {
    val model: PlacesViewModel by viewModel()
    val adapter: PlacesAdapter by lazy { PlacesAdapter() }

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.recycler).adapter = adapter

        model.state.observe(this) { state ->
            state.render()
        }
    }

    private fun PlacesViewState.render() {
        findViewById<TextView>(R.id.text).text = error ?: if (loading) "busy..." else null
        adapter.submitList(places)
    }
}
