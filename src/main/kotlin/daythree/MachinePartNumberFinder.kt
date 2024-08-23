package org.example.daythree

import org.example.utils.getLinesFromTextfile

class MachinePartNumberFinder(
    fileName: String
) {
    private val inputLines = getLinesFromTextfile(fileName)

    init {
        val charMatrix = inputLines.map { it.toList() }

        val sumOfMachinePartNumbers = findAndAddAllMachineParts(charMatrix)
        println("sum of all numbers: ${sumOfMachinePartNumbers}") // should be 520019

        val sumOfGearNumbers = findAllAndAddGearRatios(charMatrix)
        println("sum of all gear ratios: ${sumOfGearNumbers}") // should be
    }

    /**
     * Wrong
     */
    private fun findAllAndAddGearRatios(matrix: List<List<Char>>): Int {
        var yPos = 0
        var xPos = 0

        while (yPos < matrix.size) {
            val line = matrix[yPos]

            while (xPos < line.size) {
                val character = line[xPos]

                if (isTimesOperator(character)) {
                    val topLine = matrix.getOrNull(yPos - 1)
                    val charTopLeft = topLine?.getOrNull(xPos - 1)

                    if (topLine != null && charTopLeft != null) {
                        if (isDigit(charTopLeft)) {
                            val gearDigits = mutableListOf(charTopLeft)
                            var xLeftIndex = 0

                            // go left as long you find a digit
                            // put every found digit at the start of gearDigits
                            // afterwards go right as long as you find a digit
                            // put every found digit at the end of gearDigits
                        }
                    }
                }

                xPos++
            }

            yPos++
        }

        return 0
    }

    private fun isDigit(character: Char): Boolean {
        return character.toString().toIntOrNull() is Int
    }

    private fun isTimesOperator(character: Char): Boolean {
        return character.toString() == "*"
    }

    /**
     * A machine part number is a number from the input, which is adjacent to a special symbol
     * like '$', '*' or '#' for example. The sum of all those numbers is the solution.
     */
    private fun findAndAddAllMachineParts(matrix: List<List<Char>>): Int {
        val machinePartNumbersToAdd = mutableListOf<Int>()
        var yPos = 0
        var xPos = 0

        while (yPos < matrix.size) {
            val lastFoundNumberCache = mutableListOf<Char>()
            val currentLine = matrix[yPos]
            val symbolsFound = mutableListOf<Boolean>()
            xPos = 0 // reset x here for next line

            while (xPos < currentLine.size) {

                val character = currentLine[xPos]

                if (isPartOfMachineNumber(character)) {

                    lastFoundNumberCache.add(character)

                    // scanNeighbours

                    // top line
                    if (yPos - 1 > 0) {
                        val topLine = matrix[yPos - 1]
                        symbolsFound.add(lineHasSymbol(topLine, xPos))
                    }

                    // current line
                    symbolsFound.add(lineHasSymbol(currentLine, xPos))

                    // bottom line
                    if (yPos + 1 < matrix.size) {
                        val bottomLine = matrix[yPos + 1]
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
        return machinePartNumbersToAdd.sum()
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
}