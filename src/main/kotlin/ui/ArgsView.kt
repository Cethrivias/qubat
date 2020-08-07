package ui

import config.ConfigContainer

class ArgsView {
    private val argsViews by lazy {
        ConfigContainer.getConfig().args.map { ArgView(it.key, it.value) }
    }

    fun get() = "Args:\n${this.argsViews.joinToString(separator = "\n") { " - ${it.key} = ${it.value}" }}"
}
