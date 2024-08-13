# try-finally 보다는 try-with-resources를 사용하라

### 주요 개념:

- 자원을 닫아야 하는 경우 try-with-resources를 사용하는 것이 좋음.
- 이는 Java 7에서 도입된 기능으로, 코드를 더 짧고 분명하게 만들며 예외 처리를 개선함.


### try-finally의 문제점:

- 자원이 둘 이상이면 try-finally 방식은 코드가 지저분해짐.
- 예외가 중첩되면 실제 발생한 첫 번째 예외가 숨겨질 수 있음.

```java
public static void inputAndWriteString() throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    try {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        try {
             String line = br.readLine();
            bw.write(line);
        } finally {
            bw.close();
        }
    } finally {
        br.close();
    }
}
```

### try-with-resources의 장점:

- 코드가 더 간결하고 읽기 쉬워짐.
- 숨겨진 예외도 스택 추적 내역에 '억제된 예외'로 출력됨.
- 여러 예외가 발생해도 실제 발생한 예외를 정확히 파악할 수 있음.


### 사용 방법:

- AutoCloseable 인터페이스를 구현한 객체에 대해 사용 가능함.
- try 괄호 안에 닫아야 할 자원을 선언


### 예시 코드:
```java
try (InputStream in = new FileInputStream(src);
    OutputStream out = new FileOutputStream(dst)) {
    byte[] buf = new byte[BUFFER_SIZE];
    int n;
    while ((n = in.read(buf)) >= 0)
    out.write(buf, 0, n);
}
```

### 주의사항:

- 꼭 회수해야 하는 자원을 다룰 때는 try-finally 대신 try-with-resources를 사용해야함. 
- 코드는 더 짧고 분명해지며, 만들어지는 예외 정보도 훨씬 유용함.