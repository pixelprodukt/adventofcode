package org.example

import org.example.dayone.ExtractNumbersFromTextlinesParser
import org.example.daythree.MachinePartNumberFinder
import org.example.daytwo.CubeDataAnalyzer

fun main() {
    /*val extractedNumbersResult = ExtractNumbersFromTextlinesParser("input_day_one.txt").getSumOfAllLines()
    println(extractedNumbersResult)*/

    /*val extractedCubesResult = CubeDataAnalyzer("input_day_two.txt").getPossibleGamesResultPartTwo()
    println(extractedCubesResult)*/

    val machineGearResult = MachinePartNumberFinder("input_day_three.txt").getResultAllMachinePartsPartTwo()
    println(machineGearResult)
}






