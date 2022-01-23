package tutorial6_class_object


fun main() {
    println("1-------------------------------------------------")
    val ab = object: A1(1), B1 {
        override val y: Int = 10
    }
    println("ab.y = ${ab.y}")
    println("1.2-------------------------------------------------")
    // 1.2. 只需要“一个对象而已”，并不需要特殊超类型，那么我们可以简单地写：
    val adHoc = object {
        var x = 0
        var y = 0
    }
    println("adHoc.x = ${adHoc.x}, adHoc.y = ${adHoc.y}")

    println("2-------------------------------------------------")
    // 对象声明的初始化过程是线程安全的并且在首次访问时进行。
    // 如需引用该对象，我们直接使用其名称即可：
    DataProvider.registerDataProvider(ExDataProvider())

    println("3-------------------------------------------------")
    // 该伴生对象的成员可通过只使用类名作为限定符来调用：
    val instance = MyClass2.create()
}

/*
    1. 对象表达式
    如果超类型有一个构造函数，则必须传递适当的构造函数参数给它。
    多个超类型可以由跟在冒号后面的逗号分隔的列表指定：
 */
open class A1(x: Int) {
    public open val y: Int = x
}

interface B1 {
    //...
}

class C2 {
    var actionListenerCount: Int = 0
    private fun foo() = object {
        val x: String = "x"
    }

    /*
        1.3 请注意，匿名对象只在本地和私有作用域中声明的类型。

        如果作为公有函数的返回类型
        或者用作公有属性的类型，
        那么该函数或属性的实际类型会是匿名对象声明的"超类型"，
        如果你没有声明任何超类型，就会是 Any。
        在匿名对象中添加的成员将无法访问。
    */
    fun foo2() = object {
        val x: String = "x2"
    }

    fun bar() {
        val x1 = foo().x

        // 错误：未能解析的引用“x”
        // val x2 = foo2().x
    }

    private fun addActionListener(al: ActionListener) { /*...*/ }

    fun initActionListener() {
        addActionListener {
            // 1.4 对象表达式中的代码可以访问来自, 包含它的作用域的变量
            actionListenerCount++
        }
//        等同於
//        addActionListener(object : ActionListener {
//            override fun action() { }
//        })
    }
}

class ExDataProvider

/*
    2. 对象声明
    单例模式在一些场景中很有用， 而 Kotlin使单例声明变得很容易：
 */
object DataProvider {
    fun registerDataProvider(provider: ExDataProvider) {

    }

    val allDataProviders: Collection<ExDataProvider> = arrayListOf()
}
// 并且它总是在 object 关键字后跟一个名称。 就像变量声明一样，
// 对象声明不是一个表达式，不能用在赋值语句的右边

// 2.1 这些对象可以有超类型
// 注意：对象声明不能在局部作用域（即直接嵌套在函数内部），但是它们可以嵌套到其他对象声明或非内部类中。
object DefaultActionListener: ActionListener {
    override fun action() {
        TODO("Not yet implemented")
    }
}

/*
    3. 伴生对象
    类内部的对象声明可以用 companion 关键字标记：
 */
class MyClass2 {
    companion object Factory {
        fun create(): MyClass2 = MyClass2()
    }

    // 3.2 可以省略伴生对象的名称，在这种情况下将使用名称 Companion：
    val myClass3Companion = MyClass3.Companion

    // 3.3 其自身所用的类的名称（不是另一个名称的限定符）可用作对该类的伴生对象 （无论是否具名）的引用：
    val myClass3 = MyClass3
    val myClass4 = MyClass4

    // 3.4
    val f = MyClass5
}

class MyClass3 {
    companion object {}
}

class MyClass4 {
    companion object Named {}
}

interface Factory<T> {
    fun create() : T
}

// 伴生对象的成员看起来像其他语言的静态成员，在运行时他们仍然是真实对象的实例成员，
// 3.4 例如还可以实现接口：
class MyClass5 {
    companion object MyFactory: Factory<MyClass3> {
        override fun create(): MyClass3 {
            return MyClass3()
        }
    }
}