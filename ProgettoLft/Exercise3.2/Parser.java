import java.io.*;


public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }


    /**
     * Stampa il token: "token = <...>"
     */
    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }


    /**
     * 
     * @param s: Stringa dell'errore
     * 
     */
    void error(String s) {
	throw new Error("near line " + lex.line + ": " + s);
    }


    /**
     * 
     * @param t
     * Controllo del carattere altrimenti chiama un errore
     */
    void match(int t) {
	if (look.tag == t) {
	    if (look.tag != Tag.EOF ) move();
	} else error("syntax error");
    }




    /**
     * Inizio delle procedure (prima dello svolgimento di ogni procedura, 
     * troverete commentato il rispettivo insieme guida)
     */

    

    //<PROG> ::== <STATLIST> EOF:  Assign , Print , Read , While , Cond , '{'
    public void prog(){
        if(look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ
        || look.tag == Tag.WHILE || look.tag == Tag.COND || look.tag == '{' ){
            statlist();
            match(Tag.EOF);
        }
        else
            error("Error in prog production");
    }


    //<STATLIST> ::== <STAT> <STATLISTP>:  Assign , Print , Read , While , Cond , '{'
    private void statlist() {
        if(look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ
        || look.tag == Tag.WHILE || look.tag == Tag.COND || look.tag == '{' ){
            stat();
            statlistp();
        }

        else
            error("Error in statlist production");
    }


    //<STATLISTP> ::== ; <STAT> <STATLISTP>:  ';'
    //<STATLISTP> ::== epsilon:  EOF , }
    private void statlistp() {
	
        if(look.tag == ';'){
            match(';');
            stat();
            statlistp();
        }
        else if(look.tag == Tag.EOF || look.tag == '}')
            return;
    
        else
            error("Error in statlistp production");
    }


    //<STAT> ::== assign <EXPR> to <IDLIST>: assign
    //<STAT> ::== print <[EXPRLIST]>: print
    //<STAT> ::== read <[IDLIST]>: read
    //<STAT> ::== where <(BEXPR)> <STAT> : where
    //<STAT> ::== conditional <[OPTLIST]> <STATP> : conditional
    //<STAT> ::== <{STATLIST}>: {
    private void stat() {
       
        switch(look.tag){

            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist();
                break;

            case Tag.PRINT: 
                match(Tag.PRINT);
                match('[');
                exprlist();
                match(']');
                break;

            case Tag.READ:
                match(Tag.READ);    
                match('[');
                idlist();
                match(']');
                break;

            case Tag.WHILE:
                match(Tag.WHILE);    
                match('(');
                bexpr();
                match(')');
                stat();
                break;

            case Tag.COND:
                match(Tag.COND);
                match('[');
                optlist();
                match(']');
                statp();
                match(Tag.END);
                break;

            case '{':
                match('{');
                statlist();
                match('}');
                break;

            default:
                 error("Error in stat production");
                    
        }
    }


    //<STATP> ::== end : end
    //<STATP> ::== else <STAT> end : else
    private void statp(){
        if(look.tag == Tag.ELSE){
            match(Tag.ELSE);
            stat();
        }
        else if(look.tag == Tag.END){
            return;
        }
        else
            error("error in statp production");
    }



    //<IDLIST> ::== id <IDLISTP> : id
    private void idlist() { 
        
        if(look.tag == Tag.ID){
            match(Tag.ID);
            idlistp();
        }
        else
            error("Error in idlist production");
    }


    //<IDLISTP> ::== , id <IDLISTP> : ,
    //<IDLISTP> ::== epsilon :  } ] ; end option EOF
    private void idlistp(){
        if(look.tag == ','){
            match(',');
            match(Tag.ID);
            idlistp();
        }else if(look.tag == '}' || look.tag == ']' || look.tag == ';' || look.tag == Tag.END || look.tag == Tag.OPTION || look.tag == Tag.EOF)
            return;
        else
            error("Error in idlistp production");
    }


    //<OPTLIST> ::== <OPTITEM> <OPTLISTP> : option
    private void optlist(){
        if(look.tag == Tag.OPTION){
            optitem();
            optlistp();

        }
        else
            error("Error in optlist production");
    }


    //<OPTLISTP> ::== <OPTITEM> <OPTLISTP> : option
    //<OPTLIST> ::== epsilon: ]
    private void optlistp(){
        if(look.tag == Tag.OPTION){
            optitem();
            optlistp();
        }
        else if(look.tag == ']')
            return;
        else
            error("Error in optlistp production");
    }


    //<OPTITEM> ::== option <(BEXPR)> do <STAT> : option
    private void optitem(){
        if(look.tag == Tag.OPTION){
            match(Tag.OPTION);
            match('(');
            bexpr();
            match(')');
            match(Tag.DO);
            stat();
        }
        else
            error("Error in optitem production");
    }



    //<BEXPR> ::== relop <EXPR> <EXPR> : relop
    private void bexpr(){
        if(look.tag == Tag.RELOP){
            match(Tag.RELOP);
            expr();
            expr();
        }
        else
            error("Error in bexpr production");
    }


    //<EXPR> ::== + <(EXPRLIST)> : +
    //<EXPR> ::== * <(EXPRLIST)> : *
    //<EXPR> ::== - <EXPR> <EXPR> : -
    //<EXPR> ::== / <EXPR> <EXPR> : /
    //<EXPR> ::== ID : id
    //<EXPR> ::== NUM : num
    private void expr() {
        switch(look.tag){
            case '+':
                match('+');
                match('(');    
                exprlist();
                match(')');
                break;
            case '-':
                match('-');
                expr();
                expr();    
                break;

            case '*':
                match('*');
                match('(');    
                exprlist();
                match(')');
                break;

            case '/':
                match('/');
                expr();
                expr();    
                break;  

            case Tag.NUM:
                match(Tag.NUM);    
                break;

            case Tag.ID:
                match(Tag.ID);
                break;

            default:
                 error("Error in expr production");
            
        }
    }

    
    //<EXPRLIST> ::== <EXPR> <EXPRLISTP> :  +  -  *  /  num  id
    private void exprlist(){
        if(look.tag == '+' || look.tag == '-' || look.tag == '*' || look.tag == '/' || look.tag == Tag.NUM || look.tag == Tag.ID){
            expr();
            exprlistp();
        }
        else
            error("Error in exprilist production");
    }


    //<EXPRLISTP> ::== , <EXPR> <EXPRLISTP> :  ,
    //<EXPRLIST> ::== epsilon : ] )
    private void exprlistp(){
        if(look.tag == ','){
            match(',');
            expr();
            exprlistp();

        }
        else if(look.tag == ']' || look.tag == ')' )
            return;
        else
            error("Error in exprilistp production");
    }
    
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        //...Inserire il path...
        String path = "/Users/davide/ProgettoLft/Exercise3.2/Test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}