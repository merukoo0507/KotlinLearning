package tutorial06

fun main() {
    println("1-------------------------------------------------")
    val list = mutableListOf(1, 2, 3)
    list.swap(0, 1)
    println(list)
    val list2 = mutableListOf("a", "b", "c")
    list2.swap(0, 1)
    println(list2)

    println("2-------------------------------------------------")
    printClassName(Rectangle1())

    // 总是取成员函数
    println("Rectangle1().getAngleCount() = ${Rectangle1().getWidth()}")

    // 扩展函数重载同样名字但不同签名成员函数也完全可以
    println("Rectangle1().getWidth(1) = ${Rectangle1().getWidth(1)}")

    println("3-------------------------------------------------")
    var n = null
    println(n.toString())

    println("4-------------------------------------------------")
    println("list.lastIndex = ${list.lastIndex}")

    println("5-------------------------------------------------")
    MyClass1.printCompanion()
    MyObject.printObject()

    println("6-------------------------------------------------")
    Connection().connect(Host())

    BaseCaller().call(Base1())
    BaseCaller().call(Derived1())
    DerivedCaller().call(Base1())
    DerivedCaller().call(Derived1())
}

// 扩展

/*
    1. 扩展函数
    fun MutableList<Int>.swap(index1: Int, index2: Int) {
        // this對應該列表
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }
 */

//泛型
fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}

// 2. 扩展是静态解析的
open class Shape1

class Rectangle1: Shape1() {
    var width = 0
    fun getTotalWidth() = width * 4
}

fun Shape1.getName() = "Shape1"
fun Rectangle1.getName() = "Rectangle1"
fun printClassName(s: Shape1) { println(s.getName()) }

fun Rectangle1.getWidth() = 1
fun Rectangle1.getWidth(w: Int): Int {
    this.width = w
    return w * 4
}

/*
    3. 可空接收者

    为可空的接收者类型定义扩展, 在没有检测 null 的时候调用 Kotlin 中的toString()
 */
fun Any?.toString(): String {
    if (this == null) return "null"
    return this.javaClass.simpleName
}

/*
    4. 扩展属性
    注意：由于扩展没有实际的将成员插入类中，因此对扩展属性来说幕后字段是无效的。
    这就是为什么扩展属性不能有初始化器。
    他们的行为只能由显式提供的 getters/setters 定义。
    例如:
        val <T> List<T>.lastIndex: Int = this.size - 1
*/
val <T> List<T>.lastIndex: Int
    get() = size - 1

/*
    5. 伴生对象的扩展
    如果一个类定义有一个伴生对象 ，你也可以为伴生对象定义扩展函数与属性。
    就像伴生对象的常规成员一样， 可以只使用类名作为限定符来调用伴生对象的扩展成员：
 */

class MyClass1 {
    companion object {}

    fun Shape1.print2String() {
        toString()                  // 调用 Host.toString()
        this@MyClass1.toString()    // 调用 MyClass1.toString()
    }
}
fun MyClass1.Companion.printCompanion() = println("Companion")
object MyObject {}
fun MyObject.printObject() = println("Object Expand Function")


/*
    6. 扩展声明为成员
*/
class Host
class Connection {
    fun Host.printConnectionString() {
        println(toString())
        // 分发接收者与扩展接收者的成员名字冲突的情况，扩展接收者优先
        println(this@Connection.toString())
    }

    fun connect(host: Host) {
        host.printConnectionString()
    }
}

open class Base1
class Derived1: Base1()

open class BaseCaller {
    // 扩展可以声明为 open 并在子类中覆盖
    open fun Base1.printInfo() {
        println("Base1 in BaseCaller.")
    }

    open fun Derived1.printInfo() {
        println("Derived1 in BaseCaller.")
    }

    fun call(b: Base1) {
        b.printInfo()
    }
}

class DerivedCaller: BaseCaller() {
    override fun Base1.printInfo() {
        println("Base1 in DerivedCaller.")
    }

    override fun Derived1.printInfo() {
        println("Derived1 in DerivedCaller.")
    }
}