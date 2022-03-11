package tutorial9_coroutine

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

fun main() {
    /*
        协程上下文与调度器
     */
//    test5_1()
//    test5_2()
//    test5_3()
//    test5_4()
//    test5_5()
//    test5_6()
//    test5_7()
//    test5_8()
//    test5_9()
    test5_10()
}

// 程序执行使用了 -Dkotlinx.coroutines.debug JVM 参数，输出如下所示：
fun log(msg: String, remain: String? = null) = println("$msg: [${Thread.currentThread().name}]${remain ?: ""}")

/*
    1. 调度器与线程
    协程上下文包含一个 协程调度器 （参见 CoroutineDispatcher）它确定了相关的协程在哪个线程或哪些线程上执行。
    协程调度器可以将协程限制在一个特定的线程执行，
    或将它分派到一个线程池，
    亦或是让它不受限地运行。

    所有的协程构建器诸如 launch 和 async 接收一个可选的 CoroutineContext 参数，
    它可以被用来显式的为一个新协程或其它上下文元素指定一个调度器。
 */
fun test5_1() {
    println("1-------------------------------------------------")
    runBlocking {
        // launch { …… } 时不传参数，它从启动了它的 CoroutineScope 中承袭了上下文（以及调度器）。
        // 在这个案例中，它从 main 线程中的 runBlocking 主协程承袭了上下文。
        launch {
            log("main runBlocking")
        }

        // Dispatchers.Unconfined 是一个特殊的调度器，在调用它的线程启动了一个协程，但它仅仅只是运行到第一个挂起点，这会在5-2中讲到。
        launch(Dispatchers.Unconfined) {
            log("Unconfined")
        }

        // 当协程在 GlobalScope 中启动时，使用的是由 Dispatchers.Default 代表的默认调度器。 默认调度器使用共享的后台线程池。
        launch(Dispatchers.Default) {
            log("Default")
        }

        // 为协程的运行启动了一个线程。 一个专用的线程是一种非常昂贵的资源。
        // 在真实的应用程序中两者都必须被释放，
        // 当不再需要的时候，使用 close 函数，或存储在一个顶层变量中使它在整个应用程序中被重用。
        launch(newSingleThreadContext("MyOwnThread1")) {
            log("newSingleThreadContext")
        }
    }

    GlobalScope.launch(newSingleThreadContext("MyOwnThread2")) {
        log("newSingleThreadContext2")
    }
}

/*
    2. 非受限调度器 vs 受限调度器
    Dispatchers.Unconfined 协程调度器，在调用它的线程启动了一个协程，但它仅仅只是运行到第一个挂起点。
    "挂起后，它恢复线程中的协程"，而这完全由被调用的挂起函数来决定。
    非受限的调度器非常适用于执行不消耗 CPU 时间的任务，以及不更新局限于特定线程的任何共享数据（如UI）的协程。
    非受限调度器不应该在通常的代码中使用。

    另一方面，该调度器默认继承了外部的 CoroutineScope。
    当它被限制在调用者线程时，runBlocking 协程的默认调度器，继承自它，
    将会有效地限制协程在该线程运行并且具有可预测的 FIFO 调度。
 */
fun test5_2() {
    println("2-------------------------------------------------")
    runBlocking {
        // 非受限调度器
        launch(Dispatchers.Unconfined) {
            log("Unconfined")
            delay(500)
            log("Unconfined")
        }

        // 受限调度器
        launch {
            log("main runBlocking")
            delay(500)
            log("main runBlocking")
        }
    }
}

/*
    3. 调试协程与线程
    协程可以在一个线程上挂起并在其它线程上恢复。
    如果没有特殊工具，甚至对于一个单线程的调度器也是难以弄清楚协程在何时何地正在做什么事情。
    - 用 IDEA 调试
    https://kotlinlang.org/docs/debug-coroutines-with-idea.html
    https://www.kotlincn.net/docs/reference/coroutines/coroutine-context-and-dispatchers.html#%E7%94%A8-idea-%E8%B0%83%E8%AF%95

    - 用日志调试 (範例如下)
    这种特性在日志框架中是普遍受支持的。
    但是在使用协程时，单独的线程名称不会给出很多协程上下文信息，
    所以 kotlinx.coroutines 包含了调试工具来让它更简单。
 */
