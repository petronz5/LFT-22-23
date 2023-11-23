public class NumberTok extends Token {
    
    public int N;
    public NumberTok(int n){
        super(Tag.NUM);
        N = n;
    }

    public String toString(){
        return "<" +tag+ " , " + N + " >";
    }


}
