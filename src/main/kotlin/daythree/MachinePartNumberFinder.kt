package org.example.daythree

import org.example.utils.getLinesFromTextfile

class MachinePartNumberFinder(
    fileName: String
) {
    private val inputLines = getLinesFromTextfile(fileName)

    init {
        val charMatrix = mutableListOf<List<Char>>()
        inputLines.forEach { line ->
            println(line.map { it })
            charMatrix.add(line.map { it })
        }
        //val charMap: List<List<Char>> = inputLines.map { line -> line.map { it } }
        //println(charMap.joinToString("\n"))

        val machinePartNumbersToAdd = mutableListOf<Int>()

        var yPos = 0
        var xPos = 0

        while (yPos < charMatrix.size) {

            val lastFoundNumberCache = mutableListOf<Char>()
            val currentLine = charMatrix[yPos]
            xPos = 0 // reset x here for next line

            while (xPos < currentLine.size) {

                val character = currentLine[xPos]

                //println("$character is special character: ${isSpecialCharacter(character)}")

                if (isPartOfMachineNumber(character)) {
                    //println("isPartOfMachineNumber: $character")

                    lastFoundNumberCache.add(character)

                    var symbolInNeighboursFound = false

                    // scanNeighbours()
                    // blabla refactor everything to one function
                    if (yPos - 1 > 0) {
                        val topLine = charMatrix[yPos - 1]
                        val topLeftChar = if(xPos - 1 < 0) null else topLine[xPos - 1]
                        val topMiddleChar = topLine[xPos]
                        val topRightChar = if(xPos + 1 < topLine.size) topLine[xPos + 1] else null

                        println("symbolInNeighboursFound in top:     ${listOf(topLeftChar, topMiddleChar, topRightChar)}")
                        if (
                            listOf(
                                isSpecialCharacter(topLeftChar),
                                isSpecialCharacter(topMiddleChar),
                                isSpecialCharacter(topRightChar)
                            )
                                .contains(true)
                        ) {
                            symbolInNeighboursFound = true
                        }
                    }

                    val currentLeftChar = if(xPos - 1 < 0) null else currentLine[xPos - 1]
                    val currentChar = currentLine[xPos]
                    val currentRightChar = if(xPos + 1 < currentLine.size) currentLine[xPos + 1] else null

                    println("symbolInNeighboursFound in current: ${listOf(currentLeftChar, currentChar, currentRightChar)}")
                    if (listOf(
                            isSpecialCharacter(currentLeftChar),
                            isSpecialCharacter(currentChar),
                            isSpecialCharacter(currentRightChar)
                        ).contains(true)
                    ) {
                        symbolInNeighboursFound = true
                    }

                    if (yPos + 1 < charMatrix.size) {
                        val bottomLine = charMatrix[yPos + 1]
                        val bottomLeftChar = if(xPos - 1 < 0) null else bottomLine[xPos - 1]
                        val bottomMiddleChar = bottomLine[xPos]
                        val bottomRightChar = if(xPos + 1 < bottomLine.size) bottomLine[xPos + 1] else null

                        println("symbolInNeighboursFound in bottom:  ${listOf(bottomLeftChar, bottomMiddleChar, bottomRightChar)}")
                        if (listOf(
                                isSpecialCharacter(bottomLeftChar),
                                isSpecialCharacter(bottomMiddleChar),
                                isSpecialCharacter(bottomRightChar)
                            ).contains(true)
                        ) {
                            symbolInNeighboursFound = true
                        }
                    }

                    // here lies the problem I think
                    if (!isPartOfMachineNumber(currentLine[xPos + 1]) && symbolInNeighboursFound) {
                        lastFoundNumberCache.clear()
                    }

                    // look ahead for end of number and compile if there is no further digit
                    if (!isPartOfMachineNumber(currentLine[xPos + 1]) && !symbolInNeighboursFound) {
                        println("lastNumberFoundCache: $lastFoundNumberCache")
                        // compile complete number and add to total
                        machinePartNumbersToAdd.add(lastFoundNumberCache.joinToString("").toInt())
                        // clear cache
                        lastFoundNumberCache.clear()
                    }

                }

                xPos++
            }

            yPos++
        }

        println("machine part numbers: ${machinePartNumbersToAdd}")
        println("sum of all numbers: ${machinePartNumbersToAdd.sum()}")
    }

    private fun isPartOfMachineNumber(character: Char): Boolean {
        return character.digitToIntOrNull() != null
    }

    private fun isSpecialCharacter(character: Char?): Boolean {
        // better use regex, doesn't seem to work on hastag "#"
        return if (character != null) character.toString() != "#" && character.toString() != "." && character.digitToIntOrNull() == null else false
    }
}