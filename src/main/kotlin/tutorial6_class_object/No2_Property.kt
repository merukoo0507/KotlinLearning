package tutorial6_class_object

import java.text.SimpleDateFormat

// 属性
// Reference: https://www.kotlincn.net/docs/reference/properties.html

fun main() {
    println("1-------------------------------------------------")
    var addr = Address()
    println("${addr.city}: ${addr.establishTiming}")
}

/*
* 1.
* var <propertyName>[: <PropertyType>] [= <property_initializer>]
    [<getter>]
    [<setter>]
* 其初始器（initializer）、getter 和 setter 都是可选的。
* 属性类型如果可以从初始器 （或者从其 getter 返回值，如下文所示）中推断出来，也可以省略。
* */
class Address {
    // 2.
    // var city ?: String // 错误：需要显式初始化器，隐含默认 getter 和 setter
    var city = "Taipei"  // 类型 String、默认 getter 和 setter
    var district = ""
    var size = 0

    // 3. 为属性定义自定义的访问器
    val isEmpty: Boolean
        get() = size == 0
    var establishTiming: String = "0"
        get() {
            // 4. 幕后字段, 表示目前屬性的在記憶體的值
            if (field == "0")
                this.establishTiming = "0"
            return field
        }
        set(value) {
            var format = "yyyy-MM-dd"
            var simpleDateFormat = SimpleDateFormat(format)
            var lvalue: Long = value.toLong()
            var data = simpleDateFormat.format(lvalue)
            field = data
        }

    // 6. 延迟初始化属性与变量
    // Int型態不能被lateinit
    lateinit var log: String

    init {
        // 检测一个 lateinit var 是否已初始化
            if (!::log.isInitialized){
            log = if (isEmpty) {
                "There is no one in this addr."
            } else {
                "There is someone in this addr"
            }
            println(log)
        }
    }
}

/*
* 5. 编译期常量
* 只读属性的值在编译期是已知的，那么可以使用 const 修饰符将其标记为编译期常量。
* 位于顶层或者是 object 声明 或 companion object 的一个成员
* 以 String 或原生类型值初始化
* 没有自定义 getter
* */
const val SUBSYSTEM_DEPRECATED: String = "This subsystem is deprecated"
