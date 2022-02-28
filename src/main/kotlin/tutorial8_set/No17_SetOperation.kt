package tutorial8_set

fun main() {
    /*
        Set 相关操作
        Kotlin 集合包中包含 set 常用操作的扩展函数：查找交集、并集或差集。

        要将两个集合合并为一个（并集），可使用 union() 函数。也能以中缀形式使用 a union b。
        注意，对于有序集合，操作数的顺序很重要：在结果集合中，左侧操作数在前。

        要查找两个集合中都存在的元素（交集），请使用 intersect() 。
        要查找另一个集合中不存在的集合元素（差集），请使用 subtract() 。 这两个函数也能以中缀形式调用，例如， a intersect b 。
    */
    val numbers = listOf("one", "two", "three")
    println(numbers union listOf("four", "five"))
    println(listOf("four", "five") union numbers)

    println(numbers intersect listOf("two", "one"))
    println(numbers subtract listOf("three", "four"))
    println(numbers subtract listOf("four", "three"))   // 相同的输出
}