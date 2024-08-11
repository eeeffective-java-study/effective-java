package dependency_injection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class AutoLottoGenerator implements LottoNumberGenerator {
    @Override
    public List<Integer> generate(int size) {
        List<Integer> lottoTotalNumbers = new ArrayList<>(45);
        Collections.shuffle(lottoTotalNumbers);

        return lottoTotalNumbers.stream()
                .limit(size)
                .collect(toList());
    }
}
