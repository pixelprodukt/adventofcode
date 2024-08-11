package org.example

import java.io.BufferedReader
import java.io.FileReader

fun main() {
    val codeList = getLinesFromTextfile("input_day_one.txt")
    val sumList = mutableListOf<Int>()

    codeList.forEach {
        val intListFromString = extractNumberCharsFromString(it)
        val sum = "${intListFromString.first()}${intListFromString.last()}"
        sumList.add(sum.toInt())
        println(extractNumberCharsFromString(it))
    }

    val total = sumList.sum()
    println("The sumList is: ${sumList}")
    println("The total is: ${total}") // should output 55358
}

fun extractNumberCharsFromString(str: String): List<String> {
    val numberNames = listOf(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
    )

    return Regex("(?i)\\d|(?=(one))|(?=(two))|(?=(three))|(?=(four))|(?=(five))|(?=(six))|(?=(seven))|(?=(eight))|(?=(nine))")
        .findAll(str)
        .map {
            val groupList = it.groupValues.toMutableList()
            val iterator = groupList.iterator()
            while (iterator.hasNext()) {
                val element = iterator.next()
                if (element == null || element.isEmpty()) {
                    iterator.remove()
                }
            }
            groupList.first()
        }
        .map { mapWordToNumber(it) }
        .toList()
}

fun mapWordToNumber(str: String): String {
    val mapping = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9",
    )
    if (str.length == 1) {
        return str
    }
    return mapping.getOrElse(str) {
        throw Exception("No mapping for number name found: $str")
    }
}

fun getLinesFromTextfile(fileName: String): List<String> {
    val lines = mutableListOf<String>()

    val basePath = "src/main/resources/"
    val filePath = basePath + fileName
    var reader: BufferedReader? = null

    try {
        reader = BufferedReader(FileReader(filePath))
        var line: String?

        while (reader.readLine().also { line = it } != null) {
            lines.add(line!!)
        }
    } catch (e: Exception) {
        println("An error occured: ${e.message}")
    } finally {
        try {
            reader?.close()
        } catch (e: Exception) {
            println("An error occurred while closing the file: ${e.message}")
        }
    }
    return lines.toList()
}
