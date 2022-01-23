package tutorial7FunctionLambda

fun main() {
    println("1.1-------------------------------------------------")
    // lambda 表达式广泛用于此(高阶函数用作参数..)。
    var items = listOf(1, 2, 3)
    var res = items.fold(0) { i, next ->
        var result = i + next
        result
    }
    println("res = items.fold(0){...} -> $res")

    // lambda 表达式的参数类型是可选的，如果能够推断出来的话：
    val joinedToString = items.fold("Element") { i, n -> "$i $n" }
    println("joinedToString = $joinedToString")

    // 函数引用(扩展函数)也可以用于高阶函数调用：
    val product = items.fold(1, Int::times)
    println("product = $product")

    println("4.3.-------------------------------------------------")
    val n1 = 10
    val intFunction1 = IntTransformer()
    val n2 = intFunction1(n1)
    println("n2 = intFunction1(n1) -> $n2")

    // 4.4 如果有足够信息，编译器可以推断变量的函数类型：
    val foo = { n1: Int -> n1 + 1 }     // 推断出的类型是 (Int) -> Int

    println("4.5.-------------------------------------------------")
    // 4.5 带与不带接收者的函数类型非字面值可以互换，
    // 其中接收者可以替代第一个参数，反之亦然。
    // 例如，(A, B) -> C 类型的值可以传给或赋值给期待 A.(B) -> C 的地方，反之亦然：
    val repeatFun: String.(Int) -> String = {
        rn ->
        var rs = this
        for( i in 1 .. rn )
            rs += this
        rs
    }
    val twoArgRepeatFun: (String, Int) -> String = repeatFun         // OK

    fun runTransform(f: (String, Int) -> String): String {
        return f("hello ", 3)
    }
    println("runTransform(repeatFun) = ${runTransform(repeatFun)}")  // OK


    // 5. 函数类型实例调用
    val stringPlus: (String, String) -> String = String::plus
    val intPlus: Int.(Int) -> Int = Int::plus

    // 函数类型的值可以通过其 invoke(……) 操作符调用：f.invoke(x) 或者直接 f(x)。
    println("stringPlus.invoke(\"<-\", \"->\") = ${stringPlus.invoke("<-", "->")}")
    println("stringPlus(\"<-\", \"->\") = ${stringPlus("<-", "->")}")
    println("intPlus.invoke(1, 1) = ${intPlus.invoke(1, 1)}")
    println("intPlus(1, 1)= ${intPlus(1, 1)}")
    /*
        如果该值具有接收者类型，那么应该将接收者对象作为第一个参数传递。
        调用带有接收者的函数类型值的另一个方式是在其前面加上接收者对象，
        就好比该值是一个扩展函数：1.foo(2)，
     */
    println("n.intPlus(1) = ${1.intPlus(1)}")  // 类扩展调用

    /*
        7. Lambda 表达式与匿名函数
        "lambda"与"匿名函数"是“函数字面值”，即未声明的函数，但立即做为表达式传递。
     */

    fun <T> max(arr: Array<T>, f: (T, T) -> T) {
        for(t1 in arr) {
            for (t2 in arr) f(t1, t2)
        }
    }
    max(
        arrayOf("a", "abc"),
        { s1, s2 -> if (s1.length > s2.length) s1 else s2 }
    )
    // 函数 max 是一个高阶函数，它接受一个函数作为第二个参数。
    // 其第二个参数是一个表达式，它本身是一个函数，即函数字面值，它等价于以下具名函数：
    fun compare(a: String, b: String): String = if (a.length > b.length) a else b

    // 如果推断出的该 lambda 的返回类型不是 Unit，
    // 那么该 lambda 主体中的最后一个（或可能是单个） 表达式会视为返回值。

    // 7.1 传递末尾的 lambda 表达式
    max(arrayOf(1, 2)) { n1, n2 -> if (n1 > n2) n1 else n2 }

    println("7.2-------------------------------------------------")
    /*
        7.2 在 Kotlin 中有一个约定：如果函数的最后一个参数是函数，
        那么作为相应参数传入的 lambda 表达式可以放在圆括号之外
     */
    run {
        println("Only one arg in the function.")
    }

    println("8-------------------------------------------------")
    /*
        8. it：单个参数的隐式名称
        一个 lambda 表达式只有一个参数是很常见的。
        如果编译器自己可以识别出签名，也可以不用声明唯一的参数并忽略 ->。 该参数会隐式声明为 it
     */
    println(listOf(1, 2, 3).filter { it > 1 })

    println("9-------------------------------------------------")
    /*
        9. 从 lambda 表达式中返回一个值
        我们可以使用限定的返回语法从 lambda 显式返回一个值。 否则，将隐式返回最后一个表达式的值。
        因此，以下两个片段是等价的：
     */
    println(listOf(1, 2, 3).filter {
        it > 1
    })
    println(listOf(1, 2, 3).filter {
        return@filter it > 1
    })

    // 10. 这一约定连同在圆括号外传递 lambda 表达式一起支持 LINQ-风格 的代码：
    val strings = arrayOf("a", "b", "ab", "bc", "abc")
    println(strings.filter { it.length > 1 }.sortedBy { it }.map { it.toUpperCase() })

    println("12-------------------------------------------------")
    /*
        12. 匿名函数
        上面提供的 lambda 表达式语法缺少的一个东西是指定函数的返回类型的能力。
        在大多数情况下，这是不必要的。因为返回类型可以自动推断出来。
        然而，如果确实需要显式指定，可以使用另一种语法： 匿名函数 。
     */
    fun (x: Int, y: Int) = x + y

    // 12.1 参数和返回类型的指定方式与常规函数相同，除了能够从上下文推断出的参数类型可以省略：
    println(listOf(1, 2, 3).filter(fun (i: Int) =  i > 1 ))

    // 匿名函数的返回类型推断机制与正常函数一样：对于具有表达式函数体的匿名函数将自动推断返回类型，而
    // 12.2 具有代码块函数体的返回类型必须显式指定（或者已假定为 Unit）
    fun (a: Int, b: Int, c: Int): Int {
        var r = a + b
        return if (r > c) r else c
    }

    /*
        12.3 Lambda表达式与匿名函数之间的另一个区别是非局部返回的行为
        Reference: https://www.kotlincn.net/docs/reference/inline-functions.html#%E9%9D%9E%E5%B1%80%E9%83%A8%E8%BF%94%E5%9B%9E
     */

    println("13-------------------------------------------------")
    /*
        13. 闭包
        Lambda 表达式或者匿名函数（以及局部函数和对象表达式） 可以访问其 闭包 ，
        即在外部作用域中声明的变量。
        在 lambda 表达式中可以修改闭包中捕获的变量：
     */
    var sum = 0
    listOf(1, 2, 3).filter { it > 1 }.map{
        sum += it
    }
    println(sum)

    /*
        14. 带有接收者的函数字面值
        使用 this 表达式 访问接收者对象
        与扩展函数类似
     */
    val sum1: Int.(Int) -> Int = { other ->
        this + other
    }
    // 14.2 匿名函数语法允许直接指定函数接收者类型
    val sum2 = fun Int.(other: Int): Int = this + other

    println("15-------------------------------------------------")
    html {      // 带接收者的 lambda 由此开始
        body()  // 调用该接收者对象的一个方法
    }
}

