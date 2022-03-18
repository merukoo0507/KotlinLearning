package tutorial9_coroutine

import kotlinx.coroutines.*

fun main() {
    /*
        取消与超时
     */
//    test3_1()
//    test3_2()
//    test3_3()
//    test3_4()
//    test3_5()
    test3_6()
//    test3_6_2()
//    test3_7()
//    test3_7_2()
}

/*
    1. 取消协程的执行
    也有一个可以使 Job 挂起的函数 cancelAndJoin 它合并了对 cancel 以及 join 的调用。
 */
fun test3_1() = runBlocking {
    println("1-------------------------------------------------")
    val job = launch {
        repeat(100) { i ->
            println("Thread time: ${i * 0.5}")
            delay(500)
        }
    }
    delay(1300)
    println("Try to cancel.")
    job.cancel()
    job.join()  //等待作业执行结束
    println("Now I can quit.")
}

/*
    2. 取消是协作的
    一段协程代码必须协作才能被取消。
    所有 kotlinx.coroutines 中的挂起函数都是可被取消的 。
    它们检查协程的取消，并在取消时抛出 CancellationException。

    然而，如果协程正在执行计算任务，并且没有检查取消的话，那么它是不能被取消的：
 */
fun test3_2() = runBlocking {
    println("2-------------------------------------------------")
    var job = launch(Dispatchers.Default) {     // 在后台启动一个新的协程并继续
        var nextPrintTime = System.currentTimeMillis()
        var i = 0
        while(i < 5) {    // 一个执行计算的循环，只是为了占用 CPU
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("Thread time: ${i * 0.5}")
                nextPrintTime += 500L
                i++
            }
        }
    }
    delay(1500)
    println("Try to cancel the job.")
    job.cancelAndJoin()
    println("Now I can quit.")
}

/*
    3. 使计算代码可取消
    有两种方法来使执行计算的代码可以被取消:
    - 第一种方法是定期调用挂起函数来检查取消。对于这种目的 yield 是一个好的选择。

    - 另一种方法是显式的检查取消状态。(這裡是第二种方法)
    - isActive 是一个可以被使用在 CoroutineScope 中的扩展属性
      https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/is-active.html
 */
fun test3_3() = runBlocking {
    println("3-------------------------------------------------")
    var currentTime = System.currentTimeMillis()
    var i = 0
    var job = launch(Dispatchers.Default) {
        while(isActive) {
            if (System.currentTimeMillis() >= currentTime) {
                println("Thread time: ${i * 0.5}")
                currentTime += 500L
                i++
            }
        }
    }
    delay(1500)
    println("Try to cancel the job.")
    job.cancelAndJoin()
    println("Now I can quit.")
}

/*
    4. 在 finally 中释放资源
    可被取消的挂起函数, 被取消时會抛出 CancellationException。

    一般在协程被取消的时候执行它们的终结动作:
    - 使用try {……} finally {……} 表达式 (這裡是第一种方法),
      join 和 cancelAndJoin 等待了所有的终结动作执行完毕
    - 以及 Kotlin 的 use 函数
 */
fun test3_4() = runBlocking {
    println("4-------------------------------------------------")
    var job = launch {
        try {
            var i = 0
            while (true) {
                println("Thread time: ${i * 0.5}")
                delay(500)
                i++
            }
        } finally {
            println("The job is running finally.")
//            delay(1000L)
//            println("job: And I've just delayed for 1 sec.")
        }
    }
    delay(1500)
    job.cancelAndJoin()
    println("Now I can quit.")
}

/*
    5. 运行不能取消的代码块
    在前一个例子中任何尝试在 finally 块中调用挂起函数的行为都会抛出 CancellationException，
    因为这里持续运行的代码是可以被取消的。
    通常所有良好的关闭操作（关闭一个文件、取消一个作业、或是关闭任何一种通信通道）通常都是非阻塞的，并且不会调用任何挂起函数。
    然而，在真实的案例中，当你需要挂起一个被取消的协程，
    你可以将相应的代码包装在 withContext(NonCancellable) {……} 中，
    并使用 withContext 函数以及 NonCancellable 上下文，见如下示例所示：
 */
