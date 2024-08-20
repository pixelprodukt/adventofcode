package org.example.daythree

import org.example.utils.getLinesFromTextfile

class MachinePartNumberFinder(
    fileName: String
) {
    private val inputLines = getLinesFromTextfile(fileName)

    init {
        val charMatrix = inputLines.map { it.toList() }

        val machinePartNumbersToAdd = mutableListOf<Int>()
        var yPos = 0
        var xPos = 0

        while (yPos < charMatrix.size) {
            val lastFoundNumberCache = mutableListOf<Char>()
            val currentLine = charMatrix[yPos]
            val symbolsFound = mutableListOf<Boolean>()
            xPos = 0 // reset x here for next line

            while (xPos < currentLine.size) {

                val character = currentLine[xPos]

                if (isPartOfMachineNumber(character)) {

                    lastFoundNumberCache.add(character)

                    // scanNeighbours

                    // top line
                    if (yPos - 1 > 0) {
                        val topLine = charMatrix[yPos - 1]
                        symbolsFound.add(lineHasSymbol(topLine, xPos))
                    }

                    // current line
                    symbolsFound.add(lineHasSymbol(currentLine, xPos))

                    // bottom line
                    if (yPos + 1 < charMatrix.size) {
                        val bottomLine = charMatrix[yPos + 1]
                        symbolsFound.add(lineHasSymbol(bottomLine, xPos))
                    }

                    // look ahead for end of number and compile if there is no further digit
                    if (!isPartOfMachineNumber(currentLine.getOrNull(xPos + 1)) && symbolsFound.contains(true)) {
                        println("lastNumberFoundCache: $lastFoundNumberCache")
                        // compile complete number and add to total
                        machinePartNumbersToAdd.add(lastFoundNumberCache.joinToString("").toInt())
                        // clear cache
                        lastFoundNumberCache.clear()
                        symbolsFound.clear()
                    } else if (!isPartOfMachineNumber(currentLine.getOrNull(xPos + 1)) && !symbolsFound.contains(true)) {
                        lastFoundNumberCache.clear()
                        symbolsFound.clear()
                    }
                }

                xPos++
            }

            yPos++
        }

        println("machine part numbers: ${machinePartNumbersToAdd}")
        println("sum of all numbers: ${machinePartNumbersToAdd.sum()}") // should return 520019
    }

    private fun isPartOfMachineNumber(character: Char?): Boolean {
        return character?.digitToIntOrNull() != null
    }

    private fun isSpecialCharacter(character: Char?): Boolean {
        return character != null && character in "!@#$%&*()/_+=|<>?{}[]~-"
    }

    private fun lineHasSymbol(line: List<Char>, xPos: Int): Boolean {
        val leftChar = line.getOrNull(xPos - 1) ?: '.'
        val middleChar = line[xPos]
        val rightChar = line.getOrNull(xPos + 1) ?: '.'

        return listOf(leftChar, middleChar, rightChar).any { isSpecialCharacter(it) }
    }

    private fun checkNeighborsForSymbols(charMatrix: List<List<Char>>, yPos: Int, xPos: Int): Boolean {
        val topLine = charMatrix.getOrNull(yPos - 1)
        val currentLine = charMatrix[yPos]
        val bottomLine = charMatrix.getOrNull(yPos + 1)

        return listOf(topLine, currentLine, bottomLine).any { line ->
            line?.let { lineHasSymbol(it, xPos) } == true
        }
    }
}