package com.teno.openmarket.api;

import java.util.*; // [위반] 와일드카드 Import 금지

public class BadStyle {

    public void BadMethodName() { // [위반] 메서드명은 소문자로 시작해야 함
            System.out.println("Indentation Error"); // [위반] 들여쓰기 4칸이어야 하는데 여긴 8칸
    }
}
