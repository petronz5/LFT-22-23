public class Name {
    
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);

            switch(state){
                case 0:
                    if(ch == 'D' || ch == 'd')
                        state = 1;
                    else
                        state = 2;
                    break;
                
                case 1:
                    if(ch == 'A' || ch == 'a')
                            state = 3;
                        else
                            state = 9;
                        break;

                case 2:
                    if(ch == 'a' || ch == 'a')
                        state = 4;
                    else
                        state = -1;
                    break;

                case 3:
                    if(ch == 'v' || ch == 'V')
                        state = 14;
                    else
                        state = 15;
                    break;

                case 4:
                    if(ch == 'v' || ch == 'V')
                        state = 5;
                    else
                        state = -1;
                    break;
                
                case 5:
                    if(ch == 'i' || ch == 'I')
                        state = 6;
                    else
                        state = -1;
                    break;

                case 6:
                    if(ch == 'd' || ch == 'D')
                        state = 7;
                    else
                        state = -1;
                    break;

                case 7:
                    if(ch == 'e' || ch == 'E')
                        state = 8;
                    else
                        state = -1;
                    break;

                case 9:
                    if(ch == 'v' || ch == 'V')
                        state = 10;
                    else
                        state = -1;
                    break;
                
                case 10:
                    if(ch == 'i' || ch == 'I')
                        state = 11;
                    else
                        state = -1;
                    break;

                case 11:
                    if(ch == 'd' || ch == 'D')
                        state = 12;
                    else
                        state = -1;
                    break;

                case 12:
                    if(ch == 'e' || ch == 'E')
                        state = 13;
                    else
                        state = -1;
                    break;

                case 14:
                    if(ch == 'i' || ch == 'I')
                        state = 19;
                    else
                        state = 20;
                    break;

                case 15:
                    if(ch == 'i' || ch == 'I')
                        state = 16;
                    else
                        state = -1;
                    break;

                case 16:
                    if(ch == 'd' || ch == 'D')
                        state = 17;
                    else
                        state = -1;
                    break;

                case 17:
                    if(ch == 'e' || ch == 'E')
                        state = 18;
                    else
                        state = -1;
                    break;

                case 19:
                    if(ch == 'd' || ch == 'D')
                        state = 23;
                    else
                        state = 24;
                    break;

                case 20:
                    if(ch == 'd' || ch == 'D')
                        state = 21;
                    else
                        state = -1;
                    break;

                case 21:
                    if(ch == 'e' || ch == 'E')
                        state = 22;
                    else
                        state = -1;
                    break;

                case 23:
                    if(ch == 'e' || ch == 'E')
                        state = 26;
                    else
                        state = 27;
                    break;

                case 24:
                    if(ch == 'e' || ch == 'E')
                        state = 25;
                    else
                        state = -1;
                    break;

            }
        }
        return state == 8 || state == 13 || state == 18 || state == 22 || state == 25 || state == 26 || state == 27;
    }

    public static void main(String[] args) {
        
        //now true
        System.out.println(scan("Davide") ? "OK" : "NOPE");
        System.out.println(scan("Dvvide") ? "OK" : "NOPE");
        System.out.println(scan("Dav%de") ? "OK" : "NOPE");
        
        //now false
        System.out.println(scan("Dvaide") ? "OK" : "NOPE");
        System.out.println(scan("Da*i*e") ? "OK" : "NOPE");
    }
}
