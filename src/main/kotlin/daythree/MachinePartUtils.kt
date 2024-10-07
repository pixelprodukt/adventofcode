package org.example.daythree

data class Position(var x: Int, var y: Int) {
    fun skipNextX() {
        x += 1
    }
    fun resetX() {
        x = 0
    }
}