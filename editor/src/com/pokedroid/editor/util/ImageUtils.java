package com.pokedroid.editor.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public final class ImageUtils {

	private ImageUtils() {
	}

	public static BufferedImage[] splitImage(Image img, int width, int height) {
		BufferedImage i = toBuffered(img);
		int cols = i.getWidth() / width;
		int rows = i.getHeight() / height;
		BufferedImage[] ret = new BufferedImage[rows * cols];
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				ret[x + (y * cols)] = i.getSubimage(x * width, y * height, width, height);
			}
		}
		return ret;
	}

	public static BufferedImage toBuffered(Image img) {
		if (img instanceof BufferedImage)
			return (BufferedImage) img;
		BufferedImage i = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = i.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return i;
	}

}