fun test5_3() {
    println("3-------------------------------------------------")
    runBlocking {
        val a = async {
            log("a")
            6
        }
        val b = async {
            log("b")
            7
        }
        println("a + b = ${a.await() + b.await()}")
    }
}

/*
    4. 在不同线程间跳转
 */
fun test5_4() {
    println("4-------------------------------------------------")

    // 当我们不再需要某个在 newSingleThreadContext 中创建的线程的时候， 它使用了 Kotlin 标准库中的 use 函数来释放该线程。
    newSingleThreadContext("Ctx1").use { ctx1 ->
        newSingleThreadContext("Ctx2").use { ctx2 ->

            // 使用 runBlocking 来显式指定了一个上下文
            runBlocking(ctx1) {
                log("Start in ctx1.")

                //使用 withContext 函数来改变协程的上下文，而仍然驻留在相同的协程中
                withContext(ctx2) {
                    log("Start in ctx2.")
                }
            }
        }
    }
}

/*
    5. 上下文中的作业
    协程的 Job 是上下文的一部分，并且可以使用 coroutineContext [Job] 表达式在上下文中检索它：

    CoroutineScope 中的 isActive 只是 coroutineContext[Job]?.isActive == true 的一种方便的快捷方式。
 */
suspend fun <T> printJob(job: Deferred<T>, msg: String) = println("$msg: ${coroutineContext[job.key]}")

fun test5_5() {
    println("5-------------------------------------------------")
    runBlocking {
        GlobalScope.launch {
            val a = async {
                10
            }
            log("log a")
            printJob(a, "job a")
        }
        val b = async {
            10
        }
        log("log b")
        printJob(b, "job b")
    }
}

/*
    6. 子协程
    当一个协程被其它协程在 CoroutineScope 中启动的时候，
    它将通过 CoroutineScope.coroutineContext 来承袭上下文，
    并且这个新协程的 Job 将会成为父协程作业的 子 作业。
    当一个父协程被取消的时候，所有它的子协程也会被递归的取消。

    然而，当使用 GlobalScope 来启动一个协程时，则新协程的作业没有父作业。
    因此它与这个启动的作用域无关且独立运作。
 */
fun test5_6() {
    println("6-------------------------------------------------")
    runBlocking {
        // 启动一个协程来处理某种传入请求（request）
        val request = launch {
            // 孵化了两个子作业, 其中一个通过 GlobalScope 启动
            GlobalScope.launch {
                println("job1: I run in GlobalScope and execute independently!")
                delay(1000)
                println("job1: I'm not affected by cancellation of the request")
            }

            launch {
                delay(100)
                println("job2: I'm a child of the request coroutine.")
                delay(1000)
                println("job2: I will not execute this line if my parent request is cancelled.")
            }
        }

        delay(500)
        // 取消请求（request）的执行
        request.cancel()
        delay(1000) // 延迟一秒钟来看看发生了什么
        println("main: Who has survived request cancellation?")
    }
}

/*
    7. 父协程的职责
    一个父协程总是等待所有的子协程执行结束。父协程并不显式的跟踪所有子协程的启动，
    并且不必使用 Job.join 在最后的时候等待它们：
 */
fun test5_7() {
    println("7-------------------------------------------------")
    runBlocking {
        val request = launch {
            repeat(3) { i ->
                launch {
                    delay(500L * i)
                    println("Child coroutine: $i")
                }
            }
            println("request: I'm done and I don't explicitly join my children that are still active")
        }
        request.join()
        println("The request is completed.")
    }
}

/*
    8. 组合上下文中的元素
    有时我们需要在协程上下文中定义多个元素。
    我们可以使用 + 操作符来实现。
    比如说，我们可以显式指定一个调度器来启动协程并且同时显式指定一个命名：
 */
