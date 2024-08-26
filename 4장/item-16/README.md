# <public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용하라>
```java
class Point {
  public double x;
  public double y;
}
```
- 이런 클래스는 데이터 필드에 직접 접근할 수 있으니 캡슐화의 이점을 제공하지 못한다.

> ## 철저한 객체 지향 프로그래머가 캡슐화한 클래스
```java
// 패키지 바깥에서 접근할 수 있는 클래스라면 접근자를 제공함으로써 클래스 내부 표현 방식을 언제
public class Ex02 {
    private double x;
    private double y;

    public Ex02(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
}
```
> ## 자바 플랫폼 라이브러리에서 필드를 실제 노출시킨 사례
- java.awt.package 패키지 - Point, Dimension 클래스
```
// java.awt.Component 클래스 내부.
    public Dimension getSize() {
		return size();
	}

	@Deprecated
	public Dimension size() {
		return new Dimension(width, height);
	}

```
```
// java.awt.Dimension 클래스 내부.
public class Dimension extends Dimension2D implements java.io.Serializable {
	public int width;
	public int height;

    ...
}
```
- Dimesion 클래스의 필드는 가변으로 설계됨
- getSize 를 호출하는 모든 곳에서 방어적 복사를 위해 인스턴스를 새로 생성해야 함

> ## public 클래스의 필드가 불변일때 직접 노출하는 경우의 문제점
- API를 변경하지 않고는 표현 방식을 바꿀 수 없다.
- 필드를 읽을 때 부수 작업을 수행할 수 없다는 단점이 있으나, 불변식은 보장할 수 있다.
```java
public final class Time {
    private static final int HOURS_PER_DAY = 24;
    private static final int MINUTES_PER_HOUR = 60;

    public final int hour;
    public final int minute;

    public Time(int hour, int minute) {
        if (hour < 0 || hour >= HOURS_PER_DAY) {
            throw new IllegalArgumentException(" 시간 : " + hour);
        }
        if (minute < 0 || minute >= MINUTES_PER_HOUR) {
            throw new IllegalArgumentException(" 분 : " + minute);
        }
        this.hour = hour;
        this.minute = minute;
    }
}
```
