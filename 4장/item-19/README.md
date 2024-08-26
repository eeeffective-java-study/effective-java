# ����� ����� �����ϰ� ����ȭ�ض�. �׷��� �ʾҴٸ� ����� �����϶�.
> ## ����� ���� ����ȭ

- ����� ���� ����ȭ�� ����� ������ Ŭ������ ������ ���� �޼��忡 �ش� �޼��带 ���������� ��� �̿��ϰ� �ִ���, �׷��� � ������ �����ϵ��� �����Ǿ�� �ϴ��� ������ ���ܵδ� ���� ���Ѵ�.

- �̷��� �ؾ� �ϴ� ������ Ŭ������ ��ӹ޾� ������ Ŭ�������� �ش� �޼��带 �θ� Ŭ���������� �ǵ��� �ٸ��� ������ ��� �ǵ�ġ ���� �������� �̾��� �� �ֱ� �����̴�.

- �ڹ� API������ �̷��� ������ Implementation Requirements(�ڵ忡�� @ImplSpec)��� �׸����� ����ȭ�Ͽ� �����ϰ� �ִ�. �Ʒ��� AbstractCollection.remove() �޼��� �Ϻ��̴�.

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
- �̸� ���� Abstract iterator()�� ��ȯ�ϴ� Iterator�� remove() ������ ���Ƿ� �������ϰ� �Ǹ� �÷��ǿ��� ���Ҹ� �����ϴ� ��ɿ� ������ �߻��� ���� ������ �� �� �ִ�. �̷� ������ ���� ������ ����ȭ�Ͽ� �������ν� Ŭ������ ����ؼ� �޼��带 �������� �� �����ڰ� ��� �����ؾ� �ϴ��� �����ϵ��� �ؾ� �Ѵ�.

> ## ����� ĸ��ȭ�� ��ģ��.
- ������ API�� ���״�� �������̽��̱� ������ ���� API������ '���' �� �ƴ� '����'�� ������ ���߾� �����ؾ� �Ѵ�. �̴� ����� ĸ��ȭ�� ��ġ�� ������ �߻��ϴ� �����̴�.
- ��, ����� �����ϵ��� Ŭ������ ���������ν� ���� ������ (������ ����) �ܺο� ����Ǵ� ���� �������� �Ǵ� ���̴�.

> ## ��ӿ� Ŭ���� ���� �� ���ǻ���
- hook: Ŭ������ ���� ���� ���� �߰��� ����� �� �ִ� �ڵ�
- ����� ������ ���� ���� �� ������ protected �޼��� ���·� ����
- �� �κп� ���ؼ� ��Ȯ�� ������ ���� �� �����ؾ� ��
- ���� ���� Ŭ������ ����� �����ϴ� ������ �ʼ���


>## example - AbstractList�� clear()
```
public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {
    /**
     * Removes all of the elements from this list (optional operation).
     * The list will be empty after this call returns.
     *
     * @implSpec
     * This implementation calls {@code removeRange(0, size())}.
     *
     * <p>Note that this implementation throws an
     * {@code UnsupportedOperationException} unless {@code remove(int
     * index)} or {@code removeRange(int fromIndex, int toIndex)} is
     * overridden.
     *
     * @throws UnsupportedOperationException if the {@code clear} operation
     *         is not supported by this list
     */
    public void clear() {
        removeRange(0, size());
    }

    /**
     * Removes from this list all of the elements whose index is between
     * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.
     * Shifts any succeeding elements to the left (reduces their index).
     * This call shortens the list by {@code (toIndex - fromIndex)} elements.
     * (If {@code toIndex==fromIndex}, this operation has no effect.)
     *
     * <p>This method is called by the {@code clear} operation on this list
     * and its subLists.  Overriding this method to take advantage of
     * the internals of the list implementation can <i>substantially</i>
     * improve the performance of the {@code clear} operation on this list
     * and its subLists.
     *
     * @implSpec
     * This implementation gets a list iterator positioned before
     * {@code fromIndex}, and repeatedly calls {@code ListIterator.next}
     * followed by {@code ListIterator.remove} until the entire range has
     * been removed.  <b>Note: if {@code ListIterator.remove} requires linear
     * time, this implementation requires quadratic time.</b>
     *
     * @param fromIndex index of first element to be removed
     * @param toIndex index after last element to be removed
     */
    protected void removeRange(int fromIndex, int toIndex) {
        ListIterator<E> it = listIterator(fromIndex);
        for (int i = 0, n = toIndex - fromIndex; i < n; i++) {
            it.next();
            it.remove();
        }
    }

    // ...
}
```
- clear()
    - removeRange()�� ȣ���� index ó������ ������ ����
