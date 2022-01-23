package tutorial6_class_object

fun main() {
    println("2-------------------------------------------------")
    println("SubClass")
    var subClass = SubClass()

    println("Unrelated")
    Unrelated(subClass)

    println("3-------------------------------------------------")
    println("private constructor")
    C1()
}

/*
    类、对象、接口、构造函数、方法、属性
    和它们的 setter 都可以有可见性修饰符。 （getter 总是与属性有着相同的可见性。）
    在 Kotlin 中有这四个可见性修饰符：private、 protected、 internal 和 public。
    如果没有显式指定修饰符的话，默认可见性是 public。
 */

/*
    1. 包
    如果你不指定任何可见性修饰符，默认为 public，这意味着你的声明将随处可见；
    如果你声明为 private，它只会在声明它的文件内可见；
    如果你声明为 internal，它会在相同模块内随处可见；
    protected 不适用于顶层声明。
 */

var bar: Int = 5        // 该属性随处可见
    private set         // setter 只在 example.kt 内可见
private fun foo() {}    // No5_VisibilityModifiers.kt 内可见

/*
    2. 类和接口
    private 意味着只在这个类内部（包含其所有成员）可见；
    protected—— 和 private一样 + 在子类中可见。
    internal —— 能见到类声明的 本模块内 的任何客户端都可见其 internal 成员；
    public —— 能见到类声明的任何客户端都可见其 public 成员。
 */
open class Outer {
    private val a = 1
    protected open val b = 2
    internal val c = 3
    val d = 4           // 默认 public

    protected class Nested {
        val e = 5
    }
}

class SubClass: Outer() {
    // a 不可见
    // 如果你覆盖一个 protected 成员并且没有显式指定其可见性，该成员还会是 protected 可见性
    override var b = 22

    init {
        println("b = $b, c = $c=, d = $d")
    }
}

class Unrelated(o: Outer) {
    // o.a、o.b 不可见
    // o.c、o.d 可见
    init {
        println("o.c = ${o.c}, o.d = ${o.d}")
    }
    // Outer.Nested 不可见，Nested::e 也不可见
}

/*
    3. 构造函数
    主构造函数的可见性（注意你需要添加一个显式 constructor 关键字)

    这里的构造函数是私有的, 默认情况下，所有构造函数都是 public
 */
class C1 private constructor(i: Int) {
    constructor() : this(0)

    init {
        println("i = $i")
    }
}

/*
    4. 局部声明
    局部变量、函数和类不能有可见性修饰符
 */