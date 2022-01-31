package tutorial8_set

fun main() {
    /*
        过滤
        接受一个集合元素并且返回布尔值的 lambda 表达式：true 匹配，false 不匹配。

        通过单个调用就可以过滤集合的扩展函数。
        这些函数不会改变原始集合，因此它们既可用于可变集合也可用于只读集合。
        过滤结果，应该在过滤后将其赋值给变量或链接其他函数。
    */

    /*
        1. 按谓词过滤
        filter()
        基本的过滤函数，返回与其匹配的集合元素。
        对于 List 和 Set，过滤结果都是一个 List，对 Map 来说结果还是一个 Map。
    */
    println("1-------------------------------------------------")
    val numbers = listOf("one", "two", "three", "four")
    val longerThan3 = numbers.filter { it.length > 3 }
    println("longerThan3: $longerThan3")

    val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key11" to 11)
    val filterMap = numbersMap.filter { (key, value) -> key.endsWith("1") && value > 10 }
    println("filterMap: $filterMap")

    /*
        1.2 filter() 只能检查元素的值。
        filterIndexed()，在过滤中使用元素在集合中的位置，带有两个参数的谓词：元素的索引和元素的值。

        如果想使用否定条件来过滤集合，请使用 filterNot()。
        它返回一个让谓词产生 false 的元素列表。
     */
    println("1.2-------------------------------------------------")
    val filteredIdx = numbers.filterIndexed { index, s -> (index != 0) && (s.length < 5) }
    val filteredNot = numbers.filterNot { it.length <= 3 }
    println("filteredIdx: $filteredIdx")
    println("filteredNot: $filteredNot")

    /*
        1.3 还有一些函数能够通过过滤给定类型的元素来缩小元素的类型：
        filterIsInstance() 返回给定类型的集合元素。
        在一个 List<Any> 上被调用时，filterIsInstance<T>() 返回一个 List<T>，从而让你能够在集合元素上调用 T 类型的函数。
     */
    println("1.3-------------------------------------------------")
    val numbers2 = listOf(null, 1, "two", 3.0, "four")
    println("All String elements: ")
    println(numbers2.filterIsInstance<String>())

    /*
        1.4 filterNotNull() 返回所有的非空元素。
        在一个 List<T?> 上被调用时，filterNotNull() 返回一个 List<T: Any>，从而让你能够将所有元素视为非空对象
     */
    println("1.4-------------------------------------------------")
    println(numbers2.filterNotNull())

    /*
        2. 划分
        另一个过滤函数 – partition()
        通过一个谓词过滤集合并且将不匹配的元素存放在一个单独的列表中。
        因此，你得到一个Pair包含兩個List，作为返回值：
        第一个列表包含与谓词匹配的元素并且第二个列表包含原始集合中的所有其他元素
    */
    println("2-------------------------------------------------")
    val (match, rest) = numbers.partition { it.length > 3 }
    println("match: $match")
    println("rest: $rest")

    /*
        3. 检验谓词
        最后，有些函数只是针对集合元素简单地检测一个谓词：

        如果至少有一个元素匹配给定谓词，那么 any() 返回 true。
        如果没有元素与给定谓词匹配，那么 none() 返回 true。
        如果所有元素都匹配给定谓词，那么 all() 返回 true。注意，
        在一个空集合上使用任何有效的谓词去调用 all() 都会返回 true 。这种行为在逻辑上被称为 vacuous truth。
    */
    println("3-------------------------------------------------")
    println(numbers.any { it.endsWith("e") })
    println(numbers.none { it.endsWith("e") })
    println(numbers.all { it.endsWith("e") })

    println(emptyList<String>().all { it.endsWith("e") })

    /*
        3.2 any() 和 none() 也可以不带谓词使用：在这种情况下它们只是用来检查集合是否为空。
        如果集合中有元素，any() 返回 true，否则返回 false；none() 则相反。
     */
    println("3.2-------------------------------------------------")
    println(numbers.any())
    println(numbers.none())
    println(emptyList<Int>().any())
    println(emptyList<Int>().none())
}
