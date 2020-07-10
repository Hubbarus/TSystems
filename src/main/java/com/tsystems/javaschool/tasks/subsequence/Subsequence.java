package com.tsystems.javaschool.tasks.subsequence;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Subsequence {
    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    public boolean find(List x, List y) {
        if (x == null || y == null) throw new IllegalArgumentException();

        Queue<Object> q1 = new LinkedBlockingQueue<>();
        Queue<Object> q2 = new LinkedBlockingQueue<>();

        q1.addAll(x);
        q2.addAll(y);

        while (!q1.isEmpty()) {
            if (q1.peek().equals(q2.peek())) {
                q1.poll();
                q2.poll();
            } else if (!q2.isEmpty()){
                q2.poll();
            } else break;
        }

        return q1.isEmpty();
    }
}
