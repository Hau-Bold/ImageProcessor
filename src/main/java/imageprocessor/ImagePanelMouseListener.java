package imageprocessor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import imageUtils.Utils;

public class ImagePanelMouseListener implements MouseListener {

	private BufferedImage bufferedimage;
	private ImagePanel imagePanel;

	public ImagePanelMouseListener(BufferedImage bufferedimage, ImagePanel imagePanel) {
		this.bufferedimage = bufferedimage;
		this.imagePanel = imagePanel;

	}

	public void mouseClicked(MouseEvent event) {

		Color colorToReplace = new Color(bufferedimage.getRGB(event.getX(), event.getY()));

		BufferedImage argbImage = Utils.makeRGBImage(bufferedimage, colorToReplace);

		imagePanel.setBufferedimage(argbImage);
		imagePanel.repaint();

	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
