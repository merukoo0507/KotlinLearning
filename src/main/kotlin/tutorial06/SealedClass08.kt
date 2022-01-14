package tutorial06

fun main() {
    println("1-------------------------------------------------")
    println(eval(Sum(Const(1.0), Const(Double.NaN))))
}

/*
    密封类
    密封类用来表示受限的类继承结构：当一个值为有限几种的类型、而不能有任何其他类型时

    一个密封类是自身抽象的，它不能直接实例化并可以有抽象（abstract）成员。
    密封类不允许有非-private 构造函数（其构造函数默认为 private）。
 */
sealed class Expr
data class Const(var num: Double): Expr()
data class Sum(var e1: Expr, var e2: Expr): Expr()
object NotANumber: Expr()

fun eval(e: Expr): Double = when (e) {
    is Const -> e.num
    is Sum -> eval(e.e1) + eval(e.e2)
    is NotANumber -> Double.NaN
}

