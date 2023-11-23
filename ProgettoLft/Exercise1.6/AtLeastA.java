public class AtLeastA {
    
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);

            switch(state){
                case 0:
                    if(ch == 'a')
                        state = 1;
                    else if(ch == 'b')
                        state = 2;
                    else
                        state = -1;
                    break;
                
                case 1:
                    if(ch == 'a')
                        state = 1;
                    else if(ch == 'b')
                        state = 3;
                    else
                        state = -1;
                    break;

                case 2:
                    if(ch == 'a')
                        state = 1;
                    else if(ch == 'b')
                        state = 2;
                    else 
                        state = -1;
                    break;

                case 3:
                    if(ch == 'a')
                        state = 1;
                    else if(ch == 'b')
                        state = 4;
                    else
                        state = -1;
                    break;

                case 4:
                    if(ch == 'a')
                        state = 1;
                    else if(ch == 'b')
                        state = 2;
                    else
                        state = -1;

            }
        }
        return state == 1 || state == 3 || state == 4;
    }

    public static void main(String[] args) {
        
        //now true
        System.out.println(scan("abb") ? "OK" : "NOPE");
        System.out.println(scan("bbaba") ? "OK" : "NOPE");
        System.out.println(scan("baaaaaaa") ? "OK" : "NOPE");
        System.out.println(scan("aaaa") ? "OK" : "NOPE");
        System.out.println(scan("a") ? "OK" : "NOPE");
        System.out.println(scan("ba") ? "OK" : "NOPE");
        System.out.println(scan("bba") ? "OK" : "NOPE");
        System.out.println(scan("bbbababab") ? "OK" : "NOPE");


        //now false
        System.out.println(scan("abbbbb") ? "OK" : "NOPE");
        System.out.println(scan("bbabbbbb") ? "OK" : "NOPE"); 
        System.out.println(scan("b") ? "OK" : "NOPE");
        
        
    }
}
