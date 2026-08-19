import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 600;
        int boardHeight = 600;

        JFrame frame = new JFrame("Snake");
        frame.setSize(boardWidth, boardHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // --- TẠO GIAO DIỆN MENU ---
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.BLACK);
        menuPanel.setPreferredSize(new Dimension(boardWidth, boardHeight));

        // Tiêu đề
        JLabel titleLabel = new JLabel("GAME RẮN SĂN MỒI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(Color.blue);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Chế độ chơi
        JPanel modePanel = new JPanel();
        modePanel.setBackground(Color.BLACK);
        modePanel.setMaximumSize(new Dimension(350, 50));
        JLabel modeLabel = new JLabel("Chế độ: ");
        modeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        modeLabel.setForeground(Color.WHITE);
        String[] modes = {"Cổ điển", "Xuyên tường"};
        JComboBox<String> modeBox = new JComboBox<>(modes);
        modeBox.setFont(new Font("Arial", Font.PLAIN, 18));
        modePanel.add(modeLabel);
        modePanel.add(modeBox);

        // Tốc độ
        JPanel speedPanel = new JPanel();
        speedPanel.setBackground(Color.BLACK);
        speedPanel.setMaximumSize(new Dimension(350, 50));
        JLabel speedLabel = new JLabel("Tốc độ: ");
        speedLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        speedLabel.setForeground(Color.WHITE);
        String[] speeds = {"Dễ", "Bình thường", "Khó"};
        JComboBox<String> speedBox = new JComboBox<>(speeds);
        speedBox.setSelectedIndex(1); 
        speedBox.setFont(new Font("Arial", Font.PLAIN, 18));
        speedPanel.add(speedLabel);
        speedPanel.add(speedBox);
        // Đồ họa 
        JPanel graphicsPanel = new JPanel();
        graphicsPanel.setBackground(Color.BLACK);
        graphicsPanel.setMaximumSize(new Dimension(350, 50));
        JLabel graphicsLabel = new JLabel("Đồ họa: ");
        graphicsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        graphicsLabel.setForeground(Color.WHITE);
        String[] graphicsOptions = {"Mặc định", "Tùy chỉnh"};
        JComboBox<String> graphicsBox = new JComboBox<>(graphicsOptions);
        graphicsBox.setFont(new Font("Arial", Font.PLAIN, 18));
        graphicsPanel.add(graphicsLabel);
        graphicsPanel.add(graphicsBox);

        // Nút Bắt đầu
        JButton startBtn = new JButton("BẮT ĐẦU");
        startBtn.setFont(new Font("Arial", Font.BOLD, 25));
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.setFocusPainted(false);
        startBtn.setBackground(Color.GREEN);
        startBtn.setForeground(Color.BLACK);

        // Căn chỉnh layout cho Menu
        menuPanel.add(Box.createVerticalStrut(100)); // Khoảng cách từ trên xuống
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createVerticalStrut(60));
        menuPanel.add(modePanel);
        menuPanel.add(speedPanel);
        menuPanel.add(graphicsPanel);
        menuPanel.add(Box.createVerticalStrut(50));
        menuPanel.add(startBtn);

        // Thêm Menu vào frame ban đầu
        frame.add(menuPanel);
        frame.pack();
        frame.setVisible(true);

        // --- XỬ LÝ KHI BẤM NÚT BẮT ĐẦU ---
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Xóa Menu khỏi Frame
                frame.remove(menuPanel);

                // Tạo đối tượng Game
                snake snakeGame = new snake(boardWidth, boardHeight);

                // 1. Áp dụng Tốc độ (Truy cập trực tiếp vào gameLoop của snake)
                int speedIndex = speedBox.getSelectedIndex();
                if (speedIndex == 0) {
                    snakeGame.gameLoop.setDelay(150); // Chậm
                } else if (speedIndex == 1) {
                    snakeGame.gameLoop.setDelay(100); // Vừa
                } else if (speedIndex == 2) {
                    snakeGame.gameLoop.setDelay(50);  // Nhanh
                }

                // 2. Áp dụng Chế độ xuyên tường (cần thêm cờ xuyenTuong bên class snake)
                if (modeBox.getSelectedIndex() == 1) {
                    snakeGame.xuyenTuong = true; 
                }
                // 3. Áp dụng Đồ họa
                snakeGame.loaiDoHoa = graphicsBox.getSelectedIndex();

                // 3. Nạp Game 
                frame.add(snakeGame);
                frame.pack();
                snakeGame.requestFocus(); // Quan trọng: Yêu cầu lấy focus để nhận phím di chuyển
                frame.revalidate();       // Báo cho UI cấu trúc đã thay đổi
                frame.repaint();          // Vẽ lại UI
            }
        });
    }
}