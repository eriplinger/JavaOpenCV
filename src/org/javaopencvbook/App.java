package org.javaopencvbook;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.javaopencvbook.utils.ImageProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class App {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	private JFrame frame;
	private JLabel imageLabel;

	public static void main(String[] args) {
		App app = new App();
		app.initGUI();
		app.runMainLoop(args);
	}

	public void initGUI() {
		frame = new JFrame("Camera Input Example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1024, 768);
		imageLabel = new JLabel();
		frame.add(imageLabel);
		frame.setVisible(true);
	}

	public void runMainLoop(String[] args) {
		ImageProcessor imageProcessor = new ImageProcessor();
		Mat webcamMatImage = new Mat();
		Mat filterImage = new Mat();
		Image tempImage;
		VideoCapture capture = new VideoCapture(0);
		capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 1024);
		capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 768);

		Scalar hsvRedLower = new Scalar(0, 100, 100);
		Scalar hsvRedUpper = new Scalar(10, 255, 255);
		Scalar hsvColorLower = new Scalar(160, 100, 100);
		Scalar hsvColorUpper = new Scalar(179, 255, 255);
		Mat loRed = new Mat();
		Mat hiRed = new Mat();
		// LinkedList pts = new LinkedList();

		int erosion_size = 5;
		int dilation_size = 5;

		Mat eroElem = new Mat();
		Mat dilaElem = new Mat();

		if (capture.isOpened()) {
			while (true) {
				capture.read(webcamMatImage);
				if (!webcamMatImage.empty()) {
					// tempImage =
					// imageProcessor.toBufferedImage(webcamMatImage);
					Imgproc.GaussianBlur(webcamMatImage, filterImage, new Size(11, 11), 0);
					Imgproc.cvtColor(filterImage, filterImage, Imgproc.COLOR_BGR2HSV);
					Core.inRange(filterImage, hsvRedLower, hsvRedUpper, loRed);
					Core.inRange(filterImage, hsvColorLower, hsvColorUpper, hiRed);
					Core.add(loRed, hiRed, filterImage);

					eroElem = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
							new Size(2 * erosion_size + 1, 2 * erosion_size + 1));
					Imgproc.erode(filterImage, filterImage, eroElem);
					dilaElem = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
							new Size(2 * dilation_size + 1, 2 * dilation_size + 1));
					Imgproc.dilate(filterImage, filterImage, dilaElem);

					tempImage = imageProcessor.toBufferedImage(filterImage);
					ImageIcon imageIcon = new ImageIcon(tempImage, "Captured video");
					imageLabel.setIcon(imageIcon);
					frame.pack();
				} else {
					System.out.println(" -- Frame not captured -- Break!");
					break;
				}
			}
		} else {
			System.out.println("Couldn't open capture");
		}

	}
}
