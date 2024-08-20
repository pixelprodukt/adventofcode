package org.example.daythree

import org.example.utils.getLinesFromTextfile

class MachinePartNumberFinder(
    fileName: String
) {
    private val inputLines = getLinesFromTextfile(fileName)

    init {
        val charMatrix = mutableListOf<MutableList<Char>>()
        inputLines.forEach { line ->
            println(line.map { it })
            charMatrix.add(line.map { it }.toMutableList())
        }

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
        val pattern = "[!@#$%&*()/_+=|<>?{}\\[\\]~-]"
        return Regex(pattern).containsMatchIn(character.toString())
    }

    private fun lineHasSymbol(line: List<Char>, xPos: Int): Boolean {
        val leftChar = if(xPos - 1 < 0) ".".toCharArray().first() else line[xPos - 1]
        val middleChar = line[xPos]
        val rightChar = if(xPos + 1 < line.size) line[xPos + 1] else ".".toCharArray().first()

        println("symbolInNeighboursFound in bottom:  ${listOf(leftChar, middleChar, rightChar)}")

        return listOf(
            isSpecialCharacter(leftChar),
            isSpecialCharacter(middleChar),
            isSpecialCharacter(rightChar)
        ).contains(true)
    }
}