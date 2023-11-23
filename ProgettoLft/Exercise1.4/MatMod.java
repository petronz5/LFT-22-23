public class MatMod {
    
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);

            switch(state){
                /**
                 * Se al termine dei numeri dovessimo finire nello stato 1 vuol dire che il numero è pari
                 * Mentre se dovessimo finire nello stato 2 vuol dire che il numero è dispari
                 */
                case 0:
                    if(ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')   //se è numero pari
                        state = 1;
                    else if(ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')    //se è numero dispari  
                        state = 2;
                    else if(ch == ' ')
                        state = 0;
                    else
                        state = -1;
                    break;
                
                case 1:
                    if(ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 1;
                    else if(ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
                        state = 2;

                    else if(ch >= 'a' && ch <= 'k' || ch >= 'A' && ch <= 'K')
                        state = 4;
                    else if (ch == ' ')
                        state = 6;
                    else
                        state = -1;
                    break;



                case 2:
                    if(ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
                        state = 2;
                    else if(ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 1;
                    else if(ch >= 'l' && ch <= 'z' || ch >= 'L' && ch <= 'Z' )
                        state = 3;
                    else if(ch == ' ')
                        state = 7;
                    else 
                        state = -1;
                    break;

                
                case 3:
                    if(Character.isLetter(ch))
                        state = 3;
                    else if(ch == ' ')
                        state = 5;
                    else
                        state = -1;
                    break;

                case 4:
                    if(Character.isLetter(ch))
                        state = 4;
                    else if(ch == ' ')
                        state = 5;
                    else
                        state = -1;
                    break;

                case 5:
                    if (ch == ' ')
                        state = 5;
                    else if(ch >= 'A' && ch <= 'Z')
                        state = 4;
                    else
                        state = -1;
                    break;


                //Stato per gestire gli spazi nel caso di numero pari
                case 6:
                    if(ch == ' ')
                        state = 6;
                    else if (ch >= 'a' && ch <= 'k' || ch >= 'A' && ch <= 'K')
                        state = 4;
                    else
                        state = -1;
                    break;

                //Stato per gestire gli spazi nel caso di numero dispari    
                case 7:
                    if(ch == ' ')
                        state = 7;
                    else if (ch >= 'l' && ch <= 'z' || ch >= 'L' && ch <= 'Z')
                        state = 3;
                    else
                        state = -1;
                    break;
            }
        }
        return state == 3 || state == 4 || state == 5;
    }

    public static void main(String[] args) {
        
        //now true
        System.out.println(scan("20Davide") ? "OK" : "NOPE");
        System.out.println(scan(" 20 Davide  ") ? "OK" : "NOPE");
        System.out.println(scan("20 Dav Ide") ? "OK" : "NOPE");

        //now false
        System.out.println(scan("20 Dav ide") ? "OK" : "NOPE");
        System.out.println(scan("2 0 Davide") ? "OK" : "NOPE");
        

        
        
    }
}
