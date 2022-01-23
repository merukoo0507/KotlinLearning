package tutorial7FunctionLambda

import java.util.concurrent.locks.Lock

fun main() {
    println("1-------------------------------------------------")
//    f {
//        println("Lambda in object function.")
//        listOf(1).forEach {
//            if (it == 1) return@f
//        }
//        println("After return ")
//    }
//
//    println("6.1-------------------------------------------------")
//    val treeNode = TreeNode(TreeNode(MyClass()))
//    // 在这里我们向上遍历一棵树并且检测每个节点是不是特定的类型。
//    // 这都没有问题，但是调用处不是很优雅：
//    treeNode.findParentType(MyClass::class.java)
//    // 我们真正想要的只是传一个类型给该函数，即像这样调用它：
//    treeNode.findParentType2<MyClass>()
//
//    println("6.2-------------------------------------------------")
//    println("treeNode.findParentType2<MyClass>() != null -> ${treeNode.findParentType2<MyClass>()!= null}")
//    println(memberOf<TreeNode>())
}

/*
    1. 内联函数
    使用高阶函数会带来一些运行时的效率损失：
    每一个函数都是一个对象，并且会捕获一个闭包。
    即那些在函数体内会访问到的变量。
    内存分配（对于函数对象和类）和虚拟调用会引入运行时间开销。

    编译器没有为参数创建一个函数对象并生成一个调用。取而代之，编译器可以生成以下代码：
    l.lock()
    try {
        foo()
    }
    finally {
        l.unlock()
    }
 */

inline fun <T> lock(l: Lock, foo2: () -> T): T {
    l.lock()
    val res = foo2()
    l.unlock()
    return res
}
/*
    inline 修饰符影响函数本身和传给它的 lambda 表达式：所有这些都将内联到调用处。

    内联可能导致生成的代码增加；
    不过如果我们使用得当（即避免内联过大函数），性能上会有所提升，
    尤其是在循环中的“超多态（megamorphic）”调用处。
 */

/*
    2. 禁用内联
    如果希望只内联一部分传给内联函数的 lambda 表达式参数，
    那么可以用 noinline 修饰符标记不希望内联的函数参数
 */
inline fun foo(inlined: () -> Unit, noinline noinlined: () -> Unit) { }

/*
    可以"内联的 lambda 表达式"只能在内联函数内部调用或者作为可内联的参数传递，
    但是 noinline 的可以以任何我们喜欢的方式操作：存储在字段中、传送它等等。

    需要注意的是，如果一个内联函数没有可内联的函数参数并且没有具体化的类型参数，
    编译器会产生一个警告，因为内联这样的函数很可能并无益处
    （如果你确认需要内联，则可以用 @Suppress("NOTHING_TO_INLINE") 注解关掉该警告）。
 */

/*
    4. 非局部返回
    在 Kotlin 中，我们只能对具名或匿名函数使用正常的、非限定的 return 来退出。
    这意味着要退出一个 lambda 表达式，我们必须使用一个标签，
    并且在 lambda 表达式内部禁止使用裸 return，
    因为 lambda 表达式不能使包含它的函数返回：
 */
fun foo(f: () -> Unit) { }

inline fun inlined(f: () -> Unit) { }

fun foo1() {
    foo {
        // 4.1 错误：不能使 `foo` 在此处返回
        // return
    }

    // 4.2 但是如果 lambda 表达式传给的函数是内联的，该 return 也可以内联，所以它是允许的：
    inlined {
        return
    }
}

// 4.3 这种返回（位于 lambda 表达式中，但退出包含它的函数）称为非局部返回。
// 我们习惯了在循环中用这种结构，其内联函数通常包含：
fun hasZeros(ints: List<Int>): Boolean {
    ints.forEach {
        if (it == 0)
            return true    // 从 hasZeros 返回
    }
    return false
}

/*
    5. 请注意，一些内联函数可能调用传给它们的不是直接来自函数体、
    而是来自另一个执行上下文的 lambda 表达式参数，
    例如来自"局部对象或嵌套函数"。

    在这种情况下，该 lambda 表达式中也不允许非局部控制流。
    为了标识这种情况，该 lambda 表达式参数需要用 crossinline 修饰符标记:
 */
inline fun f(crossinline body: () -> Unit) {
    val f = Runnable {
        println("Runnable starts.")
        /*
            如果是一般function body():
            Can't inline 'body' here: it may contain non-local returns.
            Add 'crossinline' modifier to parameter declaration 'body'
         */
        body()
        println("Runnable ends.")
    }
    f.run()
}

data class TreeNode(val parent: Any)

/*
    6. reified 具体化的类型参数

    6.1 有时候我们需要访问一个作为参数传给我们的一个类型：
 */
fun <T> TreeNode.findParentType(clazz: Class<T>): T? {
    var p = parent
    while (p != null && !clazz.isInstance(p)) {
        if (p is TreeNode)
            p = p.parent
    }
    return p as T?
}

/*
    6.2 为能够这么做，内联函数支持具体化的类型参数，于是我们可以这样写：

    我们使用 reified 修饰符来限定类型参数，现在可以在函数内部访问它了，
    几乎就像是一个普通的类一样。由于函数是内联的，不需要反射，
    正常的操作符如 !is 和 as 现在都能用了。
    此外，我们还可以按照上面提到的方式调用它：myTree.findParentOfType<MyTreeNodeType>()。
 */
inline fun <reified T> TreeNode.findParentType2(): T? {
    var p = parent
    while (p != null && p !is T) {
        if (p is TreeNode)
            p = p.parent
    }
    return p as T?
}

// 虽然在许多情况下可能不需要反射，但我们仍然可以对一个具体化的类型参数使用它：
inline fun <reified T> memberOf() = T::class.java

/*
    1. 普通的函数（未标记为内联函数的）不能有具体化参数。
    2. 不具有运行时表示的类型（例如非具体化的类型参数或者类似于Nothing的虚构类型） 不能用作具体化的类型参数的实参。
    Reference: 规范文档
    https://github.com/JetBrains/kotlin/blob/master/spec-docs/reified-type-parameters.md
*/


/*
    7. inline 修饰符可用于没有幕后字段的属性的访问器。 你可以标注独立的属性访问器：
    (不清楚...)
 */
class MyClass3 {
    val status: String
        inline get() = "accessTimes: $accessTimes"

    var _accessTimes = 0
    var accessTimes: Int
        get() = this._accessTimes
        inline set(value) {
            //Cannot access field here since extension property cannot have backing field
            //Do something with `obj
        }

    var _accessUser = ""
    // 你也可以标注整个属性，将它的两个访问器都标记为内联：
    inline var accessUser: String
        get() = this._accessUser
        set(value) {
            //...
        }
}

// 公有 API 内联函数的限制
// 略...
