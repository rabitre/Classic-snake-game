import java.io.File;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class snake extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;
        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWight;
    int boardHeight;
    int tileSize = 25;

    // snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    // food
    Tile food;
    Random random;
    
    // Đồ họa
    Image bgImage;        // Chế độ 1
    Image bgImage2;       // Chế độ 2: Nền
    Image textureImage;   // Chế độ 2: Họa tiết rắn
    int offsetY = 0;      // Chế độ 2: Cuộn ảnh

    // game logic
    Timer gameLoop;
    int vantocx;
    int vantocy;
    boolean gameOver = false;
    public boolean xuyenTuong = false;
    public int loaiDoHoa = 0;

    snake(int boardWight, int boardHeight) {
        this.boardWight = boardWight;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWight, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        food = new Tile(10, 10);
        random = new Random();

        // --- ẢNH CHẾ ĐỘ 1 ---
        String[] danhSachAnh = {
           "phuongtien/meme1.jpg",
            "phuongtien/meme2.jpg",
            "phuongtien/meme3.jpg",
            "phuongtien/kd1.jpg",
            "phuongtien/meme4.jpg",
            "phuongtien/kd2.jpg",
            "phuongtien/meme5.jpg",
            "phuongtien/meme6.jpg",
            "phuongtien/meme7.jpg",
        };
        int chonNgauNhien = random.nextInt(danhSachAnh.length);
        Image anhGoc = new ImageIcon(danhSachAnh[chonNgauNhien]).getImage();
        bgImage = anhGoc.getScaledInstance(boardWight, boardHeight, Image.SCALE_SMOOTH);
        if (chonNgauNhien == 3||chonNgauNhien == 5) {
            phatAmThanh("phuongtien/nhac1.wav"); 
        }

        // --- ẢNH CHẾ ĐỘ 2 ---
        Image anhNen2 = new ImageIcon("phuongtien/nen.jpg").getImage();
        bgImage2 = anhNen2.getScaledInstance(boardWight, boardHeight, Image.SCALE_SMOOTH);

        Image anhHoaTiet = new ImageIcon("phuongtien/nen.jpg").getImage();
        textureImage = anhHoaTiet.getScaledInstance(boardWight, boardHeight, Image.SCALE_SMOOTH);

        placeFood();
        vantocx = 0;
        vantocy = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
      
        // 1. VẼ NỀN (Dành riêng cho chế độ 2)
       
        if (loaiDoHoa == 2 && bgImage2 != null) {
            g.drawImage(bgImage2, 0, 0, null); 
        }

     
        // 2. VẼ RẮN VÀ MỒI (Chia các chế độ)
     
        if (loaiDoHoa == 2) {
            // --- CHẾ ĐỘ 2: ẢNH CUỘN MẶT NẠ ---
            Graphics2D g2d = (Graphics2D) g.create();
            java.awt.geom.Area mask = new java.awt.geom.Area();
            
            // Gộp Mồi
            mask.add(new java.awt.geom.Area(new Rectangle(food.x * tileSize, food.y * tileSize, tileSize, tileSize)));
            // Gộp Đầu
            mask.add(new java.awt.geom.Area(new Rectangle(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize)));
            // Gộp Thân
            for (Tile snakePart : snakeBody) {
                mask.add(new java.awt.geom.Area(new Rectangle(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize)));
            }

            g2d.setClip(mask); // Áp dụng mặt nạ

            if (textureImage != null) {
                g2d.drawImage(textureImage, 0, offsetY, null);
                g2d.drawImage(textureImage, 0, offsetY - boardHeight, null); 
            }
            g2d.dispose();
        } 
        else {
          
            // Mồi
            g.setColor(Color.yellow);
            g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

            // Thân rắn
            for (int i = 0; i < snakeBody.size(); i++) {
                Tile snakePart = snakeBody.get(i);
                int px = snakePart.x * tileSize;
                int py = snakePart.y * tileSize;

                if (loaiDoHoa == 1 && bgImage != null) {
                    g.drawImage(bgImage, px, py, px + tileSize, py + tileSize, 
                                         px, py, px + tileSize, py + tileSize, null);
                } else if (loaiDoHoa == 3) {
                    g.setColor(Color.yellow); 
                    g.fillRect(px, py, tileSize, tileSize);
                } else {
                    float hue = (0.55f + (float) (i + 1) * 0.015f) % 1.0f;
                    g.setColor(Color.getHSBColor(hue, 0.85f, 1.0f));
                    g.fillRect(px, py, tileSize, tileSize);
                }
            }

            // Đầu rắn
            int headX = snakeHead.x * tileSize;
            int headY = snakeHead.y * tileSize;

            if (loaiDoHoa == 1 && bgImage != null) {
                g.drawImage(bgImage, headX, headY, headX + tileSize, headY + tileSize, 
                                     headX, headY, headX + tileSize, headY + tileSize, null);
            } else if (loaiDoHoa == 3) {
                g.setColor(Color.orange);
                g.fillRect(headX, headY, tileSize, tileSize);
            } else {
                g.setColor(Color.getHSBColor(0.55f, 0.85f, 1.0f));
                g.fillRect(headX, headY, tileSize, tileSize);
            }
        }

    
        // 3. VẼ MẮT RẮN (Luôn vẽ lên trên cùng)
     
        g.setColor(Color.black);
        int eyeSize = 4;
        int headX = snakeHead.x * tileSize;
        int headY = snakeHead.y * tileSize;

        if (vantocx == 1) { 
            g.fillOval(headX + 16, headY + 5, eyeSize, eyeSize);
            g.fillOval(headX + 16, headY + 15, eyeSize, eyeSize);
        } else if (vantocx == -1) { 
            g.fillOval(headX + 5, headY + 5, eyeSize, eyeSize);
            g.fillOval(headX + 5, headY + 15, eyeSize, eyeSize);
        } else if (vantocy == -1) { 
            g.fillOval(headX + 5, headY + 5, eyeSize, eyeSize);
            g.fillOval(headX + 15, headY + 5, eyeSize, eyeSize);
        } else { 
            g.fillOval(headX + 5, headY + 16, eyeSize, eyeSize);
            g.fillOval(headX + 15, headY + 16, eyeSize, eyeSize);
        }

  
        // HIỂN THỊ ĐIỂM SỐ & GAME OVER
        
        if (gameOver) {
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.setColor(Color.red);
            g.drawString("Game Over!", tileSize * 10, tileSize * 11);
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize * 10, tileSize * 12);
        } else {
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.setColor(Color.blue);
            g.drawString("Score :" + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    public void placeFood() {
        boolean onSnake;
        do {
            onSnake = false;
            food.x = random.nextInt(boardWight / tileSize);
            food.y = random.nextInt(boardHeight / tileSize);

            if (collision(food, snakeHead)) {
                onSnake = true;
                continue;
            }

            for (int i = 0; i < snakeBody.size(); i++) {
                Tile snakePart = snakeBody.get(i);
                if (collision(food, snakePart)) {
                    onSnake = true;
                    break;
                }
            }
        } while (onSnake);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
    public void phatAmThanh(String duongDanFile) {
    try {
        File fileAmThanh = new File(duongDanFile);
        if (fileAmThanh.exists()) {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(fileAmThanh);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start(); 
        }
    } catch (Exception e) {
        System.out.println("Lỗi âm thanh: " + e.getMessage());
    }
}

    public void move() {
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

        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize >= boardWight ||
            snakeHead.y * tileSize < 0 || snakeHead.y * tileSize >= boardHeight) {
            
            if (xuyenTuong) {
                if (snakeHead.x < 0) {
                    snakeHead.x = (boardWight / tileSize) - 1;
                } else if (snakeHead.x >= boardWight / tileSize) {
                    snakeHead.x = 0;
                } else if (snakeHead.y < 0) {
                    snakeHead.y = (boardHeight / tileSize) - 1;
                } else if (snakeHead.y >= boardHeight / tileSize) {
                    snakeHead.y = 0;
                }
            } else {
                gameOver = true;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();

        // Cuộn ảnh cho chế độ 2
        if (loaiDoHoa == 2) {
            offsetY += 30;
            if (offsetY >= boardHeight) {
                offsetY = 0; 
            }
        }

        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && vantocy != 1) {
            vantocx = 0;
            vantocy = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && vantocy != -1) {
            vantocx = 0;
            vantocy = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && vantocx != 1) {
            vantocx = -1;
            vantocy = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && vantocx != -1) {
            vantocx = 1;
            vantocy = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}