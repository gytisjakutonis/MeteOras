package gj.meteoras

import gj.meteoras.ext.content.InitProvider
import gj.meteoras.ext.timber.CrashlyticsTree
import timber.log.Timber

class Init : InitProvider() {

    override fun onCreate(): Boolean {
        Timber.plant(CrashlyticsTree())

        return true
    }
}
