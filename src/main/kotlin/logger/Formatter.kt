package logger

import java.util.*
import java.util.logging.LogRecord
import java.util.logging.SimpleFormatter

class Formatter : SimpleFormatter() {
    @Synchronized
    override fun format(lr: LogRecord): String =
        String.format(
            "[%1\$tF %1\$tT] - [%2$-7s]: %3\$s %n",
            Date(lr.millis),
            lr.level.localizedName,
            lr.message
        )
}
