package ui

import config.Query
import enums.QueryStatus

class QueryView(val query: Query) {
    fun get() = query.log + " - " + when (query.status) {
        QueryStatus.pending -> Colors.white("Pending")
        QueryStatus.running -> Colors.yellow("Running")
        QueryStatus.done -> Colors.green("Done")
        QueryStatus.error -> Colors.red("Error")
    }
}
