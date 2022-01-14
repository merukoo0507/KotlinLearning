package tutorial06


fun main() {
    println("1-------------------------------------------------")
    // 编译器自动从主构造函数中声明的所有属性导出以下成员：
    // equals()/hashCode() 对；
    var u1 = User1("John", 12)
    println("hashCode: ${u1.hashCode()}")

    //toString()
    println("toString: ${u1.toString()}")

    //componentN() 函数 按声明顺序对应于所有属性；
    println("componentN: ${u1.component1()}, ${u1.component2()}")

    //copy()函数
    var u2 = u1.copy()
    println("u2 = $u2")

    println("2-------------------------------------------------")
    var u3 = Person1("Amy")
    u3.age = 18
    var u4 = Person1("Amy")
    u4.age = 19
    println("u3 == u4: ${u3 == u4}")

    println("3-------------------------------------------------")
    /*
        3. 复制
        在很多情况下，我们需要复制一个对象改变它的一些属性，但其余部分保持不变
     */
    var u5 = u1.copy(name = "Michael")
    println(u5)

    println("4-------------------------------------------------")
    /*
        4. 数据类与解构声明
        数据类生成的 Component 函数 使它们可在解构声明中使用：
     */
    var (name, age) = u1
    println("name = $name, age = $age")
}

/*
    数据类
    只保存数据的类
 */
data class User1(val name: String, val age: Int)

// 如果生成的类需要含有一个无参的构造函数，则所有的属性必须指定默认值。
data class User2(val name: String = "", var age: Int = 0)

/*
    2. 在类体中声明的属性
    编译器只使用在主构造函数内部定义的属性。
    如需在生成的实现中排除一个属性，请将其声明在类体中：
 */
data class Person1(val name: String) {
    var age: Int = 0
}