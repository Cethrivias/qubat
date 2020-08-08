class Countdown {
    companion object {
        var seconds: Int = 5
    }
}

class ElapsedTime {
    companion object {
        private val startedAt = System.currentTimeMillis()

        fun get() = (System.currentTimeMillis() - startedAt) / 1000
    }
}

val errors = mutableListOf<String>()
