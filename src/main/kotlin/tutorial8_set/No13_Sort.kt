package tutorial8_set

fun main() {
    println("1-------------------------------------------------")
    println(Version(1, 2) > Version(1, 3))
    println(Version(2, 0) > Version(1, 5))

    /*
        1.2 Comparator 包含 compare() 函数：
        自定义 顺序让你可以按自己喜欢的方式对任何类型的实例进行排序。
        特别是，你可以为不可比较类型定义顺序，或者为可比较类型定义非自然顺序。

        如需为类型定义自定义顺序，可以为其创建一个 Comparator。
        它接受一个类的两个实例并返回它们之间比较的整数结果。
        如上所述，对结果的解释与 compareTo() 的结果相同。
    */
    println("1.2-------------------------------------------------")
    val lengthComparator = Comparator { s1: String, s2: String -> s1.length - s2.length }
    println(listOf("aa", "bb", "c").sortedWith(lengthComparator))

    /*
        1.3 定义一个 Comparator 的一种比较简短的方式是标准库中的 compareBy() 函数。
        compareBy() 接受一个 lambda 表达式，该表达式从一个实例产生一个 Comparable 值，
        并将自定义顺序定义为生成值的自然顺序。
        使用 compareBy()，上面示例中的长度比较器如下所示：
    */
    println("1.3-------------------------------------------------")
    println(listOf("aa", "bb", "c").sortedWith(compareBy { it.length }))

    /*
        Kotlin 集合包提供了用于按照自然顺序、自定义顺序甚至随机顺序对集合排序的函数。
        在此页面上，我们将介绍适用于只读集合的排序函数。
        这些函数将它们的结果作为一个新集合返回，集合里包含了按照请求顺序排序的来自原始集合的元素。
        如果想学习就地对可变集合排序的函数，请参见 List 相关操作。
     */

    /*
        2. 自然顺序
        基本的函数 sorted() 和 sortedDescending() 返回集合的元素，这些元素按照其自然顺序升序和降序排序。
        这些函数适用于 Comparable 元素的集合。
    */
    println("2-------------------------------------------------")
    val numbers = listOf("one", "two", "three", "four")
    println("Sorted ascending: ${numbers.sorted()}")
    println("Sorted decending: ${numbers.sortedDescending()}")

    /*
        3. 自定义顺序
        为了按照自定义顺序排序或者对不可比较对象排序，可以使用函数 sortedBy() 和 sortedByDescending()。
        它们接受一个将集合元素映射为 Comparable 值的选择器函数，并以该值的自然顺序对集合排序。
    */
    println("3-------------------------------------------------")
    val sortedNumbers = numbers.sortedBy { it.length }
    println("Sorted by length ascending: $sortedNumbers")
    val sortedByLast = numbers.sortedByDescending { it.last() }
    println("Sorted by the last letter descending: $sortedByLast")

    /*
        3.2 如需为集合排序定义自定义顺序，可以提供自己的 Comparator。
        为此，调用传入 Comparator 的 sortedWith() 函数。 使用此函数，按照字符串长度排序如下所示：
    */
    println("3.2-------------------------------------------------")
    println("Sorted by length ascending: ${numbers.sortedWith(compareBy { it.length })}")


    /*
        4. 倒序
        你可以使用 reversed() 函数以相反的顺序检索集合。
    */
    println("4-------------------------------------------------")
    println("reversed: ${numbers.reversed()}")

    /*
        4.2 reversed() 返回带有元素副本的新集合。
        因此，如果你之后改变了原始集合，这并不会影响先前获得的 reversed() 的结果。

        另一个反向函数——asReversed()——返回相同集合实例的一个反向视图，因此，如果原始列表不会发生变化，那么它会比 reversed() 更轻量，更合适。
    */
    println("4.2-------------------------------------------------")
    val reverseNumbers = numbers.asReversed()
    println("reverseNumbers: $reverseNumbers")

    /*
        4.3 如果原始列表是可变的，那么其所有更改都会反映在其反向视图中，反之亦然。
        但是，如果列表的可变性未知或者源根本不是一个列表，那么 reversed() 更合适，因为其结果是一个未来不会更改的副本。
    */
    println("4.3-------------------------------------------------")
    val numbers2 = mutableListOf("one", "two", "three", "four")
    val reversedNumbers = numbers2.asReversed()
    println("reverseNumbers: $reverseNumbers")
    numbers2.add("five")
    println(reversedNumbers)


    /*
        5. 随机顺序
        shuffled() 函数返回一个包含了以随机顺序排序的集合元素的新的 List。
        你可以不带参数或者使用 Random 对象来调用它
    */
    println("5-------------------------------------------------")
    println("shuffled: ${(1..10).toList().shuffled()}")
}

/*
    1. 集合排序
    元素的顺序是某些集合类型的一个重要方面。
    例如，如果拥有相同元素的两个列表的元素顺序不同，那么这两个列表也不相等。

    在 Kotlin 中，可以通过多种方式定义对象的顺序。
    首先，有 自然 顺序。
    它是为 Comparable 接口的继承者定义的。
    当没有指定其他顺序时，使用自然顺序为它们排序。

    大多数内置类型是可比较的：
    数值类型使用传统的数值顺序：1 大于 0； -3.4f 大于 -5f，以此类推。
    Char 和 String 使用字典顺序： b 大于 a； world 大于 hello。
    如需为用户定义的类型定义一个自然顺序，可以让这个类型继承 Comparable。 这需要实现 compareTo() 函数。 compareTo() 必须将另一个具有相同类型的对象作为参数并返回一个整数值来显示哪个对象更大：

    正值表明接收者对象更大。
    负值表明它小于参数。
    0 说明对象相等。
    下面是一个类，可用于排序由主版本号和次版本号两部分组成的版本。
*/
class Version(val major: Int, val minor: Int): Comparable<Version> {
    override fun compareTo(other: Version): Int {
        if (this.major != other.major) {
            return this.major - other.major
        }
        return this.minor - other.minor
    }
}