package tutorial6_class_object

// Kotlin原生實現委托模式
// Reference: https://www.kotlincn.net/docs/reference/delegation.html

fun main() {
    val b = BaseImpl(10)
    val d = Derived2(b)
    d.print()
    println(d.message)
}

interface Base2 {
    val message: String
    fun print()
}

// 1. 委託對象
class BaseImpl(val x: Int) : Base2 {
    override val message: String = "x value is $x"
    override fun print() {
        println(x)
    }
}

class Derived2(b: Base2): Base2 by b {
    //不委託的成員則override
    override var message: String = ""
        get() = "Derived message"
}