import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 600;
        int boardHeight = 600;
        JFrame frame = new JFrame("Snake");
        frame.setSize(boardWidth, boardHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        snake ran = new snake(boardWidth, boardHeight);
        frame.add(ran);
        frame.pack();
        frame.setVisible(true); 
        ran.requestFocus();   
    }
}