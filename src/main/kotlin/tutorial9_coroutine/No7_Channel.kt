package tutorial9_coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*

fun main() {
    /*
        通道
        延期的值提供了一种便捷的方法使单个值在多个协程之间进行相互传输。 通道提供了一种在流中传输值的方法。
     */
//    test7_1()
//    test7_2()
//    test7_3()
//    test7_4()
//    test7_5()
//    test7_6()
//    test7_7()
//    test7_8()
//    test7_9()
    test7_10()
}

fun test7_1() = runBlocking {
    /*
        1. 通道基础
        一个 Channel 是一个和 BlockingQueue 非常相似的概念。
        其中一个不同是它代替了阻塞的 put 操作并提供了挂起的 send，
        还替代了阻塞的 take 操作并提供了挂起的 receive。
     */
    println("1-------------------------------------------------")
    var channel = Channel<Int>()
    launch {
        for (i in 1..3) {
            channel.send(i)
        }
    }

    repeat(3) { //如果大於3程式不會終止
        println(channel.receive())
    }
    println("Done.")
}

fun test7_2() = runBlocking {
    /*
        2.关闭与迭代通道
        和队列不同，一个通道可以通过被关闭来表明没有更多的元素将会进入通道。
        在接收者中可以定期的使用 for 循环来从通道中接收元素。

        从概念上来说，一个 close 操作就像向通道发送了一个特殊的关闭指令。
        这个迭代停止就说明关闭指令已经被接收了。
        所以这里保证所有先前发送出去的元素都在通道关闭前被接收到。
     */
    println("2-------------------------------------------------")
    var channel = Channel<Int>()
    launch {
        repeat(5) {
            channel.send(it)
        }
        channel.close()
    }

    for (i in channel) {
        println(i)
    }
}

fun test7_3() = runBlocking {
    /*
        3. 构建通道生产者
        协程生成一系列元素的模式很常见。
        这是 生产者——消费者 模式的一部分，并且经常能在并发的代码中看到它。
        你可以将生产者抽象成一个函数，并且使通道作为它的参数，但这与必须从函数中返回结果的常识相违悖。

        这里有一个名为 produce 的便捷的协程构建器，可以很容易的在生产者端正确工作，
        并且我们使用扩展函数 consumeEach 在消费者端替代 for 循环：
     */
    println("3-------------------------------------------------")
    val square = produceNumbers()
    square.consumeEach {
        println(it)
    }
}

fun CoroutineScope.produceNumbers(): ReceiveChannel<Int> = produce {
    for (i in 1..5) {
        send(i)
    }
}

/*
    4. 管道
    管道是一种一个协程在流中开始生产可能无穷多个元素的模式：
 */
fun test7_4() = runBlocking {
    println("4-------------------------------------------------")
    var produceSquare = produceNumbers2()
    var consumerSquare = consumeSquare(produceSquare)
    repeat(5) {
        println(consumerSquare.receive())
    }
    println("Done.")
    coroutineContext.cancelChildren()
    /*
        所有创建了协程的函数被定义在了 CoroutineScope 的扩展上，
        所以我们可以依靠结构化并发来确保没有常驻在我们的应用程序中的全局协程。
     */
}

fun CoroutineScope.produceNumbers2(): ReceiveChannel<Int> = produce {
    var i = 1
    while(true) {
        send(i++)    // 在流中开始从 1 生产无穷多个整数
    }
}

// 另一个或多个协程开始消费这些流
fun CoroutineScope.consumeSquare(channel: ReceiveChannel<Int>) = produce {
    for (i in channel) {
        send(i * i)
    }
}

/*
    5. 使用管道的質數
    现在我们开启了一个从 2 开始的数字流管道，从当前的通道中取一个素数， 并为每一个我们发现的素数启动一个流水线阶段：

    numbersFrom(2) -> filter(2) -> filter(3) -> filter(5) -> filter(7) ……
 */
fun test7_5() = runBlocking {
    println("5-------------------------------------------------")
    var cur = numbersFrom(2)
    repeat(5) {
        val prime = cur.receive()
        println(prime)
        cur = filter(cur, prime)
    }
    coroutineContext.cancelChildren()
    /*
        注意，你可以在标准库中使用 iterator 协程构建器来构建一个相似的管道。
        使用 iterator 替换 produce、yield 替换 send、next 替换 receive、 Iterator 替换 ReceiveChannel 来摆脱协程作用域，
        你将不再需要 runBlocking。
        然而，如上所示，如果你在 Dispatchers.Default 上下文中运行它，使用通道的管道的好处在于它可以充分利用多核心 CPU。

        不过，这是一种非常不切实际的寻找質數的方法。
        在实践中，管道调用了另外的一些挂起中的调用（就像异步调用远程服务）并且这些管道不能内置使用 sequence/iterator，
        因为它们不被允许随意的挂起，不像 produce 是完全异步的。
     */
}

fun CoroutineScope.numbersFrom(from: Int) = produce {
    var x = from
    while(true) {
        send(x++)
    }
}


fun CoroutineScope.filter(channel: ReceiveChannel<Int>, prime: Int) = produce {
    for (i in channel){
        println("i: $i, prime: $prime")
        if (i % prime != 0) {
            send(i)
        }
    }
}

/*
    6. 扇出
    多个协程也许会接收相同的管道，在它们之间进行分布式工作。
 */
