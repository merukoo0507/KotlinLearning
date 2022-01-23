package tutorial6_class_object

fun main() {
    println("1-------------------------------------------------")
    println("Outer2.Nested().foo() = ${Outer2.Nested().foo()}")

    println("3-------------------------------------------------")
    println("Outer3().Inner().foo() = ${Outer3().Inner().foo()}")

    println("4-------------------------------------------------")
    /*
        4. 匿名内部类
        使用对象表达式创建匿名内部类实例：
    */
    Outer3().addMyInterface(object : MyInterface {
        override val prop: Int
            get() = TODO("Not yet implemented")

        override fun bar() {
            TODO("Not yet implemented")
        }
    })

    println("5-------------------------------------------------")
    Outer3().action()
}

// 嵌套类与内部类

// 1. 类可以嵌套在其他类中
class Outer2 {
    private val bar: Int = 1
    class Nested {
        fun foo() = 2
    }
}

// 2. Interface可以嵌套在其他类中, 反之亦然
interface OuterInterface {
    class InnerClass
    interface InnerInterface
}

class OuterClass {
    class InnerClass
    interface InnerInterface
}

/*
    3. 内部类
    标记为 inner 的嵌套类能够访问其外部类的成员。内部类会带有一个对外部类的对象的引用：
 */
class Outer3 {
    private val bar: Int = 1
    inner class Inner {
        fun foo() = bar
    }

    private var myInterface: MyInterface ?= null
    fun addMyInterface(i: MyInterface) {
        myInterface = i
    }

    // 对象是函数式 Java 接口（即具有单个抽象方法的 Java 接口）的实例，
    // 你可以使用带接口类型前缀的lambda表达式创建它：
//    private val actionListener = ActionListener { println("Java interface with one function to lambda.") }

    fun action() {
//        actionListener.action()
    }

    //在kotlin使用java的部分需注意公開度public, protected, private, 不然會出現以下錯誤
    //public' function exposes its 'public/*package*/' parameter type ActionListener
    private var myActionListener: ActionListener?= null
    private fun addActionListener(i: ActionListener) {
        myActionListener = i
    }
    // Reference: https://stackoverflow.com/questions/55076483/public-function-exposes-its-public-package-parameter-type-solaredgeexcept
    // Classes by default in Kotlin are public, so every member/function is exposed to the outer world. Contrarily, in Java the default visibility, that is omitting a visibility keyword such as public, protected or private, is package-private.
    // SolarEdgeException is package-private, while its user SiteStorage is entirely public.
    // That means a user of SiteStorage cannot catch or use your exception at all.
    // Unfortunately Kotlin doesn't have the concept of package-private, because packages aren't managed the same way as in Java.
    // The best you can have is internal.
}