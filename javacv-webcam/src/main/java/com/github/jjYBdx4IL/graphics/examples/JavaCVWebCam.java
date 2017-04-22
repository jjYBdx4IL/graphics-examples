package com.github.jjYBdx4IL.graphics.examples;

import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgproc;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;
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
    CanvasFrame canvas = new CanvasFrame("JavaCV Webcam");
    ParamInputFrame params = new ParamInputFrame();

    public JavaCVWebCam() {
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        params.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void run() {
        FrameGrabber grabber = new VideoInputFrameGrabber(0); // 1 for next camera
        //OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        OpenCVFrameConverter.ToMat cvt = new OpenCVFrameConverter.ToMat();
        int i = 0;
        try {
            grabber.start();
            IplImage img;
            IplImage imgGray = null;
            while (true) {
                Frame frame = grabber.grab();
                //img = cvt.convertToIplImage(frame);
                if (frame != null) {
                    Mat m = cvt.convert(frame);
                    //opencv_imgproc.GaussianBlur(m, m, new opencv_core.Size(3, 3), 15d);

                    //Mat frameMat = cvt.convert(frame);
                    //Mat sobelX = new Mat();
                    //Sobel(frameMat, sobelX, CV_32F, 1, 0);
                    if (imgGray == null) {
                        imgGray = opencv_core.cvCreateImage(opencv_core.cvSize(frame.imageWidth, frame.imageHeight), IPL_DEPTH_8U, 1);
                    }
                    img = cvt.convertToIplImage(cvt.convert(m));
                    opencv_imgproc.cvCvtColor(img, imgGray, CV_RGB2GRAY);

                    //opencv_core.cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise
                    //opencv_imgproc.cvSobel(img, img, 1, 1);
                    opencv_imgproc.cvCanny(imgGray, imgGray, params.threshold1(), params.threshold2());

                    opencv_imgproc.cvDilate(imgGray, imgGray);

                    //opencv_imgcodecs.cvSaveImage((i++)+"-aa.jpg", img);
                    // show image on window
                    canvas.showImage(cvt.convert(imgGray));
                    //canvas.showImage(cvt.convert(sobelX));
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
