package tutorial5_basic
/*
    依造中文官網學習Kotlin
    https://www.kotlincn.net/docs/reference/
*/

//1. 程序入口点
fun main(args: Array<String>) {
    println("sum(1, 2) = ${sum(1, 2)}")
    printSum(1, 2)

    //3. 变量
    //定义只读局部变量使用关键字 val 定义。只能为其赋值一次。
    val a: Int = 1  // 立即赋值
    val b = 2   // 自动推断出 `Int` 类型
    val c: Int  // 如果没有初始值类型不能省略
    c = 3       // 明确赋值

    //可重新赋值的变量使用 var 关键字：
    var d = 5
    d += 1

    //顶层变量：
    incrementX()
    println("x = $x")

    //4. 注释
    //与大多数现代语言一样，Kotlin 支持单行（或行末）与多行（块）注释。
    // 这是一个行注释
    /* 这是一个多行的
       块注释。 */

    //Kotlin 中的块注释可以嵌套。
    /* 注释从这里开始
    /* 包含嵌套的注释 */
    并且在这里结束。 */
}

val PI = 3.14
var x = 0
fun incrementX() {
    x += 1
}

/*
    2. 函數
    带有两个 Int 参数、返回 Int 的函数：
    fun sum(a: Int, b: Int): Int {
        return a + b
    }
*/

//将表达式作为函数体、返回值类型自动推断的函数：
fun sum(a: Int, b: Int) = a + b

/*
    函数返回无意义的值
    fun printSum(a: Int, b: Int): Unit {
        println("a + b = ${a + b}")
    }
*/

//Unit 返回类型可以省略：
fun printSum(a: Int, b: Int) {
    println("a + b = ${a + b}")
}