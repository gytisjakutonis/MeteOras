package gj.meteoras.db

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            Database::class.java,
            "gj.meteoras.database"
        ).build()
    }
}
