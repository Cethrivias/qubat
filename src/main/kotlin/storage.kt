import config.ConfigContainer
import config.Query

val queries: HashMap<String, Query> by lazy {
    val queries = HashMap<String, Query>()
    ConfigContainer.getConfig().queries.map { db ->
        db.value.forEach {
            queries[it.log] = it
        }
    }
    queries
}

class Countdown {
    companion object {
        var seconds: Int = 5
    }
}
