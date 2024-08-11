package without_dependency_injection;

public class Main {

    public static void main(String[] args) {
        Lotto lotto = new Lotto();

        System.out.println(lotto.getLottoNumbers());
    }
}
