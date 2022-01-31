package tutorial8_set

fun main() {
    /*
        集合转换
        Kotlin 标准库为集合 转换 提供了一组扩展函数。
        这些函数根据提供的转换规则从现有集合中构建新集合。
        在此页面中，我们将概述可用的集合转换函数。
    */

    /*
        1. 映射
        映射 转换从另一个集合的，元素上的函数结果创建一个集合。
        基本的映射函数是 map()。
        它将给定的 lambda 函数应用于每个后续元素，并返回 lambda 结果列表。
        结果的顺序与元素的原始顺序相同。

        如需应用还要用到元素索引作为参数的转换，请使用 mapIndexed()。
    */
    println("1-------------------------------------------------")
    val numbers = setOf(1, 2, 3)
    println(numbers.map{ it })
    println(numbers.mapIndexed { index, i -> "$index - $i" })

    /*
        1.2 如果转换在某些元素上产生 null 值，
        则可以通过调用 mapNotNull() 函数取代 map()
        或 mapIndexedNotNull() 取代 mapIndexed() 来从结果集中过滤掉 null 值。
     */
    println("1.2-------------------------------------------------")
    println(numbers.mapNotNull { if (it == 0) null else it })
    println(numbers.mapIndexedNotNull { index, i ->  if (index == 0) null else i })


    /*
        1.3 转换键，使值保持不变，反之亦然。
        要将指定转换应用于键，请使用 mapKeys()；反过来，mapValues() 转换值。
        这两个函数都使用将映射条目作为参数的转换，因此可以操作其键与值。
     */
    println("1.3-------------------------------------------------")
    val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key11" to 11)
    println(numbersMap.mapKeys { it.key.toUpperCase() })
    println(numbersMap.mapValues { it.value * 10 })
    println()

    /*
        2. 合拢
        合拢 转换是根据两个集合中具有相同位置的元素构建配对。

        2.1 zip()
        扩展函数完成合拢。
        在一个集合（或数组）上以另一个集合（或数组）作为参数调用时，zip() 返回 Pair 对象的列表（List）。
        接收者集合的元素是这些配对中的第一个元素。
        如果集合的大小不同，则 zip() 的结果为较小集合的大小；结果中不包含较大集合的后续元素。
        zip() 也可以中缀形式调用 a zip b 。
    */
    println("2.1-------------------------------------------------")
    val colors = listOf("red", "brown", "grey")
    val animals = listOf("fox", "bear", "wolf")
    println(colors zip animals)
    val twoAnimals = listOf("fox", "bear")
    println(colors.zip(twoAnimals))

    /*
        2.2 zip()
        使用带有两个参数的转换函数来调用：
        接收者元素和参数元素。
        List 包含在具有相同位置的接收者对和参数元素对上调用的，转换函数的返回值。
     */
    println("2.2-------------------------------------------------")
    println(colors.zip(animals) { color, animal -> "The ${animal.capitalize()} is $color."})

    /*
        2.2 当拥有 Pair 的 List 时，可以进行反向转换 unzipping——从这些键值对中构建两个列表：
        第一个列表包含原始列表中每个 Pair 的键。
        第二个列表包含原始列表中每个 Pair 的值。
        要分割键值对列表，请调用 unzip()。
     */
    println("2.3-------------------------------------------------")
    val numberPairs = animals zip colors
    println(numberPairs)
    println(numberPairs.unzip())

    /*
        3. 关联
        关联 转换允许从集合元素和与其关联的某些值构建 Map。
        在不同的关联类型中，元素可以是关联 Map 中的键或值。

        3.1 基本的关联函数
        associateWith()
        创建一个 Map，
        其中原始集合的元素是键，并通过给定的转换函数从中产生值。
        如果两个元素相等，则仅最后一个保留在 Map 中。
    */
    println("3.1-------------------------------------------------")
    val numbers2 = listOf("one", "two", "three", "four")
    println(numbers2.associateWith { it.length })

    /*
        3.2 为了使用集合元素作为值来构建 Map，
        associateBy()
        它需要一个函数，该函数根据元素的值返回键。
        如果两个元素相等，则仅最后一个保留在 Map 中。
        还可以使用值转换函数来调用 associateBy()。
     */
    println("3.2-------------------------------------------------")
    println(numbers2.associateBy { it.first().toUpperCase() })
    println(numbers2.associateBy({ it.first().toUpperCase() }, { it.length }))

    /*
        3.3 另一种构建 Map 的方法
        associate()
        通过集合元素生成，
        它需要一个 lambda 函数，该函数返回 Pair：键和相应 Map 条目的值。

        请注意，associate() 会生成临时的 Pair 对象，这可能会影响性能。
        因此，当性能不是很关键或比其他选项更可取时，应使用 associate()。
        后者的一个示例：从一个元素一起生成键和相应的值。
     */
    println("3.3-------------------------------------------------")
    val names = listOf("Alice Adams", "Brian Brown", "Clara Campbell")
    fun parseFullName(name: String): FullName = FullName(name.split(' ')[0], name.split(' ')[1])
    //官網範例
    println(names.associate { name -> parseFullName(name).let { it.lastName to it.firstName } })
    println(names.associate { it.split(' ')[0] to it.split(' ')[1] })

    /*
        4. 打平
        如需操作嵌套的集合，對嵌套集合元素进行打平访问，的标准库函数很有用。

        4.1 flatten()。
        可以在一群集合的集合（例如，一群 Set 组成的 List）上调用它。
        该函数返回，嵌套集合中的所有元素的List。
    */
    println("4.1-------------------------------------------------")
    val numberSets = listOf(setOf(1, 2, 3), setOf(4, 5, 6), setOf(1, 2))
    println(numberSets.flatten())

    /*
        4.2 flatMap()
        提供了一种灵活的方式来处理嵌套的集合。
        它需要一个函数将一个集合元素映射到另一个集合。
        因此，flatMap() 返回单个列表其中包含所有元素的值。
        所以，flatMap() 表现为 map()（以集合作为映射结果）与 flatten() 的连续调用。
     */
    println(numberSets.flatMap { println("flatMap: $it"); it })
    //官網範例
    val containers = listOf(StringContainer(listOf(1, 2, 3)), StringContainer(listOf(4, 5, 6)), StringContainer(listOf(1, 2)))
    println(containers.flatMap{ it.values })

    /*
        5. 字符串表示
        joinToString() 与 joinTo()
        如果需要以可读格式检索集合内容，请使用将集合转换为字符串的函数。
        joinToString() 根据提供的参数从集合元素构建单个 String。
        joinTo() 执行相同的操作，但将结果附加到给定的 Appendable 对象。

        当使用默认参数调用时，函数返回的结果类似于在集合上调用 toString()：
        各元素的字符串表示形式以空格分隔而成的 String。
    */
    println("5-------------------------------------------------")
    println(numbers2)
    println(numbers2.joinToString())

    val listString = StringBuffer("The list of numbers: ")
    numbers.joinTo(listString)
    println("listString: $listString")

    /*
        5.2 joinToString
        要构建自定义字符串表示形式，
        结果字符串将以 prefix 开头，以 postfix 结尾。
        除最后一个元素外，separator 将位于每个元素之后。
     */
    println("5.2-------------------------------------------------")
    println(numbers2.joinToString(separator = "|", prefix = "start: ", postfix = ": end"))

    /*
        5.3 joinToString
        可能需要指定 limit，表示结果中元素的数量。
        集合大小超出 limit，其他元素将被 truncated 参数的单个值替换。
     */
    println("5.3-------------------------------------------------")
    println(numbers2.joinToString(limit = 10, truncated = "<...>"))

    /*
        5.4 joinToString
        要自定义元素本身的表示形式
     */
    println("5.4-------------------------------------------------")
    println(numbers2.joinToString { "Element: ${it.toUpperCase()}" })
}

data class FullName(val firstName: String, val lastName: String)
class StringContainer(val list: List<Int>) {
    val values = list
}