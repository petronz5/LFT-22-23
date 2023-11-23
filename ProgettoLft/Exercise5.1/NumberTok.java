public class NumberTok extends Token {

    public final int N;
    public NumberTok(int n){
        super(Tag.NUM);
        N = n;
    }

    public String toString(){
        return "<" +tag+ " , " + N + " >";
    }


}
