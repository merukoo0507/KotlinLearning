package tutorial8_set

fun main() {
    /*
        1. 区间与数列

        kotlin.ranges 包中的 rangeTo() 函数及其操作符形式的 .. 轻松地创建两个值的区间。
        通常，rangeTo() 会辅以 in 或 !in 函数。。
    */
    println("1-------------------------------------------------")
    val range = 1..10
    println(range)
    println(range.javaClass)

    val i = 2
    if (i in 1..4) {
        print(i)
    }

    /*
        整数类型区间（IntRange、LongRange、CharRange）还有一个拓展特性：可以对其进行迭代。
        这些区间也是相应整数类型的"等差"数列。
        这种区间通常用于 for 循环中的迭代
     */
    for(i in 1..4) {
        print("$i ")
    }
    println()

    // 要反向迭代数字，请使用 downTo 函数而不是 ..
    for (i in 4 downTo 1) {
        print("$i ")
    }
    println()

    // 也可以通过任意step（不一定为 1 ）迭代数字
    for (i in 1..10 step 2) {
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
//    val versionRange = Version(1, 11)..Version(1, 30)
//    println(Version(0, 9) in versionRange)
//    println(Version(1, 20) in versionRange)


    /*
        3. 数列
        如上个示例所示，整数类型的区间（例如 Int、Long 与 Char）可视为等差数列。
        这些数列由特殊类型定义：IntProgression、LongProgression 与 CharProgression。

        数列具有三个基本属性：first 元素、last 元素和一个非零的 step。 首个元素为 first，后续元素是前一个元素加上一个 step。
        以确定的步长在数列上进行迭代等效于 Java/JavaScript 中基于索引的 for 循环。
     */
    println("3-------------------------------------------------")
    // 通过迭代数列隐式创建区间时，此数列的 first 与 last 元素是区间的端点，step 为 1
    for (i in 1..10) print(i)
    println()

    // 要指定数列步长，请在区间上使用 step 函数
    for (i in 1..10 step 2) print(i)
    println()

    // last 元素并非总与指定的结束值相同
    for (i in 1..9 step 3) print(i) // 最后一个元素是 7
    println()

    // 要创建反向迭代的数列，请在定义其区间时使用 downTo 而不是 ..
    for (i in 4 downTo 1) print(i)
    println()

}