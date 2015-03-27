package tutor1;

import java.io.IOException;

/**
 * tutor 1 just a cradle
 * @author lucienSun
 *
 */
public class Cradle {
    // Constant Declarations 
    private final static char TAB = '\t';
    // Variable Declarations
    /**
     * Lookahead Character
     */
    private char lookAhead;
    
    /**
     * 
    * @Title: getChar 
    * @Description:  Read New Character From Input Stream
    * @param @throws IOException    设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void getChar() {
        try {
            lookAhead = (char)System.in.read();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * 
    * @Title: Error 
    * @Description: Report an Error 
    * @param     设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void error(String str) {
        System.out.println("\nError: " + str + ".");
    }
    
    /**
     * 
    * @Title: Abort 
    * @Description: Report Error and Halt
    * @param     设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void abort(String str) {
        error(str);
        System.exit(1);
    }
    
    /**
     * 
    * @Title: Expected 
    * @Description: Report What Was Expected
    * @param @param str    设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void expected(String str) {
        abort(str + " expected");
    }
    
    /**
     * 
    * @Title: match 
    * @Description: Match a Specific Input Character
    * @param @param x    设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void match(char x) {
        if (lookAhead == x) {
            getChar();
        } else {
            expected(" ' " + x + " ' ");
        }
    }
    
    /**
     * 
    * @Title: isAlpha 
    * @Description: Recognize an Alpha Character
    * @param @return    设定文件 
    * @return boolean    返回类型 
    * @throws
     */
    public boolean isAlpha(char c) {
        if ((c >= 'A' && c <= 'Z') ||(c >= 'a' && c <= 'z')) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 
    * @Title: isDigit 
    * @Description: Recognize a Decimal Digit
    * @param @param i
    * @param @return    设定文件 
    * @return boolean    返回类型 
    * @throws
     */
    public boolean isDigit(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 
    * @Title: getName 
    * @Description: Get an Identifier
    * @param @return    设定文件 
    * @return char    返回类型 
    * @throws
     */
    public char getName() {
        char ch = lookAhead;
        
        if (!isAlpha(ch)) {
            expected("Name");
        } else {
            getChar();    
        }
        return ch;
    }
    
    /**
     * 
    * @Title: getNum 
    * @Description: Get a Number
    * @param @return    设定文件 
    * @return char    返回类型 
    * @throws
     */
    public char getNum() {
        char ch = lookAhead;
        if (!isDigit(ch)) {
            expected("Integer");
        } else {
            getChar();
        }
        return ch;
    }
    
    /**
     * 
    * @Title: emit 
    * @Description: Output a String with TAB
    * @param @param str    设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void emit(String str) {
        System.out.print(TAB + str);
    }
    
    /**
     * 
    * @Title: emitLn 
    * @Description: Output a String with TAB and CRLF
    * @param @param str    设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void emitLn(String str) {
        emit(str);
        System.out.println("");
    }
    
    /**
     * 
    * @Title: init 
    * @Description: Initialize
    * @param     设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void init() {
        getChar();
    }
    
    /**
     * 
    * @Title: main 
    * @Description: Main Program(aka entry point)
    * @param     设定文件 
    * @return void    返回类型 
    * @throws
     */
    public static void main(String[] args) {
        Cradle cradle = new Cradle();
        cradle.init();
    }
}
