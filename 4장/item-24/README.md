# 멤버 클래스는 되도록 static으로 만들라
> 필요 없는 결합은 자제해라

> [중첩 Class](#중첩-Class)

## 왜냐하면
굳이 필요 없는 Outer class 인스턴스화를 하면
굳이 참조를 저장하는 시간과 공간을 낭비하고,
가비지 컬렉션이 바깥 클래스의 인스턴스를 수거하지 못할 가능성이 생겨벌이고


##  나는 어떻게 할 거냐
1. Inner class로 만듦
2. Outer class의 "**Instance**"에 참조하지 않으면 static을 붙임
3. 근데 이게 **Outer class와 무관**하게 다른 class에서도 쓰일 가능성이 있다면 독립된 class로 분리할 거임.

## Static Inner

### Map Interface 내부 Interface
```java
interface Entry<K, V> {

        K getKey();
        V getValue();
        V setValue(V value);
        boolean equals(Object o);
        int hashCode();
 
    }
```
### HashMap 내부 static class Node
```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;

    Node(int hash, K key, V value, Node<K,V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public final K getKey()        { return key; }
    public final V getValue()      { return value; }

    public final int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    public final V setValue(V newValue) {
        V oldValue = value;
        value = newValue;
        return oldValue;
    }

    public final boolean equals(Object o) {
        if (o == this)
            return true;

        return o instanceof Map.Entry<?, ?> e
                && Objects.equals(key, e.getKey())
                && Objects.equals(value, e.getValue());
    }
}
```

## non static inner
### Arraylist 내부 inner Itr
```java
public Iterator<E> iterator() {
    return new Itr();
}

/**
 * An optimized version of AbstractList.Itr
 */
private class Itr implements Iterator<E> {
    int cursor;
    int lastRet = -1;
    int expectedModCount = modCount;

    Itr() {
    }

    public boolean hasNext() {
        return cursor != size;
    }

    @SuppressWarnings("unchecked")
    public E next() {
        checkForComodification();
        int i = cursor;
        if (i >= size)
            throw new NoSuchElementException();
        Object[] elementData = ArrayList.this.elementData; // 저도 잘 모르고요 이거만 신기했어요
        if (i >= elementData.length)
            throw new ConcurrentModificationException();
        cursor = i + 1;
        return (E) elementData[lastRet = i];
    }
}
```

## 익명 클래스
- 정적 팩토리 메서드 만들 때 씀

## 지역 클래스
- 잘 안 쓰임


## 중첩 Class
> 필드처럼 사용할 수 있음
```java
class Outer {
    
    int instanceVar;
    static int staticVar; // 보통 클래스 변수라고 하는데 걍 일케 씀
    
    class InstanceInner { }
    static class StaticInner { }
    
}
```
```java
class Main {

    void useInstance () {
        Outer outer = new Outer();
        int instanceVar = outer.instanceVar;
        Outer.InstanceInner instanceInner = outer.new InstanceInner();
    }

    void useStatic () {
        int staticVar = Outer.staticVar;
        Outer.StaticInner staticInner = new Outer.StaticInner();
    }
    
}
```


### 장점
- Inner에서 Outer 접근 용이 (Outer와 밀접하지 않으면 필드에 박는 컴포지션 쓰겠지)
- 캡슐화(응집도 good, 코드 간결)

### static inner class?
- 필드, 메서드의 static(로딩 시점 초기화 후, 모든 인스턴스에서 공유)과 조금 다름
- 바로바로 그냥 Outer와 독립적으로 작용한다는 뜻임

### 정규화된 this?
- 걍 Inner class 자원이랑 Outer 자원 구분하기 위한 거라고 생각함.
```java
class Outer {
    String value = "outer";
    
    class Inner {
        String value = "inner";
        
        void method (){
            String value = "local";
            
            sout(value);    //local
            sout(this.value);   //inner
            sout(Outer.this.value); //outer
        }
    }
}
```