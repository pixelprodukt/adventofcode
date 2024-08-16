package org.example.utils

import java.nio.file.Paths

fun getLinesFromTextfile(fileName: String): List<String> {
    val filePath = Paths.get("src/main/resources", fileName).toFile()

    return try {
        filePath.readLines()
    } catch (e: Exception) {
        println("An error occurred: ${e.message}")
        emptyList()
    }
}