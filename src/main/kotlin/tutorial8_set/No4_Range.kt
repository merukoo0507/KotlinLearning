package tutorial8_set

fun main() {
    /*
        1. 区间与数列

        kotlin.ranges 包中的 rangeTo() 函数及其操作符形式的 .. 轻松地创建两个值的区间。
        通常，rangeTo() 会辅以 in 或 !in 函数。。
    */
    println("1-------------------------------------------------")
    val range = 1 .. 10
    println(range)
    println(range.javaClass)

    val i = 2
    if (i in 1 .. 4) {
        print(i)
    }

    /*
        整数类型区间（IntRange、LongRange、CharRange）还有一个拓展特性：可以对其进行迭代。
        这些区间也是相应整数类型的"等差"数列。
        这种区间通常用于 for 循环中的迭代
     */
    for(i in 1 .. 4) {
        print("$i ")
    }
    println()

    // 要反向迭代数字，请使用 downTo 函数而不是 ..
    for (i in 4 downTo 1) {
        print("$i ")
    }
    println()

    // 也可以通过任意step（不一定为 1 ）迭代数字
    for (i in 1 .. 10 step 2) {
        print("$i ")
    }
    println()

    for (i in 10 downTo 1 step 2) {
        print("$i ")
    }
    println()

    // 要迭代不包含其结束元素的数字区间，请使用 until 函数：
    for (i in 1 until 10) { // i in [1, 10), 10被排除
        print("$i ")
    }
    println()


    /*
        (不清楚...)
        2. 区间
        要为类创建一个区间，请在区间起始值上调用 rangeTo() 函数，并提供结束值作为参数。
        rangeTo() 通常以操作符 .. 形式调用。
     */
    println("2-------------------------------------------------")
//    val versionRange = Version(1, 11)..Version(1, 30)
//    println(Version(0, 9) in versionRange)
//    println(Version(1, 20) in versionRange)


    /*
        3. 数列
        (不清楚...)
        要为类创建一个区间，请在区间起始值上调用 rangeTo() 函数，并提供结束值作为参数。
        rangeTo() 通常以操作符 .. 形式调用。
     */
}