fun test7_6() = runBlocking {
    println("6-------------------------------------------------")
    var producer = produceNumbers3()
    // 得到几个处理器协程。在这个示例中，它们只是打印它们的 id 和接收到的数字：
    repeat(5) {
        launchProcessor(it, producer)
    }
    // 注意，取消生产者协程将关闭它的通道，从而最终终止处理器协程正在执行的此通道上的迭代。
    delay(950)
    producer.cancel()
    /*
        注意我们如何使用 for 循环显式迭代通道以在 launchProcessor 代码中执行扇出。
        与 consumeEach 不同，这个 for 循环是安全完美地使用多个协程的。
        如果其中一个处理器协程执行失败，其它的处理器协程仍然会继续处理通道，
        而通过 consumeEach 编写的处理器始终在正常或非正常完成时消耗（取消）底层通道。
     */
}

fun CoroutineScope.produceNumbers3() = produce<Int> {
    var i = 1
    while (true) {
        send(i++)
        delay(100)
    }
}

fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
    for(msg in channel) {
        println("{Processor#$id received $msg")
    }
}

/*
    7. 扇入
    多个协程可以发送到同一个通道。

    比如说，让我们创建一个字符串的通道，
    和一个在这个通道中以指定的延迟反复发送一个指定字符串的挂起函数：
 */
fun test7_7() = runBlocking {
    println("7-------------------------------------------------")
    var stringChannel = Channel<String>()
    launch { sendString(stringChannel, "foo", 200) }
    launch { sendString(stringChannel, "Bar", 500) }
    repeat(6) {
        println(stringChannel.receive())
    }
    coroutineContext.cancelChildren()
}

suspend fun CoroutineScope.sendString(channel: SendChannel<String>, msg: String, time: Long) {
    while (true) {
        delay(time)
        channel.send(msg)
    }
}

/*
    8. 带缓冲的通道
    到目前为止展示的通道都是没有缓冲区的。

    无缓冲的通道在发送者和接收者相遇时传输元素（也称“对接”）。
    如果发送先被调用，则它将被挂起直到接收被调用， 如果接收先被调用，它将被挂起直到发送被调用。

    Channel() 工厂函数与 produce 建造器通过一个可选的参数 capacity 来指定 缓冲区大小 。
    缓冲允许发送者在被挂起前发送多个元素， 就像 BlockingQueue 有指定的容量一样，当缓冲区被占满的时候将会引起阻塞。
 */
fun test7_8() = runBlocking {
    println("8-------------------------------------------------")
    val channel = Channel<Int>(4)
    val sender = launch {
        repeat(10) {
            println("Send $it")
            channel.send(it)
        }
    }
    /*
        没有接收到东西……只是等待……

        使用缓冲通道并给 capacity 参数传入 四,
        它将打印“sending” 五 次,
        前四个元素被加入到了缓冲区并且发送者在试图发送第五个元素的时候被挂起。
     */
    delay(1000L)
    sender.cancel()
}

/*
    9. 通道是公平的
    发送和接收操作是 公平的 并且尊重调用它们的多个协程。
    它们遵守先进先出原则，可以看到第一个协程调用 receive 并得到了元素。
    在下面的例子中两个协程“乒”和“乓”都从共享的“桌子”通道接收到这个“球”元素。
 */
fun test7_9() = runBlocking {
    println("9-------------------------------------------------")
    val table = Channel<Ball>()
    launch { player("Ping", table) }
    launch { player("Pong", table) }
    table.send(Ball(0))
    delay(1000)
    coroutineContext.cancelChildren()
}

data class Ball(var hits: Int)

suspend fun player(name: String, table: Channel<Ball>) {
    for (ball in table) {
        ball.hits ++
        println("$name $ball")
        delay(300L)
        table.send(ball)
    }
}

/*
    10. 计时器通道
    计时器通道是一种特别的会合通道，每次经过特定的延迟都会从该通道进行消费并产生 Unit。
    虽然它看起来似乎没用，
    它被用来构建分段来创建复杂的基于时间的 produce 管道和进行窗口化操作以及其它时间相关的处理。
    可以在 select 中使用计时器通道来进行“打勾”操作。

    使用工厂方法 ticker 来创建这些通道。 为了表明不需要其它元素，请使用 ReceiveChannel.cancel 方法。
 */
fun test7_10() = runBlocking {
    println("10-------------------------------------------------")
    val tickerChannel = ticker(delayMillis = 100, initialDelayMillis = 0) //创建计时器通道
    var nextElement = withTimeoutOrNull(1) {
        tickerChannel.receive()
    }
    println("Initial elemnet is available immediately: $nextElement")   // no initial delay
    nextElement = withTimeoutOrNull(50) {
        tickerChannel.receive()
    }
    println("Next element is not ready in 50 ms: $nextElement")  // all subsequent elements have 100ms delay

    nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
    println("Next element is ready in 100 ms: $nextElement")

    // 模拟大量消费延迟
    delay(150)
    // 下一个元素立即可用
    nextElement = withTimeoutOrNull(1) {
        tickerChannel.receive()
    }
    println("Next element is available immediately after large consumer delay: $nextElement")
    // 请注意，`receive` 调用之间的暂停被考虑在内，下一个元素的到达速度更快
    nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
    println("Next element is ready in 50ms after consumer pause in 150ms: $nextElement")

    tickerChannel.cancel() // 表明不再需要更多的元素
}