/*
    1. 高阶函数与 lambda 表达式
    Kotlin 函数都是頭等函數，这意味着它们可以存储在变量与数据结构中、作为参数传递给其他高阶函数以及从其他高阶函数返回。
    可以像操作任何其他非函数值一样操作函数。

    为促成这点，作为一门静态类型编程语言的 Kotlin 使用一系列函数类型来表示函数并提供一组特定的语言结构，
    例如 lambda 表达式。
 */

/*
    2. 高阶函数
    高阶函数是将函数用作参数或返回值的函数。

    一个不错的示例是集合的函数式风格的 fold，
    它接受一个初始累积值与一个接合函数，
    并通过将当前累积值与每个集合元素连续接合起来代入累积值来构建返回值：
 */
fun <T, R> Collection<T>.fold(
    initial: R,
    f: (acc: R, nextElement: T) -> R
): R {
    var accumulator: R = initial
    for (ele: T in this) {
        accumulator = f(accumulator, ele)
    }
    return accumulator
}

/*
    3. 函数类型
    这些类型具有与函数签名相对应的特殊表示法，即它们的参数和返回值：

    - 所有函数类型都有一个圆括号括起来的参数类型列表以及一个返回类型
    - 函数类型可以有一个额外的接收者类型，它在表示法中的点之前指定：
      类型 A.(B) -> C 表示可以在 A 的接收者对象上以一个 B 类型参数来调用并返回一个 C 类型值的函数。
    - 挂起函数属于特殊种类的函数类型，它的表示法中有一个 suspend 修饰符 ，例如 suspend () -> Unit

    - 如需将函数类型指定为可空，请使用圆括号：((Int, Int) -> Int)?
    - 函数类型可以使用圆括号进行接合：(Int) -> ((Int) -> Unit)
    - 箭头表示法是右结合的，(Int) -> (Int) -> Unit 与前述示例等价，但不等于 ((Int) -> (Int)) -> Unit

    - 还可以通过使用类型别名给函数类型起一个别称：
 */
typealias CountHandler = (Num: Int, CountEvent: () -> Unit) -> Unit

/*
    4. 函数类型实例化
    有几种方法可以获得函数类型的实例：

    4.1. 使用函数字面值的代码块，采用以下形式之一：
    - lambda 表达式: { a, b -> a + b }
    - 匿名函数: fun(s: String): Int { return s.toIntOrNull() ?: 0 }
    - 带有接收者的函数字面值可用作带有接收者的函数类型的值

    4.2. 使用已有声明的可调用引用：
    - 顶层、局部、成员、扩展函数：::isOdd、 String::toInt，
    - 顶层、成员、扩展属性：List<Int>::size，
    - 构造函数：::Regex
    这包括指向特定实例成员的绑定的可调用引用：foo::toString

    4.3. 使用实现“函数类型接口”的“自定义类”的实例：
 */
class IntTransformer: (Int) -> Int {
    override fun invoke(x: Int): Int {
        return x + 1
    }
}

/*
    6. 内联函数
    有时使用内联函数可以为高阶函数提供灵活的控制流。
    請參考：InlineFunction03
 */

/*
    11. 在 lambda 表达式中解构
    Reference: https://www.kotlincn.net/docs/reference/multi-declarations.html#%E5%9C%A8-lambda-%E8%A1%A8%E8%BE%BE%E5%BC%8F%E4%B8%AD%E8%A7%A3%E6%9E%84%E8%87%AA-11-%E8%B5%B7
 */


/*
    15. 当接收者类型可以从上下文推断时，lambda 表达式可以用作带接收者的函数字面值。
 */
class HTML {
    init {
        println("<html />")
    }
    fun body() {
        println("<body />")
    }
    /*...*/
}

fun html(init: HTML.() -> Unit): HTML {
    val html = HTML()
    html.init()
    return html
}