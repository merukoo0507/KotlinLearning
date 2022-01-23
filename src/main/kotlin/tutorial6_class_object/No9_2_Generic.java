package tutorial6_class_object;

import java.util.ArrayList;
import java.util.List;

// Reference:
// http://www.angelikalanger.com/GenericsFAQ/JavaGenericsFAQ.html
// https://ethan-imagination.blogspot.com/2018/11/javase-gettingstarted-017.html

public class No9_2_Generic {
    public static void main(String[] args) {
        // 1. Java 中的泛型是不型变的:
        // 意味着 List<String> 并不是 List<Object> 的子类型
        List<String> strs = new ArrayList<>();

        // ！！！此处的编译器错误让我们避免了之后的运行时异常
        // List<Object> objs = strs;

        List<Object> objs = new ArrayList<>();
        objs.add(1);
        // ！！！ ClassCastException：无法将整数转换为字符串
        // String s = objs.get(0);

        objs.addAll(strs);
    }

    // 直觉上，我们会这样：
//    interface Collection<E> {
//        void addAll(Collection<E> items);
//    }

    // 实际是以下这样：
    interface Collection<E> {
        // 型態通配字元“？”使用時機，在於宣告變數的時候⬇️
        void addAll(Collection<? extends E> items);
    }

    void copyAll(Collection<Object> to, Collection<String> from) {
        to.addAll(from);
    }

    // 2. 声明处型变
    // 该接口中不存在任何以 T 作为参数的方法，只是方法返回 T 类型值：
    interface Source<T> {
        T nextT();
    }
    //    void demo(Source<String> strs) {
    //        Source<Object> objs = strs; // ！！！在 Java 中不允许
    //    }

    /*
        PECS 代表生产者-Extends、消费者-Super（Producer-Extends, Consumer-Super）。
        注意：如果你使用一个生产者对象，如 List<? extends Foo>，
        在该对象上不允许调用 add() 或 set()。
        但这并不意味着该对象是不可变的：
        例如，没有什么阻止你调用 clear()从列表中删除所有元素，因为 clear() 根本无需任何参数。
        通配符（或其他类型的型变）保证的唯一的事情是类型安全。不可变性完全是另一回事。
    */

    // 必须声明对象的类型为 Source<? extends Object>，
    // 这是毫无意义的，因为我们可以像以前一样在该对象上调用所有相同的方法，所以更复杂的类型并没有带来价值。
    void demo(Source<String> str) {
        Source<? extends Object> obj = str;
    }

    interface Comparable<T> {
        void compareTo(T n);
    }

    void demo(Comparable<Number> n) {
        n.compareTo(1.0);
    }
}
