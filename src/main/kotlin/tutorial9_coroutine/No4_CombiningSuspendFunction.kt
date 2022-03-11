package tutorial9_coroutine

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

fun main() {
    /*
        组合挂起函数
     */
//    test4_1()
//    test4_2()
//    test4_3()
//    test4_4()
//    test4_5()
    test4_6()
}

suspend fun one(): Int {
    delay(1000)
    return 13
}

suspend fun two(): Int {
    delay(1000)
    return 14
}

/*
    1. 默认顺序调用
    因为这些代码是运行在协程中的，只要像常规的代码一样 顺序 都是默认的。
 */
fun test4_1() {
    println("1-------------------------------------------------")
    val time = measureTimeMillis {
        runBlocking {
            val _one = one()
            val _two = two()
            println("The result is ${_one + _two}.")
        }
    }
    println("Completed in $time ms.")
}

/*
    2. 使用 async 并发
    如果 one() 与 two() 之间没有依赖，并且更快的得到结果，可使用async。

    async 就类似于 launch，
    不同之处在于 launch 返回一个 Job 并且不附带任何结果值，而 async 返回一个 Deferred，
    可以使用 .await() 在一个延期的值上得到它的最终结果。

    Deferred 也是一个 Job，可以取消它：
    https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-deferred/index.html
 */
fun test4_2() {
    println("2-------------------------------------------------")
    val time = measureTimeMillis {
        runBlocking {
            val _one = async{ one() }
            val _two = async{ two() }
            println("The result is ${_one.await() + _two.await()}.")
        }
    }
    println("Completed in $time ms.")
}

/*
    3. 惰性启动的 async
    async 可以通过将 start 参数设置为 CoroutineStart.LAZY 而变为惰性的。
    在这个模式下，只有结果通过 await 获取的时候协程才会启动，或者在 Job 的 start 函数调用的时候。

    注意，如果我们只是在 println 中调用 await，而没有在单独的协程中调用 start，
    这将会导致顺序行为，直到 await 启动该协程 执行并等待至它结束，这并不是惰性的预期用例。
 */
fun test4_3() {
    println("3-------------------------------------------------")
    val time = measureTimeMillis {
        runBlocking {
            val _one = async(start = CoroutineStart.LAZY) { one() }
            val _two = async(start = CoroutineStart.LAZY) { two() }
            // 执行一些计算
            _one.start()  // 启动第一个
            _two.start()  // 启动第二个
            println("The result is ${_one.await() + _two.await()}.")
        }
    }
    println("Completed in $time ms.")
}

/*
    4. async 风格的函数
    它们只做异步计算并且需要使用延期的值来获得结果。

    注意，这些 xxxAsync 函数不是 挂起 函数。它们可以在任何地方使用。
    然而，它们总是在调用它们的代码中意味着异步（这里的意思是 并发 ）执行。
 */

// oneAsync函数的返回值类型是 Deferred<Int>
fun oneAsync() = GlobalScope.async {
    one()
}

// twoAsync函数的返回值类型是 Deferred<Int>
fun twoAsync() = GlobalScope.async {
    two()
}

fun test4_4() {
    println("4-------------------------------------------------")
    val time = measureTimeMillis {
        val _one = oneAsync()
        val _two = twoAsync()
        runBlocking {
                println("The result is ${_one.await() + _two.await()}.")
        }
    }
    println("Completed in $time ms.")
}

/*
    5. 使用 async 的结构化并发
    一个函数，并发的调用one() 与 two() 并且返回它们两个的结果之和。
    由于 async 被定义为了 CoroutineScope 上的扩展，我们需要将它写在作用域内:
 */
suspend fun coroutineSum(): Int = coroutineScope {
    val _one = async { one() }
    val _two = async { two() }
    _one.start()
    _two.start()
    _one.await() + _two.await()
}
fun test4_5() {
    println("5-------------------------------------------------")
    val time = measureTimeMillis {
        runBlocking {
            println("The result is ${coroutineSum()}.")
        }
    }
    println("Completed in $time ms.")
}

/*
    6. 如果在 concurrentSum 函数内部发生了错误，并且它抛出了一个异常， 所有在作用域中启动的协程都会被取消。
    取消始终通过协程的层次结构来进行传递：
 */
suspend fun failedCoroutineSum(): Int = coroutineScope {
    val one = async<Int> {
        try {
            delay(Long.MAX_VALUE)   // 模拟一个长时间的运算
            17
        } finally {
            println("First child was canceled.")
        }
    }

    val two = async<Int> {
        println("Second child throws an exception.")
        throw ArithmeticException()
    }

    one.await() + two.await()
}

fun test4_6() {
    println("6-------------------------------------------------")
    try {
        runBlocking {
            failedCoroutineSum()
        }
    } catch (e: Exception) {
        println("Computation Failed by $e")
    } finally {
        println("The coroutine quits.")
    }
}