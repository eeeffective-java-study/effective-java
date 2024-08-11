package dependency_injection;

import java.util.List;

@FunctionalInterface
public interface LottoNumberGenerator {
    List<Integer> generate(int size);
}
