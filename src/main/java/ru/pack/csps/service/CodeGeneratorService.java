package ru.pack.csps.service;

import ru.pack.csps.util.ICodeGenerator;

public class CodeGeneratorService {
    private ICodeGenerator codeGenerator;

    public String generateCode() {
        return codeGenerator.randomCode().toString();
    }

    public void setCodeGenerator(ICodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }
}
