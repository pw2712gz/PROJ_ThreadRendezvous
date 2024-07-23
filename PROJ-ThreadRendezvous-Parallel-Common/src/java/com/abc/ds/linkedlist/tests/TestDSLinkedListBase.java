package com.abc.ds.linkedlist.tests;

import com.abc.ds.linkedlist.*;
import com.abc.ds.linkedlist.DSLinkedList.Node;
import com.programix.testing.*;
import com.programix.util.*;

/* deliberate package access */
abstract class TestDSLinkedListBase extends BaseTest {
    protected final DSLinkedListFactory factory;

    protected TestDSLinkedListBase(String title, DSLinkedListFactory factory) {
        super(title);
        this.factory = factory;
    }

    protected DSLinkedList<String> createDSLinkedList() {
        outln("Creating a new DSLinkedList<String> instance...");
        DSLinkedList<String> ll = factory.create(String.class);
        outln("   ...created: " + ll.getClass().getCanonicalName());
        return ll;
    }

    protected void insertFirst(DSLinkedList<String> ll, String... items) {
        for ( String item : items ) {
            ll.insertFirst(item);
            outln("insertFirst(" + StringTools.quoteWrap(item) + ")");
        }
    }

    protected void insertLast(DSLinkedList<String> ll, String... items) {
        for ( String item : items ) {
            ll.insertLast(item);
            outln("insertLast(" + StringTools.quoteWrap(item) + ")");
        }
    }

    protected String[] extractPayloads(DSLinkedList.Node<String>[] nodes) {
        if ( ObjectTools.isEmpty(nodes) ) {
            return StringTools.ZERO_LEN_ARRAY;
        }

        String[] payloads = new String[nodes.length];
        for ( int i = 0; i < payloads.length; i++ ) {
            payloads[i] = nodes[i] == null ? null : nodes[i].getPayload();
        }
        return payloads;
    }

    protected void confirmPeekAllOnEmpty(DSLinkedList<String> ll) {
        if ( !ll.isEmpty() ) {
            outln("can't attempt - isEmpty() is not returning true", false);
            return;
        }

        Node<String>[] nodes = ll.peekAll();
        if ( nodes == null ) {
            outln("peekAll() returned null, expected a zero-length array", false);
        } else if ( nodes.length > 0 ) {
            outln("peekAll() returned an array with length=" + nodes.length +
                ", expected a zero-length array", false);
        } else {
            outln("peekAll() returned a zero-length array", true);
        }
    }

    protected void confirmPeekAll(DSLinkedList<String> ll,
                                  String... expectedPayloads) {

        if ( ObjectTools.isEmpty(expectedPayloads) && ll.isEmpty() ) {
            confirmPeekAllOnEmpty(ll);
            return;
        }
        outln("peekAll()...");
        Node<String>[] nodes = ll.peekAll();
        String[] resultPayloads = extractPayloads(nodes);
        checkArrayMatch(resultPayloads, expectedPayloads);
    }

    protected void checkArrayMatch(String[] results,
                                   String[] expecteds) {

        if ( ObjectTools.isSameArray(results, expecteds) ) {
            String suffix = results.length > 100
                ? "[too many to list here]"
                : "[" + StringTools.formatCommaDelimited(results) + "]";
            outln("all " + results.length +
                " items match what is expected " + suffix, true);
        } else {
            outln("mismatch on Result items and Expected items in array:", false);
            outln("    number of items", results.length, expecteds.length);

            int lenOfLonger = Math.max(results.length, expecteds.length);
            boolean printMatchesToo = expecteds.length <= 100;
            outln(String.format(
                "    %5.5s %-20.20s %-20.20s", "Index", "Result", "Expected"));
            outln(String.format(
                "    %5.5s %-20.20s %-20.20s", "-----", "-----", "---------"));
            for ( int i = 0; i < lenOfLonger; i++ ) {
                boolean hasResult = i < results.length;
                boolean hasExpected = i < expecteds.length;
                boolean matches =
                    hasResult &&
                    hasExpected &&
                    ObjectTools.isSame(results[i], expecteds[i]);

                boolean printLine = printMatchesToo || !matches;
                if ( printLine ) {
                    String text = String.format("    %5d %-20.20s %-20.20s",
                        i,
                        hasResult ? StringTools.quoteWrap(results[i]) : "",
                        hasExpected ? StringTools.quoteWrap(expecteds[i]) : ""
                        );

                    if ( matches ) {
                        outln(text);
                    } else {
                        outlnErrorText(text);
                    }
                }
            }
        }
    }
}
