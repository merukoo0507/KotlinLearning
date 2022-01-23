package tutorial8_set

import tutorial6_class_object.Person

fun main() {
    println("2-------------------------------------------------")
    /*
        2. 集合类型

        2.1 Kotlin 标准库提供了基本集合类型的实现： set、list 以及 map。

        *更改可变集合"不"需要它是以 var 定义的变量：
        写操作修改同一个可变集合对象，因此引用不会改变。
        但是，如果尝试对 val 集合重新赋值，你将收到编译错误。
     */
    val numbers = mutableListOf(1, 2, 3)
    numbers.add(4)  // ok
    // numbers = mutableListOf(1, 2) // 编译错误

    println("3-------------------------------------------------")
    printAll(listOf(1, "a", 2, "abc"))
    printAll(setOf(1, "a", 2, "abc"))
    println("3.1 -------------------------------------------------")
    val words = "A long time ago in a galaxy far far away".split(" ")
    var shortWords = mutableListOf<String>()
    words.getShortWord(shortWords, 3)
    println("shortWords: $shortWords")

    println("4-------------------------------------------------")
    /*
        4. List
        List<T> 以指定的顺序存储元素，并提供使用索引访问元素的方法。
        索引从 0 开始 – 第一个元素的索引
        – 直到 最后一个元素的索引 即 (list.size - 1)。
     */
    var nums = listOf("one", "two", "three", "four")
    println("number of elements: ${nums.size}")
    println("Third element: ${nums[2]}")
    println("Index of element \"two\": ${nums.indexOf("two")}")
    /*
        List 元素（包括空值）可以重复：List 可以包含任意数量的相同对象或单个对象的出现。
        如果两个 List 在相同的位置具有相同大小和相同结构的元素，则认为它们是相等的。
     */
    println(listOf("a", "b") == listOf("a", "b"))
    println(listOf("a", "b") === listOf("a", "b"))
    var bob = Person("Bob", 31)
    val people = listOf(bob, bob)
    val people2 = listOf(Person("Bob", 31), bob)
    println(people == people2)

    // MutableList<T> 是可以进行写操作的 List，例如用于在特定位置添加或删除元素。
    var numbers2 = mutableListOf(1, 2, 3, 4)
    numbers2.removeAt(1)
    numbers2[0] = 0
    numbers2.shuffle()
    println("numbers2 = $numbers2")
    /*
        在某些方面，List 与 Array 非常相似。
        但是，数组的大小是在初始化时定义的，永远不会改变;
        反之，List 没有预定义的大小；作为写操作的结果，可以更改 List 的大小：添加，更新或删除元素。

        在 Kotlin 中，List 的默认实现是 ArrayList，可以将其视为可调整大小的Array。
     */

    println("5-------------------------------------------------")
    /*
        5. Set
        Set<T> 存储唯一的元素；它们的顺序通常是未定义的。
        null 元素也是唯一的：一个 Set 只能包含一个 null。
        当两个 set 具有相同的大小并且对于一个 set 中的每个元素都能在另一个 set 中存在相同元素，则两个 set 相等。
     */
    var strs = setOf("a", "b")
    println(setOf("a", "b") == strs)

    var numbers3 = setOf(1, 2, 3, 4)
    println("number3.size: ${numbers3.size}")
    println("numbers3.contains(1): ${numbers3.contains(1)}")

}

/*
    1. Kotlin 集合概述
    - List 是一个有序集合，可通过索引（反映元素位置的整数）访问元素。顺序很重要并且字可以重复。
    - Set 是唯一元素的集合。它反映了集合（set）的数学抽象：一组无重复的对象。顺序并不重要。
    - Map（或者字典）是一组键值对。键是唯一的，每个键都刚好映射到一个值。
      值可以重复。map 对于存储对象之间的逻辑连接非常有用，例如，员工的 ID 与员工的位置。
 */


/*
    2.2
    - 只读集合类型是型变的。
      这意味着，如果类 Rectangle 继承自 Shape，则可以在需要 List <Shape> 的任何地方使用 List <Rectangle>。
      换句话说，集合类型与元素类型具有相同的子类型关系。
      map 在值（value）类型上是型变的，但在键（key）类型上不是。

    - 可变集合不是型变的
    否则将导致运行时故障。
    如果 MutableList <Rectangle> 是 MutableList <Shape> 的子类型，
    你可以在其中插入其他 Shape 的继承者（例如，Circle），从而违反了它的 Rectangle 类型参数。
 */

/*
    2.3 Kotlin 集合接口的图表
    https://www.kotlincn.net/assets/images/reference/collections-overview/collections-diagram.png
 */


/*
    3. Collection
    Collection<T> 是集合层次结构的根。
    此接口表示一个"只读"集合的共同行为：检索大小、检测是否为成员等等。
    Collection 继承自 Iterable <T> 接口，它定义了迭代元素的操作。

    可以使用 Collection 作为适用于不同集合类型的函数的参数。
    对于更具体的情况，请使用 Collection 的继承者： List 与 Set。
 */
fun <T> printAll(items: Collection<T>) {
    for(i in items)
        print(i)
    println()
}

// 3.1 MutableCollection 是一个具有写操作的 Collection 接口，例如 add 以及 remove。
fun List<String>.getShortWord(shortWords: MutableList<String>, maxLength: Int): MutableList<String> {
    this.filterTo(shortWords) { it.length > maxLength }
    val articles = listOf("A", "a", "An", "an", "the", "The")
    shortWords -= articles
    return shortWords
}
