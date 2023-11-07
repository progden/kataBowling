package io.progden.katabowling;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KataBowlingApplicationTests {

    private BowlingGame game;

    @BeforeEach
    void setUp() {
        game = new BowlingGame();
    }

    @ParameterizedTest
    @MethodSource("firstCellTestCase")
    void firstCell(String pins, int expected, BowlingGameCell cell) {
        // Arrange

        // Act
        roll(pins);

        // Assert
        assertEquals(expected, game.score());
        assertEquals(cell, game.cellStatus());
    }

    @ParameterizedTest
    @MethodSource("secondCellTestCase")
    void secondCell(String pins, int expected) {
        // Arrange

        // Act
        roll(pins);

        // Assert
        assertEquals(expected, game.score());
    }

    private static Stream<Arguments> firstCellTestCase(){
        return Stream.of(
                Arguments.of("", 0, BowlingGameCell.Normal),
                Arguments.of("1", 1, BowlingGameCell.Normal),
                Arguments.of("5, 4", 9, BowlingGameCell.Normal),
                Arguments.of("5, 5", 10, BowlingGameCell.Spare),
                Arguments.of("10", 10, BowlingGameCell.Strike),
                Arguments.of("5", 5, BowlingGameCell.Normal)
        );
    }

    private static Stream<Arguments> secondCellTestCase(){
        return Stream.of(
                Arguments.of("5, 5, 5", 20, BowlingGameCell.Normal),
                Arguments.of("5, 4, 1, 5", 15, BowlingGameCell.Normal),
                Arguments.of("10, 5", 20, BowlingGameCell.Normal),
                Arguments.of("10, 5, 5", 30, BowlingGameCell.Normal),
                Arguments.of("10, 10", 30, BowlingGameCell.Normal),
                Arguments.of("5", 5, BowlingGameCell.Normal)
        );
    }

    private void roll(String pins) {
        if (StringUtils.isNotBlank(pins)) {
            Arrays.stream(pins.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .forEach(game::roll);
        }
    }
}
