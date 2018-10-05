package imageUtils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import imageprocessor.DisplayImage;

public class Utils {

	public static BufferedImage makeARGBImage(Color color, BufferedImage bufferedimage) {
		int width = bufferedimage.getWidth();
		int height = bufferedimage.getHeight();

		int redRef = color.getRed();
		int greenRef = color.getGreen();
		int blueRef = color.getBlue();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// WritableRaster raster = image.getRaster();
		// ColorModel model = image.getColorModel();
		// WritableRaster alphaRaster = image.getAlphaRaster();

		// Color c2 = Color.GRAY;
		// int argb2 = c2.getRGB();
		// Object data2 = model.getDataElements(argb2, null);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

				Color tempColor = new Color(bufferedimage.getRGB(i, j));

				// value at position
				int argb = tempColor.getRGB();

				// Object data2 = model.getDataElements(argb2, null);

				int alpha = (argb >> 24) & 0xff;
				int red = (argb >> 16) & 0xff;
				int green = (argb >> 8) & 0xff;
				int blue = argb & 0xff;

				int result = 0;

				if ((red == redRef) && (green == greenRef) && (blue == blueRef)) {
					// result = result | (alpha << 24);
					// result = result | (red << 16);
					// result = result | (green << 8);
					// result = result | blue;
					result = result | (alpha << 24);
					result = result | (240 << 16);
					result = result | (240 << 8);
					result = result | 240;

					image.setRGB(i, j, result);
				} else {
					result = result | (alpha << 24);
					result = result | (red << 16);
					result = result | (green << 8);
					result = result | blue;
					// result = result | (240 << 24);
					// result = result | (240 << 16);
					// result = result | (240 << 8);
					// result = result | 240;

					image.setRGB(i, j, result);
				}

			}
		}
		return image;
	}

	/**
	 * replaces all pixels of bufferedimage touched by mouse by color selected in
	 * DisplayImage.
	 * 
	 * @param bufferedimage
	 *            - the bufferedimage
	 * @param colorToReplace
	 *            - the color to replace
	 * @return buffered image with replaced color
	 */
	public static BufferedImage makeRGBImage(BufferedImage bufferedimage, Color colorToReplace) {
		int width = bufferedimage.getWidth();
		int height = bufferedimage.getHeight();

		Color replacement = DisplayImage.color;

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

				Color tempColor = new Color(bufferedimage.getRGB(i, j));
				int argb = tempColor.getRGB();

				int alpha = (argb >> 24) & 0xff;
				int red = (argb >> 16) & 0xff;
				int green = (argb >> 8) & 0xff;
				int blue = argb & 0xff;

				if ((red == colorToReplace.getRed()) && (green == colorToReplace.getGreen())
						&& (blue == colorToReplace.getBlue())) {
					red = replacement.getRed();
					green = replacement.getGreen();
					blue = replacement.getBlue();
				}
				int result = 0;

				result = result | (alpha << 24);
				result = result | (red << 16);
				result = result | (green << 8);
				result = result | blue;

				image.setRGB(i, j, result);
			}

		}
		return image;
	}

	/**
	 * executes the precondition sub on the buffered image
	 * 
	 * @param bufferedImage
	 *            - the buffered image
	 * @return - the preconditioned buffered image
	 */
	public static BufferedImage precondition_Sub(BufferedImage bufferedImage) {

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int j = 0; j < height; j++) {

			for (int i = 1; i < width; i++) {

				Color colorCurrent = new Color(bufferedImage.getRGB(i, j));
				Color colorBefore = new Color(bufferedImage.getRGB(i - 1, j));

				int argbCurrent = colorCurrent.getRGB();
				int argbBefore = colorBefore.getRGB();

				int redCurrent = (argbCurrent >> 16) & 0xff;
				int greenCurrent = (argbCurrent >> 8) & 0xff;
				int blueCurrent = argbCurrent & 0xff;

				int redBefore = (argbBefore >> 16) & 0xff;
				int greenBefore = (argbBefore >> 8) & 0xff;
				int blueBefore = argbBefore & 0xff;

				int red = transformToUint8(redCurrent - redBefore);
				int green = transformToUint8(greenCurrent - greenBefore);
				int blue = transformToUint8(blueCurrent - blueBefore);

				int result = 0;

				result = result | (red << 16);
				result = result | (green << 8);
				result = result | blue;

				image.setRGB(i, j, result);
			}

		}
		return image;
	}

	/**
	 * executes the precondition sub on the buffered image
	 * 
	 * @param bufferedImage
	 *            - the buffered image
	 * @return - the preconditioned buffered image
	 */
	public static BufferedImage precondition_UP(BufferedImage bufferedImage) {

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < width; i++) {

			for (int j = 1; j < height; j++) {

				Color colorCurrent = new Color(bufferedImage.getRGB(i, j));
				Color colorBefore = new Color(bufferedImage.getRGB(i, j - 1));

				int argbCurrent = colorCurrent.getRGB();
				int argbBefore = colorBefore.getRGB();

				int redCurrent = (argbCurrent >> 16) & 0xff;
				int greenCurrent = (argbCurrent >> 8) & 0xff;
				int blueCurrent = argbCurrent & 0xff;

				int redBefore = (argbBefore >> 16) & 0xff;
				int greenBefore = (argbBefore >> 8) & 0xff;
				int blueBefore = argbBefore & 0xff;

				int red = transformToUint8(redCurrent - redBefore);
				int green = transformToUint8(greenCurrent - greenBefore);
				int blue = transformToUint8(blueCurrent - blueBefore);

				int result = 0;

				result = result | (red << 16);
				result = result | (green << 8);
				result = result | blue;

				image.setRGB(i, j, result);
			}

		}
		return image;
	}

	/**
	 * executes the precondition average2 on the buffered image
	 * 
	 * @param bufferedImage
	 *            - the buffered image
	 * @return - the preconditioned buffered image
	 */
	public static BufferedImage precondition_AVERAGE2(BufferedImage bufferedImage) {

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < width; i++) {

			for (int j = 0; j < height; j++) {

				Color colorCurrent = new Color(bufferedImage.getRGB(i, j));
				Color colorBefore = Integer.compare(i, 0) == 0 ? Color.BLACK
						: new Color(bufferedImage.getRGB(i - 1, j));
				Color colorUp = Integer.compare(j, 0) == 0 ? Color.BLACK : new Color(bufferedImage.getRGB(i, j - 1));

				int argbCurrent = colorCurrent.getRGB();
				int argbBefore = colorBefore.getRGB();
				int argbUp = colorUp.getRGB();

				int redCurrent = (argbCurrent >> 16) & 0xff;
				int greenCurrent = (argbCurrent >> 8) & 0xff;
				int blueCurrent = argbCurrent & 0xff;

				int redBefore = (argbBefore >> 16) & 0xff;
				int greenBefore = (argbBefore >> 8) & 0xff;
				int blueBefore = argbBefore & 0xff;

				int redUp = (argbUp >> 16) & 0xff;
				int greenUp = (argbUp >> 8) & 0xff;
				int blueUp = argbUp & 0xff;

				int red = transformToUint8(redCurrent, redBefore, redUp);
				int green = transformToUint8(greenCurrent, greenBefore, greenUp);
				int blue = transformToUint8(blueCurrent, blueBefore, blueUp);

				int result = 0;

				result = result | (red << 16);
				result = result | (green << 8);
				result = result | blue;

				image.setRGB(i, j, result);
			}

		}
		return image;
	}

	/**
	 * executes the precondition average3 on the buffered image
	 * 
	 * @param bufferedImage
	 *            - the buffered image
	 * @return - the preconditioned buffered image
	 */
	public static BufferedImage precondition_AVERAGE3(BufferedImage bufferedImage) {

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < width; i++) {

			for (int j = 0; j < height; j++) {

				Color colorCurrent = new Color(bufferedImage.getRGB(i, j));
				Color colorLeft = Integer.compare(i, 0) == 0 ? Color.BLACK : new Color(bufferedImage.getRGB(i - 1, j));
				Color colorUp = Integer.compare(j, 0) == 0 ? Color.BLACK : new Color(bufferedImage.getRGB(i, j - 1));
				Color colorRight = Integer.compare(i, width - 1) == 0 ? Color.BLACK
						: new Color(bufferedImage.getRGB(i + 1, j));

				int argbCurrent = colorCurrent.getRGB();
				int argbLeft = colorLeft.getRGB();
				int argbUp = colorUp.getRGB();
				int argbRight = colorRight.getRGB();

				int redCurrent = (argbCurrent >> 16) & 0xff;
				int greenCurrent = (argbCurrent >> 8) & 0xff;
				int blueCurrent = argbCurrent & 0xff;

				int redLeft = (argbLeft >> 16) & 0xff;
				int greenLeft = (argbLeft >> 8) & 0xff;
				int blueLeft = argbLeft & 0xff;

				int redUp = (argbUp >> 16) & 0xff;
				int greenUp = (argbUp >> 8) & 0xff;
				int blueUp = argbUp & 0xff;

				int redRight = (argbRight >> 16) & 0xff;
				int greenRight = (argbRight >> 8) & 0xff;
				int blueRight = argbRight & 0xff;

				int red = transformToUint8(redCurrent, redLeft, redUp, redRight);
				int green = transformToUint8(greenCurrent, greenLeft, greenUp, greenRight);
				int blue = transformToUint8(blueCurrent, blueLeft, blueUp, blueRight);

				int result = 0;

				result = result | (red << 16);
				result = result | (green << 8);
				result = result | blue;

				image.setRGB(i, j, result);
			}

		}
		return image;
	}

	/**
	 * executes the precondition average4 on the buffered image
	 * 
	 * @param bufferedImage
	 *            - the buffered image
	 * @return - the preconditioned buffered image
	 */
	public static BufferedImage precondition_AVERAGE4(BufferedImage bufferedImage) {

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < width; i++) {

			for (int j = 0; j < height; j++) {

				Color colorCurrent = new Color(bufferedImage.getRGB(i, j));
				Color colorLeft = Integer.compare(i, 0) == 0 ? Color.BLACK : new Color(bufferedImage.getRGB(i - 1, j));
				Color colorUp = Integer.compare(j, 0) == 0 ? Color.BLACK : new Color(bufferedImage.getRGB(i, j - 1));
				Color colorRight = Integer.compare(i, width - 1) == 0 ? Color.BLACK
						: new Color(bufferedImage.getRGB(i + 1, j));
				Color colorBelow = Integer.compare(j, height - 1) == 0 ? Color.BLACK
						: new Color(bufferedImage.getRGB(i, j + 1));

				int argbCurrent = colorCurrent.getRGB();
				int argbLeft = colorLeft.getRGB();
				int argbUp = colorUp.getRGB();
				int argbRight = colorRight.getRGB();
				int argbBelow = colorBelow.getRGB();

				int redCurrent = (argbCurrent >> 16) & 0xff;
				int greenCurrent = (argbCurrent >> 8) & 0xff;
				int blueCurrent = argbCurrent & 0xff;

				int redLeft = (argbLeft >> 16) & 0xff;
				int greenLeft = (argbLeft >> 8) & 0xff;
				int blueLeft = argbLeft & 0xff;

				int redUp = (argbUp >> 16) & 0xff;
				int greenUp = (argbUp >> 8) & 0xff;
				int blueUp = argbUp & 0xff;

				int redRight = (argbRight >> 16) & 0xff;
				int greenRight = (argbRight >> 8) & 0xff;
				int blueRight = argbRight & 0xff;

				int redBelow = (argbBelow >> 16) & 0xff;
				int greenBelow = (argbBelow >> 8) & 0xff;
				int blueBelow = argbBelow & 0xff;

				int red = transformToUint8(redCurrent, redLeft, redUp, redRight, redBelow);
				int green = transformToUint8(greenCurrent, greenLeft, greenUp, greenRight, greenBelow);
				int blue = transformToUint8(blueCurrent, blueLeft, blueUp, blueRight, blueBelow);

				int result = 0;

				result = result | (red << 16);
				result = result | (green << 8);
				result = result | blue;

				image.setRGB(i, j, result);
			}

		}
		return image;
	}

	/**
	 * executes the precondition average5 on the buffered image
	 * 
	 * @param bufferedImage
	 *            - the buffered image
	 * @return - the preconditioned buffered image
	 */
	public static BufferedImage precondition_AVERAGE5(BufferedImage bufferedImage) {

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < width; i++) {

			for (int j = 0; j < height; j++) {

				Color colorCurrent = new Color(bufferedImage.getRGB(i, j));
				Color colorLeft = Integer.compare(i, 0) == 0 ? Color.BLACK : new Color(bufferedImage.getRGB(i - 1, j));
				Color colorLeftUp = ((Integer.compare(i, 0) == 0) || (Integer.compare(j, 0) == 0)) ? Color.BLACK
						: new Color(bufferedImage.getRGB(i - 1, j - 1));
				Color colorUp = Integer.compare(j, 0) == 0 ? Color.BLACK : new Color(bufferedImage.getRGB(i, j - 1));
				Color colorRight = Integer.compare(i, width - 1) == 0 ? Color.BLACK
						: new Color(bufferedImage.getRGB(i + 1, j));
				Color colorBelow = Integer.compare(j, height - 1) == 0 ? Color.BLACK
						: new Color(bufferedImage.getRGB(i, j + 1));

				int argbCurrent = colorCurrent.getRGB();
				int argbLeft = colorLeft.getRGB();
				int argbLeftUp = colorLeftUp.getRGB();
				int argbUp = colorUp.getRGB();
				int argbRight = colorRight.getRGB();
				int argbBelow = colorBelow.getRGB();

				int redCurrent = (argbCurrent >> 16) & 0xff;
				int greenCurrent = (argbCurrent >> 8) & 0xff;
				int blueCurrent = argbCurrent & 0xff;

				int redLeft = (argbLeft >> 16) & 0xff;
				int greenLeft = (argbLeft >> 8) & 0xff;
				int blueLeft = argbLeft & 0xff;

				int redLeftUp = (argbLeftUp >> 16) & 0xff;
				int greenLeftUp = (argbLeftUp >> 8) & 0xff;
				int blueLeftUp = argbLeftUp & 0xff;

				int redUp = (argbUp >> 16) & 0xff;
				int greenUp = (argbUp >> 8) & 0xff;
				int blueUp = argbUp & 0xff;

				int redRight = (argbRight >> 16) & 0xff;
				int greenRight = (argbRight >> 8) & 0xff;
				int blueRight = argbRight & 0xff;

				int redBelow = (argbBelow >> 16) & 0xff;
				int greenBelow = (argbBelow >> 8) & 0xff;
				int blueBelow = argbBelow & 0xff;

				int red = transformToUint8(redCurrent, redLeft, redLeftUp, redUp, redRight, redBelow);
				int green = transformToUint8(greenCurrent, greenLeft, greenLeftUp, greenUp, greenRight, greenBelow);
				int blue = transformToUint8(blueCurrent, blueLeft, blueLeftUp, blueUp, blueRight, blueBelow);

				int result = 0;

				result = result | (red << 16);
				result = result | (green << 8);
				result = result | blue;

				image.setRGB(i, j, result);
			}

		}
		return image;
	}

	/**
	 * executes the precondition average6 on the buffered image
	 * 
	 * @param bufferedImage
	 *            - the buffered image
	 * @return - the preconditioned buffered image
	 */
	public static BufferedImage precondition_AVERAGE6(BufferedImage bufferedImage) {

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < width; i++) {

			for (int j = 0; j < height; j++) {

				Color colorCurrent = new Color(bufferedImage.getRGB(i, j));
				Color colorLeft = Integer.compare(i, 0) == 0 ? Color.BLACK : new Color(bufferedImage.getRGB(i - 1, j));
				Color colorLeftUp = ((Integer.compare(i, 0) == 0) || (Integer.compare(j, 0) == 0)) ? Color.BLACK
						: new Color(bufferedImage.getRGB(i - 1, j - 1));
				Color colorUp = Integer.compare(j, 0) == 0 ? Color.BLACK : new Color(bufferedImage.getRGB(i, j - 1));
				Color colorRightUp = ((Integer.compare(i, width - 1) == 0) || (Integer.compare(j, 0) == 0))
						? Color.BLACK
						: new Color(bufferedImage.getRGB(i + 1, j - 1));
				Color colorRight = Integer.compare(i, width - 1) == 0 ? Color.BLACK
						: new Color(bufferedImage.getRGB(i + 1, j));
				Color colorBelow = Integer.compare(j, height - 1) == 0 ? Color.BLACK
						: new Color(bufferedImage.getRGB(i, j + 1));

				int argbCurrent = colorCurrent.getRGB();
				int argbLeft = colorLeft.getRGB();
				int argbLeftUp = colorLeftUp.getRGB();
				int argbUp = colorUp.getRGB();
				int argbRightUp = colorRightUp.getRGB();
				int argbRight = colorRight.getRGB();
				int argbBelow = colorBelow.getRGB();

				int redCurrent = (argbCurrent >> 16) & 0xff;
				int greenCurrent = (argbCurrent >> 8) & 0xff;
				int blueCurrent = argbCurrent & 0xff;

				int redLeft = (argbLeft >> 16) & 0xff;
				int greenLeft = (argbLeft >> 8) & 0xff;
				int blueLeft = argbLeft & 0xff;

				int redLeftUp = (argbLeftUp >> 16) & 0xff;
				int greenLeftUp = (argbLeftUp >> 8) & 0xff;
				int blueLeftUp = argbLeftUp & 0xff;

				int redUp = (argbUp >> 16) & 0xff;
				int greenUp = (argbUp >> 8) & 0xff;
				int blueUp = argbUp & 0xff;

				int redRightUp = (argbRightUp >> 16) & 0xff;
				int greenRightUp = (argbRightUp >> 8) & 0xff;
				int blueRightUp = argbRightUp & 0xff;

				int redRight = (argbRight >> 16) & 0xff;
				int greenRight = (argbRight >> 8) & 0xff;
				int blueRight = argbRight & 0xff;

				int redBelow = (argbBelow >> 16) & 0xff;
				int greenBelow = (argbBelow >> 8) & 0xff;
				int blueBelow = argbBelow & 0xff;

				int red = transformToUint8(redCurrent, redLeft, redLeftUp, redUp, redRightUp, redRight, redBelow);
				int green = transformToUint8(greenCurrent, greenLeft, greenLeftUp, greenUp, greenRightUp, greenRight,
						greenBelow);
				int blue = transformToUint8(blueCurrent, blueLeft, blueLeftUp, blueUp, blueRightUp, blueRight,
						blueBelow);

				int result = 0;

				result = result | (red << 16);
				result = result | (green << 8);
				result = result | blue;

				image.setRGB(i, j, result);
			}

		}
		return image;
	}

	/**
	 * executes the precondition average7 on the buffered image
	 * 
	 * @param bufferedImage
	 *            - the buffered image
	 * @return - the preconditioned buffered image
	 */
	public static BufferedImage precondition_AVERAGE7(BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < width; i++) {

			for (int j = 0; j < height; j++) {

				Color colorCurrent = new Color(bufferedImage.getRGB(i, j));
				Color colorLeft = Integer.compare(i, 0) == 0 ? Color.BLACK : new Color(bufferedImage.getRGB(i - 1, j));
				Color colorLeftUp = ((Integer.compare(i, 0) == 0) || (Integer.compare(j, 0) == 0)) ? Color.BLACK
						: new Color(bufferedImage.getRGB(i - 1, j - 1));
				Color colorUp = Integer.compare(j, 0) == 0 ? Color.BLACK : new Color(bufferedImage.getRGB(i, j - 1));
				Color colorRightUp = ((Integer.compare(i, width - 1) == 0) || (Integer.compare(j, 0) == 0))
						? Color.BLACK
						: new Color(bufferedImage.getRGB(i + 1, j - 1));
				Color colorRight = Integer.compare(i, width - 1) == 0 ? Color.BLACK
						: new Color(bufferedImage.getRGB(i + 1, j));
				Color colorRightBelow = ((Integer.compare(i, width - 1) == 0) || (Integer.compare(j, height - 1) == 0))
						? Color.BLACK
						: new Color(bufferedImage.getRGB(i + 1, j));
				Color colorBelow = Integer.compare(j, height - 1) == 0 ? Color.BLACK
						: new Color(bufferedImage.getRGB(i, j + 1));

				int argbCurrent = colorCurrent.getRGB();
				int argbLeft = colorLeft.getRGB();
				int argbLeftUp = colorLeftUp.getRGB();
				int argbUp = colorUp.getRGB();
				int argbRightUp = colorRightUp.getRGB();
				int argbRight = colorRight.getRGB();
				int argbRightBelow = colorRightBelow.getRGB();
				int argbBelow = colorBelow.getRGB();

				int redCurrent = (argbCurrent >> 16) & 0xff;
				int greenCurrent = (argbCurrent >> 8) & 0xff;
				int blueCurrent = argbCurrent & 0xff;

				int redLeft = (argbLeft >> 16) & 0xff;
				int greenLeft = (argbLeft >> 8) & 0xff;
				int blueLeft = argbLeft & 0xff;

				int redLeftUp = (argbLeftUp >> 16) & 0xff;
				int greenLeftUp = (argbLeftUp >> 8) & 0xff;
				int blueLeftUp = argbLeftUp & 0xff;

				int redUp = (argbUp >> 16) & 0xff;
				int greenUp = (argbUp >> 8) & 0xff;
				int blueUp = argbUp & 0xff;

				int redRightUp = (argbRightUp >> 16) & 0xff;
				int greenRightUp = (argbRightUp >> 8) & 0xff;
				int blueRightUp = argbRightUp & 0xff;

				int redRight = (argbRight >> 16) & 0xff;
				int greenRight = (argbRight >> 8) & 0xff;
				int blueRight = argbRight & 0xff;

				int redRightBelow = (argbRightBelow >> 16) & 0xff;
				int greenRightBelow = (argbRightBelow >> 8) & 0xff;
				int blueRightBelow = argbRightBelow & 0xff;

				int redBelow = (argbBelow >> 16) & 0xff;
				int greenBelow = (argbBelow >> 8) & 0xff;
				int blueBelow = argbBelow & 0xff;

				int red = transformToUint8(redCurrent, redLeft, redLeftUp, redUp, redRightUp, redRight, redRightBelow,
						redBelow);
				int green = transformToUint8(greenCurrent, greenLeft, greenLeftUp, greenUp, greenRightUp, greenRight,
						greenRightBelow, greenBelow);
				int blue = transformToUint8(blueCurrent, blueLeft, blueLeftUp, blueUp, blueRightUp, blueRight,
						blueRightBelow, blueBelow);

				int result = 0;

				result = result | (red << 16);
				result = result | (green << 8);
				result = result | blue;

				image.setRGB(i, j, result);
			}

		}
		return image;
	}

	/**
	 * executes the precondition average8 on the buffered image
	 * 
	 * @param bufferedImage
	 *            - the buffered image
	 * @return - the preconditioned buffered image
	 */
	public static BufferedImage precondition_AVERAGE8(BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < width; i++) {

			for (int j = 0; j < height; j++) {

				Color colorCurrent = new Color(bufferedImage.getRGB(i, j));
				Color colorLeft = Integer.compare(i, 0) == 0 ? Color.BLACK : new Color(bufferedImage.getRGB(i - 1, j));
				Color colorLeftUp = ((Integer.compare(i, 0) == 0) || (Integer.compare(j, 0) == 0)) ? Color.BLACK
						: new Color(bufferedImage.getRGB(i - 1, j - 1));
				Color colorUp = Integer.compare(j, 0) == 0 ? Color.BLACK : new Color(bufferedImage.getRGB(i, j - 1));
				Color colorRightUp = ((Integer.compare(i, width - 1) == 0) || (Integer.compare(j, 0) == 0))
						? Color.BLACK
						: new Color(bufferedImage.getRGB(i + 1, j - 1));
				Color colorRight = Integer.compare(i, width - 1) == 0 ? Color.BLACK
						: new Color(bufferedImage.getRGB(i + 1, j));
				Color colorBelowRight = ((Integer.compare(i, width - 1) == 0) || (Integer.compare(j, height - 1) == 0))
						? Color.BLACK
						: new Color(bufferedImage.getRGB(i + 1, j));
				Color colorBelow = Integer.compare(j, height - 1) == 0 ? Color.BLACK
						: new Color(bufferedImage.getRGB(i, j + 1));
				Color colorBelowLeft = (Integer.compare(j, height - 1) == 0 || (Integer.compare(i, 0) == 0))
						? Color.BLACK
						: new Color(bufferedImage.getRGB(i, j + 1));

				int argbCurrent = colorCurrent.getRGB();
				int argbLeft = colorLeft.getRGB();
				int argbLeftUp = colorLeftUp.getRGB();
				int argbUp = colorUp.getRGB();
				int argbRightUp = colorRightUp.getRGB();
				int argbRight = colorRight.getRGB();
				int argbBelowRight = colorBelowRight.getRGB();
				int argbBelow = colorBelow.getRGB();
				int argbBelowLeft = colorBelowLeft.getRGB();

				int redCurrent = (argbCurrent >> 16) & 0xff;
				int greenCurrent = (argbCurrent >> 8) & 0xff;
				int blueCurrent = argbCurrent & 0xff;

				int redLeft = (argbLeft >> 16) & 0xff;
				int greenLeft = (argbLeft >> 8) & 0xff;
				int blueLeft = argbLeft & 0xff;

				int redLeftUp = (argbLeftUp >> 16) & 0xff;
				int greenLeftUp = (argbLeftUp >> 8) & 0xff;
				int blueLeftUp = argbLeftUp & 0xff;

				int redUp = (argbUp >> 16) & 0xff;
				int greenUp = (argbUp >> 8) & 0xff;
				int blueUp = argbUp & 0xff;

				int redRightUp = (argbRightUp >> 16) & 0xff;
				int greenRightUp = (argbRightUp >> 8) & 0xff;
				int blueRightUp = argbRightUp & 0xff;

				int redRight = (argbRight >> 16) & 0xff;
				int greenRight = (argbRight >> 8) & 0xff;
				int blueRight = argbRight & 0xff;

				int redRightBelow = (argbBelowRight >> 16) & 0xff;
				int greenRightBelow = (argbBelowRight >> 8) & 0xff;
				int blueRightBelow = argbBelowRight & 0xff;

				int redBelow = (argbBelow >> 16) & 0xff;
				int greenBelow = (argbBelow >> 8) & 0xff;
				int blueBelow = argbBelow & 0xff;

				int redBelowLeft = (argbBelowLeft >> 16) & 0xff;
				int greenBelowLeft = (argbBelowLeft >> 8) & 0xff;
				int blueBelowLeft = argbBelowLeft & 0xff;

				int red = transformToUint8(redCurrent, redLeft, redLeftUp, redUp, redRightUp, redRight, redRightBelow,
						redBelow, redBelowLeft);
				int green = transformToUint8(greenCurrent, greenLeft, greenLeftUp, greenUp, greenRightUp, greenRight,
						greenRightBelow, greenBelow, greenBelowLeft);
				int blue = transformToUint8(blueCurrent, blueLeft, blueLeftUp, blueUp, blueRightUp, blueRight,
						blueRightBelow, blueBelow, blueBelowLeft);

				int result = 0;

				result = result | (red << 16);
				result = result | (green << 8);
				result = result | blue;

				image.setRGB(i, j, result);
			}

		}
		return image;
	}

	/**
	 * executes the precondition paeth on the buffered image
	 * 
	 * @param bufferedImage
	 *            - the buffered image
	 * @return - the preconditioned buffered image
	 */
	public static BufferedImage precondition_PAETH(BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < width; i++) {

			for (int j = 0; j < height; j++) {

				Color colorCurrent = new Color(bufferedImage.getRGB(i, j));
				Color colorLeft = Integer.compare(i, 0) == 0 ? Color.BLACK : new Color(bufferedImage.getRGB(i - 1, j));
				Color colorUp = Integer.compare(j, 0) == 0 ? Color.BLACK : new Color(bufferedImage.getRGB(i, j - 1));
				Color colorLeftUp = ((Integer.compare(i, 0) == 0) || (Integer.compare(j, 0) == 0)) ? Color.BLACK
						: new Color(bufferedImage.getRGB(i - 1, j - 1));

				int argbCurrent = colorCurrent.getRGB();
				int argbLeft = colorLeft.getRGB();
				int argbUp = colorUp.getRGB();
				int argbLeftUp = colorLeftUp.getRGB();

				int redCurrent = (argbCurrent >> 16) & 0xff;
				int greenCurrent = (argbCurrent >> 8) & 0xff;
				int blueCurrent = argbCurrent & 0xff;

				int redLeft = (argbLeft >> 16) & 0xff;
				int greenLeft = (argbLeft >> 8) & 0xff;
				int blueLeft = argbLeft & 0xff;

				int redUp = (argbUp >> 16) & 0xff;
				int greenUp = (argbUp >> 8) & 0xff;
				int blueUp = argbUp & 0xff;

				int redLeftUp = (argbLeftUp >> 16) & 0xff;
				int greenLeftUp = (argbLeftUp >> 8) & 0xff;
				int blueLeftUp = argbLeftUp & 0xff;

				int redPredictor = paethPredictor(redLeft, redUp, redLeftUp);
				int greenPredictor = paethPredictor(greenLeft, greenUp, greenLeftUp);
				int bluePredictor = paethPredictor(blueLeft, blueUp, blueLeftUp);

				int red = transformToUint8(redCurrent, redPredictor);
				int green = transformToUint8(greenCurrent, greenPredictor);
				int blue = transformToUint8(blueCurrent, bluePredictor);

				int result = 0;

				result = result | (red << 16);
				result = result | (green << 8);
				result = result | blue;

				image.setRGB(i, j, result);
			}

		}
		return image;
	}

	private static int transformToUint8(int current, int left, int up) {
		int noOverflow = 2 * current - left - up;
		return transformToUint8(noOverflow);
	}

	private static int transformToUint8(int current, int left, int up, int right) {
		int noOverflow = 3 * current - left - up - right;
		return transformToUint8(noOverflow);
	}

	private static int transformToUint8(int current, int left, int up, int right, int below) {
		int noOverflow = 4 * current - left - up - right - below;
		return transformToUint8(noOverflow);
	}

	private static int transformToUint8(int current, int left, int leftUp, int up, int right, int below) {
		int noOverflow = 5 * current - left - leftUp - up - right - below;
		return transformToUint8(noOverflow);
	}

	private static int transformToUint8(int current, int left, int leftUp, int up, int rightUp, int right, int below) {
		int noOverflow = 6 * current - left - leftUp - up - rightUp - right - below;
		return transformToUint8(noOverflow);
	}

	private static int transformToUint8(int current, int left, int leftUp, int up, int rightUp, int right,
			int rightBelow, int below) {
		int noOverflow = 7 * current - left - leftUp - up - rightUp - right - rightBelow - below;
		return transformToUint8(noOverflow);
	}

	private static int transformToUint8(int current, int left, int leftUp, int up, int rightUp, int right,
			int rightBelow, int below, int belowLeft) {
		int noOverflow = 8 * current - left - leftUp - up - rightUp - right - rightBelow - below - belowLeft;
		return transformToUint8(noOverflow);
	}

	private static int transformToUint8(int current, int predictor) {
		int noOverflow = current - predictor;
		return transformToUint8(noOverflow);
	}

	/**
	 * moves the image to desktop
	 * 
	 * @param path
	 *            - the path of the directory
	 * @param name
	 *            - the current name
	 * @param pathToTopoA
	 * @throws IOException
	 */
	public static void moveImageToDesktop(String path, byte[] byteArray) throws IOException {
		InputStream in = new ByteArrayInputStream(byteArray);
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		File outPutImage = new File(path);

		// Encapsulate the outPut image
		ImageOutputStream ios = ImageIO.createImageOutputStream(outPutImage);

		// List of ImageWritre's for jpeg format
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");

		// Capture the first ImageWriter
		ImageWriter writer = iter.next();

		// define the o outPut file to the write
		writer.setOutput(ios);

		// Here you define the changes you wanna make to the image
		ImageWriteParam iwParam = writer.getDefaultWriteParam();
		iwParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwParam.setCompressionQuality(.90f);
		// Compression, etc... being made
		writer.write(null, new IIOImage(bufferedImage, null, null), iwParam);
		writer.dispose();
		// Write to altered image in memory to the final file
		ImageIO.write(bufferedImage, "png", ios);
		in.close();
	}

	static int paethPredictor(int left, int up, int leftUp) {

		int predictor = left + up - leftUp;
		int diffLeft = Math.abs(predictor - left);
		int diffUp = Math.abs(predictor - up);
		int diffLeftUp = Math.abs(predictor - leftUp);

		if ((diffLeft <= diffUp) && (diffLeft <= diffLeftUp)) {
			return left;
		} else if ((diffUp <= diffLeftUp)) {
			return up;
		}
		return leftUp;
	}

	/**
	 * convertes the input File to a Byte Array
	 * 
	 * @param pathToTopo
	 *            the path to the topo @return the image as Byte Array @throws
	 */
	public static byte[] convertBufferedImageToByteArry(BufferedImage bufferedImage) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
		} catch (IOException e) {
			System.err.println("Not able to generate byte array from bufferedImage");
			e.printStackTrace();
		}
		return byteArrayOutputStream.toByteArray();
	}

	/**
	 * casts the value to an uint8
	 * 
	 * @param value
	 *            - the value
	 * @return value as uint8
	 */
	private static int transformToUint8(int value) {

		if (value < 0) {
			value += 256;
		}

		return value % 256;
	}

	/**
	 * counts the frequencies of the buffered image
	 * 
	 * @param bufferedImage
	 *            - the bufferedf image
	 * @return - the frequencies in a vector of size 256
	 */
	public static int[] countFrequencies(BufferedImage bufferedImage) {

		int[] response = new int[256];

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		for (int i = 0; i < width; i++) {

			for (int j = 0; j < height; j++) {
				Color current = new Color(bufferedImage.getRGB(i, j));

				int rgb = current.getRGB();
				int alpha = (rgb >> 24) & 0xff;
				int red = (rgb >> 16) & 0xff;
				int green = (rgb >> 8) & 0xff;
				int blue = rgb & 0xff;

				response[alpha]++;
				response[red]++;
				response[green]++;
				response[blue]++;

			}
		}

		return response;

	}

	/**
	 * Yields the nearest power-of-2 to the sum of frequencies
	 * 
	 * @param frequencies
	 *            - the frequencies
	 * 
	 * @return nearest power of two
	 */
	public static int getNearestPowerOfTwo(int[] frequencies) {

		int M = getSumOfFrequencies(frequencies);
		int lowerBound = 1;
		int upperBound = 2;
		int result;

		while (M > upperBound) {
			upperBound *= 2;
		}

		lowerBound = upperBound / 2;

		result = Math.abs(M - lowerBound) > Math.abs(M - upperBound) ? upperBound : lowerBound;

		return (int) (Math.log(result) / Math.log(2));
	}

	public static int[] balanceFrequencies(int[] frequencies) {

		int M = getSumOfFrequencies(frequencies);

		int logTwoDenominator = getNearestPowerOfTwo(frequencies);

		int powerOfTwo = 2 << logTwoDenominator;

		for (int item : frequencies) {

			double relativeFrequency = ((double) item) / ((double) M);

			double scaledFrequency = relativeFrequency * powerOfTwo + 0.5;

			item = (int) scaledFrequency;

		}

		int sum = 0;
		while ((sum = getSumOfFrequencies(frequencies)) != powerOfTwo) {
			if (sum < powerOfTwo) {
				int index = positionOfMinimalFrequency(frequencies);

				frequencies[index]++;
			}

			else if (sum > powerOfTwo) {
				int index = positionOfMaximalFrequency(frequencies);

				frequencies[index]--;
			}
		}

		return frequencies;
	}

	private static int positionOfMinimalFrequency(int[] frequencies) {

		int minimalPosition = 0;
		int minimum = frequencies[0];

		for (int i = 0; i < frequencies.length; i++) {
			minimalPosition = i;
			if (frequencies[i] != 0) {
				minimum = frequencies[i];
				break;
			}
		}

		for (int i = minimalPosition + 1; i < frequencies.length; i++) {
			int current = frequencies[i];
			if ((current < minimum) && (current > 0)) {
				minimum = current;
				minimalPosition = i;
			}
		}

		return minimalPosition;
	}

	private static int positionOfMaximalFrequency(int[] frequencies) {

		int maximalPosition = 0;
		int maximum = frequencies[0];

		for (int i = 1; i < frequencies.length; i++) {
			int current = frequencies[i];
			if (current > maximum) {
				maximum = current;
				maximalPosition = i;
			}
		}
		return maximalPosition;
	}

	private static int getSumOfFrequencies(int[] frequencies) {

		int response = 0;

		for (int item : frequencies) {
			response += item;
		}

		return response;
	}

	public static int[] balanceToPowerOfTwoFrequencies(int[] frequencies) {
		int M = getSumOfFrequencies(frequencies);

		int powerOfTwo = getNearestPowerOfTwo(M);

		for (int i = 0; i < frequencies.length; i++) {

			int tmp = frequencies[i];
			int powerOfTwotmp = getNearestPowerOfTwo(tmp);
			frequencies[i] = powerOfTwotmp;
		}

		// for (int item : frequencies) {
		//
		// double relativeFrequency = ((double) item) / ((double) M);
		//
		// double scaledFrequency = relativeFrequency * powerOfTwo + 0.5;
		//
		// item = (int) scaledFrequency;
		//
		// }

		int sum = 0;
		while ((sum = getSumOfFrequencies(frequencies)) != powerOfTwo) {
			if (sum < powerOfTwo) {
				int index = positionOfMinimalFrequency(frequencies);

				frequencies[index] *= 2;
			}

			else if (sum > powerOfTwo) {
				int index = positionOfMaximalFrequency(frequencies);

				frequencies[index] /= 2;
			}
		}

		return frequencies;
	}

	private static int getNearestPowerOfTwo(int M) {
		int lowerBound = 1;
		int upperBound = 2;
		int result;

		while (M > upperBound) {
			upperBound *= 2;
		}

		lowerBound = upperBound / 2;

		result = Math.abs(M - lowerBound) > Math.abs(M - upperBound) ? upperBound : lowerBound;

		return result;
	}

}