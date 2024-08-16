package org.example.dayone

import org.example.utils.getLinesFromTextfile

class ExtractNumbersFromTextlinesParser(
    private val fileName: String
) {

    private val wordToNumberCharMapping = mapOf(
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

    fun getSumOfAllLines(): Int {
        return getLinesFromTextfile(fileName)
            .mapNotNull { line ->
                val intCharListFromString = extractNumberCharsFromString(line)
                if (intCharListFromString.isNotEmpty()) {
                    "${intCharListFromString.first()}${intCharListFromString.last()}".toIntOrNull()
                } else null
            }
            .sum()
    }

    private fun extractNumberCharsFromString(str: String): List<String> {
        val pattern =
            "(?i)\\d|(?=(one))|(?=(two))|(?=(three))|(?=(four))|(?=(five))|(?=(six))|(?=(seven))|(?=(eight))|(?=(nine))"
        return Regex(pattern)
            .findAll(str)
            .map {
                val groupList = it.groupValues.toMutableList()
                val iterator = groupList.iterator()
                while (iterator.hasNext()) {
                    val element = iterator.next()
                    if (element.isEmpty()) {
                        iterator.remove()
                    }
                }
                groupList.first()
            }
            .map { mapWordToNumber(it) }
            .toList()
    }

    private fun mapWordToNumber(str: String): String {
        return if (str.length == 1) {
            str
        } else {
            wordToNumberCharMapping[str] ?: throw IllegalArgumentException("No mapping for number name found: $str")
        }
    }
}