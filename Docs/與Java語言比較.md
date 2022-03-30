### Kotlin 解决了一些 Java 中的问题
* 空引用由类型系统控制
  * 使用安全调用操作符?
  ```
  println(a?.length)
  ```
  * 无原始类型
    * 在 Kotlin 中，所有直都是object，Java 的装箱原始类型映射到可空的 Kotlin 类型：
  
    | Java type	| Kotlin type
    |----------|-----|
    | java.lang.Byte | kotlin.Byte?
    | java.lang.Short | kotlin.Short?
    | java.lang.Integer | kotlin.Int?
    | java.lang.Long | kotlin.Long?
    | java.lang.Character | kotlin.Char?
    | java.lang.Float | kotlin.Float?
    | java.lang.Double | kotlin.Double?
    | java.lang.Boolean | kotlin.Boolean?

* kotlin的lambda有類型：
  * () -> Unit, Java為一個interface內只包含一個方法
* Kotlin 没有Checked Exception
  * 進行檔案IO操作時，各種各樣潛在的異常可能會發生，因此這些異常必須被捕獲或者拋出
* ...

### Java 有而 Kotlin 没有的东西
* Checked Exception
* 不是类的原生类型
* 静态成员 —— 以 伴生对象、 顶层函数、 扩展函数 或者 @JvmStatic 取代。
* 三目操作符 a ? b : c —— 以 if 表达式取代。
* ...

### Kotlin 有而 Java 没有的东西
* Kotlin 函数都是头等的，这意味着它们可以存储在变量与数据结构中、作为参数传递给其他高阶函数以及从其他高阶函数返回。
  * [Lambda 表达式](../src/main/kotlin/tutorial7FunctionLambda/No2_Lambda.kt)
* Kotlin 能够扩展个类的新功能而无需继承该类或者使用像装饰者这样的设计模式。
  * [扩展](../src/main/kotlin/tutorial6_class_object/No6_Expand.kt)
* 空安全
* 類型轉換
  * 類型檢測"is"
  * “不安全的”类型转换"as"，若非該類型則會拋出異常
  * 智能轉換，在顯示中會自動轉換型態
  ```
    var n = 13
    println(n)
  ```
* 字串可加入，以＄符號開頭，花括号括起来的任意表达式的值
* 類中屬性語法如下，包含初始器（initializer）、getter 和 setter
  * [属性](../src/main/kotlin/tutorial6_class_object/No2_Property.kt)
  ```  
  var <propertyName>[: <PropertyType>] [= <property_initializer>]
      [<getter>]
      [<setter>
  ```
* [主构造函数](../src/main/kotlin/tutorial6_class_object/No1_ClassInheritance.kt)
  * 有一个主构造函数以及一个或多个次构造函数
* [委托](../src/main/kotlin/tutorial6_class_object/No15_Delegation.kt)
  * 可用來實現繼承
* 变量与属性的类型推断
  * 因為java宣告變數時會需要data type, 但kotlin可以依照初始值推斷
* [单例](../src/main/kotlin/tutorial6_class_object/No12_ObjectExpression.kt)
  * 只需要“一个对象而已”，并不需要特殊超类型
  * 创建一个对某个类做了轻微改动的类的对象
* [區間](../src/main/kotlin/tutorial8_set/No4_Range.kt)
* companion object，类内部的对象声明可以用此标记
* [数据类](../src/main/kotlin/tutorial6_class_object/No7_DataClass.kt)
  * 常创建一些只保存数据的类
  * 可使用copy复制一个对象改变它的一些属性，但其余部分保持不变。
* 分別只读与可变集合
  * ex. List 和 MutableList
* [协程](../src/main/kotlin/tutorial9_coroutine)
  * 在非同步操作上新的實現
* ...

### Reference
* 官網https://www.kotlincn.net/docs/reference/comparison-to-java.html
  
