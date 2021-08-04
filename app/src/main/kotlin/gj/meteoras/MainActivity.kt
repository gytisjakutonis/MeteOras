package gj.meteoras

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import gj.meteoras.net.api.MeteoApi
import gj.meteoras.repo.places.PlacesRepo
import gj.meteoras.ui.places.PlacesAdapter
import gj.meteoras.ui.places.PlacesViewModel
import gj.meteoras.ui.places.PlacesViewState
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.ExperimentalTime

class MainActivity : AppCompatActivity() {
    val model: PlacesViewModel by viewModel()
    val api: MeteoApi by inject()
    val repo: PlacesRepo by inject()
    val adapter: PlacesAdapter by lazy { PlacesAdapter() }

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.recycler).adapter = adapter

        findViewById<Button>(R.id.findA).setOnClickListener {
            model.filter("a")
        }

        findViewById<Button>(R.id.findB).setOnClickListener {
            model.filter("al")
        }

        model.state.observe(this) { state ->
            state.render()
        }
    }

    private fun PlacesViewState.render() {
        findViewById<TextView>(R.id.text).text = error ?: if (loading) "busy..." else null
        adapter.submitList(places)
    }
}
