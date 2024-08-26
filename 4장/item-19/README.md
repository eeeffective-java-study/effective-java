# 상속을 고려해 설계하고 문서화해라. 그러지 않았다면 상속을 금지하라.
> ## 상속을 위한 문서화

- 상속을 위한 문서화란 상속이 가능한 클래스의 재정의 가능 메서드에 해당 메서드를 내부적으로 어떻게 이용하고 있는지, 그래서 어떤 식으로 동작하도록 구현되어야 하는지 문서로 남겨두는 것을 말한다.

- 이렇게 해야 하는 이유는 클래스를 상속받아 구현된 클래스에서 해당 메서드를 부모 클래스에서의 의도와 다르게 구현할 경우 의도치 않은 동작으로 이어질 수 있기 때문이다.

- 자바 API에서는 이러한 문서를 Implementation Requirements(코드에선 @ImplSpec)라는 항목으로 문서화하여 제공하고 있다. 아래는 AbstractCollection.remove() 메서드 일부이다.

```
 public boolean remove(Object o) {
        Iterator<E> it = iterator();
        if (o==null) {
            while (it.hasNext()) {
                if (it.next()==null) {
                    it.remove();
                    return true;
                }
            }
        }
 }
```
- 이를 통해 Abstract iterator()가 반환하는 Iterator의 remove() 동작을 임의로 재정의하게 되면 컬렉션에서 원소를 삭제하는 기능에 문제가 발생할 수도 있음을 알 수 있다. 이런 식으로 내부 동작을 문서화하여 남김으로써 클래스를 상속해서 메서드를 재정의할 때 개발자가 어떻게 구현해야 하는지 인지하도록 해야 한다.

