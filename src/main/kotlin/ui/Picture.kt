package ui

import Countdown

class Picture {
    private var firstDraw = true
    private val queriesView by lazy {
        QueriesView()
    }

    private val argsView by lazy {
        ArgsView()
    }
    private val countdown = Countdown


    fun paint() {
        var body = argsView.get() + "\n" +
                "-".repeat(10) + "\n" +
                "Starting in ${Colors.red(countdown.seconds.toString())} seconds" + "\n" +
                "-".repeat(10) + "\n" +
                queriesView.get() + "\n" +
                "-".repeat(10) + "\n"

        if (!firstDraw) {
            body = body
                .split("\n")
                .let {
                    Ansi.UP_LINE_START.repeat(it.size - 1) + it.joinToString(
                        prefix = Ansi.CLEAR_LINE,
                        separator = "\n${Ansi.CLEAR_LINE}"
                    )
                }
        } else {
            firstDraw = false
        }

        print(body)
    }

}
