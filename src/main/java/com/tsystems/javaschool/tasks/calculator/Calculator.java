package com.tsystems.javaschool.tasks.calculator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    private Stack<String> stack = new Stack<>();

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        if (!isValid(statement)) return null;
        if (statement.startsWith("-")) {
            statement = "0" + statement;
        }

        stack = fillStack(statement);

        String tmpRes = "";
        while (!stack.isEmpty()) {
            if (stack.peek().equals(")")) {
                stack.pop();
                String s = "";

                do {
                    s = stack.pop() + s;
                } while (!stack.peek().equals("("));

                stack.pop();
                stack.add(calc(s));
            } else if (isDigitOrDot(stack.peek()) || isOperation(stack.peek())) {
                tmpRes = stack.pop() + tmpRes;
            }
        }

        String finalRes = calc(tmpRes);
        if (finalRes == null) return null;

        if (finalRes.endsWith(".0")) {
            finalRes = finalRes.replaceAll("\\.0$", "");
        }

        return finalRes;
    }

    public boolean isValid(String s) {
        if (s == null || s.equals("")) return false;

        String[] arr = s.split("[.+*/-]");
        if (Arrays.asList(arr).contains("")) return false;

        int open = 0;
        int close = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                open++;
            }
            if (s.charAt(i) == ')') {
                close++;
            }

            if (close > open) return false;
        }

        if (open != close ||
                s.contains(",")) return false;


        return true;
    }

    public boolean isDigitOrDot(String s) {
        if (s.equals(".")) return true;
        try {
            double d = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public boolean isOperation(String s) {
        return s.matches("[+\\-/*()]");
    }

    public String calcPlusMinus(String s) {
        Stack<String> st = fillStack(s);

        Queue<String> queue = stackToQueue(st);

        double res = Double.parseDouble(queue.poll());
        while(!queue.isEmpty()) {
            if (queue.peek().equals("+")) {
                queue.poll();
                res = res + Double.parseDouble(queue.poll());
                queue.add(String.valueOf(res));
            } else if (queue.peek().equals("-")) {
                queue.poll();
                res = res - Double.parseDouble(queue.poll());
                queue.add(String.valueOf(res));
            } else if (isDigitOrDot(queue.peek())) {
                res = Double.parseDouble(queue.poll());
            }
        }
        return String.valueOf(res);
    }

    public String calc(String s) {
        String result;
        String str = "";

        Stack<String> st = fillStack(s);
        if (!st.contains("*") && !st.contains("/")) {
            return calcPlusMinus(s);
        } else if (!st.contains("+") && !st.contains("-")) {

            Queue<String> qu = stackToQueue(st);
            double res = Double.parseDouble(qu.poll());

            while (!qu.isEmpty()) {
                if (qu.peek().equals("*")) {
                    qu.poll();
                    res = res * Double.parseDouble(qu.poll());
                    str = res + str;
                } else if (qu.peek().equals("/")) {
                    qu.poll();
                    res = res / Double.parseDouble(qu.poll());
                    if (res == Double.POSITIVE_INFINITY || res == Double.NEGATIVE_INFINITY) return null;
                    str = res + str;
                } else if (isDigitOrDot(qu.peek())) {
                    res = Double.parseDouble(qu.poll());
                    if (qu.isEmpty() || qu.peek().equals("-") || qu.peek().equals("+")) {
                        str = res + str;
                        if (!qu.isEmpty()) {
                            str = qu.poll() + str;
                            res = Double.parseDouble(qu.poll());
                        }
                    }
                } else {
                    str = qu.poll() + str;
                }
            }

            if (str.contains("+") || str.contains("-") && !str.startsWith("-")) {
                result = calcPlusMinus(str);
            } else result = String.valueOf(res);

        } else {
            double res = Double.parseDouble(st.pop());

            while (!st.isEmpty()) {
                if (st.peek().equals("*")) {
                    st.pop();
                    res = res * Double.parseDouble(st.pop());
                    str = res + str;
                } else if (st.peek().equals("/")) {
                    st.pop();
                    res = Double.parseDouble(st.pop()) / res;
                    str = res + str;
                } else if (isDigitOrDot(st.peek())) {
                    res = Double.parseDouble(st.pop());
                    if (st.isEmpty() || st.peek().equals("-") || st.peek().equals("+")) {
                        str = res + str;
                        if (!st.isEmpty()) {
                            str = st.pop() + str;
                            res = Double.parseDouble(st.pop());
                        }
                    }
                } else {
                    str = st.pop() + str;
                }
            }

            if (str.contains("+") || str.contains("-") && !str.startsWith("-")) {
                result = calcPlusMinus(str);
            } else result = String.valueOf(res);
        }
        return result;
    }

    public Stack<String> fillStack(String s) {
        Stack<String> tmpStack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            if (isDigitOrDot(s.charAt(i) + "")) {
                if (!tmpStack.isEmpty() && isDigitOrDot(tmpStack.peek())) {
                    String tmp = tmpStack.pop() + s.charAt(i);
                    tmpStack.add(tmp);
                } else {
                    tmpStack.add(s.charAt(i) + "");
                }
            } else if (isOperation(s.charAt(i) + "")) {
                if (!s.contains("(") && isOperation(tmpStack.peek())) {

                    tmpStack.add("-" + s.charAt(i+1) + "");
                    i++;
                } else tmpStack.add(s.charAt(i) + "");
            }
        }
        return tmpStack;
    }

    public Queue<String> stackToQueue(Stack<String> st) {
        Queue<String> qu = new LinkedBlockingQueue<>(st);
        return qu;
    }
}
