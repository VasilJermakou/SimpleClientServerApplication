package com.vaer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

public class SocketorTest {

    /**
     * Methods name writes in conception WhenSmtThenResultIsSmt
     * Methods body writes in conception AAA (arrange, act, assert)
     * */

    /* class fields */
    String stringResult;
    int intResult;

    @Before
    public void beforeMethod(){
        stringResult = "";
        intResult = 0;
    }

    @After
    public void afterMethod(){
        stringResult = "";
        intResult = 0;
    }

    @Test
    public void calculatePlusTest(){
        stringResult = "+";
        int a = 5;
        int b = 5;

        if(stringResult.equals("+"))
            intResult = 5 + 5;

        assertThat(intResult, is(10));
    }

    @Test
    public void calculateMinusTest(){
        stringResult = "-";
        int a = 5;
        int b = 5;

        if(stringResult.equals("-"))
            intResult = 5 - 5;

        assertThat(intResult, is(0));
    }

    @Test
    public void calculateMultiplyTest(){
        stringResult = "*";
        int a = 5;
        int b = 5;

        if(stringResult.equals("*"))
            intResult = 5 * 5;

        assertThat(intResult, is(25));
    }

    @Test(expected = RuntimeException.class)
    public void fiveDividedToZeroExpectedException(){
        stringResult = "/";
        int a = 5;
        int b = 0;

        if(stringResult.equals("/"))
            intResult = 5 / 0;
    }

    @Test
    public void operationParserTest(){
        stringResult = "";
        String operation = "+*-/";
        String request = "Accept operation +, first: 5, second: 5 end \n";

        char[] charArray = request.toCharArray();
        for(int i = 0; i < request.length(); i++) {
            if(charArray[i] == operation.charAt(0) || charArray[i] == operation.charAt(1) ||
                    charArray[i] == operation.charAt(2) || charArray[i] == operation.charAt(3) ){
                stringResult += charArray[i];
                break;
            }
        }
        assertThat(stringResult, is("+"));
    }

    @Test
    public void valuesParserTest(){
        String request = "Accept operation +, first: 5, second: 5 end \n";

        StringBuffer value1 = new StringBuffer("");
        char[] charArray = request.toCharArray();

        for(int i = 0; i < request.length(); i++) {
            if(charArray[i] >= 48 && charArray[i] <= 57) {
                value1.append(charArray[i]);
                if(charArray[i+1] < 48 || charArray[i+1] > 57)
                    break;
            }
        }

        intResult = Integer.parseInt(value1.toString());
        assertThat(intResult, is(5));
    }

}//class ends
