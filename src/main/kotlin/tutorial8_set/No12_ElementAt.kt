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
        1. Slice
        slice() 返回具有给定索引的，集合元素列表。
        索引既可以是作为区间传入的
        也可以是作为整数值的集合传入的。
    */
    println("1-------------------------------------------------")
    val numbers = listOf("one", "two", "three", "four", "five", "six")
    println(numbers.slice(1..3))
    println(numbers.slice(0..4 step 2))
    println(numbers.slice(setOf(3, 5, 0)))

    /*
        2. Take 与 drop
        要从头开始获取指定数量的元素，请使用 take() 函数。
        要从尾开始获取指定数量的元素，请使用 takeLast()。
        当调用的数字大于集合的大小时，两个函数都将返回整个集合。

        要从头或从尾去除给定数量的元素，请调用 drop() 或 dropLast() 函数。
    */
    println("2-------------------------------------------------")
    println(numbers.take(3))
    println(numbers.takeLast(3))
    println(numbers.drop(1))
    println(numbers.dropLast(5))

    /*
        2.2
        还可以使用谓词来定义要获取或去除的元素的数量。 有四个与上述功能相似的函数：
        - takeWhile() 是带有谓词的 take()：
          它将不停获取元素直到排除与谓词匹配的首个元素。
          如果首个集合元素与谓词匹配，则结果为空。

        - takeLastWhile() 与 takeLast() 类似：
          它从集合末尾获取与谓词匹配的元素区间。
          区间的首个元素是与谓词不匹配的，最后一个元素右边的元素。
          如果最后一个集合元素与谓词匹配，则结果为空。

        (不清楚)
        - dropWhile() 与具有相同谓词的 takeWhile() 相反：
          它将首个与谓词不匹配的元素返回到末尾。

        - dropLastWhile() 与具有相同谓词的 takeLastWhile() 相反：
          它返回从开头到最后一个与谓词不匹配的元素。
    */
    println("2.2-------------------------------------------------")
    println(numbers.takeWhile { !it.startsWith('f') })
    println(numbers.takeLastWhile { it != "three" })
    println(numbers.dropWhile { it.length == 3 })
    println(numbers.dropLastWhile { it.contains('i') })

    /*
        3. Chunked
        要将集合分解为给定大小的“块”，请使用 chunked() 函数。
        chunked() 采用一个参数（块的大小），并返回一个 List 其中包含给定大小的 List。
        第一个块从第一个元素开始并包含 size 元素，第二个块包含下一个 size 元素，依此类推。 最后一个块的大小可能较小。
    */
    println("3-------------------------------------------------")
    val numbers2 = (0..13).toList()
    println(numbers2.chunked(3))

    /*
        3.2 立即对返回的块应用转换
        为此，请在调用 chunked() 时将转换作为 lambda 函数提供。
        lambda 参数是集合的一块。
        当通过转换调用 chunked() 时， 这些块是临时的 List，应立即在该 lambda 中使用。
    */
    println("3-------------------------------------------------")
    println(numbers2.chunked(3) { it.sum() })

    /*
        4. windowed
        可以检索给定大小的集合元素中所有可能区间。
        它返回一个元素区间列表，比如通过给定大小的滑动窗口查看集合，则会看到该区间。
    */
    println("4-------------------------------------------------")
    println(numbers.windowed(3))

    /*
        4.1 windowed 通过可选参数提供更大的灵活性：

        - step 定义两个相邻窗口的第一个元素之间的距离。
        默认情况下，该值为 1，因此结果包含从所有元素开始的窗口。
        如果将 step 增加到 2，将只收到以奇数元素开头的窗口：第一个、第三个等。

        - partialWindows 包含从集合末尾的元素开始的较小的窗口。
        例如，如果请求三个元素的窗口，就不能为最后两个元素构建它们。
        在本例中，启用 partialWindows 将包括两个大小为2与1的列表。
        最后，可以立即对返回的区间应用转换。 为此，在调用 windowed() 时将转换作为 lambda 函数提供。
    */
    println("4.1-------------------------------------------------")
    println(numbers2.windowed(3, step = 2, partialWindows = true))
    println(numbers2.windowed(3) { it.sum() })

    /*
        4.2 要构建两个元素的窗口，有一个单独的函数——zipWithNext()。
        它创建接收器集合的相邻元素对。
        请注意，zipWithNext() 不会将集合分成几对；
        它为 每个 元素创建除最后一个元素外的对，因此它在 [1, 2, 3, 4] 上的结果为 [[1, 2], [2, 3], [3, 4]]，而不是 [[1, 2]，[3, 4]]。
        zipWithNext() 也可以通过转换函数来调用；它应该以接收者集合的两个元素作为参数。
    */
    println("4.2-------------------------------------------------")
    println(numbers.zipWithNext())
    println(numbers.zipWithNext(){ s1, s2 -> s1.length > s2.length })
}
