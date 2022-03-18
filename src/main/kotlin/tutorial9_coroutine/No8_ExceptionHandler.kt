package tutorial9_coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import java.io.IOException

fun main() {
    /*
        异常处理
        本节内容涵盖了异常处理与在异常上取消。
        我们已经知道被取消的协程会在挂起点抛出 CancellationException 并且它会被协程的机制所忽略。
        在这里我们会看看在取消过程中抛出异常或同一个协程的多个子协程抛出异常时会发生什么。
     */
//    test8_1()
//    test8_2()
//    test8_3()
//    test8_4()
//    test8_5_1()
    test8_5_2()
    test8_5_3()
}

/*
    1. 异常的传播
    协程构建器有两种形式：
    - 自动传播异常（launch 与 actor）
        当这些构建器用于创建一个根协程时，即该协程不是另一个协程的子协程， 前者这类构建器将异常视为未捕获异常，
        类似 Java 的 Thread.uncaughtExceptionHandler，
    - 向用户暴露异常（async 与 produce）。
        而后者则依赖用户来最终消费异常，例如通过 await 或 receive（produce 与 receive 的相关内容包含于通道章节）。
 */
fun test8_1() {
    runBlocking {
        println("1-------------------------------------------------")
        val job = GlobalScope.launch { // launch 根协程
            println("Throwing exception from launch")
            throw IndexOutOfBoundsException()   // 我们将在控制台打印 Thread.defaultUncaughtExceptionHandler
        }
        job.join()  // 等待直到子协程执行结束
        println("Joined failed job")

        val defered = GlobalScope.async { // async 根协程
            println("Throwing exception from async")
            throw IndexOutOfBoundsException()   // 没有打印任何东西，依赖用户去调用等待
        }

        try {
            defered.await()
        } catch (e: Exception) {
            println(e)
        }
    }
}

/*
    2.  CoroutineExceptionHandler
    将未捕获异常打印到控制台的默认行为是可自定义的。

    根协程中的 CoroutineExceptionHandler 上下文元素可以被用于这个根协程通用的 catch 块，
    及其所有可能自定义了异常处理的子协程。
    它类似于 Thread.uncaughtExceptionHandler 。

    你无法从 CoroutineExceptionHandler 的异常中恢复。
    当调用处理者的时候，协程已经完成并带有相应的异常。
    通常，该处理者用于记录异常，显示某种错误消息，终止和（或）重新启动应用程序。

    在 JVM 中可以重定义一个全局的异常处理者来将所有的协程通过 ServiceLoader 注册到 CoroutineExceptionHandler。
    全局异常处理者就如同 Thread.defaultUncaughtExceptionHandler 一样，
    在没有更多的指定的异常处理者被注册的时候被使用。
    在 Android 中，uncaughtExceptionPreHandler 被设置在全局协程异常处理者中。

    CoroutineExceptionHandler 仅在未捕获的异常上调用 — 没有以其他任何方式处理的异常。
    特别是，所有子协程（在另一个 Job 上下文中创建的协程）委托<!– 它们的父协程处理它们的异常，然后它们也委托给其父协程，以此类推直到根协程，
    因此永远不会使用在其上下文中设置的 CoroutineExceptionHandler。
    除此之外，async 构建器始终会捕获所有异常并将其表示在结果 Deferred 对象中， 因此它的 CoroutineExceptionHandler 也无效。
 */
fun test8_2() = runBlocking {
    println("2-------------------------------------------------")

    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("CoroutineExceptionHandler got $throwable")
    }

    val job = GlobalScope.launch(handler) {
        throw AssertionError()
    }

    val defered = GlobalScope.async(handler) {
        throw ArithmeticException() // 没有打印任何东西，依赖用户去调用 deferred.await()
    }

    joinAll(job, defered)
}

/*
    3.取消与异常
    取消与异常紧密相关。
    协程内部使用 CancellationException 来进行取消，这个异常会被所有的处理者忽略，
    所以那些可以被 catch 代码块捕获的异常仅仅应该被用来作为额外调试信息的资源。
    当一个协程使用 Job.cancel 取消的时候，它会被终止，但是它不会取消它的父协程。
 */
fun test8_3() = runBlocking {
    println("3-------------------------------------------------")
    val job = launch {
        val child = launch {
            try {
                delay(Long.MAX_VALUE)
            } finally {
                println("Child is canceled.")
            }
        }
        yield()
        println("Canceling child.")
        child.cancel()
        yield()
        println("Parent is not canceled.")
    }
    job.join()

    /*
        如果一个协程遇到了 CancellationException 以外的异常，它将使用该异常取消它的父协程。
        这个行为无法被覆盖，并且用于为结构化的并发（structured concurrency） 提供稳定的协程层级结构。
        CoroutineExceptionHandler 的实现并不是用于子协程。
     */
    println("3.2-------------------------------------------------")

    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("CoroutineExceptionHandler got $throwable")
    }
    val job2 = GlobalScope.launch(handler){
        launch {
            try {
                delay(Long.MAX_VALUE)
            } finally {
                withContext(NonCancellable) {
                    println("Children are cancelled, but exception is not handled until all children terminate")
                    delay(1000L)
                    println("The first child finished its non cancellable block")
                }
            }
        }
        launch { // 第二个子协程
            delay(1000)
            println("Second child throws an exception")
            throw ArithmeticException()
        }
    }
    job2.join()
}

