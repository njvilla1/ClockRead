package com.nvew.clockread;

/*import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;*/
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity{// implements CvCameraViewListener{

	ImageView imgView;
	Bitmap bp = null;
	
	private static final String TAG = "ClockRead";
	
	/*private CameraBridgeViewBase mOpenCvCameraView;
	
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.v(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };*/

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      
      //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
      
      imgView = (ImageView)findViewById(R.id.imageView1);
      imgView.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
        	 Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
             startActivityForResult(intent, 0);
         }
      });
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      // TODO Auto-generated method stub
      super.onActivityResult(requestCode, resultCode, data);
      bp = (Bitmap) data.getExtras().get("data");
      imgView.setImageBitmap(bp);
   }
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

/*@Override
public void onCameraViewStarted(int width, int height) {
	// TODO Auto-generated method stub
	
}

@Override
public void onCameraViewStopped() {
	// TODO Auto-generated method stub
	
}

@Override
public Mat onCameraFrame(Mat inputFrame) {
	// TODO Auto-generated method stub
	return null;
}*/
}