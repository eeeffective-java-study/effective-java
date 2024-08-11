package dependency_injection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Main {

    private static LottoNumberGenerator lottoNumberGenerator = new AutoLottoGenerator();

    public static void main(String[] args) {
        Lotto lotto = new Lotto(size -> {
            List<Integer> lottoTotalNumbers = new ArrayList<>(45);
            Collections.shuffle(lottoTotalNumbers);

            return lottoTotalNumbers.stream()
                    .limit(size)
                    .collect(toList());
        });

        System.out.println(lotto.getLottoNumbers());



        Lotto lotto2 = new Lotto(lottoNumberGenerator);

        System.out.println(lotto2.getLottoNumbers());
    }
}
