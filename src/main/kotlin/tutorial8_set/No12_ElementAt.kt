package tutorial8_set

fun main() {
    /*
        取单个元素
        Kotlin 集合提供了一套从集合中检索单个元素的函数。
        此页面描述的函数适用于 list 和 set。

        list 是有序集合。 因此，list 中的每个元素都有其位置可供你引用。
        除了此页面上描述的函数外，list 还提供了更广泛的一套方法去按索引检索和搜索元素。
        有关更多详细信息，请参见 List 相关操作。

        反过来，从定义来看，set 并不是有序集合。
        但是，Kotlin 中的 Set 按某些顺序存储元素。
        这些可以是插入顺序（在 LinkedHashSet 中）、自然排序顺序（在 SortedSet 中）或者其他顺序。
        一组元素的顺序也可以是未知的。 在这种情况下，元素仍会以某种顺序排序，
        因此，依赖元素位置的函数仍会返回其结果。
        但是，除非调用者知道所使用的 Set 的具体实现，否则这些结果对于调用者是不可预测的。
    */

    /*
        1. 按位置取
        为了检索特定位置的元素，有一个函数 elementAt()。
        用一个整数作为参数来调用它，你会得到给定位置的集合元素。
        第一个元素的位置是 0，最后一个元素的位置是 (size - 1)。

        elementAt() 对于不提供索引访问或非静态已知提供索引访问的集合很有用。
        在使用 List 的情况下，使用索引访问操作符 （get() 或 []）更为习惯。
    */
    println("1-------------------------------------------------")
    val numbers = listOf("one", "two", "three", "four", "five", "six")
    println(numbers.elementAt(3))

    val numbersSortedSet = sortedSetOf("one", "two", "three", "four", "five")
    println(numbersSortedSet)
    println(numbersSortedSet.elementAt(0))

    /*
        1.2 按位置取
        还有一些有用的别名来检索集合的第一个和最后一个元素：first() 和 last()。
    */
    println("1.2-------------------------------------------------")
    println(numbers.first())
    println(numbers.last())

    /*
        1.3 为了避免在检索位置不存在的元素时出现异常，请使用 elementAt() 的安全变体：

        当指定位置超出集合范围时，elementAtOrNull() 返回 null。
        elementAtOrElse() 还接受一个 lambda 表达式，该表达式能将一个 Int 参数映射为一个集合元素类型的实例。
        当使用一个越界位置来调用时，elementAtOrElse() 返回对给定值调用该 lambda 表达式的结果。
    */
    println("1.3-------------------------------------------------")
    println(numbers.elementAtOrNull(5))
    println(numbers.elementAtOrElse(5) { index -> "The value for index $index is undefined." })

    /*
        2. 按条件取
        函数 first() 和 last() 还可以让你在集合中搜索，与给定谓词匹配的元素。
        当你使用测试集合元素的谓词调用 first() 时，你会得到对其调用谓词产生 true 的第一个元素。
        反过来，带有一个谓词的 last() 返回与其匹配的最后一个元素。
    */
    println("2-------------------------------------------------")
    println(numbers.first { it.length > 3 })
    println(numbers.last { it.startsWith("f") })

    /*
        2.2 如果没有元素与谓词匹配，两个函数都会抛异常。
        为了避免它们，请改用 firstOrNull() 和 lastOrNull()：
        如果找不到匹配的元素，它们将返回 null。
    */
    println("2.2-------------------------------------------------")
    println(numbers.firstOrNull { it.length > 6 })


    /*
        2.3 或者，如果别名更适合你的情况，那么可以使用别名：

        使用 find() 代替 firstOrNull()
        使用 findLast() 代替 lastOrNull()
    */
    println("2.3-------------------------------------------------")
    val numbers2 = listOf(1, 2, 3, 4)
    println(numbers2.find { it % 2 == 0 })
    println(numbers2.findLast { it % 2 == 0 })

    /*
        3. 随机取元素
        如果需要检索集合的一个随机元素，那么请调用 random() 函数。 你可以不带参数或者使用一个 Random 对象作为随机源来调用它。
    */
    println("3-------------------------------------------------")
    println(numbers2.random())

    // 3.2 On empty collections, random() throws an exception.
    // To receive null instead, use randomOrNull()
    println("3.2-------------------------------------------------")
    println(emptyList<Int>().randomOrNull())

    /*
        4. 检测存在与否
        如需检查集合中某个元素的存在，可以使用 contains() 函数。
        如果存在一个集合元素等于（equals()）函数参数，那么它返回 true。
        你可以使用 in 关键字以操作符的形式调用 contains()。

        如需一次检查多个实例的存在，可以使用这些实例的集合作为参数调用 containsAll()。
    */
    println("4-------------------------------------------------")
    println(numbers.contains("four"))
    println("zero" in numbers)

    println(numbers.containsAll(listOf("four", "two")))
    println(numbers.containsAll(listOf("four", "zero")))

    // 4.2 此外，你可以通过调用 isEmpty() 和 isNotEmpty() 来检查集合中是否包含任何元素。
    println("4.2-------------------------------------------------")
    println(numbers.isEmpty())
    println(numbers.isNotEmpty())

    println(emptyList<Int>().isEmpty())
    println(emptyList<Int>().isNotEmpty())
}
