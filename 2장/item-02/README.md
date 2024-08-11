# 생성자에 매개변수가 많다면 빌더를 고려하라

## 점층적 생성자 패턴
```java
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;
    
    public NutritionFacts(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }
    
    public NutritionFacts(int servingSize, int servings, int calories) {
        this(servingSize, servings, calories, 0);
    }
    
    .
    .
    .
}
```
- 확장하기 어렵다.
- 클라이언트가 설정하길 원치 않는 매개변수까지 포함해줘야함.
- 매개변수가 많아지면 클라이언트 코드를 작성하거니 읽기 힘들다.

## 자바빈즈 패턴
```java
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public NutritionFacts() {}
    // 세터 메서드들
    public void setServingSize(int val) { servingSize = val; }
    .
    .
    .
}
```

- 객체 하나를 만들기 위해 메서드를 여러번 호출 해야함.
- 객체가 완성되기 전까지는 일관성이 무너진 상태에 놓이게 됨.
- 위의 이유로, 클래스를 불변으로 만들 수 없고, 스레드 안정성이 떨어짐.

## 빌더 패턴
```java
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public static class Builder {
        // 필수 매개변수
        private final int servingSize;
        private final int servings;

        // 선택 매개변수 - 기본값으로 초기화한다.
        private int calories      = 0;
        private int fat           = 0;
        private int sodium        = 0;
        private int carbohydrate  = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings    = servings;
        }

        public Builder calories(int val)
        { calories = val;      return this; }
        public Builder fat(int val)
        { fat = val;           return this; }
        public Builder sodium(int val)
        { sodium = val;        return this; }
        public Builder carbohydrate(int val)
        { carbohydrate = val;  return this; }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

    private NutritionFacts(Builder builder) {
        servingSize  = builder.servingSize;
        servings     = builder.servings;
        calories     = builder.calories;
        fat          = builder.fat;
        sodium       = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }
}
```

- 불변 클래스
- 빌더의 세터 메서드들은 빌더 자신을 반환하기 때문에, 연쇄적으로 호출 가능
```java
    public static void main(String[] args) {
        NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8)
                .calories(100)
                .sodium(35)
                .carbohydrate(27)
                .build();
    }
```
- 빌더 패턴은 계층적으로 설계된 클래스와 함께 쓰기에 좋다.(코드 생략)
