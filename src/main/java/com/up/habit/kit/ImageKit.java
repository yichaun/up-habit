package com.up.habit.kit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;

/**
 * TODO:
 *
 * @author 王剑洪 on 2019/12/17 0:45
 */
public class ImageKit {

    /**
     * 验证码字体
     */
    public static final Font[] RANDOM_FONT = new Font[]{
            new Font(Font.DIALOG, Font.BOLD, 33),
            new Font(Font.DIALOG_INPUT, Font.BOLD, 34),
            new Font(Font.SERIF, Font.BOLD, 33),
            new Font(Font.SANS_SERIF, Font.BOLD, 34),
            new Font(Font.MONOSPACED, Font.BOLD, 34)
    };
    /**
     * 默认的验证码大小
     */
    protected static final int WIDTH = 108, HEIGHT = 40;

    public static String captchaBase64(String value, Integer width, Integer height) throws Exception {
        width = width == null ? WIDTH : width;
        height = height == null ? HEIGHT : height;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        drawGraphic(value, image);
        ImageIO.write(image, "jpeg", outputStream);
        return "data:image/jpg;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }


    public static void drawGraphic(String randomString, BufferedImage image) {
        Random random = new Random(System.nanoTime());
        // 获取图形上下文
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        // 图形抗锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 字体抗锯齿
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 设定背景色
        g.setColor(getRandColor(210, 250));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //绘制小字符背景
        Color color = null;
        for (int i = 0; i < 20; i++) {
            color = getRandColor(120, 200);
            g.setColor(color);
            String rand = StrKit.getRandomString(false, 1);
            g.drawString(rand, random.nextInt(WIDTH), random.nextInt(HEIGHT));
            color = null;
        }

        //设定字体
        g.setFont(RANDOM_FONT[random.nextInt(RANDOM_FONT.length)]);
        // 绘制验证码
        for (int i = 0; i < randomString.length(); i++) {
            //旋转度数 最好小于45度
            int degree = random.nextInt(28);
            if (i % 2 == 0) {
                degree = degree * (-1);
            }
            //定义坐标
            int x = 22 * i, y = 21;
            //旋转区域
            g.rotate(Math.toRadians(degree), x, y);
            //设定字体颜色
            color = getRandColor(20, 130);
            g.setColor(color);
            //将认证码显示到图象中
            g.drawString(String.valueOf(randomString.charAt(i)), x + 8, y + 10);
            //旋转之后，必须旋转回来
            g.rotate(-Math.toRadians(degree), x, y);
        }
        //图片中间曲线，使用上面缓存的color
        g.setColor(color);
        //width是线宽,float型
        BasicStroke bs = new BasicStroke(3);
        g.setStroke(bs);
        //画出曲线
        QuadCurve2D.Double curve = new QuadCurve2D.Double(0d, random.nextInt(HEIGHT - 8) + 4, WIDTH / 2, HEIGHT / 2, WIDTH, random.nextInt(HEIGHT - 8) + 4);
        g.draw(curve);
        // 销毁图像
        g.dispose();
    }

    /*
     * 给定范围获得随机颜色
     */
    public static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        fc = fc > 255 ? 255 : fc;
        bc = bc > 255 ? 255 : bc;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
