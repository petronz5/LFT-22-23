import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    //Costructor
    public Translator(Lexer l, BufferedReader br) {
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
          if (look.tag != Tag.EOF )
            move();
        }
        else error("syntax error");
    }


   
    /**
     * Inizio delle procedure (prima dello svolgimento di ogni procedura, 
     * troverete commentato il rispettivo insieme guida)
     */
   
   

    //<PROG> ::== <STATLIST> EOF:  Assign , Print , Read , While , Cond , '{'
    public void prog() {
      if(look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ
      || look.tag == Tag.WHILE || look.tag == Tag.COND || look.tag == '{' ){
        
        int lnext_prog = code.newLabel(); //creazione etichetta
        statlist(lnext_prog);
        code.emit(OpCode.GOto, lnext_prog);
        code.emitLabel(lnext_prog);   //stampo l'ultima label   
        match(Tag.EOF);
        try {
        	code.toJasmin();
        }
        catch(java.io.IOException e) {
        	System.out.println("IO error\n");
        };
      }
      else
        error("error in prog production");
    }


    //<STATLIST> ::== <STAT> <STATLISTP>:  Assign , Print , Read , While , Cond , '{'
    private void statlist(int lnext_statlist) {   //viene passata l'etichetta a stat() in caso di salto
      if(look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ
      || look.tag == Tag.WHILE || look.tag == Tag.COND || look.tag == '{' ){
  
          stat(lnext_statlist);
          statlistp(lnext_statlist);
          }
          else
              error("Error in statlist production");
      }
    
    
    //<STATLISTP> ::== ; <STAT> <STATLISTP>:  ';'
    //<STATLISTP> ::== epsilon:  EOF , }
    private void statlistp(int lnext_statlistp) {  
      if(look.tag == ';'){
        match(';');       //Faccio il match poi passsare ;
        stat(lnext_statlistp);
        statlistp(lnext_statlistp);
    }
    //GUIDA[<statlistp>] ::== ε]
    else if(look.tag == Tag.EOF || look.tag == '}')
        return;

    else
        error("Error in statlistp production");
  }





    private void stat(int lnext_stat) {
        switch(look.tag) {

          //<STAT> ::== assign <EXPR> to <IDLIST>: assign
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist();
                break;

            //<STAT> ::== print <[EXPRLIST]>: print
            case Tag.PRINT:     
                match(Tag.PRINT);
                match('[');
                exprlist(Tag.PRINT);
                match(']');
                
                break;

            //<STAT> ::== read <[IDLIST]>: read
            case Tag.READ:
                code.emit(OpCode.invokestatic, 0);
                match(Tag.READ);
                match('[');
	              idlist();
                match(']');
                break;

            //<STAT> ::== where <(BEXPR)> <STAT> : where
            case Tag.WHILE:
            match(Tag.WHILE);
            match('(');
            int whileTrue = code.newLabel();  //etichetta per la condizione del while
            int whileInside = code.newLabel();   //etichetta per entrare nel corpo del while
            int next_instruction = code.newLabel(); //etichetta per le istruzione dopo il while

            code.emitLabel(whileTrue); // emetto label per la condizione while
            
            bexpr(whileInside, next_instruction); // vado in bexpr ed emetto if_icmp con etichetta true e sotto un goto con etichetta falsa
            match(')');
            code.emitLabel(whileInside); // stampa l'etichetta per le istruzioni dentro il while
            
            stat(next_instruction);
            code.emit(OpCode.GOto, whileTrue); // stampa goto per tornare all'inizio del ciclo
            code.emitLabel(next_instruction);
            break;

            //<STAT> ::== conditional <[OPTLIST]> <STATP>: conditional
            case Tag.COND:
            int cond_false = code.newLabel();   //creo etichetta per saltare condizione falsa
            int after_cond = code.newLabel();    //creo etichetta per le istruzioni dopo il ciclo 
            match(Tag.COND);           
            match('['); 
            optlist(cond_false);
            match(']'); 
            code.emit(OpCode.GOto, after_cond); //stampa il goto all'etichetta x
            

            code.emitLabel(cond_false);     //etichetta che chiama il conditional false
			      statp(lnext_stat, cond_false , after_cond );
            code.emit(OpCode.GOto , after_cond);
            code.emitLabel(after_cond);
            
            break;

            //<STAT> ::== <{STATLIST}>: {
            case '{':
                  match('{');
                  statlist(lnext_stat);
                  match('}');
                  break;

            default:
              error("Error in stat production");
        }
     }



    

    //<STATP> ::== end : end
    //<STATP> ::== else <STAT> end : else
    private void statp(int statp_next, int cond_false , int after_cond){
        if(look.tag == Tag.ELSE){
          match(Tag.ELSE);
          stat(statp_next);          
          match(Tag.END);
          
        }

        else if(look.tag == Tag.END)
          match(Tag.END);
        
        else
          error("error in statp production");
    }


    //<IDLIST> ::== id <IDLISTP> : id
    private void idlist() {
        switch(look.tag) {
	        
          case Tag.ID:
        	int id_addr = st.lookupAddress(((Word)look).lexeme);  //assegna ad id_addr l'indirizzo dell'identificatore se è presente
                if (id_addr==-1) {  // se id_addr è -1 vuol dire che non è  presente e lo aggiugne alla mappa
                    id_addr = count;
                    st.insert(((Word)look).lexeme,count++);
                }
                match(Tag.ID);
                  code.emit(OpCode.istore, id_addr);
                  idlistp(id_addr);
                break;
          
          default:
                error("error in idlist production");
    	}
    }



    //<IDLISTP> ::== , id <IDLISTP> : ,
    //<IDLISTP> ::== epsilon :  } ] ; end option EOF
    private void idlistp(int id_addr_istore_idlist){
      
      
        if(look.tag == ','){

          match(',');
          int id_addr = st.lookupAddress(((Word)look).lexeme);  //assegna ad id_addr l'indirizzo dell'identificatore

          if (id_addr==-1) {    // se id_addr è -1 vuol dire che non è  presente e lo aggiugne alla mappa
              id_addr = count;
              st.insert(((Word)look).lexeme,count++);
          }

            match(Tag.ID);
            if(id_addr_istore_idlist == Tag.READ)  //siccome read == true stampo invokestatic 
              code.emit(OpCode.invokestatic, 0);
            if(id_addr_istore_idlist == Tag.ASSIGN)
              code.emit(OpCode.iload, id_addr_istore_idlist); /// serve per fare più assign in idlistp
              
            code.emit(OpCode.istore, id_addr);
            idlistp(id_addr_istore_idlist);
            
      }
      else if(look.tag == '}' || look.tag == ']' || look.tag == ';' || look.tag == Tag.END || look.tag == Tag.OPTION || look.tag == Tag.EOF)
          return;
      else
          error("Error in idlistp production");
    }
    

    //<OPTLIST> ::== <OPTITEM> <OPTLISTP> : option
    private void optlist(int cond_false){
      if(look.tag == Tag.OPTION){
        optitem(cond_false);
        optlistp(cond_false);
      } 
      else
        error("Error in optlist production");
    }



    //<OPTLISTP> ::== <OPTITEM> <OPTLISTP> : option
    //<OPTLIST> ::== epsilon: ]
    private void optlistp(int cond_false){
      if(look.tag == Tag.OPTION){
        optitem(cond_false);
        optlistp(cond_false);
    }
    else if(look.tag == ']')
        return;
    else
        error("Error in optlistp production");
    }
    
    
    //<OPTITEM> ::== option <(BEXPR)> do <STAT> : option
    private void optitem(int cond_false){
      if(look.tag == Tag.OPTION){
        int cond_true = code.newLabel();  //creo etichetta per la condizione true
        match(Tag.OPTION);
        match('(');
        bexpr(cond_true , cond_false);  //chiamo bexpr sulla condizione true o false
        match(')');
        match(Tag.DO);
        code.emitLabel(cond_true);
        stat(cond_true);

    }
    else
        error("Error in optitem production");
    }





    //<BEXPR> ::== relop <EXPR> <EXPR> : relop
    /**
     * 
     * @param label_true
     * @param label_false
     * In bexpr quando scrivo relop <EXPR> <EXPR> bisogna sempre lasciare uno spazio
     * tra i caratteri, tipo così: (> 10 5)
     */
    private void bexpr(int label_true , int label_false){
      if(look.tag == Tag.RELOP){
          String relop = ((Word)look).lexeme;
          match(Tag.RELOP);
          expr();
          expr();

          switch(relop){
            case ">":
              code.emit(OpCode.if_icmpgt , label_true);
              break;

            case "<":
              code.emit(OpCode.if_icmplt , label_true);
              break;

            case ">=":
              code.emit(OpCode.if_icmpge , label_true);
              break;

            case "<=":
              code.emit(OpCode.if_icmple , label_true);
              break;

            case "<>":
              code.emit(OpCode.if_icmpne , label_true);
              break;

            case "==":
              code.emit(OpCode.if_icmpeq , label_true);
              break;

            default:
              error("error insert relop sign");
          }

          code.emit(OpCode.GOto, label_false);
    }
    else
        error("Error in bexpr production");
    }


    
    private void expr() {
        switch(look.tag) {
          //<EXPR> ::== + <(EXPRLIST)> : +
            case '+':
              match('+');
              match('(');
              exprlist('+');
              match(')');
              break;

            //<EXPR> ::== - <EXPR> <EXPR> : -
            case '-':
              match('-');
              expr();
              expr();
              code.emit(OpCode.isub);   //funzione richiamata da CodeGenerator.java
              break;

            //<EXPR> ::== * <(EXPRLIST)> : *
            case '*':
              match('*');
              match('(');
              exprlist('*');
              match(')');
              break;

            //<EXPR> ::== / <EXPR> <EXPR> : /
            case '/':
              match('/');
              expr();
              expr();
              code.emit(OpCode.idiv);   //funzione richiamata da CodeGenerator.java
              break;

            //<EXPR> ::== NUM : num
            case Tag.NUM:
              code.emit(OpCode.ldc, ((NumberTok)look).N);
              match(Tag.NUM);
              break;
            
            //<EXPR> ::== ID : id
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr == -1) {  
                    id_addr = count;
                    st.insert(((Word)look).lexeme,count++);
                }

                match(Tag.ID);
                code.emit(OpCode.iload, id_addr);
                break;

            default:
              error("error in expr production");
        }
    }


    //<EXPRLIST> ::== <EXPR> <EXPRLISTP> :  +  -  *  /  num  id
    private void exprlist(int op){
      if(look.tag == '+' || look.tag == '-' || look.tag == '*' || look.tag == '/' || look.tag == Tag.NUM || look.tag == Tag.ID){
        expr();
        exprlistp(op);
        if(op == Tag.PRINT) // serve per fare invokestatic print in exprlistp
          code.emit(OpCode.invokestatic, 1);   
    }
    else
        error("Error in exprlist production");
    }


    //<EXPRLISTP> ::== , <EXPR> <EXPRLISTP> :  ,
    //<EXPRLIST> ::== epsilon : ] )
    private void exprlistp(int op){
      if(look.tag == ','){
          match(',');
          expr();
          exprlistp(op);

        if (op == '+') 
				  code.emit(OpCode.iadd); 
			
			  if (op == '*') // caso di iadd e imul da expr, diverso da - e /
				  code.emit(OpCode.imul);

			  else if (op == Tag.PRINT) 
				  code.emit(OpCode.invokestatic,1);
			
      }
      else if(look.tag == ']' || look.tag == ')' )
          return;
      else
          error("Error in exprlistp production");
  }







    public static void main(String[] args) {
      Lexer lex = new Lexer();
      String path = "/Users/davide/ProgettoLft/Exercise5.1/test.lft"; // il percorso del file da leggere
      try {
          BufferedReader br = new BufferedReader(new FileReader(path));
          Translator tr = new Translator(lex, br);
          tr.prog();
          System.out.println("Input OK");
          br.close();
      } catch (IOException e) {e.printStackTrace();}
}
}


