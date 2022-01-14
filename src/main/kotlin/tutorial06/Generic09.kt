package tutorial06

import jdk.jfr.Threshold

// Reference: https://www.kotlincn.net/docs/reference/generics.html

fun main() {
    println("1-------------------------------------------------")
    var box = Box(1)
    println("Box<T> -> Box(1), value: ${box.value}")
    var boxs = Box<String>("Boxs")
    println("Box<T> -> Box(\"Boxs\"), value: ${boxs.value}")

    println("2-------------------------------------------------")
    var ints = arrayOf(1, 2, 3)
    var anys = Array<Any>(3) {}
    // 其类型为 Array<Int> 但此处期望 Array<Any>
    // copy(ints, anys)
    copy2(ints, anys)
    println("copy2(ints, anys) -> anys: ${anys.contentToString()}")

    var strs = "a"
    fill(anys, strs)
    println("fill(anys, strs) -> anys: ${anys.contentToString()}")

    println("4-------------------------------------------------")
    var list = singletonList(1, 10)
    println("singletonList: $list")
    println("list.toBasicString(): ${list.toBasicString()}")

    println("5-------------------------------------------------")
    var list2: List<Int> = listOf(1, 2, 3)
    sort(list2)
    println(list2)

    // 错误：HashMap<Int, String> 不是 Comparable<HashMap<Int, String>> 的子类型
    // sort(listOf(HashMap()))


}

/*
    泛型
    类也可以有类型参数
 */
class Box<T>(var value: T){
}

/*
    1. 型变
    (java部分參考Generic092)
    声明处型变：我们可以标注 Source 的类型参数 T 来确保它仅从 Source<T> 成员中返回（生产），
    并从不被消费。 为此，我们提供 out 修饰符：
 */
interface Source<out T> {
    fun next(): T
}

fun demo(strs: Source<String>) {
    var obj = Object()
    var objs: Source<Any> = strs // 这个没问题，因为 T 是一个 out-参数
}

interface Comparable1<in T> {
    fun compareTo(other: T): Int
}

fun demo2(n: Comparable1<Number>) {
    n.compareTo(2.0)     // 1.0 拥有类型 Double，它是 Number 的子类型
    // Java型態通配字元“？”使用時機，在於宣告變數的時候，
    // 無法執行以下操作⬇️
    var d: Comparable1<Double> = n
}

/*
    2. 类型投影: 使用处型变

    有些类实际上不能限制为只返回 T，例子是 Array：
    class Array<T>(val size: Int) {
        fun get(index: Int): T {...}
        fun set(index: Int, value: T) {...}
    }
 */

//该类在 T 上既不能是协变的也不能是逆变的。这造成了一些不灵活性。考虑下述函数：
fun copy(from: Array<Any>, to: Array<Any>) {
    if (from.size != to.size) return
    for (i in from.indices) {
        to[i] = from[i]
    }
}

// 问题：Array <T> 在 T 上是不型变的，因此 Array <Int> 和 Array <Any> 都不是另一个的子类型。
// 那么，我们唯一要确保的是 copy() 不会做任何坏事。我们想阻止它写到 from，我们可以：
fun copy2(from: Array<out Any>, to: Array<Any>) {
    if (from.size != to.size) return
    for (i in from.indices) {
        to[i] = from[i]
    }
}

// 也可以使用 in 投影一个类型：
/*
    (Array<in String> 对应于 Java 的 Array<? super String>，
    也就是说，你可以传递一个 CharSequence 数组或一个 Object 数组给 fill() 函数。)
*/
fun fill(to: Array<in Any>, value: Any) {
    for (i in to.indices) {
        to[i] = value
    }
}

/*
    3. 星投影
    請參考Reference
 */

/*
    4. 泛型函数
    类型参数要放在函数名称之前：
 */
fun <T> singletonList(item: T, size: Int): List<T> {
    var arr = arrayListOf<T>()
    for (i in 0..size)
        arr.add(item)
    return arr
}

fun <T> T.toBasicString(): String {
    return "$this"
}

/*
    5. 泛型约束
    上界
 */

//冒号之后指定的类型是上界：只有 Comparable<T> 的子类型可以替代 T。 例如：
fun <T: Comparable<T>> sort(list: List<T>) {
    list.sorted()//.reversed()
}

/*
    默认的上界（如果没有声明）是 Any?。

    在尖括号中只能指定一个上界。
    如果同一类型参数需要多个上界，我们需要一个单独的 where-子句：
 */

fun <T> copyWhenGenerate(list: List<T>, threshold: T): List<String>
    where T: Comparable<T>, T: CharSequence {
    return list.filter { it > threshold }.map { it.toString() }
}

/*
    6. 类型擦除
    Kotlin 为泛型声明用法执行的类型安全检测仅在编译期进行
    运行时泛型类型的实例不保留关于其类型实参的任何信息。 其类型信息称为被擦除。
    例如，Foo<Bar> 与 Foo<Baz?> 的实例都会被擦除为 Foo<*>
    參考：类型擦除与泛型检测
    https://www.kotlincn.net/docs/reference/typecasts.html#%E7%B1%BB%E5%9E%8B%E6%93%A6%E9%99%A4%E4%B8%8E%E6%B3%9B%E5%9E%8B%E6%A3%80%E6%B5%8B
 */