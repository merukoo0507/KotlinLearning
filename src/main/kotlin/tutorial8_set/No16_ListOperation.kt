package tutorial8_set

fun main() {
    /*
        List 相关操作
        List 是 Kotlin 标准库中最受欢迎的集合类型。对列表元素的索引访问为 List 提供了一组强大的操作。
    */

    /*
        1. 按索引取元素
        List 支持按索引取元素的所有常用操作：
        elementAt() 、 first() 、 last() 与取单个元素中列出的其他操作。

        List 的特点是能通过索引访问特定元素，因此读取元素的最简单方法是按索引检索它。
        这是通过 get() 函数或简写语法 [index] 来传递索引参数完成的。

        如果 List 长度小于指定的索引，则抛出异常。 另外，还有两个函数能避免此类异常：
        getOrElse() 提供用于计算默认值的函数，如果集合中不存在索引，则返回默认值。
        getOrNull() 返回 null 作为默认值。
    */
    println("1-------------------------------------------------")
    val numbers = listOf(1, 2, 3, 4)
    println(numbers.get(0))
    println(numbers[0])
    // numbers.get(5)  // exception!
    println(numbers.getOrNull(5))
    println(numbers.getOrElse(5) { it })

    /*
        2. 取列表的一部分
        除了取集合的一部分中常用的操作， List 还提供 subList() 该函数将指定元素范围的视图作为列表返回。
        因此，如果原始集合的元素发生变化，则它在先前创建的子列表中也会发生变化，反之亦然。
     */
    println("2-------------------------------------------------")
    var list1 = (0..13).toMutableList()
    var subList = list1.subList(3, 6)
    println("subList: $subList")
    list1[3] = 30
    println("subList: $subList")

    /*
        3. 查找元素位置

        3.1 线性查找
        在任何列表中，都可以使用 indexOf() 或 lastIndexOf() 函数找到元素的位置。
        它们返回与列表中给定参数相等的元素的第一个或最后一个位置。
        如果没有这样的元素，则两个函数均返回 -1。
     */
    println("3.1-------------------------------------------------")
    val numbers2 = listOf(1, 2, 3, 2, 4)
    println("The value 2's index: ${numbers2.indexOf(2)}")
    println("The value 2's index: ${numbers2.lastIndexOf(2)}")

    /*
        还有一对函数接受谓词并搜索与之匹配的元素：

        indexOfFirst() 返回与谓词匹配的第一个元素的索引，如果没有此类元素，则返回 -1。
        indexOfLast() 返回与谓词匹配的最后一个元素的索引，如果没有此类元素，则返回 -1。
     */
    println("The index of the first even number: ${numbers2.indexOfFirst { n -> n%2 == 0 }}")
    println("The index of the last even number: ${numbers2.indexOfLast { n -> n%2 == 0 }}")

    /*
        3.2 在有序列表中二分查找
        还有另一种搜索列表中元素的方法——二分查找算法。
        它的工作速度明显快于其他内置搜索功能，

        但要求该列表按照一定的顺序（自然排序或函数参数中提供的另一种排序）按升序排序过。
        否则，结果是不确定的。

        要搜索已排序列表中的元素，请调用 binarySearch() 函数，并将该值作为参数传递。
        如果存在这样的元素，则函数返回其索引；
        否则，将返回 (-insertionPoint - 1)，其中 insertionPoint 为应插入此元素的索引，以便列表保持排序。
        如果有多个具有给定值的元素，搜索则可以返回其任何索引。

        还可以指定要搜索的索引区间：在这种情况下，该函数仅在两个提供的索引之间搜索。
     */
    println("3.2-------------------------------------------------")
    val numbers3 = mutableListOf("one", "two", "three", "four")
    numbers3.sort()
    println(numbers3)
    println(numbers3.binarySearch("two"))
    println(numbers3.binarySearch("z"))
    println(numbers3.binarySearch("two", 0, 3))

    /*
        3.3 Comparator 二分搜索
        如果列表元素不是 Comparable，则应提供一个用于二分搜索的 Comparator。
        该列表必须根据此 Comparator 以升序排序。
     */
    println("3.3-------------------------------------------------")
    var productList = listOf(
        Product("WebStorm", 49.0),
        Product("AppCode", 99.0),
        Product("DotTrace", 129.0),
        Product("ReSharper", 149.0)
    )
    println(productList.binarySearch(Product("AppCode", 99.0), compareBy<Product> { it.price }.thenBy { it.name }))
    println(productList.binarySearch(Product("DotTrace", 129.0), compareBy<Product> { it.price }.thenBy { it.name }))
    println(productList.sortedBy { it.price })

    /*
        当列表使用与自然排序不同的顺序时（例如，对 String 元素不区分大小写的顺序），自定义 Comparator 也很方便。
     */
    var strList = listOf("Blue", "green", "ORANGE", "Red", "yellow")
    println(strList.binarySearch("RED", String.CASE_INSENSITIVE_ORDER))
    println(strList.sortedWith(String.CASE_INSENSITIVE_ORDER))

    /*
        3.4 比较函数二分搜索
        使用 比较 函数的二分搜索无需提供, 明确的搜索值即可查找元素。
        取而代之的是，它使用一个比较函数将元素映射到 Int 值，并搜索函数返回 0 的元素。

        该列表必须根据提供的函数以升序排序；
        换句话说，比较的返回值必须从一个列表元素增长到下一个列表元素。
     */
    println("3.4-------------------------------------------------")
    fun priceComparison(product: Product, price: Double) = (product.price - price).toInt()
    println(productList.binarySearch{ priceComparison(it, 99.0) })

    /*
        4. List 写操作
        除了集合写操作中描述的集合修改操作之外，可变列表还支持特定的写操作。
        这些操作使用索引来访问元素以扩展列表修改功能。
     */
    /*
        4.1 添加
        要将元素添加到列表中的特定位置，请使用 add() 或 addAll() 并提供元素插入的位置作为附加参数。
        位置之后的所有元素都将向右移动。
     */
    println("4.1-------------------------------------------------")
    var numbers4 = mutableListOf("one", "five", "six")
    numbers4.add(1, "two")
    println(numbers4)

    numbers4.addAll(3, listOf("three", "four"))
    println(numbers4)

    /*
        4.2 更新
        列表还提供了在指定位置替换元素的函数——set() 及其操作符形式 []。set() 不会更改其他元素的索引。
     */
    println("4.2-------------------------------------------------")
    var numbers5 = mutableListOf("one", "five", "six")
    numbers5[1] = "two"
    println(numbers5)

    /*
        4.3 删除
        要从列表中删除指定位置的元素，请使用 removeAt() 函数，并将位置作为参数。
        在元素被删除之后出现的所有元素索引将减 1。
     */
    println("4.3-------------------------------------------------")
    numbers5.removeAt(1)
    println(numbers5)

    /*
        For removing the first and the last element,
        there are handy shortcuts removeFirst() and removeLast().
        Note that on empty lists, they throw an exception.
        To receive null instead, use removeFirstOrNull() and removeLastOrNull()
     */
    println("-------------------------------------------------")
    println(numbers4)
    numbers4.removeFirst()
    numbers4.removeLast()
    println(numbers4)

    var numbers6 = mutableListOf<Int>()
    // numbers6.removeFirst()  // NoSuchElementException: List is empty.
    numbers6.removeFirstOrNull()

    /*
        4.5 排序
        对于可变列表，标准库中提供了类似的扩展函数，它将更改指定实例中元素的顺序。

        就地排序函数的名称与应用于只读列表的函数的名称相似，但没有 ed/d 后缀：

        sort* 在所有排序函数的名称中代替 sorted*：sort()、sortDescending()、sortBy() 等等。
        shuffle() 代替 shuffled()。
        reverse() 代替 reversed()。
        asReversed() 在可变列表上调用会返回另一个可变列表，该列表是原始列表的反向视图。在该视图中的更改将反映在原始列表中。
     */
    println("4.5-------------------------------------------------")
    var numbers7 = mutableListOf("one", "two", "three", "four")
    numbers7.sort()
    println("Sort into ascending: $numbers7")
    numbers7.sortDescending()
    println("Sort into descending: $numbers7")
    numbers7.sortBy { it.length }
    println("Sort into ascending by length: $numbers7")
    numbers7.sortByDescending { it.length }
    println("Sort into descending by length: $numbers7")

    numbers7.sortWith(compareBy<String> { it.length }.thenBy { it })
    println("Sort by Comparator: $numbers7")

    numbers7.shuffle()
    println("Shuffle: $numbers7")

    numbers7.reverse()
    println("Reverse: $numbers7")


}

data class Product(var name: String, var price: Double)