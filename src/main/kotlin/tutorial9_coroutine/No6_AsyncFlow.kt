package tutorial9_coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

fun main() {
    /*
        异步流
     */

//    test6_1()
//    test6_1_1()
//    test6_1_2()
//    test6_1_3()
//    test6_1_4()
//    test6_1_5()
    test6_1_6()
}

fun test6_1() {
    println("1-------------------------------------------------")
    println(simple())
}

fun test6_1_1() {
    println("1.1-------------------------------------------------")
    println(simple1().toList())
}

fun test6_1_2() = runBlocking {
    println("1.2-------------------------------------------------")
    println(simple2())
}

fun test6_1_3() = runBlocking {
    println("1.3-------------------------------------------------")
    // 启动并发的协程以验证主线程并未阻塞
    launch {
        for (i in 1..3) {
            println("It's not blocked $i.")
            delay(100L)
        }
    }
    simple3().collect { // flow收集值
        println(it)
    }
}

fun test6_1_4() = runBlocking {
    /*
        1.4 流是冷的
        Flow 是一种类似于序列的冷流 — 这段 flow 构建器中的代码直到流被收集的时候才运行。

        这是返回一个流的 simple 函数没有标记 suspend 修饰符的主要原因。
        通过它自己，simple() 调用会尽快返回且不会进行任何等待。
        该流在每次收集的时候启动， 这就是为什么当我们再次调用 collect 时我们会看到“Flow started”。
     */
    println("1.4-------------------------------------------------")
    println("Calling simple function...")
    var flow = simple4()
    println("Calling connection...")
    flow.collect { i->
        println(i)
    }
    println("Calling connection again...")
    flow.collect { i->
        println(i)
    }
}

fun test6_1_5() = runBlocking {
    /*
        1.5 流取消基础
        流采用与协程同样的协作取消。
        像往常一样，流的收集可以在当流在一个可取消的挂起函数（例如 delay）中挂起的时候取消。
        以下示例展示了当 withTimeoutOrNull 块中代码在运行的时候流是如何在超时的情况下取消并停止执行其代码的：
     */
    println("1.5-------------------------------------------------")
    withTimeoutOrNull(250) {
        simple5().collect { // flow收集值
            println(it)
        }
    }
    println("Done.")
//        withTimeoutOrNull(250) {
//            simple3().collect()
//        }
//        withTimeoutOrNull(250) {
//            simple2()
//        }
}

fun test6_1_6() = runBlocking {
    /*
        1.6 流构建器
        先前示例中的 flow { ... } 构建器是最基础的一个。还有其他构建器使流的声明更简单：

        - flowOf 构建器定义了一个发射固定值集的流。
        - 使用 .asFlow() 扩展函数，可以将各种集合与序列转换为流。
        因此，从流中打印从 1 到 3 的数字的示例可以写成：
     */
    println("1.6-------------------------------------------------")
}


/*
    1. 表示多个值
 */
fun simple() = listOf(1, 2, 3)

/*
    1.1 序列
    如果使用一些消耗 CPU 资源的阻塞代码计算数字（每次计算需要 100 毫秒）那么我们可以使用 Sequence 来表示数字：
 */
fun simple1(): Sequence<Int> = sequence {
    for (i in 1..3) {
        Thread.sleep(100L) // 假装我们正在计算
        yield(i) // 产生下一个值
    }
}

/*
    1.2 挂起函数
    计算过程阻塞运行该代码的主线程。
    当这些值由异步代码计算时，我们可以使用 suspend 修饰符标记函数 simple，
    这样它就可以在不阻塞的情况下执行其工作, 并将结果作为列表返回：
 */
suspend fun simple2(): List<Int> {
    delay(1000L) // 假装我们正在计算
    return listOf(1, 2, 3)
}

/*
    1.3 流
    使用 List 结果类型，意味着我们只能一次返回所有值。
    为了表示异步计算的值流（stream），我们可以使用 Flow 类型（正如同步计算值会使用 Sequence 类型）：
 */
fun simple3(): Flow<Int> = flow { // 流构建器, 不再标有 suspend 修饰符
    for (i in 1..3) {
        delay(100)
        emit(i) //flow发射值
    }
}

fun simple4(): Flow<Int> = flow { // 流构建器, 不再标有 suspend 修饰符
    println("Flow started.")
    for (i in 1..3) {
        delay(100)
        emit(i) //flow发射值
    }
}

fun simple5(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100)
        println("Emit: $i")
        emit(i) //flow发射值
    }
}
