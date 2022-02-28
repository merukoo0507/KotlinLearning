package tutorial8_set

fun main() {
    /*
        Map 相关操作
        在 map 中，键和值的类型都是用户定义的。
        对基于键的访问启用了各种特定于 map 的处理函数，
        从键获取值到对键和值进行单独过滤。
    */
    /*
        1. 取键与值
        要从 Map 中检索值
        - 提供其键作为 get() 函数的参数。
        - 简写 [key] 语法，如果找不到给定的键，则返回 null 。
        - getValue()：如果在 Map 中找不到键，则抛出异常。

        还有两个选项可以解决键缺失的问题：
        - getOrElse() 与 list 的工作方式相同：对于不存在的键，其值由给定的 lambda 表达式返回。
        - getOrDefault() 如果找不到键，则返回指定的默认值。
    */
    println("1-------------------------------------------------")
    val numbersMap = mapOf("one" to 1, "two" to 2, "three" to 3)
    println("numbersMap: $numbersMap")
    println(numbersMap.get("one"))
    println(numbersMap["one"])
    println(numbersMap.getOrDefault("four", 10))
    println(numbersMap["five"]) // null
    println(numbersMap.get("five")) // exception!

    /*
        要对 map 的所有键或所有值执行操作，可以从属性 keys 和 values 中相应地检索它们。
        keys 是 Map 中所有键的集合， values 是 Map 中所有值的集合。
     */
    println("keys: ${numbersMap.keys}")
    println("values: ${numbersMap.values}")

    /*
        2. 过滤
        可以使用 filter() 函数来过滤 map 或其他集合。
        对 map 使用 filter() 函数时， Pair 将作为参数的谓词传递给它。 它将使用谓词同时过滤其中的键和值。
    */
    println("2-------------------------------------------------")
    val filterMap = numbersMap.filter { entry -> entry.key.length > 3 && entry.value > 2 }
    println("filterMap: $filterMap")

    /*
        还有两种用于过滤 map 的特定函数：按键或按值。
        filterKeys() 和 filterValues() 。
        两者都将返回一个新 Map ，其中包含与给定谓词相匹配的条目。
     */
    val filterKeyMap = numbersMap.filterKeys { it.startsWith("t", true) }
    val filterValueMap = numbersMap.filterValues { it < 3 }
    println("filterKeyMap: $filterKeyMap")
    println("filterValueMap: $filterValueMap")

    /*
        3. plus 与 minus 操作
        由于需要访问元素的键，plus（+）与 minus（-）运算符对 map 的作用与其他集合不同。
        plus 返回包含两个操作数元素的 Map ：左侧的 Map 与右侧的 Pair 或另一个 Map 。
        当右侧操作数中有左侧 Map 中已存在的键时，该条目将使用右侧的值。
    */
    println("3-------------------------------------------------")
    println(numbersMap + Pair("four", 4))
    println(numbersMap + Pair("one", 10))
    println(numbersMap + mapOf("one" to 11, "four" to 14))

    /*
        minus 将根据左侧 Map 条目创建一个新 Map ，右侧操作数带有键的条目将被剔除。
        因此，右侧操作数可以是单个键或键的集合： list 、 set 等。
     */
    println(numbersMap - "one")
    println(numbersMap - listOf("one", "two"))

    /*
        4. Map 写操作
        Mutable Map （可变 Map ）提供特定的 Map 写操作。
        这些操作使你可以使用键来访问或更改 Map 值。

        Map 写操作的一些规则：

        值可以更新。 反过来，键也永远不会改变：添加条目后，键是不变的。
        每个键都有一个与之关联的值。也可以添加和删除整个条目。
    */

    /*
        4.1 添加与更新条目
        要将新的键值对添加到可变 Map ，请使用 put() 。
        将新条目放入 LinkedHashMap （Map的默认实现）后，会添加该条目，以便在 Map 迭代时排在最后。
        在 Map 类中，新元素的位置由其键顺序定义。
    */
    println("4.1-------------------------------------------------")
    var numbersMap2 = mutableMapOf("one" to 1, "two" to 2)
    numbersMap2.put("three", 3)
    println(numbersMap2)

    /*
        4.2 要一次添加多个条目，请使用 putAll() 。
        它的参数可以是 Map 或一组 Pair ： Iterable 、 Sequence 或 Array 。
     */
    println("4.2-------------------------------------------------")
    numbersMap2.putAll(mapOf("four" to 4, "five" to 5))
    println(numbersMap2)

    /*
        4.3 如果给定键已存在于 Map 中，则 put() 与 putAll() 都将覆盖值。
        因此，可以使用它们来更新 Map 条目的值。
     */
    println("4.3-------------------------------------------------")
    val previous = numbersMap2.put("one", 111)
    println("value associated with 'one', before: $previous, after: ${numbersMap2["one"]}")
    println("numbersMap2: $numbersMap2")

    /*
        4.4 还可以使用快速操作符将新条目添加到 Map 。 有两种方式：

        plusAssign （+=） 操作符。
        [] 操作符为 set() 的别名。
     */
    println("4.4-------------------------------------------------")
    numbersMap2["six"] = 6     // 调用 numbersMap2.set("six", 6)
    println(numbersMap2)
    numbersMap2.plusAssign(mapOf("three" to 7, "four" to 8))    // 存在的键进行操作时，将覆盖相应条目的值
    println(numbersMap2)

    /*
        4.5 删除条目
        要从可变 Map 中删除条目，请使用 remove() 函数。
        调用 remove() 时，可以传递键或整个键值对。
        如果同时指定键和值，则仅当键值都匹配时，才会删除此的元素。
     */
    println("4.5-------------------------------------------------")
    numbersMap2.remove("one")
    println(numbersMap2)

    /*
        4.6 还可以通过键或值从可变 Map 中删除条目。
        在 Map 的 .keys 或 .values 中调用 remove() 并提供键或值来删除条目。
        在 .values 中调用时， remove() 仅删除给定值匹配到的的第一个条目。
     */
    println("4.6-------------------------------------------------")
    numbersMap2.keys.remove("two")
    println(numbersMap2)
    numbersMap2.values.remove(7)
    println(numbersMap2)

    /*
        4.7 minusAssign （-=） 操作符也可用于可变 Map 。
     */
//    println("4.7-------------------------------------------------")
//    numbersMap2 -= "four"
}