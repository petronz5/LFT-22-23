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

    

    //START ::== EXPR EOF:  '(' , NUM
    public void start() {
	if(look.tag == '(' || look.tag == Tag.NUM){
        expr();
        match(Tag.EOF);
    }
	else
        error("Syntax error");
    }

    //EXPR ::== TERM EXPRP:  '(' , NUM
    private void expr() {
        if(look.tag == '(' || look.tag == Tag.NUM){
            term();
            exprp();
        }
        else 
            error("");
    }

   
   //EXPRP ::== + TERM EXPRP:  '+'
   //EXPRP ::== - TERM EXPRP:  '-'
   //EXPRP ::== epsilon :  ')'  EOF

    private void exprp() {
	
        if(look.tag == '+'){
                match('+');
                term();
                exprp();
        }
        else if(look.tag == '-'){
                match('-');
                term();
                expr();
                
        }
        else if(look.tag == ')' || look.tag == Tag.EOF){
            return;
        }
        else
            error("");
    }


    //TERM ::== FACT TERMP  '(' , NUM
    private void term() {
        if(look.tag == '(' || look.tag == Tag.NUM){
            fact();
            termp();
        }
        else
            error("");
    }


    //TERMP ::== * FACT TERMP :  '*'
    //TERMP ::== / FACT TERMP :  '/'
    //TERMP ::== epsilon : '+'  '-'  ')'  EOF
    private void termp() {
        
        if(look.tag == '*'){
                match('*');
                fact();
                termp();
                
        }
        else if(look.tag == '/'){
        
                match('/');
                fact();
                termp();
        }

        else if(look.tag == '+' || look.tag == '-' || look.tag == ')' || look.tag == Tag.EOF){
            return;
        }
        else
            error("");
    }


    //FACT ::== (EXPR) : '('
    //FACT ::== NUM :  NUM
    private void fact() {
        if(look.tag == '(' || look.tag == Tag.NUM){

            switch(look.tag){
                case '(':    
                    match('(');
                    expr();
                    match(')');
                    break;

                case Tag.NUM:
                    match(Tag.NUM);
                    break;

                default:
                    error("null");
            }
        }
    }
    
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        //...Inserire il path...
        String path = "/Users/davide/ProgettoLft/Exercise3.1/Test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}