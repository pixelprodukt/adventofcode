package org.example.daytwo

data class GameOfCubes(val id: Int, val setsOfCubesList: List<List<SetOfCubes>>)

data class SetOfCubes(val quantity: Int, val color: CubeColor)

enum class CubeColor {
    RED, BLUE, YELLOW, GREEN, BLACK
}