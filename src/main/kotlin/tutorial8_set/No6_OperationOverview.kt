package tutorial8_set

fun main() {
    /*
        1. 集合操作概述

       提供了对集合执行操作的多种函数。
       这包括简单的操作，例如获取或添加元素，
       以及更复杂的操作，包括搜索、排序、过滤、转换等。
    */

    /*
        2. 扩展与成员函数
        集合操作在标准库中以两种方式声明：
        - 集合接口的成员函数
        - 扩展函数

        成员函数定义了对于集合类型是必不可少的操作。
            例如，Collection 包含函数 isEmpty() 来检查其是否为空；
            List 包含用于对元素进行索引访问的 get()，等等。

        创建自己的集合接口实现时，必须实现其成员函数。
        为了使新实现的创建更加容易，请使用标准库中集合接口的框架实现：
            AbstractCollection、AbstractList、AbstractSet、AbstractMap 及其相应可变抽象类。

        其他集合操作被声明为扩展函数。
     */

    /*
        3. 公共操作
        公共操作可用于只读集合与可变集合。 常见操作分为以下几类：

        - 集合转换
        - 集合过滤
        - plus 与 minus 操作符
        - 分组
        - 取集合的一部分
        - 取单个元素
        - 集合排序
        - 集合聚合操作
     */
    /*
        3.2 这些页面中描述的操作将返回其结果，而不会影响原始集合。
        例如，一个过滤操作产生一个新集合，其中包含与过滤谓词匹配的所有元素。
        此类操作的结果应存储在变量中，或以其他方式使用，例如，传到其他函数中。
     */
    println("3.2 -------------------------------------------------")
    val numbers = listOf("one", "two", "three", "four")
    numbers.filter { it.length > 3 }    // `numbers` 没有任何改变，结果丢失
    println("numbers are still: $numbers")
    val longerThan3 = numbers.filter { it.length > 3 }
    println("numbers are longerThan3: $longerThan3")

    /*
        3.3 对于某些集合操作，有一个选项可以指定 目标 对象。
        目标是一个可变集合，该函数将其结果项附加到该可变对象中，而不是在新对象中返回它们。
        对于执行带有目标的操作，有单独的函数，其名称中带有 To 后缀，
        例如，用 filterTo() 代替 filter() 以及用 associateTo() 代替 associate()。
        这些函数将目标集合作为附加参数。
     */
    println("3.3 -------------------------------------------------")
    val filterResult = mutableListOf<String>()
    numbers.filterTo(filterResult) { it.length > 3 }
    numbers.filterIndexedTo(filterResult) { index, _ -> index == 0 }
    println(filterResult)

    /*
        3.4 可以在函数调用的相应参数中直接创建它：
     */
    println("3.4 -------------------------------------------------")
    // 将数字直接过滤到新的哈希集中，
    // 从而消除结果中的重复项
    val result = numbers.mapTo(HashSet()) { it.length }
    println("distinct item lengths are $result")

    /*
        3.5 具有目标的函数可用于过滤、关联、分组、展平以及其他操作。
        有关目标操作的完整列表，请参见 Kotlin collections reference。
     */

    /*
        4. 写操作
        对于可变集合，还存在可更改集合状态的 写操作 。
        这些操作包括添加、删除和更新元素。
        写操作在集合写操作以及 List 写操作与 Map 写操作的相应部分中列出。

        对于某些操作，有成对的函数可以执行相同的操作：
        一个函数就地应用该操作，
        另一个函数将结果作为单独的集合返回。
        例如， sort() 就地对可变集合进行排序，因此其状态发生了变化；
        sorted() 创建一个新集合，该集合包含按排序顺序相同的元素。
     */
    println("4 -------------------------------------------------")
    val numbers2 = mutableListOf("one", "two", "three", "four")
    val sortedNumbers = numbers2.sorted()
    println(numbers2 == sortedNumbers)
    numbers2.sort()
    println(numbers2 == sortedNumbers)
}