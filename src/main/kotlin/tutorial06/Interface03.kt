package tutorial06

fun main() {
    println("2-------------------------------------------------")
    Child().bar()
    println("3-------------------------------------------------")
    println("Child().prop = ${Child().prop}")
    println("4-------------------------------------------------")
    var employee = Employee("Bob", "Chen", 5, "male", "waiter")
    println(employee)
    println("5-------------------------------------------------")
    var d = D()
    d.foo()
    d.bar()
}

// 1. 接口
interface MyInterface {
    /*
        3. 接口中的属性
        不能有幕后字段（backing field）
     */
    val prop: Int

    fun bar()
    fun foo() {

    }
}

/*
    2. 实现接口
    一个类或者对象可以实现一个或多个接口。
*/
class Child: MyInterface {
    override var prop: Int = 2

    override fun bar() {
        println("bar()")
    }
}

interface Name {
    val name: String
}

interface Health {
    val age: Int
    val sex: String
}

// 4. 接口继承
interface Person3: Name, Health {
    var firstName: String
    var lastName: String

    override val name: String
        get() = firstName + lastName
}

data class Employee (
    // 不必实现"name"
    override var firstName: String = "",
    override var lastName: String = "",
    override val age: Int = 0,
    override val sex: String = "male",
    var position: String
): Person3

/*
    5. 解决覆盖冲突
    实现多个接口时，可能会遇到同一方法继承多个实现的问题
 */
interface A {
    fun foo() { println("fooA") }
    fun bar()
}

interface B {
    fun foo() { println("fooB") }
    fun bar() { println("barB") }
}

class C: A {
    override fun bar() {
        println("barC")
    }
}

class D: A, B {
    override fun foo() {
        super<A>.foo()
        super<B>.foo()
    }

    override fun bar() {
        super.bar()
    }
}