package tutorial6_class_object

import java.io.File

// 4. 类型别名不会引入新类型。
// 当你在代码中添加 typealias Predicate<T> 并使用 Predicate<Int> 时，Kotlin 编译器总是把它扩展为 (Int) -> Boolean。
// 因此，当你需要泛型函数类型时，你可以传递该类型的变量，反之亦然：
typealias Predicate<T> = (T) -> Boolean
fun foo(p: Predicate<Int>, i: Int) = p(i)

fun main() {
    println("2-------------------------------------------------")
    val copyArgs: CopyArgs = { arr1, arr2 -> println("${arr1.toList()} ${arr2.toList()}") }
    copyArgs(arrayOf(1, 2, 3), arrayOf("a", "b"))
    println("4-------------------------------------------------")
    val f: (Int) -> Boolean = { it > 0 }
    println(foo(f, 42))

    val p: Predicate<Int> = { it != 0 }
    println(foo(p, 0))
}

/*
    1. 类型别名
    类型别名为现有类型提供替代名称。
    如果类型名称太长，你可以另外引入较短的名称，并使用新的名称替代原类型名。
 */
typealias PersonSet = Set<Person>

typealias FileTable = MutableMap<Int, MutableList<File>>

// 2. 为函数类型提供另外的别名：
typealias CopyArgs = (Array<Any>, Array<Any>) -> Unit
typealias Copy2Args = (Array<out Any>, Array<Any>) -> Unit
typealias SingletonListArgs<T> = (T, Int) -> List<T>

class A2 {
    inner class Inner
}

class B2 {
    inner class Inner
}

// 3. 你可以为内部类和嵌套类创建新名称：
typealias A2Inner = A2.Inner