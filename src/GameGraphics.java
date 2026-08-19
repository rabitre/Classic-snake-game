import java.awt.*;

public class GameGraphics {
    public int loaiDoHoa; // 0: Mặc định, 1: Hình ảnh (tùy chỉnh)

    public GameGraphics(int loaiDoHoa) {
        this.loaiDoHoa = loaiDoHoa;
    }

    // Hàm này để trống, sau này bạn code logic vẽ đồ họa (vd: vẽ đầu rắn bằng ảnh png) vào đây
    public void drawCustomSnake(Graphics g, int x, int y, int tileSize) {
        if (loaiDoHoa == 1) {
            // TODO: Code load và vẽ hình ảnh của bạn ở đây
        }
    }
}