fun test5_8() {
    println("8-------------------------------------------------")
    runBlocking {
        launch(Dispatchers.Default + CoroutineName("test")) {
            log("test")
        }
    }
}

/*
    9. 协程作用域
    让我们将关于上下文，子协程以及作业的知识综合在一起。
    假设我们的应用程序拥有一个具有生命周期的对象，但这个对象并不是一个协程。
    举例来说，我们编写了一个 Android 应用程序并在 Android 的 activity 上下文中启动了一组协程来使用异步操作拉取并更新数据以及执行动画等等。
    所有这些协程必须在这个 activity 销毁的时候取消以避免内存泄漏。
    我们也可以手动操作上下文与作业，以结合 activity 的生命周期与它的协程，
    Android 在所有具有生命周期的实体中都对协程作用域提供了一等的支持。 请查看相关文档:
    https://developer.android.com/topic/libraries/architecture/coroutines#lifecyclescope

    但是 kotlinx.coroutines 提供了一个封装：CoroutineScope 的抽象。
    你应该已经熟悉了协程作用域，因为所有的协程构建器都声明为在它之上的扩展。

    我们通过创建一个 CoroutineScope 实例来管理协程的生命周期，并使它与 activity 的生命周期相关联。

    - CoroutineScope 可以通过 CoroutineScope() 创建了一个通用作用域，
    - MainScope() 工厂函数使用 Dispatchers.Main 作为默认调度器的 UI 应用程序 创建作用域：
 */
class Activity {
    private val main = CoroutineScope(Dispatchers.Default)  // use Default for test purposes
//    private val main = MainScope()

    fun destroy() {
        main.cancel()
    }

    fun doSomething() {
        main.launch {
            repeat(10) { i ->
                launch {
                    delay(200L * i)
                    println("Coroutine $i is finished.")
                }
            }
        }
    }
}

fun test5_9() {
    println("9-------------------------------------------------")
    var activity = Activity()
    activity.doSomething()
    println("Launch activity.")

    runBlocking {
        delay(500L)
        activity.destroy() // 取消所有的协程
        delay(1000) // 为了在视觉上确认它们没有工作
    }
}

/*
    10. 线程局部数据
    能够将一些线程局部数据传递到协程与协程之间是很方便的。
    然而，由于它们不受任何特定线程的约束，如果手动完成，可能会导致出现样板代码。

    ThreadLocal， asContextElement 扩展函数在这里会充当救兵。
    它创建了额外的上下文元素， 且保留给定 ThreadLocal 的值，并在每次协程切换其上下文时恢复它。

    很容易忘记去设置相应的上下文元素。
    如果运行协程的线程不同， 在协程中访问的线程局部变量则可能会产生意外的值。
    为了避免这种情况，建议使用 ensurePresent 方法并且在不正确的使用时快速失败。

    ThreadLocal 具有一流的支持，可以与任何 kotlinx.coroutines 提供的原语一起使用。
    但它有一个关键限制，即：
    当一个线程局部变量变化时，则这个新值不会传播给协程调用者（因为上下文元素无法追踪所有 ThreadLocal 对象访问），并且下次挂起时更新的值将丢失。 使用 withContext 在协程中更新线程局部变量，详见 asContextElement。
 */
val threadLocal = ThreadLocal<String>()

fun test5_10() = runBlocking {
    println("10-------------------------------------------------")
    threadLocal.set("main")
    log("Pre-main, current thread", "thread local value: ${threadLocal.get()}")

    // Dispatchers.Default 在后台线程池中启动了一个新的协程
    // 在线程池中的不同线程中，但它仍然具有线程局部变量的值
    val job = launch(Dispatchers.Default + threadLocal.asContextElement("launch")) {
        log("Launch start", "thread local value: ${threadLocal.get()}")
        yield()
        log("After yield", "thread local value: ${threadLocal.get()}")
    }

    job.join()
    log("Post-main, current thread", "thread local value: ${threadLocal.get()}")
}