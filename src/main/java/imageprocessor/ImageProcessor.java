package imageprocessor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class ImageProcessor extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	public static JScrollPane scrollPane;
	private JButton loadImageButton;
	static BufferedImage bufferedImage;
	private JPanel panelMain;

	/**
	 * Constructor.
	 */
	public ImageProcessor() {
		initComponents();
	}

	private void initComponents() {

		/** Setting the Layout */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (InstantiationException e2) {
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			e2.printStackTrace();
		} catch (UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		}

		this.setBounds(50, 50, 200, 100);
		this.setTitle(Constants.TITLE);
		this.setLayout(new BorderLayout());

		panelMain = new JPanel();
		panelMain.setLayout(new GridLayout(1, 1));
		panelMain.setBackground(Color.GREEN);

		panelMain.setBorder(BorderFactory.createRaisedBevelBorder());

		this.add(panelMain, BorderLayout.CENTER);

		loadImageButton = new JButton(Constants.LOAD_IMAGE);
		loadImageButton.setSize(new Dimension(35, 35));
		loadImageButton.addActionListener(this);

		panelMain.add(loadImageButton);
	}

	public void showFrame() {

		this.setVisible(Boolean.TRUE);

	}

	public void actionPerformed(ActionEvent event) {
		Object o = event.getSource();

		if (o.equals(loadImageButton)) {

			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle(Constants.SELECT_IMAGE);
			chooser.setDialogType(JFileChooser.FILES_ONLY);
			FileNameExtensionFilter markUpFilter = new FileNameExtensionFilter("Markup: jpg,png", "jpg", "png");
			chooser.setFileFilter(markUpFilter);
			chooser.setBackground(Color.WHITE);

			chooser.setSelectedFile(FileSystemView.getFileSystemView().getHomeDirectory());

			int response = chooser.showDialog(null, null);
			if (response == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();

				try {
					bufferedImage = ImageIO.read(new FileImageInputStream(selectedFile));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				DisplayImage displayImage = new DisplayImage(bufferedImage);
				displayImage.showFrame();

			}
		}

	}

}
