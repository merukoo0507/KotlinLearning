package tutorial9_coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

fun main() {
    /*
        异步流
     */
    test6_1()
//    test6_2()
}

/*
    1. 表示多个值
 */
fun simple() = listOf(1, 2, 3)

/*
    1.1 序列
    如果使用一些消耗 CPU 资源的阻塞代码计算数字（每次计算需要 100 毫秒）那么我们可以使用 Sequence 来表示数字：
 */
fun simple2(): Sequence<Int> = sequence {
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
suspend fun simple3(): List<Int> {
    delay(1000L) // 假装我们正在计算
    return listOf(1, 2, 3)
}

/*
    1.3 流
    使用 List 结果类型，意味着我们只能一次返回所有值。
    为了表示异步计算的值流（stream），我们可以使用 Flow 类型（正如同步计算值会使用 Sequence 类型）：
 */
fun simple4(): Flow<Int> = flow { // 流构建器, 不再标有 suspend 修饰符
    for (i in 1..3) {
        delay(100)
        emit(i) //flow发射值
    }
}

/*
    1.3 流
    使用 List 结果类型，意味着我们只能一次返回所有值。
    为了表示异步计算的值流（stream），我们可以使用 Flow 类型（正如同步计算值会使用 Sequence 类型）：
 */
fun simple5(): Flow<Int> = flow { // 流构建器, 不再标有 suspend 修饰符
    for (i in 1..3) {
        delay(100)
        emit(i) //flow发射值
    }
}

fun test6_1() {
    println("1-------------------------------------------------")
    println(simple())
    println("1.1-------------------------------------------------")
    println(simple2().toList())
    runBlocking {
        println("1.2-------------------------------------------------")
        println(simple3())
        println("1.3-------------------------------------------------")
        // 启动并发的协程以验证主线程并未阻塞
        launch {
            for (i in 1..3) {
                println("It's not blocked $i.")
                delay(100L)
            }
        }
        simple4().collect { // flow收集值
            println(it)
        }
    }

    runBlocking {
        println("1.4-------------------------------------------------")
    }
}
