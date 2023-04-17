package com.wedding.serviceapi.filter;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ServiceLoginFilterTest {

    @Test
    @DisplayName("map 스트림 변환 테스트")
    void mapToStream() {
        // given
        Map<String, Integer> testMap = Map.of(
                "test1", 1,
                "test2", 2,
                "test3", 3
        );
        // when
        Stream<Integer> integerStream = testMap.entrySet().stream().filter(map -> map.getKey().startsWith("test1"))
                .map(Map.Entry::getValue);
        // then
        Assertions.assertThat(integerStream.findFirst().get()).isEqualTo(1);
    }


}