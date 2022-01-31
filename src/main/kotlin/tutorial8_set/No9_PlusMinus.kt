package tutorial8_set

fun main() {
    /*
        plus 与 minus 操作符
        在 Kotlin 中，为集合定义了 plus (+) 和 minus (-) 操作符。
        它们把一个集合作为第一个操作数；
        第二个操作数可以是一个元素或者是另一个集合。
        返回值是一个新的只读集合：

        plus 的结果包含原始集合 和 第二个操作数中的元素。

        minus 的结果包含原始集合中的元素，但第二个操作数中的元素 除外。
        如果第二个操作数是一个元素，那么 minus 移除其在原始集合中的 第一次 出现；
        如果是一个集合，那么移除其元素在原始集合中的 所有 出现。
    */
    println("1-------------------------------------------------")
    val numbers = listOf("one", "two", "three", "four")
    val plusList = numbers + "five"
    val minusList = numbers - listOf("three", "four")
    println("plusList: $plusList")
    println("minusList: $minusList")


    /*
        1.2 有关 map 的 plus 和 minus 操作符的详细信息，请参见 Map 相关操作。
        也为集合定义了广义赋值操作符 plusAssign (+=) 和 minusAssign (-=)。

        然而，对于只读集合，它们实际上使用 plus 或者 minus 操作符并尝试将结果赋值给同一变量。

        因此，它们仅在由 var 声明的只读集合中可用。
        对于可变集合，如果它是一个 val，那么它们会修改集合。更多详细信息请参见集合写操作。
     */
    println("1.2-------------------------------------------------")

    // Error
//    val plusList2 = listOf<String>()
//    plusList2 += numbers
    var plusList2 = listOf<String>()
    plusList2 += "one"
    val minusList2 = mutableListOf<String>()
    minusList2 -= numbers
    println("plusList2: $plusList2")
    println("minusList2: $minusList2")
}
