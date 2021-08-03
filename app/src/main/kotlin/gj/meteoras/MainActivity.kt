package gj.meteoras

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.RecyclerView
import gj.meteoras.net.api.MeteoApi
import gj.meteoras.repo.places.PlacesRepo
import gj.meteoras.ui.places.PlacesAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    val api: MeteoApi by inject()
    val repo: PlacesRepo by inject()
    val adapter: PlacesAdapter by lazy { PlacesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.recycler).adapter = adapter

        findViewById<Button>(R.id.findA).setOnClickListener {
            lifecycleScope.launch {
                val result = api.places()


                with(repo) {
                    lifecycleScope.findPlaces(
                        "a",
                        PagingConfig(
                            pageSize = 5
                        )
                    )
                }.flow.collect { data ->
                    Timber.d("REPO submit data")

                    adapter.submitData(data)
                }
            }
        }
    }
}
