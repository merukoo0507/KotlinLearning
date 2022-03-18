package tutorial9_coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() {
    /*
        异步流
     */

//    test6_1()
//    test6_1_1()
//    test6_1_2()
//    test6_1_3()
//    test6_2()
//    test6_3()
//    test6_4()
//    test6_5()
//    test6_6_1()
//    test6_6_2()
//    test6_7()
//    test6_8()
//    test6_9()
}

/*
    1. 表示多个值
 */
fun simple() = listOf(1, 2, 3)

fun test6_1() {
    println("1-------------------------------------------------")
    println(simple())
}

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

fun test6_1_1() {
    println("1.1-------------------------------------------------")
    println(simple1().toList())
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

fun test6_1_2() = runBlocking {
    println("1.2-------------------------------------------------")
    println(simple2())
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

fun test6_1_3() = runBlocking {
    println("1.3-------------------------------------------------")
    // 启动并发的协程以验证主线程并未阻塞
    launch {
//        for (i in 1..3) {
//            println("It's not blocked $i.")
//            delay(100L)
//        }
    }
    simple3().collect { // flow收集值
        println(it)
    }
}

fun simple4(): Flow<Int> = flow { // 流构建器, 不再标有 suspend 修饰符
    println("Flow started.")
    for (i in 1..3) {
        delay(100)
        emit(i) //flow发射值
    }
}

fun test6_2() = runBlocking {
    /*
        2 流是冷的
        Flow 是一种类似于序列的冷流 — 这段 flow 构建器中的代码直到流被收集的时候才运行。

        这是返回一个流的 simple 函数没有标记 suspend 修饰符的主要原因。
        通过它自己，simple() 调用会尽快返回且不会进行任何等待。
        该流在每次收集的时候启动， 这就是为什么当我们再次调用 collect 时我们会看到“Flow started”。
     */
    println("2-------------------------------------------------")
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

fun simple5(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100)
        println("Emit: $i")
        emit(i) //flow发射值
    }
}

fun test6_3() = runBlocking {
    /*
        3 流取消基础
        流采用与协程同样的协作取消。
        像往常一样，流的收集可以在当流在一个可取消的挂起函数（例如 delay）中挂起的时候取消。
        以下示例展示了当 withTimeoutOrNull 块中代码在运行的时候流是如何在超时的情况下取消并停止执行其代码的：
     */
    println("3-------------------------------------------------")
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

fun test6_4() = runBlocking {
    /*
        4 流构建器
        先前示例中的 flow { ... } 构建器是最基础的一个。还有其他构建器使流的声明更简单：

        - flowOf 构建器定义了一个发射固定值集的流。
        - 使用 .asFlow() 扩展函数，可以将各种集合与序列转换为流。
        因此，从流中打印从 1 到 3 的数字的示例可以写成：
     */
    println("4-------------------------------------------------")
    println("asFlow - ")
    (1..3).asFlow().collect {
        println(it)
    }
    println("flowOf - ")
    flowOf(1, 2, 3).collect {
        println(it)
    }
}

suspend fun performRequest(request: Int): String {
    delay(1000L)
    return "response $request"
}

fun test6_5() = runBlocking {
    /*
        5 过渡流操作符
        可以使用操作符转换流，就像使用集合与序列一样。
        过渡操作符应用于上游流，并返回下游流。
        这些操作符也是冷操作符，就像流一样。这类操作符本身不是挂起函数。
        它运行的速度很快，返回新的转换流的定义。

        基础的操作符拥有相似的名字，比如 map 与 filter。 流与序列的主要区别在于这些操作符中的代码可以调用挂起函数。

        举例来说，一个请求中的流可以使用 map 操作符映射出结果，即使执行一个长时间的请求操作也可以使用挂起函数来实现：
     */
    println("5-------------------------------------------------")
    (1..3).asFlow().map {
        performRequest(it)
    }.collect {
        println(it)
    }
}

fun test6_6_1() = runBlocking {
    /*
        6.1 转换操作符
        在流转换操作符中，最通用的一种称为 transform。
        它可以用来模仿简单的转换，例如 map 与 filter，以及实施更复杂的转换。
        使用 transform 操作符，我们可以 发射 任意值任意次。

        比如说，使用 transform 我们可以在执行长时间运行的异步请求之前发射一个字符串并跟踪这个响应：
     */
    println("6.1-------------------------------------------------")
    (1..3).asFlow()
        .transform { request ->
            emit("Make request: $request")
            emit(performRequest(request))
        }
        .collect {
            println(it)
        }
}

fun nums(): Flow<Int> = flow {
    try {
        emit(1)
        emit(2)
        println("This line will not execute.")
        emit(3)
    } finally {
        println("Finally in numbers.")
    }
}

fun test6_6_2() = runBlocking {
    /*
        6.2 限长操作符
        限长过渡操作符（例如 take）在流触及相应限制的时候会将它的执行取消。
        协程中的取消操作总是通过抛出异常来执行，这样所有的资源管理函数（如 try {...} finally {...} 块）会在取消的情况下正常运行：
     */
    println("6.2-------------------------------------------------")
    nums()
        .take(2)
        .collect {
            println(it)
        }
}

fun test6_7() = runBlocking {
    /*
        7. 末端流操作符
        末端操作符是在流上用于启动流收集的挂起函数。 collect 是最基础的末端操作符，但是还有另外一些更方便使用的末端操作符：

        转化为各种集合，例如 toList 与 toSet。
        获取第一个（first）值与确保流发射单个（single）值的操作符。
        使用 reduce 与 fold 将流规约到单个值。
     */
    println("7-------------------------------------------------")
    val num = (1..5).asFlow()
        .map { it * it } // 数字 1 至 5 的平方
        .reduce { accumulator, value ->  // 求和（末端操作符）
        println("accumulator: $accumulator, value: $value")
        accumulator + value
        }
    println("num: $num")
}

fun test6_8() = runBlocking {
    /*
        8. 流是连续的
        流的每次单独收集都是按顺序执行的，除非进行特殊操作的操作符使用多个流。
        该收集过程直接在协程中运行，该协程调用末端操作符。

        默认情况下不启动新协程。
        从上游到下游每个过渡操作符都会处理每个发射出的值然后再交给末端操作符。
        请参见以下示例，该示例过滤偶数并将其映射到字符串：
     */
    println("8-------------------------------------------------")
    (1..5).asFlow().filter {
        println("Filter $it")
        it % 2 == 0
    }.map {
        println("Map $it")
        "string $it"
    }.collect {
        println("it")
    }
}

fun simple6(): Flow<Int> = flow {
    withContext(Dispatchers.Default) {
        for (i in 1..3) {
            Thread.sleep(100L)
            emit(i)
        }
    }
}

/*
    9.2 flowOn 操作符
    例外的是 flowOn 函数，该函数用于更改流发射的上下文。
    以下示例展示了更改流上下文的正确方法，该示例还通过打印相应线程的名字以展示它们的工作方式：

    这里要观察的另一件事是 flowOn 操作符已改变流的默认顺序性。
    现在收集发生在一个协程中（“coroutine#1”）而发射发生在运行于另一个线程中与收集协程并发运行的另一个协程（“coroutine#2”）中。
    当上游流必须改变其上下文中的 CoroutineDispatcher 的时候，flowOn 操作符创建了另一个协程。
 */
fun simple7(): Flow<Int> = flow<Int> {
    for (i in 1..3) {
        Thread.sleep(100L)
        log("Emit $i")
        emit(i)
    }
}.flowOn(Dispatchers.Default)

fun test6_9() = runBlocking {
    /*
        9 流上下文
        流的收集总是在调用协程的上下文中发生。
        所以默认的，flow { ... } 构建器中的代码运行在相应流的收集器提供的上下文中。

        举例来说，考虑打印线程的一个 simple 函数的实现， 它被调用并发射三个数字：
        由于 simple().collect 是在主线程调用的，那么 simple 的流主体也是在主线程调用的。
        这是快速运行或异步代码的理想默认形式，它不关心执行的上下文并且不会阻塞调用者。
     */
    println("9-------------------------------------------------")
    simple4().collect {
        log("Collected $it")
    }

    /*
        9.1 withContext 发出错误
        然而，长时间运行的消耗 CPU 的代码也许需要在 Dispatchers.Default 上下文中执行，
        并且更新 UI 的代码也许需要在 Dispatchers.Main 中执行。
        通常，withContext 用于在 Kotlin 协程中改变代码的上下文，但是 flow {...} 构建器中的代码必须遵循上下文保存属性，
        并且不允许从其他上下文中发射（emit）。

        这段代码产生异常:
     */
//    println("9.1-------------------------------------------------")
//    simple6().collect {
//        log("Collected $it")
//    }

    println("9.2-------------------------------------------------")
    simple7().collect {
        log("Collected $it")
    }
}