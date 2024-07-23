package com.abc.ds.compare;

public enum DSCompareMatchType {
    EQUAL_TO("=", DSCompareResult.EQUAL_TO),
    NOT_EQUAL_TO("!=", DSCompareResult.LESS_THAN, DSCompareResult.GREATER_THAN),
    LESS_THAN("<", DSCompareResult.LESS_THAN),
    LESS_THAN_OR_EQUAL_TO("<=", DSCompareResult.LESS_THAN, DSCompareResult.EQUAL_TO),
    GREATER_THAN(">", DSCompareResult.GREATER_THAN),
    GREATER_THAN_OR_EQUAL_TO(">=", DSCompareResult.GREATER_THAN, DSCompareResult.EQUAL_TO);

    private final String symbolDislplayName;
    private final DSCompareResult[] matchOneOrMoreOfList;

    private DSCompareMatchType(String symbolDislplayName,
                               DSCompareResult... matchOneOrMoreOfList) {
        this.symbolDislplayName = symbolDislplayName;
        this.matchOneOrMoreOfList = matchOneOrMoreOfList;
    }

    public boolean matches(DSCompareResult result) {
        for ( DSCompareResult matchingResult : matchOneOrMoreOfList ) {
            if ( result == matchingResult ) {
                return true;
            }
        }
        return false;
    }

    public String getSymbolDislplayName() {
        return symbolDislplayName;
    }

    @Override
    public String toString() {
        return symbolDislplayName;
    }
}