/*
    4.异常聚合
    当协程的多个子协程因异常而失败时，
    一般规则是“取第一个异常”，因此将处理第一个异常。
    在第一个异常之后发生的所有其他异常都作为被抑制的异常绑定至第一个异常。

    注意：上面的代码将只在 JDK7 以上支持 suppressed 异常的环境中才能正确工作。
 */
fun test8_4() = runBlocking {
    println("4-------------------------------------------------")
    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("CoroutineExceptionHandler got $throwable with suppressed: ${throwable.suppressed.contentToString()}")
    }
    val job = GlobalScope.launch(handler) {
        launch {
            try {
                delay(Long.MAX_VALUE) // 当另一个同级的协程因 IOException  失败时，它将被取消
            } finally {
                println("Child2 have a exception.")
                ArithmeticException()
            }
        }
        launch {
            delay(100)
            throw IOException()
        }
        delay(Long.MAX_VALUE)
    }
    job.join()
}

/*
   5. 监督
    正如我们之前研究的那样，取消是在协程的整个层次结构中传播的双向关系。
    让我们看一下需要单向取消的情况。

    此类需求的一个良好示例是在其作用域内定义作业的 UI 组件。
    如果任何一个 UI 的子作业执行失败了，它并不总是有必要取消（有效地杀死）整个 UI 组件，
    但是如果 UI 组件被销毁了（并且它的作业也被取消了），由于它的结果不再被需要了，它有必要使所有的子作业执行失败。

    另一个例子是服务进程孵化了一些子作业并且需要 监督 它们的执行，追踪它们的故障并在这些子作业执行失败的时候重启。
 */
/*
    5.1 监督作业
    SupervisorJob 可以用于这些目的。
    它类似于常规的 Job，唯一的不同是：SupervisorJob 的取消只会向下传播。这是很容易用以下示例演示：
 */
fun test8_5_1() = runBlocking {
    println("5.1-------------------------------------------------")
    val supervisor = SupervisorJob()
    with(CoroutineScope(coroutineContext + supervisor)) {
        // 启动第一个子作业——这个示例将会忽略它的异常（不要在实践中这么做！）
        val firstChild = launch(CoroutineExceptionHandler { _, _ -> }) {
            println("The first child is failing.")
            throw AssertionError()
        }
        // 启动第二个子作业
        val secondChild = launch {
            firstChild.join()
            // 取消了第一个子作业且没有传播给第二个子作业
            println("The first child is cancelled: ${firstChild.isCancelled}, but the second one is still active")
            try {
                delay(1000)
            } finally {
                // 但是取消了监督的传播
                println("The second child is cancelled because the supervisor was cancelled.")
            }
        }
        // 等待直到第一个子作业失败且执行完成
        println("firstChild join")
        firstChild.join()
        println("Cancelling supervisor")
        supervisor.cancel()

        //我不動要加這行
        secondChild.join()
    }
}

/*
    5.2 监督作用域
    对于作用域的并发，可以用 supervisorScope 来替代 coroutineScope 来实现相同的目的。
    它只会单向的传播并且当作业自身执行失败的时候将所有子作业全部取消。
    作业自身也会在所有的子作业执行结束前等待， 就像 coroutineScope 所做的那样。
 */
fun test8_5_2() = runBlocking {
    println("5.2-------------------------------------------------")
    try {
        supervisorScope {
            val child = launch {
                try {
                    println("The child is sleeping")
                } finally {
                    println("The child is cancelled.")
                }
            }
            // 使用 yield 来给我们的子作业一个机会来执行打印
            yield()
            println("Throwing an exception from the scope")
            AssertionError()
        }
    } catch (e: Error) {
        println("Caught an assertion error")
    }
}

/*
    5.3 监督协程中的异常
    常规的作业和监督作业之间的另一个重要区别是异常处理。

    监督协程中的每一个子作业应该通过异常处理机制处理自身的异常。
    这种差异来自于子作业的执行失败不会传播给它的父作业的事实。

    这意味着在 supervisorScope 内部直接启动的协程确实使用了设置在它们作用域内的 CoroutineExceptionHandler，
    与父协程的方式相同 （参见 CoroutineExceptionHandler 小节以获知更多细节）。
 */
fun test8_5_3() = runBlocking {
    println("5.3-------------------------------------------------")
    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("CoroutineExceptionHandler got $throwable")
    }

    supervisorScope {
        launch(handler) {
            println("The child throws an exception")
            throw AssertionError()
        }
        println("The scope is completing")
    }
    println("The scope is completed")
}