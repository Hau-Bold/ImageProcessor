package imageprocessor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;

import imageUtils.Utils;
import statistics.ShowStatisticsDialog;

public class DisplayImage extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private BufferedImage bufferedImage;
	private ImagePanel imagePanel;
	private JScrollPane scrollPane;
	private JPanel panelSouth;
	private JButton buttonSaveImage, buttonSelectColor, buttonGenerateStatistics, buttonGenerateBalancedStatistics,
			buttonGeneratePowerOfTwoStatistics, buttonSharpImage;
	private JLabel labelColor = new JLabel("Choose Color");
	public static Color color;
	private static JComboBox<String> combobox_Preconditioners;

	public DisplayImage(BufferedImage bufferedImage) {

		this.bufferedImage = bufferedImage;

		initComponent();
	}

	private void initComponent() {

		this.setLocation(50, 50);
		this.setSize(bufferedImage.getWidth() + 50, bufferedImage.getHeight() + 100);
		this.setMinimumSize(new Dimension(400, 400));
		this.setLayout(new BorderLayout());
		this.setResizable(true);
		this.getContentPane().setBackground(Color.WHITE);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});

		/** save image in scrollPane */
		scrollPane = new JScrollPane();
		// Vertikales Scrollen
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// Horizontales Scrollen
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
		this.imagePanel = new ImagePanel(bufferedImage, this);
		scrollPane.setColumnHeaderView(imagePanel);
		this.add(scrollPane, BorderLayout.NORTH);

		buttonSaveImage = new JButton(Constants.SAVE_IMAGE);
		buttonSaveImage.addActionListener(this);

		buttonSelectColor = new JButton(Constants.SELECT_COLOR);
		buttonSelectColor.addActionListener(this);

		combobox_Preconditioners = new JComboBox<String>(Constants.ComboBoxItems);
		combobox_Preconditioners.addActionListener(this);

		buttonGenerateStatistics = new JButton(Constants.GENERATE_STATISTICS);
		buttonGenerateStatistics.addActionListener(this);

		buttonGenerateBalancedStatistics = new JButton(Constants.GENERATE_BALACED_STATISTCS);
		buttonGenerateBalancedStatistics.addActionListener(this);

		buttonGeneratePowerOfTwoStatistics = new JButton(Constants.GENERATE_POWEROFTWO_STATISTCS);
		buttonGeneratePowerOfTwoStatistics.addActionListener(this);

		buttonSharpImage = new JButton(Constants.SHARP_IMAGE);
		buttonSharpImage.addActionListener(this);

		panelSouth = new JPanel(new GridLayout(1, 7));

		panelSouth.add(buttonSaveImage);
		panelSouth.add(buttonSelectColor);
		panelSouth.add(combobox_Preconditioners);
		panelSouth.add(buttonGenerateStatistics);
		panelSouth.add(buttonGenerateBalancedStatistics);
		panelSouth.add(buttonGeneratePowerOfTwoStatistics);
		panelSouth.add(buttonSharpImage);

		this.add(panelSouth, BorderLayout.SOUTH);

	}

	// @Deprecated
	// private Component getEmptyPanel() {
	//
	// JPanel response = new JPanel();
	// response.setBackground(Color.RED);
	// response.setOpaque(Boolean.TRUE);
	//
	// return response;
	// }

	public void showFrame() {

		this.setVisible(Boolean.TRUE);

	}

	public void actionPerformed(ActionEvent event) {

		Object o = event.getSource();

		if (o.equals(buttonSaveImage)) {

			byte[] byteArray = Utils.convertBufferedImageToByteArry(bufferedImage);

			try {
				Utils.moveImageToDesktop(
						FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "test.jpg", byteArray);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		else if (o.equals(buttonSelectColor)) {
			color = JColorChooser.showDialog(null, labelColor.getText(), Color.BLACK);
		}

		else if (o.equals(combobox_Preconditioners)) {
			int index = combobox_Preconditioners.getSelectedIndex();

			switch (index) {

			case 0:

				this.bufferedImage = ImageProcessor.bufferedImage;
				imagePanel.setBufferedimage(bufferedImage);
				imagePanel.repaint();
				this.bufferedImage = ImageProcessor.bufferedImage;

				break;

			case 1:
				imagePanel.setBufferedimage(Utils.precondition_Sub(bufferedImage));
				imagePanel.repaint();

				break;

			case 2:
				imagePanel.setBufferedimage(Utils.precondition_UP(bufferedImage));
				imagePanel.repaint();

				break;

			case 3:
				imagePanel.setBufferedimage(Utils.precondition_AVERAGE2(bufferedImage));
				imagePanel.repaint();
				break;

			case 4:
				imagePanel.setBufferedimage(Utils.precondition_AVERAGE3(bufferedImage));
				imagePanel.repaint();
				break;

			case 5:
				imagePanel.setBufferedimage(Utils.precondition_AVERAGE4(bufferedImage));
				imagePanel.repaint();
				break;

			case 6:
				imagePanel.setBufferedimage(Utils.precondition_AVERAGE5(bufferedImage));
				imagePanel.repaint();
				break;

			case 7:
				imagePanel.setBufferedimage(Utils.precondition_AVERAGE6(bufferedImage));
				imagePanel.repaint();
				break;

			case 8:
				imagePanel.setBufferedimage(Utils.precondition_AVERAGE7(bufferedImage));
				imagePanel.repaint();
				break;

			case 9:
				imagePanel.setBufferedimage(Utils.precondition_AVERAGE8(bufferedImage));
				imagePanel.repaint();
				break;

			case 10:
				imagePanel.setBufferedimage(Utils.precondition_PAETH(bufferedImage));
				imagePanel.repaint();
				break;

			default:
				/** do nothing */
				break;

			}
		}

		else if (o.equals(buttonGenerateStatistics)) {

			final int[] frequencies = Utils.countFrequencies(bufferedImage);

			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					new ShowStatisticsDialog(frequencies).setVisible(Boolean.TRUE);

				}
			});

		}

		else if (o.equals(buttonGenerateBalancedStatistics)) {

			int[] frequencies = Utils.countFrequencies(bufferedImage);

			frequencies = Utils.balanceFrequencies(frequencies);

			ShowStatisticsDialog dialog = new ShowStatisticsDialog(frequencies);
			dialog.setVisible(Boolean.TRUE);

		}

		else if (o.equals(buttonGeneratePowerOfTwoStatistics)) {

			int[] frequencies = Utils.countFrequencies(bufferedImage);

			frequencies = Utils.balanceToPowerOfTwoFrequencies(frequencies);

			ShowStatisticsDialog dialog = new ShowStatisticsDialog(frequencies);
			dialog.setVisible(Boolean.TRUE);
		}

		else if (o.equals(buttonSharpImage)) {

			imagePanel.setBufferedimage(Utils.sharpenImage(bufferedImage));
			imagePanel.repaint();
		}

	}

	// set & get follows below here
	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}

}
