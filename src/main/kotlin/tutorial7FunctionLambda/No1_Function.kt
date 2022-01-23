package tutorial7FunctionLambda

import kotlin.math.abs
import kotlin.math.cos

fun main() {
    println("2-------------------------------------------------")
    // 2. 函数用法, 调用函数使用传统的方法：
    val n = double(1)
    println("double(1) = $n")
    // 调用成员函数使用点表示法：
    print("MyClass().printClassName() = ")
    MyClass().printClassName()

    println("3.4-------------------------------------------------")
    MyClass().foo2(i2 = 10) // i1 = 0

    println("3.5-------------------------------------------------")
    MyClass().foo3 { println("最后一个参数是 lambda 表达式") }
    MyClass().foo4(10) { x -> println("i = $x") }

    println("4-------------------------------------------------")
    // When calling this function, you don’t have to name all its arguments:
    reformat("String_Str",
        false,
        upperCaseFirstLetter = false,
        divideByCamelHumps = false,
        '_'
    )
    // 4.2 You can skip all arguments with default values:
    reformat("This is a long String!")

    // 4.3 You can skip some arguments with default values.
    // However, after the first skipped argument,
    // you must name all subsequent arguments:
    reformat("This is a long String!", upperCaseFirstLetter = false, divideByCamelHumps = false, wordSeparator = '_')

    println("4.4-------------------------------------------------")
    foo("1", "2")
    foo(strings = arrayOf("a", "b"))

    println("8-------------------------------------------------")
    // 允许将可变数量的参数传递给函数：
    val list = asList(1, 2, 3)
    println("list = $list")
    // 当我们调用 vararg-函数时，我们可以一个接一个地传参，例如 asList(1, 2, 3)，
    // 或者，如果我们已经有一个数组并希望将其内容传给该函数，我们使用伸展（spread）操作符（在数组前面加 *）
    var arr = arrayOf(5, 6, 7)
    val list2 = asList(1, *arr, 2, 3)
    println("list2 = $list2")

    println("8-------------------------------------------------")
    var n2 = 10
    println("n2.shl1(2) = ${n2 shl1 2}")

    println("8.1-------------------------------------------------")
    // 8.1 中缀函数调用的优先级低于算术操作符、类型转换以及 rangeTo 操作符。 以下表达式是等价的：
    println("1 shl 2 + 3 = ${1 shl 2 + 3}")
    println("1 shl (2 + 3) = ${1 shl (2 + 3)}")
    for (i in 0 until 5 * 2)  {
        print("$i ")
    }
    println()
    for (i in 0 until (5 * 2))  {
        print("$i ")
    }
    println()
    /*
        xs union ys as Set<*> 等价于 xs union (ys as Set<*>)
        另一方面，中缀函数调用的优先级高于布尔操作符 && 与 ||、is- 与 in- 检测以及其他一些操作符。这些表达式也是等价的：
        完整的优先级层次结构请参见其语法参考。
     */
    val a = true
    val b = true
    val c = false
    println("a && b xor c = ${a && b xor c}")
    println("a && (b xor c) = ${a && (b xor c)} \n")

    println("a xor b && c = ${a xor b && c}")
    println("(a xor b) && c = ${(a xor b) && c} \n")
    println("15-------------------------------------------------")
    println("findFixPoint(1.0) = ${findFixPoint(1.0)}")
}

/*
    1. 函数声明
    Kotlin 中的函数使用 fun 关键字声明：
 */
fun double(x: Int): Int {
    return x * 2
}

open class MyClass0 {
    open fun foo(i: Int = 10) { /*...*/ }
}

class MyClass: MyClass0() {
    fun printClassName() {
        println(this.javaClass.name)
    }

    /*
        3. 参数

        3.1 函数参数使用 Pascal 表示法定义，即 name: type。
        参数用逗号隔开。 每个参数必须有显式类型：
     */
    fun powerOf(number: Int, exponent: Int) { }

    // 3.2 You can use a trailing comma when you declare function parameters:
    fun read(
        b: Array<Byte>,
        off: Int,
        // A default value is defined using the = after the type.
        length: Int = b.size,
    ) { /*...*/ }

    /*
        3.3 覆盖方法总是使用与基类型方法相同的默认参数值。
        当覆盖一个带有默认参数值的方法时，必须从签名中省略默认参数值：
     */
    override fun foo(i: Int) // 不能有默认值
    { }

    // 3.4 如果一个默认参数在一个无默认值的参数之前，
    // 那么该默认值只能通过使用具名参数调用该函数来使用：
    fun foo2(i1: Int = 0, i2: Int) {}

    /*
        3.5 如果在默认参数之后的最后一个参数是 lambda 表达式，
        那么它既可以作为具名参数在括号内传入，也可以在括号外传入：
     */
    fun foo3(f: () -> Unit) { f() }
    fun foo4(i: Int = 0, f: (Int) -> Unit) { f(i) }
}

