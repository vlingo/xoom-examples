package io.examples.calculation.application;

public class ExecuteCalculation {

    private final String operationName;
    private final Integer firstOperand;
    private final Integer secondOperand;

    public ExecuteCalculation(final String operationName,
                              final Integer firstOperand,
                              final Integer secondOperand) {
        this.operationName = operationName;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }

    public String operationName() {
        return operationName;
    }

    public Integer firstOperand() {
        return firstOperand;
    }

    public Integer secondOperand() {
        return secondOperand;
    }

}
