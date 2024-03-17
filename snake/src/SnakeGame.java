public class SnakeGame{
    public static void gameUpdateAndRender(int[] biBuffer, Integer imageWidth){
        System.out.println("In Game Code");
        //drawRect(biBuffer, imageWidth, 100, 100, 50, 50, 0x00FFA500);
        drawRect(biBuffer, imageWidth, 100, 100, 50, 50, 0x0000FFFF);

    }
    static void drawRect(int[] buffer, Integer imageWidth,
            int x, int y, 
            int width, int height, 
            int colour){
        for(int j = y; j < y + height; ++j){
            for(int i = x; i < x + width; ++i){
                int pos = (j*imageWidth) + i;
                buffer[pos] = colour;
            }
        }
    }
}
