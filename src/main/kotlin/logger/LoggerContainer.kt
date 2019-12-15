package logger

import java.util.logging.ConsoleHandler
import java.util.logging.Logger

object LoggerContainer {
    val logger: Logger

    init {
        val mainLogger = Logger.getGlobal()
        mainLogger.setUseParentHandlers(false)
        val handler = ConsoleHandler()
        handler.setFormatter(Formatter())
        mainLogger.addHandler(handler)
        logger = Logger.getGlobal()
    }
}

