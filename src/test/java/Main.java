import me.pig.pack.impl.Discord;

public class Main {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++){
            if (i > 50) i = 0;
        }
        Discord.start();
    }

}
