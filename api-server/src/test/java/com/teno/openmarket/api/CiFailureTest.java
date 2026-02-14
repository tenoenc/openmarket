package com.teno.openmarket.api;

import java.util.*; // 스타일 위반
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

public class CiFailureTest {

    @Test
    void should_Fail_When_TestFails() {
        fail("CI 파이프라인 검증을 위한 의도적인 실패입니다.");
    }
}
