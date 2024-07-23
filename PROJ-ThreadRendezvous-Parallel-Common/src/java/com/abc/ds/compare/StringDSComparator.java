package com.abc.ds.compare;


public class StringDSComparator {
    public static final DSComparator<String> NULL_FIRST_CASE_INSENSITIVE_ASC =
        new NullDSComparator<>(DSNullOrdering.NULL_FIRST,
            CaseInsensitiveComparator.ASC);

    public static final DSComparator<String> NULL_FIRST_CASE_INSENSITIVE_DESC =
        new NullDSComparator<>(DSNullOrdering.NULL_FIRST,
            CaseInsensitiveComparator.DESC);

    public static final DSComparator<String> NULL_LAST_CASE_INSENSITIVE_ASC =
        new NullDSComparator<>(DSNullOrdering.NULL_LAST,
            CaseInsensitiveComparator.ASC);

    public static final DSComparator<String> NULL_LAST_CASE_INSENSITIVE_DESC =
        new NullDSComparator<>(DSNullOrdering.NULL_LAST,
            CaseInsensitiveComparator.DESC);

    public static final DSComparator<String> NULL_FIRST_CASE_SENSITIVE_ASC =
        new NullDSComparator<>(DSNullOrdering.NULL_FIRST,
            CaseSensitiveComparator.ASC);

    public static final DSComparator<String> NULL_FIRST_CASE_SENSITIVE_DESC =
        new NullDSComparator<>(DSNullOrdering.NULL_FIRST,
            CaseSensitiveComparator.DESC);

    public static final DSComparator<String> NULL_LAST_CASE_SENSITIVE_ASC =
        new NullDSComparator<>(DSNullOrdering.NULL_LAST,
            CaseSensitiveComparator.ASC);

    public static final DSComparator<String> NULL_LAST_CASE_SENSITIVE_DESC =
        new NullDSComparator<>(DSNullOrdering.NULL_LAST,
            CaseSensitiveComparator.DESC);

    // no instances
    private StringDSComparator() {
    }

    private static class CaseInsensitiveComparator
            implements DSComparator<String> {

        public static final DSComparator<String> ASC =
            new CaseInsensitiveComparator();

        public static final DSComparator<String> DESC =
            new ReverseDSComparator<>(ASC);

        // singleton, no need for more than one instance
        private CaseInsensitiveComparator() {
        }

        @Override
        public DSCompareResult compare(String a, String b) {
            return DSCompareResult.instanceFor(
                String.CASE_INSENSITIVE_ORDER.compare(a, b));
        }
    } // type CaseInsensitiveComparator

    private static class CaseSensitiveComparator
            implements DSComparator<String> {

        public static final DSComparator<String> ASC =
            new CaseSensitiveComparator();

        public static final DSComparator<String> DESC =
            new ReverseDSComparator<>(ASC);

        // singleton, no need for more than one instance
        private CaseSensitiveComparator() {
        }

        @Override
        public DSCompareResult compare(String a, String b) {
            return DSCompareResult.instanceFor(a.compareTo(b));
        }
    } // type CaseSensitiveComparator
}
