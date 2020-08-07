package ui

import Countdown

class Picture {
    private val queriesView by lazy {
        QueriesView()
    }

    private val argsView by lazy {
        ArgsView()
    }
    private val countdown = Countdown

    fun paint() {
        print("${Ansi.ESC}[3J${Ansi.ESC}[H")
        println(argsView.get())
        println("-".repeat(10))
        println("Starting in ${Colors.red(countdown.seconds.toString())} seconds")
        println("-".repeat(10))
        println(queriesView.get())
        println("-".repeat(10))
    }
}
