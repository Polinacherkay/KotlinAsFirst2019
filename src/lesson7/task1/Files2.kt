package lesson7.task1

import java.io.File
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import kotlin.coroutines.coroutineContext


fun fo(inputName: String, expr: String, outputName: String) {
    try {
        File(inputName).bufferedReader()
    }
    catch (e: IOException) {
        throw IOException()
    }
    if (!inputName.contains(Regex("""[^\w+=, \-]"""))) throw IllegalArgumentException()
    if (!expr.contains(Regex("""[^\d+ &]"""))) throw IllegalArgumentException()
    val check = expr.split(" & ")
    val list1 = mutableListOf<String>()
    val list2 = mutableListOf<String>()
    val r = File(inputName).readLines()
    for (line in r) {
        val s = line.split(" = ")
        if (check[0] == (s[0]))  list1.add(s[1])
        if (check[1] == (s[0])) list2.add(s[1])
    }
    if (list1.isEmpty() && list2.isEmpty()) throw IllegalArgumentException()
    val result = File(outputName).bufferedWriter()
    val list3 = list1[0].split(", ")
    val list4 = list2[0].split(", ")
    for (i in list3) {
        if (list4.contains(i)) result.write(i)
    }
    result.close()
}

fun foo(inputName: String, query: String, outputName: String) {
    try {
        File(inputName).bufferedReader()
    }
    catch (e: IOException) {
        throw IOException()
    }
    if (!inputName.contains(Regex("""[\w+,\d+ \:]"""))) throw IllegalArgumentException()
    if (!query.contains(Regex("""[ \d+\*]"""))) throw IllegalArgumentException()
    val result = File(outputName).bufferedWriter()
    if (!query.contains(Regex("""\*"""))) {
        val check = query.split(" ")
        var e = ""
        val list = File(inputName).readLines()
        for (line in list) {
            val l = line.split(": ")
            if (l[0].toInt() == check[0].toInt()) e = l[1]
        }
        val r = e.split(", ").toMutableList()
        r[1] = r[1].split(" ")[0]
        r[2] = r[2].split(" ")[0]
        result.write(r[0])
        result.write(", ")
        if (check[1].toInt() <= r[2].toInt()) { result.write("достаточно")
            result.write(", ")
            val q = (r[1].toInt() * r[2].toInt()).toString()
            result.write("общая стоимость ")
            result.write(q)
            result.write(" р")
        } else result.write("недостаточно")
        result.close()
    } else

}
