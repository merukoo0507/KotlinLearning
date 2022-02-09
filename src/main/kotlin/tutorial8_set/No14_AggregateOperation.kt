package tutorial8_set

fun main() {
    /*
        1. 集合聚合操作
        Kotlin 集合包含用于常用的 聚合操作 （基于集合内容返回单个值的操作）的函数 。
        其中大多数是众所周知的，并且其工作方式与在其他语言中相同。

        - min() 与 max() 分别返回最小和最大的元素；
        - average() 返回数字集合中元素的平均值；
        - sum() 返回数字集合中元素的总和；
        - count() 返回集合中元素的数量；
    */
    println("1-------------------------------------------------")
    val numbers = listOf(6, 42, 10, 4)
    println("Count: ${numbers.count()}")
    println("Max: ${numbers.max()}")
    println("Min: ${numbers.min()}")
    println("Average: ${numbers.average()}")
    println("Sum: ${numbers.sum()}")

    /*
        1.2 还有一些通过某些选择器函数或自定义 Comparator 来检索最小和最大元素的函数。

        maxBy()/minBy() 接受一个选择器函数并返回使选择器返回最大或最小值的元素。
        maxWith()/minWith() 接受一个 Comparator 对象并且根据此 Comparator 对象返回最大或最小元素。
    */
    println("1.2-------------------------------------------------")
    val min3Remainder = numbers.minBy { it % 3 }
    println("min3Remainder: $min3Remainder")

    val strs = listOf("one", "two", "three", "four")
    val longestString = strs.maxWith(compareBy { it.length })
    println("longestString: $longestString")

    /*
        1.3 有一些高级的求和函数，它们接受一个函数并返回对所有元素调用此函数的返回值的总和：

        sumBy() 使用对集合元素调用返回 Int 值的函数。
        sumByDouble() 与返回 Double 的函数一起使用。
    */
    println("1.3-------------------------------------------------")
    println(numbers.sumBy { it * 2 })
    println(numbers.sumByDouble { it.toDouble() / 2 })

    /*
        2. Fold 与 reduce
        依次操作集合元素并返回累积的结果。
        操作有两个参数：先前的累积值和集合元素。

        这两个函数的区别在于：
        - fold() 接受一个初始值并将其用作第一步的累积值
        - reduce() 的第一步则将第一个和第二个元素作为第一步的操作参数
    */
    println("2-------------------------------------------------")
    val sum = numbers.reduce { sum, element -> sum + element }
    println("sum: $sum")
    val sumDouble = numbers.fold(0) { sum, element -> sum + element * 2 }
    println("sumDouble: $sumDouble")

    // 错误：第一个元素在结果中没有加倍
    val sumDoubleReduce = numbers.reduce{ sum, element -> sum + element * 2 }
    println("sumDoubleReduce: $sumDoubleReduce")

    /*
        2.2 上面的实例展示了区别：fold() 用于计算加倍的元素之和。
        如果将相同的函数传给 reduce()，那么它会返回另一个结果，
        因为在第一步中它将列表的第一个和第二个元素作为参数，所以第一个元素不会被加倍。

        如需将函数以相反的顺序应用于元素，可以使用函数 reduceRight() 和 foldRight()
        它们的工作方式类似于 fold() 和 reduce()，但从最后一个元素开始，然后再继续到前一个元素。
        记住，在使用 foldRight 或 reduceRight 时，操作参数会更改其顺序：第一个参数变为元素，然后第二个参数变为累积值。
     */
    println("2.2-------------------------------------------------")
    val sumDoubledRight = numbers.foldRight(0) { element, sum -> sum + element * 2 }
    println(sumDoubledRight)


    /*
        2.3 你还可以使用将元素索引作为参数的操作。
        为此，使用函数 reduceIndexed() 和 foldIndexed() 传递元素索引作为操作的第一个参数。

        最后，还有将这些操作从右到左应用于集合元素的函数——reduceRightIndexed() 与 foldRightIndexed()。
     */
    println("2.3-------------------------------------------------")
    val sumEven = numbers.foldIndexed(0) { index, acc, i -> if (index % 2 == 0) acc + i else acc }
    println(sumEven)

    val sumEvenRight = numbers.foldRightIndexed(0) { index: Int, i: Int, acc: Int -> if (index % 2 == 0) acc + i else acc }
    println(sumEvenRight)

    /*
        All reduce operations throw an exception on empty collections.
        To receive null instead, use their *OrNull() counterparts:

        reduceOrNull()
        reduceRightOrNull()
        reduceIndexedOrNull()
        reduceRightIndexedOrNull()
     */
}