package ui

class Ansi {
    companion object{
        const val ESC = "\u001B"
        const val SCREEN_BUFFER = "$ESC[?1049h$ESC[H"
        const val SCREEN_BUFFER_OUT = "$ESC[?1049l"
        const val UP_LINE_START = "$ESC[F"
        const val UP_LINE = "$ESC[A"
        const val CLEAR_LINE = "$ESC[2K"
    }
}
