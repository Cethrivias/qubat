package ui

class ArgView(val key: String, val value: String) {
  fun get() = "$key = $value"
}
