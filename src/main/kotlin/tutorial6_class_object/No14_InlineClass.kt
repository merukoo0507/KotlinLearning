package tutorial6_class_object

fun main() {
    println("1-------------------------------------------------")
    val securedPassword = Password("Don't try this in production.")

    println("2-------------------------------------------------")
    val name = Name2("Kotlin")
    println(name.greet())   // `greet` 方法会作为一个静态方法被调用
    println("name.length = ${name.length}")    // 属性的 get 方法会作为一个静态方法被调用

    println("3-------------------------------------------------")
    val name3 = Name3("Kotlin")
    println(name3.prettyPrint())

    println("4-------------------------------------------------")
    val f = Foo(42)
    asInline(f)    // 拆箱操作: 用作 Foo 本身
    asGeneric(f)   // 装箱操作: 用作泛型类型 T
    asInterface(f) // 装箱操作: 用作类型 I
    asNull(f)      // 装箱操作: 用作不同于 Foo 的"可空类型 Foo?"
    // 在下面这里例子中，'f' 首先会被装箱（当它作为参数传递给 'id' 函数时）
    // 然后又被拆箱（当它从'id'函数中被返回时）
    // 最后， 'c' 中就包含了被拆箱后的内部表达(也就是 '42')， 和 'f' 一样
    val c = id(f)


    println("7-------------------------------------------------")
    val nameAlias: NamedTypeAlias = ""
    val nameInlineClass: NamedInlineClass = NamedInlineClass("")
    val s: String = ""

    acceptString(nameAlias)            // 正确: 传递别名类型的实参, 替代函数中基础类型的形参
//     acceptString(nameInlineClass)     // 错误: 不能传递内联类的实参, 替代函数中基础类型的形参

    acceptNamedTypeAlias(s)            // 正确: 传递基础类型的实参, 替代函数中别名类型的形参
//     acceptyNamedInlineClass(s)        // 错误: 不能传递基础类型的实参, 替代函数中内联类类型的形参


}

/*
    1. 内联类

    内联类仅在 Kotlin 1.3 之后版本可用，目前处于 Alpha 版。

    有时候，业务逻辑需要围绕某种类型创建包装器。
    然而，由于额外的堆内存分配问题，它会引入运行时的性能开销。
    此外，如果被包装的类型是原生类型，性能的损失是很糟糕的，
    因为原生类型通常在运行时就进行了大量优化，然而他们的包装器却没有得到任何特殊的处理。
 */

// 内联类必须含有唯一的一个属性在主构造函数中初始化。在运行时，将使用这个唯一属性来表示内联类的实例
inline class Password(val value: String)

// 这就是内联类的主要特性，它灵感来源于 “inline” 这个名称：
// 类的数据被 “内联”到该类使用的地方（类似于内联函数中的代码被内联到该函数调用的地方）。

/*
    2. 成员
    内联类支持普通类中的一些功能。特别是，内联类可以声明属性与函数：

    然而，内联类的成员也有一些限制：

    内联类不能含有 init 代码块
    内联类不能含有幕后字段
    因此，内联类只能含有简单的计算属性（不能含有延迟初始化/委托属性）
 */
inline class Name2(val s: String) {
    val length: Int
        get() = s.length

    fun greet() = "Hello $s"
}

interface Printable {
    fun prettyPrint(): String
}
/*
    3. 继承
    内联类允许去继承接口

    禁止内联类参与到类的继承关系结构中。这就意味着内联类不能继承其他的类而且必须是 final。
 */
inline class Name3(val s: String): Printable {
    override fun prettyPrint(): String {
        return "prettyPrint() => $s"
    }

}

interface I
/*
    4. 表示方式
    在生成的代码中，Kotlin 编译器为每个内联类保留一个包装器。
    内联类的实例可以在运行时表示为包装器或者基础类型。
    这就类似于 Int 可以表示为原生类型 int 或者包装器 Integer。

    为了生成性能最优的代码，Kotlin 编译更倾向于使用基础类型而不是包装器。
    然而，有时候使用包装器是必要的。一般来说，只要将内联类用作另一种类型，它们就会被装箱。
 */
inline class Foo(val i: Int): I

fun asInline(f: Foo) {}
fun <T> asGeneric(f: T) {}
fun asInterface(i: I) {}
fun asNull(n: Foo?) {}

fun <T> id(f: T): T = f

/*
    5. 因为内联类既可以表示为基础类型，又可以表示为包装器，
    引用相等(===)对于内联类而言毫无意义，因此这也是被禁止的。
 */

/*
    6. 名字修饰
    由于内联类被编译为其基础类型，因此可能会导致各种模糊的错误，例如意想不到的平台签名冲突：
 */
inline class UInt(val x: Int)

// 在 JVM 平台上被表示为'public final void compute(int x)'
fun compute(x: Int) {}

// 同理，在 JVM 平台上也被表示为'public final void compute(int x)'！
fun compute(x: UInt) {}
/*
    为了缓解这种问题，一般会通过在函数名后面拼接一些稳定的哈希码来重命名函数。
    因此，fun compute(x: UInt)
    将会被表示为 public final void compute-<hashcode>(int x)，
    以此来解决冲突的问题。

    请注意在 Java 中 - 是一个 无效的 符号，也就是说在 Java 中不能调用使用内联类作为形参的函数。
 */

/*
    7. 内联类与类型别名
    初看起来，内联类似乎与类型别名非常相似。

    然而，关键的区别在于类型别名与其基础类型(以及具有相同基础类型的其他类型别名)是 赋值兼容 的，而内联类却不是这样。

    换句话说，内联类引入了一个真实的新类型，
    与类型别名正好相反，类型别名仅仅是为现有的类型取了个新的替代名称(别名)：
 */
typealias NamedTypeAlias = String
inline class NamedInlineClass(val s: String)

fun acceptString(s: String) {}
fun acceptNamedTypeAlias(s: NamedTypeAlias) {}
fun acceptyNamedInlineClass(s: NamedInlineClass) {}



/*
    8. 内联类的 alpha 状态
    内联类的设计处于 Alpha 版，这意味着对于未来版本没有兼容性保证。
    在 Kotlin 1.3+ 中使用内联类时，会报一个警告，提示该特性尚未稳定发布。
    如需移除警告，必须通过指定编译器参数 -Xinline-classes 来选择使用这项特性。
 */