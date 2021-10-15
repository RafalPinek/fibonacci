package org.rafal.fibonacci;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.awaitility.Awaitility.*;

public class FibonacciCalculatorTest {

    private final FibonacciCalculator calculator = new FibonacciCalculator();

    @ParameterizedTest
    @MethodSource("provideSmallNumbersForFibo")
    public void shouldGetCorrectFiboForSmallNumbers(long input, BigInteger expectedResult) {
        // when
        BigInteger result = calculator.getFibonacci(input);

        // then
        Assertions.assertThat(result).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> provideSmallNumbersForFibo() {
        return Stream.of(
                Arguments.of(1, BigInteger.ONE),
                Arguments.of(2, BigInteger.ONE),
                Arguments.of(3, BigInteger.valueOf(2L)),
                Arguments.of(4, BigInteger.valueOf(3L)),
                Arguments.of(5, BigInteger.valueOf(5L)),
                Arguments.of(6, BigInteger.valueOf(8L)),
                Arguments.of(7, BigInteger.valueOf(13L))
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 0})
    public void shouldThrowExceptionForNegativeNumberAndZero(long input) {
        // when
        Assertions.assertThatThrownBy(() -> calculator.getFibonacci(input))
                // then
                .isInstanceOf(WrongFibonacciArgument.class)
                .hasMessageContaining("Argument must be positive");
    }

    @Test
    public void shouldGetFiboForBigNumber_1000() {
        //given
        BigInteger expectedResult = prepareLargeBigIntegerForFibo_1000();

        // when
        BigInteger result = calculator.getFibonacci(1000L);

        // then
        Assertions.assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void shouldGetFiboForBigNumber_11000() {
        //given
        BigInteger expectedResult = prepareLargeBigIntegerForFibo_11000();

        // when
        BigInteger result = calculator.getFibonacci(11_000L);

        // then
        Assertions.assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void shouldGetFiboWithinTimeoutForBigNumber_11000() {
        // when and then
        assertTimeout(Duration.ofSeconds(1), () -> {
            calculator.getFibonacci(11_000L);
        });
    }

    @Test
    public void shouldHandleManyRequestsWithinTimeout() {
        // given
        final int concurrentRequestsSize = 10_000;
        Set<Thread> threads = new HashSet<>();
        BlockingQueue<BigInteger> returnedResults = new ArrayBlockingQueue<>(concurrentRequestsSize);
        for (int i = 0; i < concurrentRequestsSize; i ++) {
            threads.add(new Thread(() -> returnedResults.add(calculator.getFibonacci(ThreadLocalRandom.current().nextLong(1000L) + 1))));
        }

        // when
        threads.forEach(Thread::start);

        // then
        await()
                .atMost(Duration.ofSeconds(1))
                .with()
                .pollInterval(Duration.ofMillis(200))
                .until(() -> areAllRequestsCompleted(threads, returnedResults));
    }

    private boolean areAllRequestsCompleted(Set<Thread> threads, Collection<BigInteger> returnedResults) {
        return threads.stream()
                .allMatch(t -> t.getState().equals(Thread.State.TERMINATED))
                && returnedResults.size() == threads.size();
    }

    private BigInteger prepareLargeBigIntegerForFibo_1000() {
        return new BigInteger("43466557686937456435688527675040625802564" +
                "6605173717804024817290895365554179490518904038798400792551692959225930803226347752" +
                "09689623239873322471161642996440906533187938298969649928516003704476137795166849228875");
    }

    private BigInteger prepareLargeBigIntegerForFibo_11000() {
        return new BigInteger("20210176547707974780286512230954953291755689547852153070705929" +
                "772403839101769429014402739152166728057269294648883597608221210150398060009986708998724939933093122042" +
                "743211515402758675304659086314872743081802714976195923159224814130119879324039923219240446054349908974" +
                "064356260911699875088954780968833631591451601605695799321611016271401185228792510415965957127675765288" +
                "978183317554333188379158573139779730568601886992183514614336093062937894845364312621547370164279702530" +
                "714277378245110284824136473994697680899041380684948879142517563393576332680574419863992933163043270353" +
                "7544905176155646313342517097875570246445400587613850789988923190703975148413574886397408413874084292035" +
                "3838730962885010867557605096684505852791343138098156239254718114800055109313466748711982138905855262389" +
                "49293587177706196169084391766725570740728702007711781278694947148934730494473580273429824758041412420111" +
                "05120231221182676062513499524779967026027036882667565837339402691759720891381105973173134623302101711187" +
                "73939549409446150625670126031003282140673874620849633193135854880798117250811912506223512718484408345808" +
                "03478901281227392299715646462932824936060878345216415937165067818246179358902224538260637680052522835312" +
                "41598778265765468968457377796378482685368884230391673989143851988436887305824234251752266121807201122410" +
                "8742660846916627616994786813747263729987015193746858562804968361481687237780589525462154994219051929211704" +
                "292283238133706956610706319963651147063912034915374767080617773480672414800802294795184368166807257131088" +
                "801368406682313338249474784990208180707388173983089203815141649035338206210521935451313920959900288122800" +
                "104496766315175314347709499753757204440230235203459922777432078438443987333455095669584671685886013555018" +
                "810546621854063868154344205320723602708708746852472222123169160601307347279941443408632709033045996709475" +
                "215194706714341982118609235043970075344889359688728977924289227198012145182809903539583083723002842801781" +
                "486859332876745462140263992854337065600054371959648788298888010277517704713137568386752172969006761941117" +
                "589133276528800168261134391261635960173296907209428548677568884470482437643861369651705020558341035797369" +
                "041256843444532942030005098864151691121660471538259722823582266149175902810768532062946448274720774719331" +
                "1601392296772065475394069858973578638044680452453811501");
    }

}