fun test3_5() = runBlocking {
    println("5-------------------------------------------------")
    var job = launch {
        try {
            var i = 0
            while (true) {
                println("Thread time: ${i*0.5}")
                i++
                delay(500)
            }
        } finally {
            withContext(NonCancellable) {
                println("The job is running finally.")
                delay(1000)
                println("Just delay 1 sec")
            }
        }
    }
    delay(1500)
    println("Try to cancel job.")
    job.cancelAndJoin()
    println("The coroutine is quit.")
}

/*
    6. 超时
    在实践中绝大多数取消一个协程的理由是它有可能超时。
    使用 withTimeout 函数, 来追踪一个相关 Job, 在延迟后取消追踪。
 */
fun test3_6() = runBlocking {
    println("6-------------------------------------------------")
    withTimeout(1500) {
        var i = 0
        while (true) {
            println("Thread time: ${i*0.5}")
            i++
            delay(500)
        }
    }
    println("The coroutine is quit.")
}

/*
    6.2
    withTimeout 抛出了 TimeoutCancellationException，它是 CancellationException 的子类。
    我们之前没有在控制台上看到堆栈跟踪信息的打印。这是因为在被取消的协程中 CancellationException 被认为是协程执行结束的正常原因。

    然而，在这个示例中我们在 main 函数中正确地使用了 withTimeout。
    由于取消只是一个例外，所有的资源都使用常用的方法来关闭。 如果你需要做一些各类使用超时的特别的额外操作，
    可以使用类似 withTimeout 的 withTimeoutOrNull 函数，
    并把这些会超时的代码包装在 try {...} catch (e: TimeoutCancellationException) {...} 代码块中，
    而 withTimeoutOrNull 通过返回 null 来进行超时操作，从而替代抛出一个异常：
 */
fun test3_6_2() = runBlocking {
    println("6.2-------------------------------------------------")
    var result = withTimeoutOrNull(1500) {
        var i = 0
        while (true) {
            println("Thread time: ${i*0.5}")
            i++
            delay(500)
        }
        null
    }
    println("The result is $result.")
}

/*
    7. Asynchronous timeout and resources
    The timeout event in withTimeout is asynchronous
    with respect to the code running in its block and may happen at any time,
    even right before the return from inside of the timeout block.

    Keep this in mind
    if you open or acquire some resource inside the block
    that needs closing or release outside of the block.

    For example, here we imitate a closeable resource with the Resource class,
    that simply keeps track of how many times
    it was created by incrementing the acquired counter
    and decrementing this counter from its close function.

    Let us run a lot of coroutines with the small timeout
    try acquire this resource from inside of the withTimeout block after a bit of delay
    and release it from outside.

    以下範例在Mac無法看出範例想表達的成果,
    他想表達的應該是: withTimeout裡資源的運用狀況, 必定反映在try{...}finally{...}
 */
var acquired = 0
class Resource {
    init {
        acquired+=2
    }

    fun close() {
        acquired--
    }
}

fun test3_7() {
    println("7-------------------------------------------------")
    runBlocking {
        repeat(100_000) {
            launch {
                var resource: Resource ?= null
                resource = withTimeout(60) {
                    delay(50)
                    Resource()
                }
                println("resource: $resource")
                resource?.close()
            }
        }
    }
    // Outside of runBlocking all coroutines have completed
    println("The acquired is $acquired.")
}

/*
    7.2
    To workaround this problem
    you can store a reference to the resource in the variable
    as opposed to returning it from the withTimeout block.
 */
fun test3_7_2() {
    println("7.2-------------------------------------------------")
    runBlocking {
        repeat(100_000) {
            launch {
                var resource: Resource ?= null
                try {
                    resource = withTimeout(60) {
//                        delay(50)
                        Resource()
                    }
                } finally {
//                    println("resource: $resource")
                    resource?.close()
                }
            }
        }
    }
    println("The acquired is $acquired.")
}