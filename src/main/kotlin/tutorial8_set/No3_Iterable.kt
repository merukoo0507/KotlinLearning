package tutorial8_set

fun main() {
    /*
        1. 迭代器
        对于遍历集合元素，
        对象可按顺序提供对元素的访问权限，而不会暴露集合的底层结构。
        当需要逐个处理集合的所有元素非常有用。
    */

    /*
        1.2 Iterable<T> 接口的继承者（包括 Set 与 List）可以通过调用 iterator() 函数获得迭代器。
        一旦获得迭代器它就指向集合的第一个元素；调用 next() 函数将返回此元素，
        并将迭代器指向下一个元素（如果下一个元素存在）。
        一旦迭代器通过了最后一个元素，它就不能再用于检索元素；也无法重新指向到以前的任何位置。
        要再次遍历集合，请创建一个新的迭代器。
     */
    println("1.2-------------------------------------------------")
    val numbers = listOf("one", "two", "three", "four")
    val numbersIterator = numbers.iterator()
    while(numbersIterator.hasNext()) {
        print(numbersIterator.next() + " ")
    }
    println()

    /*
        1.3 for
        遍历 Iterable 集合的另一种方法，在集合中使用 for 循环时，将隐式获取迭代器。因此，以下代码与上面的示例等效：
     */
    println("1.3-------------------------------------------------")
    for (item in numbers) {
        print("$item ")
    }
    println()

    // 1.4 forEach() 函数
    println("1.4-------------------------------------------------")
    numbers.forEach {
        print("$it ")
    }
    println()

    /*
        2. List 迭代器
        对于列表，有一个特殊的迭代器实现： ListIterator 它支持列表双向迭代：正向与反向。
        反向迭代由 hasPrevious() 和 previous() 函数实现。
        通过 nextIndex() 与 previousIndex() 函数提供有关元素索引的信息。

        具有双向迭代的能力意味着 ListIterator 在到达最后一个元素后仍可以使用。
     */
    println("2-------------------------------------------------")
    val listIterator = numbers.listIterator()
    while(listIterator.hasNext()) listIterator.next()
    println("Iteraing backwards: ")
    while (listIterator.hasPrevious()) {
        print("${listIterator.previous()} ")
    }
    println()

    /*
        3. 可变迭代器
        为了迭代可变集合，于是有了 MutableIterator 来扩展 Iterator 使其具有元素删除函数 remove() 。
        因此，可以在迭代时从集合中删除元素。
     */
    println("3-------------------------------------------------")
    val numbers2 = mutableListOf("one", "two", "three", "four")
    val mutableIterator = numbers2.iterator()
    mutableIterator.next()
    mutableIterator.remove()
    println("After removal: $numbers2")

    // 3.2 除了删除元素， MutableListIterator 还可以在迭代列表时插入和替换元素。
    println("3.2-------------------------------------------------")
    val numbers3 = mutableListOf("one", "four", "four")
    val mutableIterator2 = numbers3.listIterator()
    mutableIterator2.next()
    mutableIterator2.add("two")
    mutableIterator2.next()
    mutableIterator2.set("three")
    println(numbers3)
}
