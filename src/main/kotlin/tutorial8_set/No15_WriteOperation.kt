package tutorial8_set

fun main() {
    /*
        写操作
        可变集合支持更改集合内容的操作，例如添加或删除元素。
        在此页面上，我们将描述实现 MutableCollection 的所有写操作。
        有关 List 与 Map 可用的更多特定操作，请分别参见 List 相关操作与 Map 相关操作。
    */

    /*
        1. 添加元素
        要将单个元素添加到列表或集合，请使用 add() 函数。指定的对象将添加到集合的末尾。
    */
    println("1-------------------------------------------------")
    val numbers = mutableListOf(1, 2, 3, 4)
    numbers.add(5)
    println(numbers)

    /*
        1.2. addAll() 将参数对象的每个元素添加到列表或集合中。
        参数可以是 Iterable、Sequence 或 Array。
        接收者的类型和参数可能不同，例如，你可以将所有内容从 Set 添加到 List。

        当在列表上调用时，addAll() 会按照在参数中出现的顺序添加各个新元素。
        你也可以调用 addAll() 时指定一个元素位置作为第一参数。
        参数集合的第一个元素会被插入到这个位置。 其他元素将跟随在它后面，将接收者元素移到末尾
    */
    println("1.2-------------------------------------------------")
    numbers.addAll(arrayOf(6, 7))
    println(numbers)
    numbers.addAll(2, setOf(8, 9))
    println(numbers)

    /*
        1.3. 你还可以使用 plus 运算符 plusAssign (+=) 添加元素。
        将第二个操作数(一个元素或另一个集合)追加到集合的末尾。
    */
    println("1.3-------------------------------------------------")
    val numbers2 = mutableListOf("one", "two", "three")
    numbers2 += "four"
    println(numbers2)

    numbers2 += listOf("five", "six")
    println(numbers2)

    /*
        2. 删除元素
        若要从可变集合中移除元素，请使用 remove() 函数。 remove() 接受元素值，并删除该值的一个匹配项。
    */
    println("2-------------------------------------------------")
    val numbers3 = mutableListOf(1, 2, 3, 4, 3)
    numbers3.remove(3)  // 删除了第一个 `3`
    println(numbers3)
    numbers3.remove(5)  // 什么都没删除
    println(numbers3)

    /*
        2.2. 要一次删除多个元素，有以下函数：

        - removeAll() 移除参数集合中存在的所有元素。
            或者，你可以用谓词作为参数来调用它；在这种情况下，函数移除谓词产生 true 的所有元素。
        - retainAll() 与 removeAll() 相反：它移除除参数集合中的元素之外的所有元素。
            当与谓词一起使用时，它只留下与之匹配的元素。
        - clear() 从列表中移除所有元素并将其置空
    */
    println("2.2-------------------------------------------------")
    val numbers4 = mutableListOf(1, 2, 3, 4)
    numbers4.retainAll{ it >= 3 }
    println(numbers4)
    numbers4.clear()
    println(numbers4)

    val numbersSet = mutableSetOf("one", "two", "three", "four")
    numbersSet.removeAll(setOf("one", "two"))
    println(numbersSet)

    /*
        2.3. 从集合中移除元素的另一种方法是使用 minusAssign (-=) ——原地修改版的 minus 操作符。
        第二个参数可以是元素类型的单个实例或另一个集合。
        右边是单个元素时，-= 会移除它的第一个匹配项。
        反过来，如果它是一个集合，那么它的所有元素的每次出现都会删除。

        例如，如果列表包含重复的元素，它们将被同时删除。
        第二个操作数可以包含集合中不存在的元素。这些元素不会影响操作的执行。
    */
    println("2.3-------------------------------------------------")
    val numbers5 = mutableSetOf("one", "two", "three", "three", "four")
    numbers5 -= "three"
    println(numbers5)

    numbers5 -= listOf("four", "five")
    println(numbers5)

    /*
        3. 更新元素
        list 和 map 还提供更新元素的操作。
        它们在 List 相关操作与 Map 相关操作中有所描述。
        对于 set 来说，更新没有意义，因为它实际上是移除一个元素并添加另一个元素。
     */
}