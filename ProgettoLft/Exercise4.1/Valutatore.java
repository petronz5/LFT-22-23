import java.io.*;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) {
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
	      throw new Error("near line " + lex + ": " + s);
    }


    /**
     * 
     * @param t
     * Controllo del carattere altrimenti chiama un errore
     */
    void match(int t) {
	    if (look.tag == t) {
	      if (look.tag != Tag.EOF ) 
          move();
	    } 
      else error("syntax error");
    }



    /**
     * Inizio delle procedure (prima dello svolgimento di ogni procedura, 
     * troverete commentato il rispettivo insieme guida)
     */


    //START ::== EXPR EOF:  '(' , NUM
    public void start() {
      int expr_val;
      switch(look.tag){
        case '(' , Tag.NUM:
          expr_val = expr();
          match(Tag.EOF);
          System.out.println(expr_val);
          break;

        default:
          error("error in start production");
      }
    }


    //EXPR ::== TERM EXPRP:  '(' , NUM
    private int expr() {
        int term_val , exprp_val = 0;
        switch (look.tag) {
          case '(' , Tag.NUM:
            term_val = term();
            exprp_val = exprp(term_val);
            break;

          default:
            error("error in expr production");
        }

        return exprp_val;
    }


    //EXPRP ::== + TERM EXPRP:  '+'
    //EXPRP ::== - TERM EXPRP:  '-'
    //EXPRP ::== epsilon :  ')'  EOF
    private int exprp(int exprp_i) {
      int term_val, exprp_val = 0;

        switch(look.tag){
          case '+':
            match('+');
            term_val = term();
            exprp_val = exprp(exprp_i + term_val);
            break;

          case '-':
            match('-');
            term_val = term();
            exprp_val = exprp(exprp_i - term_val);
            break;

             
          case Tag.EOF:
            match(Tag.EOF);
            return exprp_i;
            
          
          default:
            return exprp_i;
        }
        return exprp_val;
    }


    //TERM ::== FACT TERMP  '(' , NUM
    private int term() {

        int fact_val, termp_val = 0;
        switch(look.tag){
          case '(' , Tag.NUM:
            fact_val = fact();
            termp_val = termp(fact_val);
            break;

          default:
            error("error in term production");
        }

        return termp_val;
    }



    //TERMP ::== * FACT TERMP :  '*'
    //TERMP ::== / FACT TERMP :  '/'
    //TERMP ::== epsilon : '+'  '-'  ')'  EOF
    private int termp(int termp_i) {

      int termp_val = 0 , fact_val;
      switch(look.tag){
          case '*':
            match('*');
            fact_val = fact();
            termp_val = termp(termp_i * fact_val);
            break;

          case '/':
            match('/');
            fact_val = fact();
            termp_val = termp(termp_i / fact_val);
            break;

          case Tag.EOF :
            match(Tag.EOF);
            return termp_i;
            
          default:
            return termp_i;
        
        }
        return termp_val;
    }

    //FACT ::== (EXPR) : '('
    //FACT ::== NUM :  NUM
    private int fact() {
      int fact_val = 0;

        switch (look.tag)
        {
          case '(':
            match('(');
            fact_val = expr();
            match(')');
            break;

          case Tag.NUM:
            fact_val = ((NumberTok)look).N;
            match(Tag.NUM);
            break;

          default:
            error("Error in fact production");

        }
        return fact_val;
      }


    public static void main(String[] args) {
        Lexer lex = new Lexer();
        //...Inserire il path...
        String path = "/Users/davide/ProgettoLft/Exercise4.1/Test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore val = new Valutatore(lex, br);
            val.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
