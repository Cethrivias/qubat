package ui

class Colors {
    companion object {
        private var default = "${Ansi.ESC}[37m"
        fun red(text: String) = "${Ansi.ESC}[31m$text$default"
        fun yellow(text: String) = "${Ansi.ESC}[33m$text$default"
        fun green(text: String) = "${Ansi.ESC}[32m$text$default"
        fun white(text: String) = "${Ansi.ESC}[37m$text$default"
        fun blue(text: String) = "${Ansi.ESC}[94m$text$default"
    }
}
