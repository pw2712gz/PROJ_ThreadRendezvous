package com.abc.ds.list.integer.tests;

import com.abc.ds.list.integer.*;

/* deliberate package access */
class TestIntDSListInsertBefore extends TestIntDSListBase {
    public TestIntDSListInsertBefore(IntDSListFactory factory) {
        super("insertBefore()", factory);
    }

    @Override
    protected void performTests() {
        testInsertBeforeInTheMiddle();
        testInsertBeforeTheLastOne();
        testInsertBeforeTheFirstOne();
        testInsertBeforeAtTheEnd();
        testInsertBeforeNegativeIndex();
        testInsertBeforeBarelyTooLargeIndex();
        testInsertBeforeWayTooLargeIndex();
    }

    private void testInsertBeforeInTheMiddle() {
        outln(" - insertBefore() a middle item -");
        IntDSList ds = createDS();
        add(ds, 10, 20, 30);
        outln("insertBefore(1, 15)...");
        ds.insertBefore(1, 15);
        checkPeekAll(ds, 10, 15, 20, 30);
    }

    private void testInsertBeforeTheLastOne() {
        outln(" - insertBefore() the last one -");
        IntDSList ds = createDS();
        add(ds, 10, 20, 30);
        outln("insertBefore(2, 25)...");
        ds.insertBefore(2, 25);
        checkPeekAll(ds, 10, 20, 25, 30);
    }

    private void testInsertBeforeTheFirstOne() {
        outln(" - insertBefore() the first one -");
        IntDSList ds = createDS();
        add(ds, 10, 20, 30);
        outln("insertBefore(0, 5)...");
        ds.insertBefore(0, 5);
        checkPeekAll(ds, 5, 10, 20, 30);
    }

    private void testInsertBeforeAtTheEnd() {
        outln(" - insertBefore() at the end (append to the end) -");
        IntDSList ds = createDS();
        add(ds, 10, 20, 30);
        outln("insertBefore(count(), 35)...");
        ds.insertBefore(ds.count(), 35);
        checkPeekAll(ds, 10, 20, 30, 35);
    }

    private void testInsertBeforeNegativeIndex() {
        outln(" - insertBefore() negative index [expect exception] -");
        IntDSList ds = createDS();
        add(ds, 10, 20, 30);
        insertBeforeBadIndex(ds, -1, 999);
    }

    private void testInsertBeforeBarelyTooLargeIndex() {
        outln(" - insertBefore() barely too large index [expect exception] -");
        IntDSList ds = createDS();
        add(ds, 10, 20, 30);
        insertBeforeBadIndex(ds, ds.count() + 1, 999);
    }

    private void testInsertBeforeWayTooLargeIndex() {
        outln(" - insertBefore() way too large index [expect exception] -");
        IntDSList ds = createDS();
        add(ds, 10, 20, 30);
        insertBeforeBadIndex(ds, 100, 999);
    }

    private void insertBeforeBadIndex(IntDSList ds,
                                      final int index,
                                      final int item) {

        checkBadIndex(ds, new MethodAccess() {
            @Override
            public String formattedMethod() {
                return "insertBefore(" + index + ", " + item + ")";
            }

            @Override
            public void execute(IntDSList ds2) {
                ds2.insertBefore(index, item);
            }
        });
    }


}
