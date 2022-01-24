package tutorial8_set

import tutorial6_class_object.Person
import java.util.*
import kotlin.collections.HashSet

fun main() {
    /*
        1. 构造集合
        创建集合的最常用方法是使用标准库函数
        listOf<T>()、setOf<T>()、mutableListOf<T>()、mutableSetOf<T>()。
        如果以逗号分隔的集合元素列表作为参数，编译器会自动检测元素类型。
        创建空集合时，须明确指定类型。
     */
    val numbersSet = setOf("one", "two", "three")
    val emptySet = mutableSetOf<String>()

    /*
        2. 同样的，Map 也有这样的函数 mapOf() 与 mutableMapOf()。
        映射的键和值作为 Pair 对象传递（通常使用中缀函数 to 创建）

        注意，to 符号创建了一个短时存活的 Pair 对象，
        因此建议仅在性能不重要时才使用它。 为避免过多的内存使用，请使用其他方法。
        例如，可以创建可写 Map 并使用写入操作填充它。 apply() 函数可以帮助保持初始化流畅。
     */
    val numbersMap = mapOf("one" to 1, "two" to 2, "three" to 3)
    val numbersMap2 = mutableMapOf<String, Int>().apply{
        this["one"] = 1
        this["two"] = 2
    }

    /*
        3. 空集合
        还有用于创建没有任何元素的集合的函数：emptyList()、emptySet() 与 emptyMap()。 创建空集合时，应指定集合将包含的元素类型。
     */
    val empty = emptyList<String>()

    /*
        4. List 的初始化函数
        有一个接受 List 的"大小"与"初始化函数"的构造函数，该初始化函数根据索引定义元素的值。
     */
    println("4-------------------------------------------------")
    val double = List(3) { it * 2 }
    println("double = $double")

    /*
        5. 具体类型的构造函数
        要创建具体类型的集合，可以使用这些类型的构造函数，
        例如 ArrayList，LinkedList，Set，Map 的实现中均有提供。
     */
    val linkedList = LinkedList(listOf("one", "two", "three"))
    val presizedSet = HashSet<String>(4)


    /*
        6. 复制
        要创建与现有集合具有相同元素的集合，可以使用复制操作。
        标准库中的集合复制操作创建了具有"相同元素引用"的 "浅 复制"集合。 因此，对集合元素所做的更改会反映在其所有副本中。

        在特定时刻通过集合复制函数，例如toList()、toMutableList()、toSet() 等等。创建了集合的快照。
        结果是创建了一个具有相同元素的新集合 如果在源集合中添加或删除元素，则不会影响副本。
        副本也可以独立于源集合进行更改。
     */
    println("6-------------------------------------------------")
    val sourceList = mutableListOf(1, 2, 3)
    val copy = sourceList.toMutableList()
    val readOnlyList = sourceList.toList()
    sourceList.add(4)
    copy.add(5)
    println("sourceList = $sourceList")
    println("copy = $copy")
    println("readOnlyList = $readOnlyList")

    // 6.2 这些函数还可用于将集合转换为其他类型，例如根据 List 构建 Set，反之亦然。
    println("6.2-------------------------------------------------")
    val copySet = sourceList.toMutableSet()
    copySet.add(7)
    copySet.add(8)
    println("copySet = $copySet")

    /*
        6.3 对同一集合实例的新引用。
        当通过引用更改集合实例时，更改将反映在其所有引用中。
     */
    println("6.3-------------------------------------------------")
    val sourceList2 = mutableListOf(1, 2, 3)
    val referenceList = sourceList2
    referenceList.add(4)
    println("sourceList2.size: ${sourceList2.size}")

    /*
        6.4 集合的初始化可用于限制其可变性
        例如，如果构建了一个 MutableList 的 List 引用，
        当你试图通过此引用修改集合的时候，编译器会抛出错误。
     */
    println("6.4-------------------------------------------------")
    val referenceList2: List<Int> = sourceList2
    // referenceList2.add(5)   // 编译错误
    sourceList2.add(5)
    println(referenceList2)

    /*
        6.5 调用其他集合的函数
        可以通过其他集合各种操作的结果来创建集合。例如，过滤列表会创建与过滤器匹配的新元素列表：
     */
    println("6.5-------------------------------------------------")
    val numbers2 = listOf("one", "two", "three", "four")
    val longerThan3 = numbers2.filter { it.length > 3 }
    println(longerThan3)
    // 映射生成转换结果列表：
    val numbers3 = setOf(1, 2, 3)
    println(numbers3.map { it * 2 })
    println(numbers3.mapIndexed { index, i -> "$index - $i" })
    // 关联生成 Map:
    println(numbers2.associateWith { it.length })

    // 6.6 有关 Kotlin 中集合操作的更多信息，参见集合操作概述.
    // Reference: https://www.kotlincn.net/docs/reference/collection-operations.html
}



