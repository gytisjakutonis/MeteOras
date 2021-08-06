package gj.meteoras

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import gj.meteoras.databinding.ActivityMainBinding
import gj.meteoras.ext.widget.DrawableLocation
import gj.meteoras.ext.widget.setDrawableOnTouchListener
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

        setTitle("Find a Place")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        binding.recycler.adapter = adapter

        binding.filter.setDrawableOnTouchListener { _, _, location ->
            if (location == DrawableLocation.Right) {
                binding.filter.text = null
                true
            } else {
                false
            }
        }

        model.state.observe(this) { state ->
            state.render()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun PlacesViewState.render() {
        binding.filter.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_magnify_grey,
            0,
            if (clear) R.drawable.ic_close_grey else 0,
            0
        )
    }
}
