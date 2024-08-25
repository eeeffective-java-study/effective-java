# <public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용하라>
```java
class Point {
  public double x;
  public double y;
}
```
- 이런 클래스는 데이터 필드에 직접 접근할 수 있으니 캡슐화의 이점을 제공하지 못한다.
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