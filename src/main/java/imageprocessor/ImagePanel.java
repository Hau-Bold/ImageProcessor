package imageprocessor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private BufferedImage bufferedimage;

	public void setBufferedimage(BufferedImage bufferedimage) {
		this.bufferedimage = bufferedimage;
		displayImage.setBufferedImage(bufferedimage);
	}

	private ImagePanelMouseListener imagePanelMouseListener;
	private DisplayImage displayImage;

	public BufferedImage getBufferedimage() {
		return bufferedimage;
	}

	public ImagePanel(BufferedImage bufferedimage, DisplayImage displayImage) {
		this.bufferedimage = bufferedimage;
		this.displayImage = displayImage;
		this.setLayout(null);
		this.setPreferredSize(new Dimension(bufferedimage.getWidth(), bufferedimage.getHeight()));

		imagePanelMouseListener = new ImagePanelMouseListener(bufferedimage, this);

		this.addMouseListener(imagePanelMouseListener);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(bufferedimage, 0, 0, ImageProcessor.scrollPane);
	}

}
