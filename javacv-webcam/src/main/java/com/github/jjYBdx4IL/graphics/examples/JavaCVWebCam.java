package com.github.jjYBdx4IL.graphics.examples;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.VideoInputFrameGrabber;

/**
 *
 * @author jjYBdx4IL
 */
public class JavaCVWebCam implements Runnable {

    //final int INTERVAL=1000;///you may use interval
    IplImage image;
    CanvasFrame canvas = new CanvasFrame("Web Cam");

    public JavaCVWebCam() {
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void run() {
        FrameGrabber grabber = new VideoInputFrameGrabber(0); // 1 for next camera
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        int i = 0;
        try {
            grabber.start();
            IplImage img;
            while (true) {
                Frame frame = grabber.grab();
                img = converter.convert(frame);
                if (img != null) {
                    opencv_core.cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise
                    //opencv_imgcodecs.cvSaveImage((i++)+"-aa.jpg", img);
                    // show image on window
                    canvas.showImage(frame);
                }
                //Thread.sleep(INTERVAL);
            }
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        JavaCVWebCam gs = new JavaCVWebCam();
        Thread th = new Thread(gs);
        th.start();
    }
}
