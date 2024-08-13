# finalizer와 cleaner 사용을 피하라

### finalizer와 cleaner의 문제점:

- 실행 시점을 보장할 수 없음 (즉시 실행되지 않을 수 있음)
- 실행 여부도 보장되지 않음 (전혀 실행되지 않을 수 있음)
- 성능 문제를 일으킬 수 있음 (객체 생성과 파괴 시간이 늘어남)
- 보안 위험이 있음 (finalizer 공격에 취약)

### 대안:

- AutoCloseable 인터페이스를 구현하고 try-with-resources 구문 사용
- 명시적인 종료 메서드(예: close()) 제공

### finalizer와 cleaner의 적절한 사용 사례:

- 자원의 소유자가 close 메서드를 호출하지 않는 것에 대한 안전망 역할
- 네이티브 피어(native peer)와 연결된 객체를 정리할 때

### cleaner 사용 예시 (만약 꼭 사용해야 한다면):

- 정리가 필요한 자원을 가진 객체를 static 내부 클래스로 선언
- 해당 클래스가 Runnable을 구현하고 정리 로직을 담당
- 외부 클래스의 cleaner가 이 Runnable의 인스턴스를 clean 메서드로 등록

### 주의사항:

- finalizer와 cleaner에 의존하지 말 것
- 중요한 상태를 가진 객체에 finalizer를 사용하지 말 것
- System.gc나 System.runFinalization 메서드에 의존하지 말 것

### 결론: 
- finalizer와 cleaner는 예측할 수 없고, 위험하며, 일반적으로 불필요함.
- 객체 정리와 자원 회수를 위해서는 try-with-resources나 try-finally를 사용하는 것이 좋음.