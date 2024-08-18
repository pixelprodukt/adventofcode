package org.example.daytwo

import org.example.utils.getLinesFromTextfile

// refactor me please
class CubeDataAnalyzer(
    fileName: String
) {
    private val inputLines = getLinesFromTextfile(fileName)
    private val rulesForPossibleGames = listOf(SetOfCubes(12, CubeColor.RED), SetOfCubes(13, CubeColor.GREEN), SetOfCubes(14, CubeColor.BLUE))

    init {
        val games = parseInputLines(inputLines)
        val possibleCubeColors = getPossibleCubeColors(rulesForPossibleGames)
        val possibleGames = games.filter { game -> isPossible(game, possibleCubeColors) }
        val result = possibleGames.map { game -> game.id }.sum()
        //println("The result is: $result") // should return 2239
        val resultTwo = games.map { game -> getFewestPossibleCubeSets(game, possibleCubeColors)}.sum()
        println(resultTwo) // should return 83435
    }

    fun parseInputLines(lines: List<String>): List<GameOfCubes> {
        val gamesOfCubes = lines.map { line ->
            val splitted = line.split(":")
            val gameWithIdString = splitted.first()
            val setsOfCubesString = splitted.last().trim()
            val setOfCubesString = setsOfCubesString.split(";").map { it.trim() }
            val setsOfCubes = setOfCubesString.map { set -> set.split(",") }

            val setsOfCubesList = setsOfCubes.map { set ->
                val trimmed = set.map { it.trim() }
                //println(trimmed)
                trimmed.map { numberOfCubesAndColor ->
                    val splittedNumberAndColor = numberOfCubesAndColor.split(" ").map { it.trim() }
                    //println(splittedNumberAndColor)
                    val numberOfCubes = splittedNumberAndColor.first().toInt()
                    val color = splittedNumberAndColor.last()
                    SetOfCubes(numberOfCubes, CubeColor.valueOf(color.uppercase()))
                }
            }
            //println(setsOfCubesList)

            val gameId = gameWithIdString.filter { it.isDigit() }.toInt() //Regex("\\d").findAll(gameWithIdString, 0).map { it.value }.first().toInt()
            val game = GameOfCubes(
                gameId,
                setsOfCubesList
            )

            //println(setsOfCubes)
            return@map game
        }
        // println(gamesOfCubes)
        return gamesOfCubes
    }

    private fun getPossibleCubeColors(setsOfCubes: List<SetOfCubes>): List<CubeColor> {
        return setsOfCubes.map { set -> set.color }.distinct()
    }

    private fun getFewestPossibleCubeSets(game: GameOfCubes, possibleColors: List<CubeColor>): Int {
        val setsOfCubesOneColor = possibleColors.map { color ->
            game.setsOfCubesList.map { setsOfCubes ->
                setsOfCubes.filter { set -> set.color == color }
            }.flatten()
        }
        //println(setsOfCubesOneColor)

        val sorted = setsOfCubesOneColor.map { setsOfCubes ->
            setsOfCubes.sortedBy { set -> set.quantity }.last()
        }
        //println(sorted)
        val powerOfSets = sorted.map { it.quantity }.reduce(Int::times)

        return powerOfSets
    }

    private fun isPossible(game: GameOfCubes, possibleColors: List<CubeColor>): Boolean {
        game.setsOfCubesList.forEach { setsOfCubes ->
            setsOfCubes.forEach { set ->
                if (set.color !in possibleColors) {
                    println("Impossible color: ${set.color} for set: $set")
                    return false
                }
                if (rulesForPossibleGames.any { rule -> rule.color == set.color && set.quantity > rule.quantity }) {
                    return false
                }
            }
        }
        return true
    }
}