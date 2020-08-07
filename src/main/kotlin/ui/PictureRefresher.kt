package ui

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch

class PictureRefresher(val picture: Picture): Runnable {
    private var refreshRate = ticker(500)
    private var refreshRoutine: Job? = null

    override fun run() {
        refreshRoutine = GlobalScope.launch {
            for (tick in refreshRate) picture.paint()
        }
    }

    suspend fun stop() = refreshRoutine?.cancelAndJoin()
}
