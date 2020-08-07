package ui

import queries

class QueriesView {
    private val queriesViews by lazy {
        queries.map { QueryView(it.value) }
    }

    fun get() = "Queries\n${queriesViews.joinToString(separator = "\n") { "${Ansi.CLEAR_LINE} - ${it.get()}" }}"
}
