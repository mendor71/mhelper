package ru.pack.csps.util;

import java.util.Random;

public class CodeGenerator implements ICodeGenerator {
    private int min;
    private int max;

    public Integer randomCode() {
        return min + new Random().nextInt(max - min);
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
