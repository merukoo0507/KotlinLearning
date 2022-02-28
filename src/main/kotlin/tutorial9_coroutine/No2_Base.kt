package tutorial9_coroutine

import kotlinx.coroutines.*

fun main() {
    /*
        协程基础
     */

//    test2_1()
//    test2_2()
//    test2_2_2()
//    test2_3()
    test2_4()
//    test2_5()
//    test2_6()
//    test2_7()
//    test2_8()
}

/*
    1. 第一个协程程序
    本质上，协程是轻量级的线程。
    它们在某些 CoroutineScope 上下文中与 launch 协程构建器 一起启动。
    这里我们在 GlobalScope 中启动了一个新的协程，这意味着新协程的生命周期只受整个应用程序的生命周期限制。

    可以将 GlobalScope.launch { …… } 替换为 thread { …… }，
    并将 delay(……) 替换为 Thread.sleep(……) 达到同样目的。
    试试看（不要忘记导入 kotlin.concurrent.thread）。

    如果你首先将 GlobalScope.launch 替换为 thread，编译器会报以下错误：
    Error: Kotlin: Suspend functions are only allowed to be called from a coroutine or another suspend function

    这是因为 delay 是一个特殊的 挂起函数 ，它不会造成线程阻塞，但是会 挂起 协程，并且只能在协程中使用。
 */
fun test2_1() {
    println("1-------------------------------------------------")
    GlobalScope.launch {     // 在后台启动一个新的协程并继续
        delay(1000) // 非阻塞的等待 1 秒钟
        println("world!")     // 在延迟后打印输出
    }
    println("Hello")         // 协程已在等待时主线程还在继续
    Thread.sleep(2000) // 阻塞主线程 2 秒钟来保证 JVM 存活
}

/*
    2. 桥接阻塞与非阻塞的世界
    第一个示例在同一段代码中混用了 非阻塞的 delay(……) 与 阻塞的 Thread.sleep(……)。
    这容易让我们记混哪个是阻塞的、哪个是非阻塞的。 让我们显式使用 runBlocking 协程构建器来阻塞：
 */
fun test2_2() {
    println("2-------------------------------------------------")
    GlobalScope.launch {
        delay(1000)
        println("world!")
    }
    println("Hello ")   // 主线程中的代码会立即执行
    runBlocking {   // 但是这个表达式阻塞了主线程
        delay(2000) // ……我们延迟 2 秒来保证 JVM 的存活
    }
}

/*
    2.2 调用了 runBlocking 的主线程会一直 阻塞 直到 runBlocking 内部的协程执行完毕。
    这个示例可以使用更合乎惯用法的方式重写，使用 runBlocking 来包装 main 函数的执行：
 */
fun test2_2_2() = runBlocking {
    println("2.2-------------------------------------------------")
    GlobalScope.launch {
        delay(1000)
        println("world!")
    }
    println("Hello ")   // 主线程中的代码会立即执行
    delay(2000) // ……我们延迟 2 秒来保证 JVM 的存活
}

/*
    3. 等待一个作业
    延迟一段时间来等待另一个协程运行并不是一个好的选择。
    让我们显式等待所启动的后台 Job 执行结束（以非阻塞方式）：
 */
fun test2_3() = runBlocking {
    println("3-------------------------------------------------")
    var job = GlobalScope.launch {
        delay(1000)
        println("world!")
    }
    println("Hello ")
    job.join()  // 等待直到子协程执行结束
}

/*
    4. 结构化的并发

    协程的实际使用还有一些需要改进的地方。
    当我们使用 GlobalScope.launch 时，我们会创建一个顶层协程。
    虽然它很轻量，但它运行时仍会消耗一些内存资源。
    如果我们忘记保持对新启动的协程的引用，它还会继续运行。
    如果协程中的代码挂起了会怎么样（例如，我们错误地延迟了太长时间），如果我们启动了太多的协程并导致内存不足会怎么样？
    必须手动保持对所有已启动协程的引用并 join 之很容易出错。

    我们可以在代码中使用结构化并发，在执行操作所在的指定作用域内启动协程，
    而不是像通常使用线程（线程总是全局的）那样在 GlobalScope 中启动。

    在我们的示例中，我们使用 runBlocking 协程构建器将 main 函数转换为协程。
    包括 runBlocking 在内的每个协程构建器都将 CoroutineScope 的实例添加到其代码块所在的作用域中。
    我们可以在这个作用域中启动协程
    而无需显式 join 之，
    因为外部协程（示例中的 runBlocking）直到在其作用域中启动的所有协程都执行完毕后才会结束。
 */

fun test2_4() = runBlocking {
    println("4-------------------------------------------------")
    launch {
        delay(1000)
        println("world!")
    }
    println("Hello ")
}

/*
    5. 作用域构建器
    使用 coroutineScope 构建器声明自己的作用域。
    它会创建一个协程作用域并且在所有已启动子协程执行完毕之前不会结束。

    runBlocking 与 coroutineScope 可能看起来很类似，因为它们都会等待其协程体以及所有子协程结束。
    主要区别在于，runBlocking 方法会阻塞当前线程来等待，
    而 coroutineScope 只是挂起，会释放底层线程用于其他用途。
    由于存在这点差异，runBlocking 是常规函数，而 coroutineScope 是挂起函数。

    请注意，（当等待内嵌 launch 时）紧挨“Task from coroutine scope”消息之后， 就会执行并输出“Task from runBlocking”
    尽管 coroutineScope 尚未结束。
 */
fun test2_5() = runBlocking { // this: CoroutineScope
    println("5-------------------------------------------------")
    launch {
        delay(2000)
        println("Task from runBlocking")
    }

    coroutineScope {    // 创建一个协程作用域
        launch {
            delay(1000)
            println("Task from nested launch")
        }

        launch {
            delay(900)
            println("Task from nested launch2")
        }

        delay(1000)
        println("Task from coroutine scope")     // 这一行会在内嵌 launch 之前输出
    }
    println("Coroutine scope is over")  // 这一行在内嵌 launch 执行完毕后才输出
}

/*
    6. 提取函数重构
    我们来将 launch { …… } 内部的代码块提取到独立的函数中。
    当你对这段代码执行“提取函数”重构时，你会得到一个带有 suspend 修饰符的新函数。
    这是你的第一个挂起函数。

    在协程内部可以像普通函数一样使用挂起函数，
    不过其额外特性是，同样可以使用其他挂起函数（如本例中的 delay）来挂起协程的执行。
 */
fun test2_6() = runBlocking {
    println("6-------------------------------------------------")
    launch {
        doWorld()
    }
    println("Hello")
}

suspend fun doWorld() {
    delay(1000)
    println("World!")
}

/*
    7. 协程很轻量
    它启动了 10 万个协程，并且在 2 秒钟后，每个协程都输出一个点。
    现在，尝试使用线程来实现。会发生什么？（很可能你的代码会产生某种内存不足的错误）
 */
fun test2_7() = runBlocking {
    println("7-------------------------------------------------")
    coroutineScope {
        repeat(10000) {
            launch {
                delay(2000)
                print(". ")
            }
        }
    }
    println("\nend")
}

/*
    8. 全局协程像守护线程
    以下代码在 GlobalScope 中启动了一个长期运行的协程，
    该协程每秒输出“I'm sleeping”两次，之后在主函数中延迟一段时间后返回。
 */
fun test2_8() = runBlocking {
    println("8-------------------------------------------------")
    GlobalScope.launch {
        repeat(10) { i ->
            println("Thread $i ...")
        }
    }
}