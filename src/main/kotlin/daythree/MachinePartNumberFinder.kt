package org.example.daythree

import org.example.utils.getLinesFromTextfile

class MachinePartNumberFinder(
    fileName: String
) {
    private val inputLines = getLinesFromTextfile(fileName)

    // should be 520019
    fun getResultAllMachinePartsPartOne(): Int {
        val charMatrix = inputLines.map { it.toList() }
        return findAndAddAllMachineParts(charMatrix)
    }

    // should be
    fun getResultAllMachinePartsPartTwo(): Int {
        val charMatrix = inputLines.map { it.toList() }
        return findAllAndAddGearRatios(charMatrix)
    }

    /**
     * Wrong
     */
    private fun findAllAndAddGearRatios(matrix: List<List<Char>>): Int {
        val dataScannerPosition = Position(0, 0)

        while (dataScannerPosition.y < matrix.size) {
            val currentLine = matrix[dataScannerPosition.y]

            println("currentLine: $currentLine")

            dataScannerPosition.resetX()

            while (dataScannerPosition.x < currentLine.size) {
                val character = currentLine[dataScannerPosition.x]

                if (isTimesOperator(character)) {

                    val linesAroundGear = listOf(
                        matrix.getOrNull(dataScannerPosition.y - 1),
                        matrix.getOrNull(dataScannerPosition.y),
                        matrix.getOrNull(dataScannerPosition.y + 1)
                    )

                    linesAroundGear.forEach { line ->
                        val charTopLeft = line?.getOrNull(dataScannerPosition.x - 1)
                        if (line != null && charTopLeft != null && isDigit(charTopLeft)) {
                            val gearDigits = mutableListOf(charTopLeft)

                            // possibly new position object?
                            var startingIndex = dataScannerPosition.x - 1
                            var nextLeftIndex = startingIndex - 1

                            while (isNextLeftNumber(startingIndex, nextLeftIndex, line)) {
                                gearDigits.add(0, line[nextLeftIndex])
                                startingIndex--
                                nextLeftIndex--
                            }
                        }
                    }

                    // [-1, -1][0, -1][1, -1]
                    // [-1,  0][0,  0][1,  0]
                    // [-1,  1][0,  1][1,  1]

                    /**
                     * positionsOfNeighbours = listOf(
                     *  Position(-1, -1),
                     *  Position(0, -1),
                     *  Position(1, -1),
                     *  Position(-1, 0),
                     *  Position(1, 0),
                     *  Position(-1, 1),
                     *  Position(0, 1),
                     *  Position(1, 1)
                     * )
                     */


                    // go left as long you find a digit
                    // put every found digit at the start of gearDigits
                    // afterwards go right as long as you find a digit
                    // put every found digit at the end of gearDigits
                }
                dataScannerPosition.x++
            }
            dataScannerPosition.y++
        }
        return 0
    }

    private fun isNextLeftNumber(startingIndex: Int, nextLeftIndex: Int, currentLine: List<Char>): Boolean {
        return startingIndex >= 0 && currentLine.getOrNull(nextLeftIndex) != null && isDigit(currentLine[nextLeftIndex])
    }

    private fun findLeftDigits(line: List<Char>, startingIndex: Int, gearDigits: MutableList<Char>) {
        val nextLeftIndex = startingIndex - 1
        while (startingIndex >= 0 && line.getOrNull(nextLeftIndex) != null) {
            if (isDigit(line[nextLeftIndex])) {
                gearDigits.add(0, line[nextLeftIndex])
            }
        }
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
                        //println("lastNumberFoundCache: $lastFoundNumberCache")
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