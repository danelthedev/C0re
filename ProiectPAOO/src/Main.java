import Game.Game;

public class Main{


    public static void main(String[] args){

        long startTime;
        long timeLoadFrame;
        long diff;
        Game game = Game.getInstance();

        while(Game.getInstance().isRunning)
        {
            startTime = System.nanoTime();
            game.update();
            game.render();

            timeLoadFrame = System.nanoTime();
            diff = (timeLoadFrame - startTime) / 1000000;


            if (diff < 16) {
                try {
                    Thread.sleep((long) 16 - diff);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

