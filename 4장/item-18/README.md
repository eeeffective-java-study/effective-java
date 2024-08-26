# 상속보다는 컴포지션을 사용하라
결론부터 먼저 말하자면 상속은 캡슐화를 깨트리게 되며, 상위 클래스에 의존적이여서 결합도가 높아진다.

상위 클래스의 구현이 하위 클래스에게 노출되기 때문에 자바의 원칙 중 하나인 캡슐화가 깨지게 된다.

또한, 상위 클래스와 하위 클래스의 관계가 컴파일 시점에 결정되어 구현에 의존하기 때문에 실행시점에 객체의 종류를 변경하는 것이 불가능하며 다형성과 같은 객체지향의 이점을 활용할 수 없다.

정리하자면, 하위 클래스가 상위 클래스에 강하게 의존 및 결합이 되는 설계가 되는 것이다.

만약, 상위 클래스에 한 로직이 변경되었다고 가정하자. 하위 클래스가 재대로 동작을 한다는 보장이 과연 되는가?

하위 클래스에 변경이 전혀 없었지만 단순히 상위 클래스가 변경됨으로 인하여 하위 클래스가 오동작할 가능성이 충분히 있다.


## 이번 로또 미션을 통해서 구체적인 예시를 살펴보자.
- Lotto 클래스는 로또 번호를 List로 가지고 있는 클래스이다.

```
public class Lotto {
    protected List<Integer> lottoNumbers;

    public Lotto(List<Integer> lottoNumbers) {
        this.lottoNumbers = new ArrayList<>(lottoNumbers);
    }

    public boolean contains(Integer lottoNumber) {
        return this.lottoNumbers.contains(lottoNumber);
    }
}
```
- WinningLotto 클래스는 당첨 로또번호를 가지고 있는 클래스이며 Lotto를 상속하는 클래스이다.
```
public class WinningLotto extends Lotto {
    private final LottoNumber bonusBall;

    public WinningLotto(List<Integer> lottoNumbers, LottoNumber bonusBall) {
        super(lottoNumbers);
        this.lottoNumber = lottoNumber;
    }

    public long calculateMatchCount(Lotto otherLotto) {
        return lottoNumbers.stream()
            .filter(otherLotto::contains)
            .count();
    }
    
}
```
- 리뷰어의 요청을 받고 Lotto 클래스의 요구사항이 변경되었다고 가정하자. 요구사항은 List<Integer>를 List<LottoNumber>로 변경하는 요구사항이다.
```
public class Lotto {
    private List<LottoNumber> lottoNumbers;

    public Lotto(List<LottoNumber> lottoNumbers) {
        this.lottoNumbers = new ArrayList<>(lottoNumbers);
    }

    public boolean contains(LottoNumber lottoNumber) {
        return this.lottoNumbers.contains(lottoNumber);
    }
}
```
```
public class WinningLotto extends Lotto {
    protected final LottoNumber bonusBall;

    // Compile Error
    public WinningLotto(List<Integer> lottoNumbers, LottoNumber bonusBall) {
        super(lottoNumber);
        this.bonusBall = bonusBall;
    }

    public long calculateMatchCount(Lotto otherLotto) {
        return lottoNumbers.stream()
            .filter(otherLotto::contains)
            .count();
    }
}
```
WinnginLotto 클래스는 Lotto 클래스를 상속하고 있어 의존적이기 때문에 상위 클래스의 변경으로 WinningLotto가 깨지게된다.

이를 해결하기 위해서는 모든 하위 클래스를 개발자가 일일히 수정을 해줘야한다.

이처럼 상속은 하위 클래스가 상위 클래스에 강하게 의존, 결합하는 구조를 띄기 때문에 변화에 유연하게 대처하기 어렵다.

해당 문제를 해결 하기 위해 상속보다는 컴포지션을 고려해보자.

```
package effectivejava.ex04;

public class WinningLotto2 {
    private final Lotto lotto;
    private final String name;

    private WinningLotto2(Lotto lotto, String name) {
        this.lotto = lotto;
        this.name = name;
    }

    public static WinningLotto2 createLotto(Lotto lotto,String name){
        return new WinningLotto2(lotto,name);
    }

    public long contains(Lotto otherLotto){
        return lotto.calculateMatchCount(otherLotto);
    }
}

```
- WinningLotto 클래스에서 인스턴스 변수로 Lotto 클래스를 가지고 있다는 것을 알 수 있게 된다.
- 이처럼, WinningLotto 클래스에서 인스턴스 변수로 Lotto 클래스를 가지는 것이 조합이다.
- WinningLotto에서 Lotto 클래스를 사용하고 싶으면 Lotto 클래스의 메서드를 호출하는 방식으로 사용하게 된다.
- 조합을 사용함으로서 우리는 내부 구현 방식의 영향에서 벗어나 Lotto 클래스의 인스턴스 변수가 List<Integer>에서 List<LottoNumber>로 변경되더라도 영향을 받지 않게 된다.


> ## 결론
- 상위 클래스에 의존하게 되어 종속적이고 변화에 유연하지 못한 상속보다는 조합을 한번 사용해보자.
- 상속이 적절하게 사용되면 조합보다 강력하고, 개발하기도 편한 부분도 있다.
- 상속을 적절하게 사용하기 위해서는 다음과 같은 최소 조건을 만족한 상태에서 사용하는 것을 추천한다.
    - 확장을 고려하고 설계한 확실한 is - a 관계일 때
    - API에 아무런 결합이 없는 경우, 결함이 있다면 하위 클래스까지 전파돼도 괜찮은 경우

- 다음과 같은 경우가 확실한 is - a 관계라고 할 수 있다.

```
public class 포유류 extends 동물 {
    protected void 숨을_쉬다() {
    }
    protected void 새끼를_낳다() {   
    }
}
``` 
포유류가 동물이라는 사실은 변할 가능성이 없고, 포유류가 숨을 쉬고 새끼를 낳는다는 행동이 변할 가능성은 거의 없다.

이처럼 확실한 is - a 관계일 경우, 상위 클래스는 변할 일이 거의 없다.


향후 상속을 사용할 경우 확실한 is - a 관계인지 꼼꼼하게 고민해보고, 상위 클래스가 변화에 의해서 결함이 발생한 경우, 하위 클래스까지 영향이 가도 괜찮다는 결론이 생겼으면 상속을 사용해도 좋다.


정말 중요한 사실은 상속은 코드 재사용의 개념이 아니다.
상속은 반드시 확장이라는 관점에서 사용해야 한다는 것을 명심하자.