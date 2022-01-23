package tutorial6_class_object

/*
* 类与继承
* Reference: https://www.kotlincn.net/docs/reference/classes.html
*
* 类成员, 类可以包含：
* 构造函数与初始化块
* 函数
* 属性
* 嵌套类与内部类
* 对象声明
*
* */

fun main() {
    println("1.2-------------------------------------------------")
    InitOrderDemo("InitOrderDemo")

    println("\n1.3-------------------------------------------------")
    Person2("Kid")

    println("\n2.1-------------------------------------------------")
    val triangle = Triangle()

    println("\n2.3-------------------------------------------------")
    Truck("CustomerTruck")

    println("\n2.4-------------------------------------------------")
    triangle.draw()
    println("triangle fillColor: ${triangle.fillColor}")

    println("\n2.4.2-------------------------------------------------")
    triangle.Data().drawAndLog()

    println("\n2.5-------------------------------------------------")
    Square().draw()
}

/*
* 1. 构造函数
* */
// class Person constructor(name: String)   // constructor 可以省略

/*
    1.1 主构造函数
 */
class Person (name: String, var age: Int = 0)
// 如果构造函数有注解或可见性修饰符，这个 constructor 关键字是必需的，并且这些修饰符在它前面：
//class NecessaryCons public @Inject constructor(name: String)

/*
* 1.2 主构造函数不能包含任何的代码。
* 初始化的代码可以放到以 init 关键字作为前缀的初始化块（initializer blocks）中。
* 在实例初始化期间，初始化块按照它们出现在类体中的顺序执行，与属性初始化器交织在一起：
* */
class InitOrderDemo(name: String) {
    //主构造的参数可以在初始化块中使用
    val firstProperty = "First Property: $name".also(::println)

    init {
        println("First initializer block that print: $name")
    }

    val secondProPerty = "Second Property: $name".also(::println)

    init {
        println("Second initializer block that println: $name")
    }
}

/*
* 1.3 次构造函数
* 如果类有一个主构造函数，每个次构造函数需要委托给主构造函数，
* 可以直接委托或者通过别的次构造函数间接委托。
* 委托到同一个类的另一个构造函数用 this 关键字即可：
* */
class Person2(name: String) {
    var children = mutableListOf<Person2>()
    constructor(name: String, parent: Person2): this(name) {
        println("Class Person2's second constructor")
        parent.children.add(parent)
    }

    /*
    * 1.4 请注意，初始化块中的代码实际上会成为主构造函数的一部分。
    * 委托给主构造函数会作为次构造函数的第一条语句，
    * 因此所有初始化块与属性初始化器中的代码都会在次构造函数体之前执行。
    * 即使该类没有主构造函数，这种委托仍会隐式发生，并且仍会执行初始化块：
    * */
    init {
        println("Class Person2's initializer blocks")
    }
}

/*
* 2. 继承
* */

// 2.1 在 Kotlin 中所有类都有一个共同的超类 Any，这对于没有超类型声明的类是默认超类：
open class Base(p: Int)

// 2.2 如需声明一个显式的超类型，请在类头中把超类型放到冒号之后：
//如果派生类有一个主构造函数，其基类可以（并且必须） 用派生类主构造函数的参数就地初始化。
class Derived(p: Int): Base(p)


// 2.3 如果派生类没有主构造函数，那么每个次构造函数必须使用 super 关键字初始化其基类型，或委托给另一个构造函数做到这一点。
// 注意，在这种情况下，不同的次构造函数可以调用基类型的不同的构造函数：
//
//class MyView: View {
//  constructor(ctx: Context): super(ctx)
//
//  constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)
//}

// 2.4 派生类初始化顺序
open class Car(open val name: String = "Car") {
    init {
        println("Initializing Car Class.")
    }

    open val size: Int = 4
}
class Truck(override val name: String = "Truck"): Car(
    name.also {  println("Argument for Car Class") }
) {
    init {
        println("Initializing Truck Class.")
    }

    override var size: Int = (6).also {
        println("Initializing size in Truck Class: $it")
    }
}

open class Shape {
    // 2.5 覆盖方法
    // Kotlin 对于可覆盖(开放)的成员以及覆盖后的成员需要显式修饰符：
    open fun draw() {
        println("Shape draw()")
    }
    fun fill() {}


    // 2.6 覆盖属性
    open val vertexCnt = 0

    open val borderColor = "black"
}

class Triangle: Shape() {
    // 2.5.2
    // 标记为 override 的成员本身是开放的，也就是说，它可以在子类中覆盖。
    // 如果你想禁止再次覆盖，使用 final 关键字：
    final override fun draw() {
        // 2.7 super 调用超类实现
        super.draw()
        println("Triangle draw()")
    }

    /*
    * 2.6.2
    * 你也可以用一个 var 属性覆盖一个 val 属性，但反之则不行。
    * 因为一个 val 属性本质上声明了一个 get 方法，
    * 而将其覆盖为 var 只是在子类中额外声明一个 set 方法。
    * */
    override var vertexCnt: Int = 0

    val fillColor = super.borderColor

    // 2.7.2 super@Outer 内部类中访问外部类的超类
    inner class Data {
        fun drawAndLog() {
            super@Triangle.draw()
            println("Draw a Triangle with fillColor $fillColor and borderColor ${super@Triangle.borderColor}")
        }
    }
}

interface Polygon {
    //若沒有實現，為abstract
    fun line()

    fun draw() {
        println("Polygen draw()")
    }
}

class Square: Shape(), Polygon {
    //implement interface, 需override abstract function
    override fun line() {
        println("Need to implements abstract function.")
    }

    override fun draw() {
        // 2.8 覆盖规则
        super<Shape>.draw()
        super<Polygon>.draw()
        println("Square draw()")
    }
}


/*
* 2.6 抽象类
* 我们并不需要用 open 标注一个抽象类或者函数——因为这不言而喻。
* */
abstract class Rectangle: Shape() {
    // 我们可以用一个抽象成员覆盖一个非抽象的开放成员
    abstract override fun draw()
}

// 0304 覆盖属性
// 0316 覆盖属性