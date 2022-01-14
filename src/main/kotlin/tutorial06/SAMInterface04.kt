package tutorial06

fun main() {
    println("2-------------------------------------------------")
    println("isEven.accept(2) = ${isEven.accept(2)}")
}

/*
* 1. 函数式（SAM）接口
* 只有一个抽象方法的接口称为函数式接口或 SAM（单一抽象方法）接口。
* 函数式接口可以有多个非抽象成员，但只能有一个抽象成员。
* */
fun interface KRunnable {
    fun invoke()
}

fun interface IntPredicate {
    fun accept(i: Int): Boolean
}

/*
    如果不使用 SAM 转换，那么你需要像这样编写代码：
    val isEven = object: IntPredicate {
        override fun accept(i: Int): Boolean {
            return i % 2 == 0
        }
    }
 */

/*
* 2. SAM 转换
* lambda 表达式实现 SAM 转换
* 通过 lambda 表达式创建一个实例
 */
val isEven = IntPredicate { it % 2 == 0 }




/*
* 函数式接口与类型别名比较
*
* 函数式接口和类型别名用途并不相同。类型别名只是现有类型的名称——它们不会创建新的类型，而函数式接口却会创建新类型。
*
* 类型别名只能有一个成员，而函数式接口可以有多个非抽象成员以及一个抽象成员。函数式接口还可以实现以及继承其他接口。
*
* 考虑到上述情况，函数式接口比类型别名更灵活并且提供了更多的功能。
* */