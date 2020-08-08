package ui

import config.ConfigContainer

class QueriesView {
    private val queriesViews by lazy {
        ConfigContainer.getConfig().queries.map { db ->
            db.key to db.value.map { QueryView(it) }
        }
    }

    fun get() =
        "Queries\n" +
            queriesViews.joinToString(separator = "\n") { pair ->
                " > ${pair.first}:\n" +
                        pair.second.joinToString(separator = "\n") { "${Ansi.CLEAR_LINE}   - ${it.get()}" }
            }

}
