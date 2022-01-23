package tutorial6_class_object

import java.util.function.BinaryOperator
import java.util.function.IntBinaryOperator

fun main() {
    println("1-------------------------------------------------")
    println("Direction.NORTH = ${Direction.NORTH}")
    println("2-------------------------------------------------")
    println("Color.RED = ${Color.RED}")
    println("3-------------------------------------------------")
    println("ProtocolState.WAITING = ${ProtocolState.WAITING}")
    println("4-------------------------------------------------")
    println("IntArithmetics.PLUS.apply(1, 2) = ${IntArithmetics.PLUS.apply(1, 2)}")

    /*
        5. 使用枚举常量
        Kotlin 中的枚举类也有合成方法
     */
    println("5-------------------------------------------------")
    // 通过名称获取枚举常量
    // 如果指定的名称与类中定义的任何枚举常量均不匹配，valueOf() 方法将抛出 IllegalArgumentException 异常。
    println("Direction.valueOf(\"NORTH\") = ${Direction.valueOf("NORTH")}")
    // 允许列出定义的枚举常量
    println("Direction.values() = ${Direction.values()[0]}")

    println("5-2.------------------------------------------------")
    print("printAllValues<Direction>() = ")
    printAllValues<Direction>()
}

/*
    枚举类

    1. 枚举类的最基本的用法是实现类型安全的枚举。
    每个枚举常量都是一个对象。枚举常量用逗号分隔。
 */
enum class Direction {
    NORTH, SOUTH, WEST, EAST
}

/*
    2. 初始化
    因为每一个枚举都是枚举类的实例，所以他们可以是这样初始化过的：
 */
enum class Color(val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF),
}

/*
    3. 匿名类
    枚举常量还可以声明其带有相应方法以及覆盖了基类方法的匿名类。
 */
enum class ProtocolState {
    WAITING {
        override fun signal() = WAITING
    },
    TALKING {
        override fun signal() = TALKING
    };
    //如果枚举类定义任何成员，那么使用分号将成员定义中的枚举常量定义分隔开。

    abstract fun signal(): ProtocolState
}

/*
    4. 在枚举类中实现接口
    一个枚举类可以实现接口（但不能从类继承）
 */
enum class IntArithmetics: BinaryOperator<Int>, IntBinaryOperator {
    PLUS {
        //也可以在相应匿名类中为每个条目提供各自的实现, 只需将接口添加到枚举类声明中即可
        override fun apply(t: Int, u: Int) = t + u
    },
    TIMES {
        override fun apply(t: Int, u: Int) = t * u
    };

    //可以为所有条目提供统一的接口成员实现
    override fun applyAsInt(left: Int, right: Int) = apply(left, right)
}

/*
    5-2. 使用枚举常量
    可以使用 enumValues<T>() 与 enumValueOf<T>() 函数以泛型的方式访问枚举类中的常量 ：

    每个枚举常量都具有在枚举类声明中获取其名称与位置的属性：
    val name: String
    val ordinal: Int
    枚举常量还实现了 Comparable 接口， 其中自然顺序是它们在枚举类中定义的顺序
 */
inline fun <reified T: Enum<T>> printAllValues() {
    print(enumValues<T>().joinToString { it.name })
}