- removeRange()
    - clear()�� �������� ����� ���� �ϱ� ���� ����
    - �ش� �޼��尡 �����ٸ� ���� Ŭ�������� clear �޼��� ȣ�� �� ������ �������ų� ���� �����߾�� ��
> ## ��ӿ� Ŭ������ �����ڿ��� ������ ������ �޼��� ȣ�� ����
���� Ŭ������ �����ڴ� ���� Ŭ������ �����ں��� ���� ����ȴ�.
��������� ���� Ŭ�������� �������� �޼��尡 ���� Ŭ������ �����ں��� ���� ȣ��Ǵ� ��Ȳ�� �߻��� �� �ִ�.

> ## example - Class1�� ����ϴ� Class2
```
class Class1 {
    public Class1() {
        test();
    }

    public void test() {
        System.out.println("test");
    }
}
class Class2 extends Class1 {
    private String string;

    public Class2() {
        string = "override!";
    }

    @Override
    public void test() {
        System.out.println(string);
    }
}
```
- �ϴ� ���� Ŭ���� ������ ���� �� test()�� ȣ���ϴ� System.out.println()�� 2�� ȣ��� ���̴�.
- string�� override! ���� �־������� �� ���� 2�� �������� ���������� ����� null�� override!����.
- Class2�� �����ں��� Class2�� test()�� ���� ȣ��Ǿ��� �����̴�.

> ## Cloneable�� Serializable �������̽��� �����ض�
- �� �������̽��� ������ Ŭ������ ��� �����ϰ� �����ϴ� ���� �Ϲ������� ���� �ʴ�.
Cloneable�� clone()�� Serializable�� readObject()�� ���ο� ��ü�� ������, �����ڿ� ����� ����� ������.
Ŭ������ ���°� �ʱ�ȭ�Ǳ� ���� �޼������ ȣ��Ǵ� ��Ȳ�� �� �� �ִ�.

- Serializable�� ������ ��ӿ� Ŭ������ readResolve(), writeReplace() �޼��带 ���� �� protected�� �����ؾ� �Ѵ�.
private���� ���� �� ���� Ŭ���������� ���õȴ�.

> ## ����� �����ϴ� ���
- Ŭ������ final�� ����
- ��� �����ڸ� private�� default�� ���� �� public ���� ���丮 ����
- �Ϲ����� ��ü Ŭ������ ����� �����ϴ°� ����� �������� �� �ִ�.
- �̸� �ذ��ϱ� ���ؼ��� Ŭ���� ���ο��� ������ ���� �޼��带 ������� �ʰ� ����� �̸� ����ȭ�ϸ� �ȴ�.
- �޼��带 �������ص� �ٸ� �޼����� ���ۿ� �ƹ��� ������ ���� �ʰԲ� �����ϸ� �ȴ�.

> ## ��ӿ��� ����� �޼��� Ȱ���ϱ�
- Ŭ���� ������ �����ϸ� ������ ���� �޼��带 ����ϴ� �ڵ带 �����ϰ� �ʹٸ�?
- ������ ���� �޼��带 private ������ ����� �޼���� �Űܺ���.

example - Class1�� ����ϴ� Class2 ����
``` 
class Class1 {
    public Class1() {
        helper();
    }

    public void test() {
        helper();
    }

    private void helper() {
        System.out.println("test");
    }
}

class Class2 extends Class1 {
    private String string;

    public Class2() {
        string = "override!";
    }

    @Override
    public void test() {
        System.out.println(string);
    }
}
```