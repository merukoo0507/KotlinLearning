package tutorial9_coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() {
    /*
        Shared mutable state and concurrency
        Coroutines can be executed parallelly using a multi-threaded dispatcher like the Dispatchers.Default.
        It presents all the usual parallelism problems.
        The main problem being synchronization of access to shared mutable state.
        Some solutions to this problem in the land of coroutines are similar to the solutions in the multi-threaded world,
        but others are unique.
     */
//    test9_1()
//    test9_2()
//    test9_3()
//    test9_4()
    test9_5()
//    test9_6()
    test9_7()
}

/*
    1.The problem
    Let us launch a hundred coroutines all doing the same action a thousand times.
    We'll also measure their completion time for further comparisons (massiveRun).

    We start with a very simple action
    that increments a shared mutable variable using multi-threaded Dispatchers.Default.
 */
fun test9_1() = runBlocking {
    println("1-------------------------------------------------")
    withContext(Dispatchers.Default) {
        massiveRun {
            counter++
        }
    }
    println("Counter = $counter")
    /*
        What does it print at the end?
        It is highly unlikely to ever print "Counter = 100000",
        because a hundred coroutines increment the counter concurrently from multiple threads without any synchronization.
     */
}
var counter = 0
suspend fun massiveRun(action: suspend () -> Unit) {
    val n = 100  // number of coroutines to launch
    val m = 1000 // times an action is repeated by each coroutine
    val time = measureTimeMillis {
        runBlocking {
            repeat(n) {
                launch {
                    repeat(m) {
                        action()
                    }
                }
            }
        }
    }
    println("Completed in ${n * m} actions in $time ms.")
}

/*
    2. Volatiles are of no help
    There is a common misconception that making a variable volatile solves concurrency problem.
    Let us try it:
 */
fun test9_2() = runBlocking {
    println("2-------------------------------------------------")
    withContext(Dispatchers.Default) {
        massiveRun {
            counter2++
        }
    }
    println("Counter = $counter2")
    /*
        This code works slower,
        but we still don't get "Counter = 100000" at the end,
        because volatile variables guarantee linearizable (this is a technical term for "atomic")
        reads and writes to the corresponding variable,
        but do not provide atomicity of larger actions (increment in our case).
     */
}
@Volatile
var counter2 = 0

/*
    3. Thread-safe data structures
    The general solution that works both for threads and for coroutines is to use a thread-safe
    (aka synchronized, linearizable, or atomic) data structure
    that provides all the necessary synchronization for the corresponding operations
    that needs to be performed on a shared state.

    In the case of a simple counter we can use AtomicInteger class which has atomic incrementAndGet operations:
 */
fun test9_3() = runBlocking {
    println("3-------------------------------------------------")
    withContext(Dispatchers.Default) {
        massiveRun {
            counter3.incrementAndGet()
        }
    }
    println("Counter = $counter3")
    /*
        This is the fastest solution for this particular problem.
        It works for plain counters, collections, queues and other standard data structures
        and "basic operations" on them.

        However, it does not easily scale to complex state
        or to complex operations
        that do not have ready-to-use thread-safe implementations.
     */
}
var counter3 = AtomicInteger(0)

/*
    4. Thread confinement fine-grained
    Thread confinement is an approach to the problem of shared mutable state
    where all access to the particular shared state is confined to a single thread.

    It is typically used in UI applications,
    where all UI state is confined to the single event-dispatch/application thread.
    It is easy to apply with coroutines by using a single-threaded context.
 */
fun test9_4() = runBlocking {
    println("4-------------------------------------------------")
    withContext(Dispatchers.Default) {
        massiveRun {
            withContext(counterContext) {
                counter++
            }
        }
    }
    println("counter = $counter")
}
val counterContext = newSingleThreadContext("CounterContext")

/*
    5. Thread confinement coarse-grained
    In practice, thread confinement is performed in large chunks,
    e.g. big pieces of state-updating business logic are confined to the single thread.
    The following example does it like that, running each coroutine in the single-threaded context to start with.
 */
fun test9_5() = runBlocking {
    println("5-------------------------------------------------")
    withContext(counterContext) {
        massiveRun {
            counter++
        }
    }
    println("counter = $counter")
    // This now works much faster and produces correct result.
}

/*
    6. Mutual exclusion
    Mutual exclusion solution to the problem is
    to protect all modifications of the shared state with a critical section
    that is never executed concurrently.
    In a blocking world you'd typically use synchronized or ReentrantLock for that.

    Coroutine's alternative is called Mutex.
    It has lock and unlock functions to delimit a critical section.

    The key difference is that Mutex.lock() is a suspending function. It does not block a thread.

    There is also withLock extension function that conveniently represents mutex.lock();
    try { ... } finally { mutex.unlock() } pattern:
 */
fun test9_6() = runBlocking {
    println("6-------------------------------------------------")
    withContext(Dispatchers.Default) {
        massiveRun {
            mutex.withLock {
                counter++
            }
        }
    }
    println("Counter = $counter")
    /*
        The locking in this example is fine-grained, so it pays the price.
        However, it is a good choice for some situations
        where you absolutely must modify some shared state periodically,
        but there is no natural thread that this state is confined to.
     */
}
val mutex = Mutex()

/*
    7. Actors
    An actor is an entity made up of a combination of a coroutine,
    the state that is confined and encapsulated into this coroutine,
    and a channel to communicate with other coroutines.
    A simple actor can be written as a function,
    but an actor with a complex state is better suited for a class.

    There is an actor coroutine builder
    that conveniently combines actor's mailbox channel into its scope to receive messages from
    and combines the send channel into the resulting job object,
    so that a single reference to the actor can be carried around as its handle.

    The first step of using an actor is to define a class of messages
    that an actor is going to process.
    Kotlin's sealed classes are well suited for that purpose.
    We define CounterMsg sealed class with IncCounter message to increment a counter
    and GetCounter message to get its value.
    The latter needs to send a response.
    A CompletableDeferred communication primitive,
    that represents a single value that will be known (communicated) in the future,
    is used here for that purpose.
 */
fun test9_7() = runBlocking {
    println("7-------------------------------------------------")
    val counter = CounterActor()
    withContext(Dispatchers.Default) {
        massiveRun {
            counter.send(IncActor)
        }
    }
    val response = CompletableDeferred<Int>()
    counter.send(GetCounter(response))
    println("Counter = ${response.await()}")
    counter.close() // shutdown the actor
    /*
        It does not matter (for correctness) what context the actor itself is executed in.
        An actor is a coroutine and a coroutine is executed sequentially,
        so confinement of the state to the specific coroutine
        works as a solution to the problem of shared mutable state.

        Indeed, actors may modify their own private state,
        but can only affect each other through messages (avoiding the need for any locks).

        Actor is more efficient than locking under load,
        because in this case it always has work to do
        and it does not have to switch to a different context at all.
     */
}

// Message types for counterActor
sealed class CounterMsg
object IncActor : CounterMsg() // one-way message to increment counter
class GetCounter(val response: CompletableDeferred<Int>): CounterMsg() // a request with reply

// Then we define a function that launches an actor using an actor coroutine builder:
fun CoroutineScope.CounterActor() = actor<CounterMsg> {
    var counter = 0 // actor state
    for (msg in channel) { // iterate over incoming messages
        when(msg) {
            is IncActor -> counter++
            is GetCounter -> msg.response.complete(counter)
        }
    }
}