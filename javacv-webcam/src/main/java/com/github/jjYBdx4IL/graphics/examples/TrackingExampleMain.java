package com.github.jjYBdx4IL.graphics.examples;

import java.util.Locale;
import javax.swing.JFrame;
import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Point2f;
import org.bytedeco.javacpp.opencv_core.Point2fVector;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_video;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://docs.opencv.org/trunk/d7/d8b/tutorial_py_lucas_kanade.html
 * http://stackoverflow.com/questions/15623128/opencvs-calcopticalflowpyrlk-throws-exception
 *
 * @author jjYBdx4IL
 */
public class TrackingExampleMain {

    // http://stackoverflow.com/questions/14125758/javacv-ffmpegframerecorder-properties-explanation-needed
    public static final int AV_PIX_FMT_YUV420P = 0;
    public static final int AV_PIX_FMT_RGB24 = 2;
    public static final int AV_PIX_FMT_BGR24 = 3;

    private static final Logger LOG = LoggerFactory.getLogger(TrackingExampleMain.class);

    public TrackingExampleMain() {
    }

    public static void main(String[] args) {
        try {
            new TrackingExampleMain().run("C:\\Users\\work\\Downloads\\video.avi");
        } catch (FrameGrabber.Exception | InterruptedException ex) {
            LOG.error("", ex);
        }
        LOG.info("done.");
    }

    private void run(String videoFileName) throws FrameGrabber.Exception, InterruptedException {
        OpenCVFrameConverter.ToMat cvt = new OpenCVFrameConverter.ToMat();
        CanvasFrame canvas = new CanvasFrame("JavaCV Tracking");
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FrameGrabber grabber = new FFmpegFrameGrabber(videoFileName);
        grabber.setPixelFormat(AV_PIX_FMT_RGB24);
        grabber.start();
        IplImage imgGray = null;
        Frame frame = grabber.grab();
        Frame lastFrame = null;
        Mat featuresPrevious = null;
        Mat featuresCurrent = null;
        int frameCount = 0;
        Point2fVector prevVec = null;
        while (frame != null) {
            LOG.info(String.format(Locale.ROOT, "dim: %d x %d", frame.imageWidth, frame.imageHeight));

            if (frame.imageHeight < 1 || frame.imageHeight < 1) {
                LOG.info("skipping frame");
                frame = grabber.grab();
                continue;
            }

            frameCount++;

            //canvas.showImage(frame);
            //Thread.sleep(3000L);
            //Mat m = cvt.convert(frame);
            if (imgGray == null) {
                imgGray = opencv_core.cvCreateImage(opencv_core.cvSize(frame.imageWidth, frame.imageHeight), IPL_DEPTH_8U, 1);
            }
            IplImage img = cvt.convertToIplImage(frame);
            opencv_imgproc.cvCvtColor(img, imgGray, opencv_imgproc.CV_RGB2GRAY);

            featuresCurrent = new Mat();
            opencv_imgproc.goodFeaturesToTrack(cvt.convert(cvt.convert(imgGray)), featuresCurrent, 100, 0.3d, 7d);

            Mat err = new Mat();
            Mat status = new Mat();
            if (lastFrame != null) {
                opencv_video.calcOpticalFlowPyrLK(cvt.convert(lastFrame), cvt.convert(frame), featuresPrevious, featuresCurrent, status, err);
                LOG.info(featuresCurrent.toString());
                Point2fVector vec = toPoint2fArray(featuresCurrent);
                LOG.info(String.format(Locale.ROOT, "vec size: %d", vec.size()));
                if (vec.size() > 0) {
                    LOG.info(String.format(Locale.ROOT, "vec[0]: %f", vec.get(0).get(0)));
                }
                for (int i=0; i<vec.size() && prevVec != null && i<prevVec.size(); i++) {
                    Point p1 = new Point(Math.round(prevVec.get(i).x()), Math.round(prevVec.get(i).y()));
                    Point p2 = new Point(Math.round(vec.get(i).x()), Math.round(vec.get(i).y()));
                    LOG.info(String.format(Locale.ROOT, "line %d,%d -> %d,%d", p1.x(), p1.y(), p2.x(), p2.y()));
                    opencv_imgproc.line(cvt.convert(cvt.convert(imgGray)), p1, p2, Scalar.BLACK, 2, 0, 0);
                    opencv_imgproc.circle(cvt.convert(cvt.convert(imgGray)), p2, 10, Scalar.BLACK);
                }
                prevVec = vec;
            }
            //canvas.showImage(frame);

            canvas.showImage(cvt.convert(imgGray));

            Frame temp = lastFrame;
            lastFrame = frame.clone(); // uh, why not just switch pointers?
            frame = lastFrame;

            featuresPrevious = featuresCurrent;
            frame = grabber.grab();
        }
        grabber.stop();
        canvas.dispose();

        LOG.info(String.format(Locale.ROOT, "frames processed: %d", frameCount));
    }

    public static Point2fVector toPoint2fArray(Mat mat) {
        if (mat.checkVector(2) < 0) {
            throw new IllegalArgumentException("Expecting a vector Mat");
        }

        FloatIndexer indexer = mat.createIndexer();
        long size = mat.total();
        Point2fVector dest = new Point2fVector(size);

        for (long i = 0; i < size; i++) {
            dest.put(i, new Point2f(indexer.get(0, i, 0), indexer.get(0, i, 1)));
        }

        return dest;
    }
}
