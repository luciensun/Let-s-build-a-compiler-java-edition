package tutor7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * tutor 5 CONTROL CONSTRUCTS
 * 
 * @author lucienSun
 *
 */
public class Cradle {
    // Constant Declarations
    private final static char TAB = '\t';
    private final static char CR = '\r';
    private final static char LF = '\n';
    // Variable Declarations

    // Lookahead Character
    private char lookAhead;
    // Label Counter
    private int lCount;

    private List<String> symTab;

    private List<String> keywordList;

    private enum Symbol {
        IF_SYMBOL, ELSE_SYMBOL, ENDIF_SYMBOL, END_SYMBOL, IDENT, NUMBER, OPERATOR;
        public void write() {
            switch(this) {
            case IF_SYMBOL:
            case ELSE_SYMBOL:
            case ENDIF_SYMBOL:
            case END_SYMBOL:System.out.print("Keyword ");break;
            case IDENT:System.out.print("Ident ");break;
            case NUMBER:System.out.print("Number ");break;
            case OPERATOR:System.out.print("Operator ");break;
            }
        }
    }

    // Current Token
    private Symbol token;
    // String Token of Look
    private String tokenVal;

    /**
     * 
     * @Title: getChar
     * @Description: Read New Character From Input Stream
     * @param @throws IOException 设定文件
     * @return void 返回类型
     * @throws
     */
    public void getChar() {
        try {
            lookAhead = (char) System.in.read();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 
     * @Title: Error
     * @Description: Report an Error
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void error(String str) {
        System.out.println("\nError: " + str + ".");
    }

    /**
     * 
     * @Title: Abort
     * @Description: Report Error and Halt
     * @param 设定文件
     * @return void 返回类型
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
     * @param @param str 设定文件
     * @return void 返回类型
     * @throws
     */
    public void expected(String str) {
        abort(str + " expected");
    }

    /**
     * 
     * @Title: match
     * @Description: Match a Specific Input Character
     * @param @param x 设定文件
     * @return void 返回类型
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
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isAlpha(char c) {
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
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
     * @param @return 设定文件
     * @return boolean 返回类型
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
     * @Title: isAlNum
     * @Description: Recognize an Alphanumeric Character
     * @param @param c
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isAlNum(char c) {
        if (isAlpha(c) || isDigit(c)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @Title: isOp
     * @Description: Recognize Any Operator
     * @param @param c
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isOp(char c) {
        if (c == '+' || c == '-' || c == '*' || c == '/' || c == '<'
                || c == '>' || c == ':' || c == '=') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @Title: isBoolean
     * @Description: Recognize a Boolean Literal
     * @param @param c
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isBoolean(char c) {
        if (c == 'T' || c == 'F') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @Title: isOrOp
     * @Description: Recognize a Boolean Orop
     * @param @param c
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isOrOp(char c) {
        if (c == '|' || c == '~') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @Title: isRelOp
     * @Description: Recognize a Relop
     * @param @param c
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isRelOp(char c) {
        if (c == '=' || c == '#' || c == '<' || c == '>') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @Title: isAddop
     * @Description: Recognize an Addop
     * @param @param c
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isAddop(char c) {
        if (c == '+' || c == '-') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @Title: isWhite
     * @Description: Recognize White Space
     * @param @param c
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isWhite(char c) {
        if (c == ' ' || c == TAB) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @Title: skipWhite
     * @Description: Skip Over Leading White Space
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void skipWhite() {
        while (isWhite(lookAhead)) {
            getChar();
        }
    }

    /**
     * 
     * @Title: skipComma
     * @Description: Skip Over a Comma
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void skipComma() {
        skipWhite();
        if (lookAhead == ',') {
            getChar();
            skipWhite();
        }
    }

    /**
     * 
    * @Title: getName 
    * @Description: Get an Identifier
    * @param     设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void getName() {
        StringBuffer buffer = new StringBuffer("");
        int k;
        
        if (!isAlpha(lookAhead)) {
            expected("Name");
        }

        while (isAlNum(lookAhead)) {
            buffer.append(lookAhead);
            getChar();
        }
        tokenVal = buffer.toString();
        
        k = lookup(keywordList, tokenVal);
        if (k == -1) {
            token = Symbol.IDENT;
        } else {
            token = Symbol.values()[k];
        }

    }

    /**
     * 
    * @Title: getNum 
    * @Description: Get a Number
    * @param     设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void getNum() {
        StringBuffer buffer = new StringBuffer("");

        if (!isDigit(lookAhead)) {
            expected("Integer");
        }

        while (isDigit(lookAhead)) {
            buffer.append(lookAhead);
            getChar();
        }
        tokenVal = buffer.toString();
        token = Symbol.NUMBER;
    }

    /**
     * 
    * @Title: getOp 
    * @Description: Get an Operator
    * @param     设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void getOp() {
        StringBuffer buffer = new StringBuffer("");

        if (!isOp(lookAhead)) {
            expected("Operator");
        }

        while (isOp(lookAhead)) {
            buffer.append(lookAhead);
            getChar();
        }
        tokenVal = buffer.toString();
        token = Symbol.OPERATOR;
    }

    /**
     * 
     * @Title: getBoolean
     * @Description: Get a Boolean Literal
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean getBoolean() {
        char ch = lookAhead;
        boolean bVal = false;
        if (!isBoolean(ch)) {
            expected("Boolean Literal");
        } else {
            bVal = lookAhead == 'T';
            getChar();
        }
        return bVal;
    }

    /**
     * 
     * @Title: fin
     * @Description: Skip a CRLF
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void fin() {
        if (lookAhead == CR) {
            getChar();
        }
        if (lookAhead == LF) {
            getChar();
        }
    }

    /**
     * 
     * @Title: scanner
     * @Description: Lexical Scanner
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void scanner() {
        int k;

        // If you want your language to be truly free-field,
        // then newlines should be transparent.
        while (lookAhead == CR) {
            fin();
        }
        if (isAlpha(lookAhead)) {
            getName();
        } else if (isDigit(lookAhead)) {
            getNum();
        } else if (isOp(lookAhead)) {
            getOp();
        } else {
            tokenVal = String.valueOf(lookAhead);
            token = Symbol.OPERATOR;
            getChar();
        }
        skipComma();
    }

    /**
     * 
     * @Title: lookup
     * @Description: Symbol Table Lookup, If the input string matches a table
     *               entry, return the entry index. If not, return a negative
     *               one .
     * @param @param symTab
     * @param @param str
     * @param @return 设定文件
     * @return int 返回类型
     * @throws
     */
    public int lookup(List<String> symTab, String str) {
        int n = symTab.size();
        int i;
        for (i = n - 1; i >= 0; i--) {
            if (str.equals(symTab.get(i))) {
                break;
            }
        }
        return i;
    }

    /**
     * 
     * @Title: newLabel
     * @Description: Generate a Unique Label in the form of 'Lnn', where nn is a
     *               label number starting from zero.
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public String newLabel() {
        String str = null;
        str = String.valueOf(lCount);
        lCount++;
        return "L" + str;
    }

    /**
     * 
     * @Title: postLabel
     * @Description: Post a Label To Output
     * @param @param str 设定文件
     * @return void 返回类型
     * @throws
     */
    public void postLabel(String label) {
        System.out.println(label + ":");
    }

    /**
     * 
     * @Title: emit
     * @Description: Output a String with TAB
     * @param @param str 设定文件
     * @return void 返回类型
     * @throws
     */
    public void emit(String str) {
        System.out.print(TAB + str);
    }

    /**
     * 
     * @Title: emitLn
     * @Description: Output a String with TAB and CRLF
     * @param @param str 设定文件
     * @return void 返回类型
     * @throws
     */
    public void emitLn(String str) {
        emit(str);
        System.out.println("");
    }

    /**
     * 
     * @Title: other
     * @Description: Recognize and Translate an "Other"
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void other() {
        emitLn(String.valueOf(getName()));
    }

   
    /**
     * 
     * @Title: init
     * @Description: Initialize
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void init() {
        lCount = 0;
        symTab = new ArrayList<String>();
        keywordList = new ArrayList<String>();

        keywordList.add("IF");
        keywordList.add("ELSE");
        keywordList.add("ENDIF");
        keywordList.add("END");

        getChar();
    }

    /**
     * 
     * @Title: main
     * @Description: Main Program(aka entry point)
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public static void main(String[] args) {
        Cradle cradle = new Cradle();
        cradle.init();

        do {
            cradle.scanner();
            cradle.token.write();
            System.out.println("output is:-->" + cradle.tokenVal);
        } while (cradle.token != Symbol.END_SYMBOL);

    }
}
