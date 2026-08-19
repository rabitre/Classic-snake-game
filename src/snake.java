import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class snake extends JPanel implements ActionListener ,KeyListener {
    private class Tile{
        int x ; 
         int y; 
        Tile (int x,int y){
            this.x=x;
            this.y=y;
        }
    }
    int boardWight ;
    int boardHeight ; 
     int tileSize=25;
     //snake
     Tile snakeHead;
     ArrayList<Tile> snakeBody;
     //food
     Tile food;
     Random random;
     //game logic
     Timer gameLoop;
     int vantocx;
     int vantocy;
     boolean gameOver=false;
     
     

    snake (int boardWight,int boardHeight){
        this.boardWight=boardWight;
         this.boardHeight=boardHeight;
         setPreferredSize(new Dimension(this.boardWight,this.boardHeight));
         setBackground(Color.black);
         addKeyListener(this);
         setFocusable(true);
         snakeHead=new Tile (5,5);
        snakeBody=new ArrayList<Tile>();
         food=new Tile(10,10);
         random=new Random();
         placeFood();

        vantocx=0;
        vantocy=0;

         gameLoop= new Timer(100,this);
        gameLoop.start();


    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        
        /*
        grid
        (x1,y1,x2,y2)
        for (int i=0;i<boardWight/tileSize;i++){
            g.drawLine(i*tileSize,0,i*tileSize,boardHeight);
            g.drawLine(0,i*tileSize,boardWight,i*tileSize);
        }
         */
        

        //food
        //(x,y,weight,height)
        g.setColor(Color.red);
    g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

    // 2. Vẽ thân rắn (Vẽ thân trước)
    for (int i = 0; i < snakeBody.size(); i++) {
        Tile snakePart = snakeBody.get(i);
        float hue = (0.55f + (float) (i + 1) * 0.015f) % 1.0f;
        g.setColor(Color.getHSBColor(hue, 0.85f, 1.0f));
        g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
    }

    // Vẽ đầu rắn
    g.setColor(Color.getHSBColor(0.55f, 0.85f, 1.0f));
    g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

    //Vẽ 2 mắt màu đen lên trên đầu rắn
    g.setColor(Color.black);
    int eyeSize = 4;
    int headX = snakeHead.x * tileSize;
    int headY = snakeHead.y * tileSize;

    if (vantocx == 1) { 
        // Bò sang PHẢI
        g.fillOval(headX + 16, headY + 5, eyeSize, eyeSize);
        g.fillOval(headX + 16, headY + 15, eyeSize, eyeSize);
    } else if (vantocx == -1) { 
        // Bò sang TRÁI
        g.fillOval(headX + 5, headY + 5, eyeSize, eyeSize);
        g.fillOval(headX + 5, headY + 15, eyeSize, eyeSize);
    } else if (vantocy == -1) { 
        // Bò lên TRÊN
        g.fillOval(headX + 5, headY + 5, eyeSize, eyeSize);
        g.fillOval(headX + 15, headY + 5, eyeSize, eyeSize);
    } else { 
        // Bò xuống DƯỚI hoặc đứng yên
        g.fillOval(headX + 5, headY + 16, eyeSize, eyeSize);
        g.fillOval(headX + 15, headY + 16, eyeSize, eyeSize);
    }

        //diem
        
        if (gameOver){
            
            g.setFont(new Font("Arial",Font.PLAIN,18));
            g.setColor(Color.red);
            g.drawString("Game Over!",tileSize*10,tileSize*11);
            g.drawString("Score: "+ String.valueOf(snakeBody.size()),tileSize*10,tileSize*12);

        }
        else {
            g.setFont(new Font("Arial",Font.PLAIN,12));
            g.setColor(Color.blue);
            g.drawString("Score :"+String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }
        
    }



    public void placeFood() {
    boolean onSnake;
    do {
        onSnake = false;
        // 1. Tạo tọa độ ngẫu nhiên mới
        food.x = random.nextInt(boardWight / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);

        // 2. Kiểm tra có trùng với đầu rắn không
        if (collision(food, snakeHead)) {
            onSnake = true;
            continue;
        }

        // 3. Kiểm tra có trùng với bất kỳ đốt thân nào không
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (collision(food, snakePart)) {
                onSnake = true;
                break; // Tìm thấy 1 điểm trùng là dừng kiểm tra ngay, quay lại random tiếp
            }
        }
    } while (onSnake); // Nếu vẫn trùng thì tiếp tục random lại
}
    
     public boolean collision (Tile tile1,Tile tile2){
        return tile1.x==tile2.x && tile1.y==tile2.y;
     }

    public void move() {
    // 1. Ăn mồi
    if (collision(snakeHead, food)) {
        snakeBody.add(new Tile(food.x, food.y));
        placeFood();
    }

    
    for (int i = snakeBody.size() - 1; i >= 0; i--) {
        Tile snakePart = snakeBody.get(i);
        if (i == 0) {
            snakePart.x = snakeHead.x;
            snakePart.y = snakeHead.y;
        } else {
            Tile prevSnakePart = snakeBody.get(i - 1);
            snakePart.x = prevSnakePart.x;
            snakePart.y = prevSnakePart.y;
        }
    }

    
    snakeHead.x += vantocx;
    snakeHead.y += vantocy;

   // Game Over
    for (int i = 0; i < snakeBody.size(); i++) {
        Tile snakePart = snakeBody.get(i);
        if (collision(snakeHead, snakePart)) {
            gameOver = true;
        }
    }

    
    if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize >= boardWight ||
        snakeHead.y * tileSize < 0 || snakeHead.y * tileSize >= boardHeight) {
        gameOver = true;
    }
}
    
    @Override
    public void actionPerformed(ActionEvent e) {
        move();

       repaint();
       if (gameOver){
        gameLoop.stop();
       }
    }
     @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode()==KeyEvent.VK_UP&&vantocy!=1){
        vantocx=0;
        vantocy=-1;
      }
      else if (e.getKeyCode()==KeyEvent.VK_DOWN&&vantocy!=-1){
        vantocx=0;
        vantocy=1;
      }
      else if (e.getKeyCode()==KeyEvent.VK_LEFT&&vantocx!=1){
        vantocx=-1;
        vantocy=0;
      }
      else if (e.getKeyCode()==KeyEvent.VK_RIGHT&&vantocx!=-1){
        vantocx=1;
        vantocy=0;
    }
}
    
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }
   
    @Override
    public void keyReleased(KeyEvent e) {
      
    }
}

