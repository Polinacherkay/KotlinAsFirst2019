package lesson7.task1

import org.junit.jupiter.api.Test
import java.io.File
import org.junit.jupiter.api.Assertions.assertEquals


class Test {
    private fun assertFileContent(name: String, expectedContent: String) {
        val file = File(name)
        val content = file.readLines().joinToString("\n")
        assertEquals(expectedContent, content)
    }

    @Test
    fun fo() {
        fo("input/many.txt", "X & Y", "temp.txt")
        assertFileContent(
            "temp.txt",
            """-1"""
        )
        File("temp.txt").delete()
    }

    @Test
    fun foo() {
        foo("input/market.txt", "009724 15", "temp.txt")
        assertFileContent(
            "temp.txt",
        """яйца, достаточно, общая стоимость 1380 р"""
        )
        File("temp.txt").delete()

    }
}