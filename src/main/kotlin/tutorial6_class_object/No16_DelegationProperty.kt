package tutorial6_class_object

import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// 委托属性
// Reference: https://www.kotlincn.net/docs/reference/delegated-properties.html
fun main() {
    println("1-------------------------------------------------")
    /*
    * 1. 延迟属性 Lazy
    * lambda, 并返回一个 Lazy <T> 实例的函数
    * 实现延迟属性，synchronized求值
    * */
    val lazyValue by lazy {
        println("Compute!")
        "lazyValue"
    }
    /*
    *  第一次调用 get() 会执行已传递给 lazy() 的 lambda 表达式并记录结果， 后续调用 get() 只是返回记录的结果
    * */
    println("LazyValue: $lazyValue")
    println("LazyValue: $lazyValue")

    println("2-------------------------------------------------")
    // 2.
    val user = User()
    user.name = "aa"
    println(user.name)
    user.name = "bb"
    println(user.name)

    println("3-------------------------------------------------")
    // 3.
    val myClass = MyClass()
    println("myClass.newName: ${myClass.newName}")
    myClass.oldName = "oldName2"
    println("myClass.newName: ${myClass.newName}")

    println("4-------------------------------------------------")
    // 4.
    println("myClass.newName: ${myClass.name}")
    println("myClass.newName: ${myClass.age}")
    println("myClass.newName: ${myClass.mname}")
    println("myClass.newName: ${myClass.mage}")

    println("5-------------------------------------------------")
    /*
    * 5. 局部委托属性
    * */
    fun ex() {
        var computeFoo: () -> Unit = { println("computeFoo") }
        val memorizedFoo by lazy(computeFoo)    //lazy<Unit> 沒有setValue函數所以是val
        memorizedFoo
    }
    ex()

    println("6-------------------------------------------------")
    var owner = Owner()
    owner.resource1.invoke()
    owner.resource2.invoke()
    owner.resource2 = Resource().apply {
        data = "22"
    }
    owner.resource2.invoke()

    println("readOnly = ${owner.readOnly}")
    owner.readWrite = 123
    println("readWrite = ${owner.readWrite}")
}

/*
* 2. 可观察属性 Observable
* Delegates.observable() 接受两个参数：初始值与修改时处理程序（handler）。
* 每当我们给属性赋值时会调用该处理程序（在赋值后执行）。它有三个参数：被赋值的属性、旧值与新值
* */
class User {
    var name by Delegates.observable("name") {
        property: KProperty<*>, oldValue: String, newValue: String ->
        println("$oldValue => $newValue")
    }
}

class MyClass {
    /*
    * 3. 委托给另一个属性
    * */
    var oldName = "oldName"
    var newName by ::oldName

    /*
    * 4. 委托给映射属性
    * */
    var map: Map<String, Any> = mapOf(
        "name" to "John Doe",
        "age" to 20)
    // 因為map屬性是Map, 使用委託的物件規定為val
    val name by map
    val age by map

    // 这也适用于 var 属性，如果把只读的 Map 换成 MutableMap 的话
    var mutableMap: MutableMap<String, Any> = mutableMapOf(
        "mname" to "John2",
        "mage" to 22)
    var mname by mutableMap
    var mage by mutableMap
}

class Resource {
    var data = "__"
    fun invoke() {
        println("Resource: $data")
    }
}

class Owner {
    val resource1 by ResourceDelegate()
    var resource2 by ResourceDelegate2()

    val readOnly by resourceDelegate()
    var readWrite by resourceDelegate()
}

/*
*   6. 属性委托要求
*   6-1 只读属性
*   （即 val 声明的），委托必须提供一个操作符函数 getValue()，该函数具有以下参数：
*   thisRef —— 必须与 属性所有者 类型（对于扩展属性——指被扩展的类型）相同或者是其超类型。
*   property —— 必须是类型 KProperty<*> 或其超类型
* */
class ResourceDelegate {
    operator fun getValue(thisRef: Owner, property: KProperty<*>): Resource {
//        println("thisRef: $thisRef, property: $property")
        return Resource()
    }
}

/*
*   6-2 可变属性
*   (即 var 声明的)，委托必须额外提供一个操作符函数 setValue()，， 该函数具有以下参数：
*   thisRef —— 必须与 属性所有者 类型（对于扩展属性——指被扩展的类型）相同或者是其超类型。
*   property —— 必须是类型 KProperty<*> 或其超类型。
*   value — 必须与属性类型相同（或者是其超类型）。
*
*   getValue() 或/与 setValue() 函数可以通过委托类的成员函数提供或者由扩展函数提供。
*   当你需要委托属性到原本未提供的这些函数的对象时后者会更便利。 两函数都需要用 operator 关键字来进行标记。
*
* */
class ResourceDelegate2(private var resource: Resource = Resource()) {
    operator fun getValue(thisRef: Owner, property: KProperty<*>): Resource {
        return resource
    }

    operator fun setValue(thisRef: Owner, property: KProperty<*>, value: Any?) {
        if (value is Resource)
            resource = value
    }
}

/*
    You can create delegates as anonymous objects without creating new classes
    using the interfaces ReadOnlyProperty and ReadWriteProperty from the Kotlin standard library.
    They provide the required methods:
    getValue() is declared in ReadOnlyProperty;
    ReadWriteProperty extends it and adds setValue().
    Thus, you can pass a ReadWriteProperty whenever a ReadOnlyProperty is expected.
 */
fun resourceDelegate() = object : ReadWriteProperty<Any?, Int> {
    var curValue = 0
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return curValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        curValue = value
    }

}

/*
* 7...略
* */
