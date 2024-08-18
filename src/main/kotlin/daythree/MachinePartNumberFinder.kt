package org.example.daythree

import org.example.utils.getLinesFromTextfile
import kotlin.streams.toList

class MachinePartNumberFinder(
    fileName: String
) {
    private val inputLines = getLinesFromTextfile(fileName)

    init {
        val charMap = mutableListOf<List<Char>>()
        inputLines.forEach { line ->
            println(line.map { it })
            charMap.add(line.map { it })
        }
        //val charMap: List<List<Char>> = inputLines.map { line -> line.map { it } }
        //println(charMap.joinToString("\n"))

        var yIndex = 0
        var xIndex = 0

        while (yIndex < charMap.size) {
            //println(y)

            val line = charMap[yIndex]
            xIndex = 0
            /*val TOP = 0
            val BOTTOM = charMap.size - 1
            val START_OF_LINE = 0
            val END_OF_LINE = line.size - 1*/

            while (xIndex < line.size) {

                val character = line[xIndex]

                if (!isPartOfMachineNumber(character)) {
                    xIndex++
                    continue
                }

                val charsList = mutableListOf<Char>()
                charsList.add(character)

                var nextIsNumber = true
                var lengthOfMachineNumber = 0

                while (nextIsNumber) {
                    lengthOfMachineNumber++
                    nextIsNumber = line[xIndex + lengthOfMachineNumber].digitToIntOrNull() != null
                    /*if (nextIsNumber) {
                        charsList.add(line[xIndex + lengthOfMachineNumber])
                    } else {
                        val number = charsList.joinToString("").toInt()
                        println("joined: $number")
                    }*/
                    //println("nextIsNumber: $nextIsNumber")
                }

                println("length of number: $lengthOfMachineNumber")

                /**
                 * if (character is Int) {
                 *      check if next (line[y][x+1]) is Int
                 *      check if previous was dot (line[y][x-1])
                 *      check if above is dot (line[y-1][x])
                 *      etc.
                 * }
                 */
                xIndex += lengthOfMachineNumber
                println("New index: $xIndex")
            }

            yIndex++
        }
    }

    private fun isPartOfMachineNumber(character: Char): Boolean {
        println("'$character' is part of the machine number: ${character.digitToIntOrNull() != null}")
        return character.digitToIntOrNull() != null
    }
}