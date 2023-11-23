public class MatRev {
    
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);

            switch(state){
                case 0:
                    if(ch >= 'a' && ch <= 'k' || ch >= 'A' && ch <= 'K')
                        state = 1;
                    else if(ch >= 'l' && ch <= 'z' || ch >= 'L' && ch <= 'Z')
                        state = 2;
                    else
                        state = -1;
                    break;
                
                case 1:
                    if(Character.isLetter(ch) || ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
                        state = 1;
                    else if(ch == '2' || ch == '4' || ch == '6' || ch == '8' || ch == '0')
                        state = 3;
                    else
                        state = -1;
                    break;

                case 2:
                    if(Character.isLetter(ch) || ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 2;
                    else if(ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
                        state = 4;
                    else 
                        state = -1;
                    break;

                case 3:
                    if(ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 3;
                    else if(ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
                        state = 1;
                    else
                        state = -1;
                    break;

                case 4:
                    if(ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
                        state = 4;
                    else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 2;
                    else
                        state = -1;

            }
        }
        return state == 3 || state == 4;
    }

    public static void main(String[] args) {
        
        //now true
        System.out.println(scan("Bianchi123456") ? "OK" : "NOPE");
        System.out.println(scan("Rossi654321") ? "OK" : "NOPE");

        //now false
        System.out.println(scan("Bianchi654321") ? "OK" : "NOPE");
        System.out.println(scan("Rossi123456") ? "OK" : "NOPE");

        //now true
        System.out.println(scan("Bianchi2") ? "OK" : "NOPE");
        System.out.println(scan("B122") ? "OK" : "NOPE");

        //now false
        System.out.println(scan("654322") ? "OK" : "NOPE");
        System.out.println(scan("Rossi") ? "OK" : "NOPE");
        
    }
}
