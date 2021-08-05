package gj.meteoras

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import gj.meteoras.databinding.ActivityMainBinding
import gj.meteoras.ui.places.PlacesAdapter
import gj.meteoras.ui.places.PlacesViewModel
import gj.meteoras.ui.places.PlacesViewState
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.ExperimentalTime

class MainActivity : AppCompatActivity() {
    val model: PlacesViewModel by viewModel()
    val adapter: PlacesAdapter by lazy { PlacesAdapter() }
    lateinit var binding: ActivityMainBinding

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )?.let { content ->
            binding = content
            binding.lifecycleOwner = this
            binding.viewModel = model
        }

        binding.recycler.adapter = adapter

        model.state.observe(this) { state ->
            state.render()
        }
    }

    private fun PlacesViewState.render() {
    }
}
