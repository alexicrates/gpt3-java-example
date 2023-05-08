package com.example.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    public static ImageIcon getResizedImageIcon(String fileName, int width, int height){
        ImageIcon imageIcon = new ImageIcon(fileName);
        Image originalImage = imageIcon.getImage();
        imageIcon.setImage(originalImage.getScaledInstance(width, height, Image.SCALE_DEFAULT));
        return imageIcon;
    }

    public static ImageIcon getResizedImageIcon(Image image, int width, int height){
        ImageIcon imageIcon = new ImageIcon(image);
        imageIcon.setImage(image.getScaledInstance(width, height, Image.SCALE_DEFAULT));
        return imageIcon;
    }

    public static BufferedImage getBufferedImage(String path, Boolean colored) throws IOException {
        BufferedImage image = ImageIO.read(new File(path));
        if (!colored) {
            ColorConvertOp colorConvertOp = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
            colorConvertOp.filter(image, image);
        }
        return image;
    }
}
