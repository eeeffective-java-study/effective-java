package dependency_injection;

import without_dependency_injection.LottoNumber;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class Lotto {

    private static final int DEFAULT_LOTTO_NUMBERS_SIZE = 6;

    private final Set<LottoNumber> lottoNumbers;

    public Lotto(LottoNumberGenerator lottoNumberGenerator) {
        List<Integer> numbers = lottoNumberGenerator.generate(DEFAULT_LOTTO_NUMBERS_SIZE);
        this.lottoNumbers = numbers.stream()
                .map(LottoNumber::new)
                .collect(toSet());
    }

    public Set<LottoNumber> getLottoNumbers() {
        return Collections.unmodifiableSet(lottoNumbers);
    }
}
