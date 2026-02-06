package com.china.soft.commons.utils;

import com.china.soft.commons.exception.BaseException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

@Slf4j
public class ImageUtils {

    /**
     * 讲文件转化生成图片
     * @param name
     * @description 测试
     */
    public static BufferedImage generateImageWithName(String name) throws IOException, FontFormatException {
        int width = 196;
        int height = 110;

        int nameLen = name.length();
        String nameWritten;
        // 如果用户输入的姓名少于等于2个字符，不用截取
        if (nameLen <= 2) {
            nameWritten = name;
        } else {
            // 如果用户输入的姓名大于等于3个字符，截取后面两位
            String first = name.substring(0, 1);
            if (isChinese(first)) {
                // 截取倒数两位汉字
                nameWritten = name.substring(nameLen - 2);
            } else {
                // 截取前面的两个英文字母
                nameWritten = name.substring(0, 2).toUpperCase();
            }
        }
        // 创建BufferedImage对象
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 设置圆角
        makeRoundedCorner(bi,30);
        Graphics2D g2 = bi.createGraphics();
        // 设置提示
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // 设置背景颜色
        g2.setBackground(getRandomColor());
        g2.clearRect(0, 0, width, height);
        // 设置字体颜色
        g2.setPaint(Color.WHITE);
        // 设置自定义字体
        File fontFile = ResourceUtils.getFile("classpath:AlibabaPuHuiTi-3-85-Bold.ttf");
        Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.PLAIN, 180);
        g2.setFont(customFont);
        // 字体的Metrics以计算文本尺寸,设置图片文字位置
        FontMetrics metrics = g2.getFontMetrics(customFont);
        int textWidth = metrics.stringWidth(nameWritten);
        // 注意getHeight可能不完全等同于字体的高度，具体取决于字体设计，但通常可用作近似值
        int textHeight = metrics.getHeight();
        // 计算文字居中的位置
        int centerX = (width - textWidth) / 2;
        int centerY = (height - textHeight) / 2 + metrics.getAscent();
        // 在计算出的中心位置绘制文字
        g2.drawString(nameWritten, centerX, centerY);
        // 处理图形上下文
        g2.dispose();
        return bi;
    }

    /**
     * 判断字符串是否为中文
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) return true;
        else return false;
    }

    /**
     * 图片做圆角处理
     *
     * @param image
     * @param cornerRadius
     * @return
     */
    public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return output;
    }

    /**
     * 获得随机颜色
     * @return
     */
    private static Color getRandomColor() {
        String[] beautifulColors = new String[]{"2,168,250"};
        int len = beautifulColors.length;
        Random random = new Random();
        String[] color = beautifulColors[random.nextInt(len)].split(",");
        return new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]));
    }

    /**
     * 讲 BufferedImage 转化为 InputStream
     * @param image
     */
    public static InputStream convert(BufferedImage image){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }catch (Exception e){
            log.info(e.getMessage());
            throw new BaseException(e.getMessage());
        }
    }
}
