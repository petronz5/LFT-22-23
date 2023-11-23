import java.io.*; 


public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    
    //funzione che legge un singolo carattere
    private void readch(BufferedReader br) { 
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    
    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }
        
        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;

            case '(':
                peek = ' ';
                return Token.lpt;

            case ')':
                peek = ' ';
                return Token.rpt;
            
            case '[':
                peek = ' ';
                return Token.lpq;

            case ']':
                peek = ' ';
                return Token.rpq;

            case '{':
                peek = ' ';
                return Token.lpg;

            case '}':
                peek = ' ';
                return Token.rpg;

            case '+':
                peek = ' ';
                return Token.plus;

            case '-':
                peek = ' ';
                return Token.minus;

            case '*':
                peek = ' ';
                return Token.mult;

            case '/':
                peek = ' ';
                return Token.div;

            case ';':
                peek = ' ';
                return Token.semicolon;

            case ',':
                peek = ' ';
                return Token.comma;

	// ... gestire i casi di ( ) { } + - * / ; , ... //
	
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }

            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                }

            case '<':
                readch(br);
                if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                }
                else if(peek == '='){
                    peek = ' ';
                    return Word.le;
                }
                else if(peek == ' '){
                    peek = ' ';
                    return Word.lt;
                }
                else {
                    System.err.println("Erroneous character"
                            + " after < : "  + peek );
                    return null;
                }

                case '>':
                readch(br);
                if (peek == ' ') {
                    peek = ' ';
                    return Word.gt;
                }
                else if(peek == '='){
                    peek = ' ';
                    return Word.ge;
                }
                else {
                    System.err.println("Erroneous character"
                            + " after > : "  + peek );
                    return null;
                }

                case '=':
                    readch(br);
                    if(peek == '='){
                        peek = ' ';
                        return Word.eq;
                    }
                    else{
                        System.err.println("Erroneous character after = : " +peek);
                        return null;
                    }

        // ... gestire i casi di || < > <= >= == <> ... //
          


            case (char)-1:
                return new Token(Tag.EOF);

                
            default:
                if (Character.isLetter(peek) || peek == '_') {

                    String keyword = "";
                    do{
                        keyword += peek;
                        readch(br);

                    }while(peek != (char) -1 && (Character.isLetterOrDigit(peek) || peek == '_'));

                    if(keyword.equals("assign"))
                        return new Word(Tag.ASSIGN , "assign");
                    else if(keyword.equals("to"))
                        return new Word(Tag.TO , "to");
                    else if(keyword.equals("conditional"))
                        return new Word(Tag.COND , "conditional");
                    else if(keyword.equals("option"))
                        return new Word(Tag.OPTION , "option");
                    else if(keyword.equals("else"))
                        return new Word(Tag.ELSE, "else");
                    else if(keyword.equals("while"))
                        return new Word(Tag.WHILE , "while");
                    else if(keyword.equals("begin"))
                        return new Word(Tag.BEGIN , "begin");
                    else if(keyword.equals("end"))
                        return new Word(Tag.END , "end");
                    else if(keyword.equals("print"))
                        return new Word(Tag.PRINT , "print");
                    else if(keyword.equals("read"))
                        return new Word(Tag.READ , "read");
                    else
                        return new Word(Tag.ID , keyword);
                }
                        
	// ... gestire il caso degli identificatori e delle parole chiave //
    

                else if (Character.isDigit(peek)) {
                    int num = 0;
                    do{
                        if(Character.isDigit(peek))
                            num = (num * 10) + Character.getNumericValue(peek);
                        readch(br);
                    }while(peek != (char) -1 && Character.isDigit(peek));

                    return new NumberTok(num);
                } 
        }   //end of the switch
            return null;
    }   //end of the method

	

		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/Users/davide/ProgettoLft/Exercise2.1/Prova.txt"; // il percorso del file da leggere
        try {
        BufferedReader br = new BufferedReader(new FileReader(path));
        Token tok;
            do {
                tok = lex.lexical_scan(br);
                
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}