/*
    4. 具名参数
    When calling a function, you can name one or more of its arguments.

    This may be helpful when a function has a large number of arguments,
    and it's difficult to associate a value with an argument,
    especially if it's a boolean or null value.

    When you use named arguments in a function call,
    you can freely change the order they are listed in,
    and if you want to use their default values you can just leave them out altogether.

    Consider the following function reformat() that has 4 arguments with default values.

    对于 JVM 平台：在调用 Java 函数时不能使用具名参数语法，因为 Java 字节码并不总是保留函数参数的名称。
 */
fun reformat(
    str: String,
    normalizeCase: Boolean = true,
    upperCaseFirstLetter: Boolean = true,
    divideByCamelHumps: Boolean = false,
    wordSeparator: Char = ' ',
) {}

// 4.4 You can pass a variable number of arguments (vararg) with names using the spread operator:
fun foo(vararg strings: String) {
    for(s in strings)
        print("$s ")
    println()
}

/*
    5. 返回 Unit 的函数
    如果一个函数不返回任何有用的值，它的返回类型是 Unit。
    Unit 是一种只有一个值的类型。这个值不需要显式返回：
 */
fun printName(name: String?): Unit {
    if (name == null)
        println("Hi there.")
    else
        println("Hi $name")
}
/*
    上面代碼等同於：
    fun printName(name: String?) { /*...*/ }
 */

/*
    6. 单表达式函数
    当函数返回单个表达式时，可以省略花括号并且在 = 符号之后指定代码体即可
 */
// fun double2(n: Int): Int = n * 2
// 当返回值类型可由编译器推断时，返回类型可省略
fun double2(n: Int) = n * 2


/*
    7. 显式返回类型
    具有块代码体的函数必须始终显式指定返回类型，
    除非他们旨在返回 Unit，在这种情况下它是可选的。

    Kotlin 不推断具有块代码体的函数的返回类型，
    因为这样的函数在代码体中可能有复杂的控制流，
    并且返回类型对于读者（有时甚至对于编译器）是不明显的。
 */

/*
    8. 可变数量的参数（Varargs）
    函数的参数（通常是最后一个）可以用 vararg 修饰符标记：
 */
// 在函数内部，类型 T 的 vararg 参数的可见方式是作为 T 数组，即上例中的 ts 变量具有类型 Array <out T>。
fun <T> asList(vararg ts: T): List<T> {
    var list = ArrayList<T>()
    for(t in ts) {
        list.add(t)
    }
    return list
}
// 只有一个参数可以标注为 vararg。
// 如果 vararg 参数不是列表中的最后一个参数，可以使用具名参数语法传递其后的参数的值，
// 或者，如果参数具有函数类型，则通过在括号外部传一个 lambda。

/*
    8. 中缀表示法
    标有 infix 关键字的函数也可以使用中缀表示法（忽略该调用的点与圆括号）调用。
    中缀函数必须满足以下要求：
        它们必须是成员函数或扩展函数；
        它们必须只有一个参数；
        其参数不得接受可变数量的参数且不能有默认值。
 */
infix fun Int.shl1(x: Int): Int {
    var y = this
    for (i in 1 .. x) {
        y *= 2
    }
    return y
}

/*
    8.2 当使用中缀表示法在当前接收者上调用方法时，需要显式使用 this；
    不能像常规方法调用那样省略。这是确保非模糊解析所必需的。
 */
class StringCollenction {
    infix fun add(x: Int) {
        /*...*/
    }

    fun build() {
        this.add(10)
        add(10)
        // add 10 // 错误：必须指定接收者
    }
}

/*
    9. 函数作用域
    在 Kotlin 中函数可以在文件顶层声明，
    这意味着你不需要像一些语言如 Java、C# 或 Scala 那样需要创建一个类来保存一个函数。
    此外除了顶层函数，Kotlin 中函数也可以声明在局部作用域、作为成员函数以及扩展函数。
 */
fun FunctionRange() {
    fun foo1() {
        var cnt = 0
        /*
            9.1 局部函数
            Kotlin 支持局部函数，即一个函数在另一个函数内部：
         */
        fun foo2() {
            // 局部函数可以访问外部函数（即闭包）的局部变量，cnt 可以是局部变量：
            cnt++
        }
    }
}

/*
    10. 成员函数
    关于类和覆盖成员的更多信息参见类和继承。
 */

/*
    11. 泛型函数
    关于泛型函数的更多信息参见泛型。
 */

/*
    12. 内联函数
    參考InlineFunction03
 */

/*
    13. 扩展函数
    參考Expand06
 */

/*
    14. 高阶函数和 Lambda 表达式
    LambdaExpression02
 */

/*
    15. 尾递归函数
    这允许一些通常用循环写的算法改用递归函数来写，而无堆栈溢出的风险。
    当一个函数用 tailrec 修饰符标记并满足所需的形式时，编译器会优化该递归，

    要符合 tailrec 修饰符的条件的话，函数必须将其自身调用作为它执行的最后一个操作。
    在递归调用后有更多代码时，不能使用尾递归，并且不能用在 try/catch/finally 块中。
 */
val eps = 0.0000000001
tailrec fun findFixPoint(x: Double): Double {
    if (abs(x - cos(x)) < eps) return x
    return findFixPoint(cos(x))
}
