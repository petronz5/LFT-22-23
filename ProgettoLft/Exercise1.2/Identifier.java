
public class Identifier {
    
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);

            switch(state){
                case 0:
                    if(ch == '_')
                        state = 1;
                    else if(Character.isLetter(ch))
                        state = 2;
                    else
                        state = -1;
                    break;
                
                case 1:
                    if(ch == '_')
                        state = 1;
                    else if(Character.isDigit(ch) || Character.isLetter(ch))
                        state = 2;
                    else
                        state = -1;
                    break;

                case 2:
                    if(ch == '_' || Character.isLetter(ch) || Character.isDigit(ch))
                        state = 2;
                    else
                        state = -1;

            }
        }
        return state == 2;
    }

    public static void main(String[] args) {
        
        System.out.println(scan("x") ? "OK" : "NOPE");
        System.out.println(scan("flag1") ? "OK" : "NOPE");
        System.out.println(scan("x2y2") ? "OK" : "NOPE");
        System.out.println(scan("x_1") ? "OK" : "NOPE");
        System.out.println(scan("lft_lab") ? "OK" : "NOPE");
        System.out.println(scan("_temp") ? "OK" : "NOPE");
        System.out.println(scan("x_1_y_2") ? "OK" : "NOPE");
        System.out.println(scan("x___") ? "OK" : "NOPE");
        System.out.println(scan("__5") ? "OK" : "NOPE");

        //now all the results are false
        System.out.println(scan("5") ? "OK" : "NOPE");
        System.out.println(scan("221B") ? "OK" : "NOPE");
        System.out.println(scan("123") ? "OK" : "NOPE");
        System.out.println(scan("___") ? "OK" : "NOPE");
    }
}
