package com.nvew.clockread;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity {

	ImageView imgView;
	Button btnEdgeDetection;
	Bitmap bp = null;
	Bitmap nbp = null;
	
	String root = Environment.getExternalStorageDirectory().toString();
	
	private static final String TAG = "ClockRead";
	
	File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    File output = new File(dir,"camera.png");
    
    String path =output.getAbsolutePath();
	
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      
      imgView = (ImageView)findViewById(R.id.imageView1);
      imgView.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
        	 Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        	 intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        	 
             startActivityForResult(intent, 0);
         }
      });
      
      btnEdgeDetection = (Button)findViewById(R.id.buttonEdgeDetection);
      btnEdgeDetection.setOnClickListener(new OnClickListener() {
    	  @Override
    	  public void onClick(View v) {

    		  int image_in[][] = new int[bp.getHeight()][bp.getWidth()];

    		  for (int j=0; j < bp.getHeight(); j++){
    	    	  for(int k=0; k < bp.getWidth(); k++){
    	    		  image_in[j][k] = Color.red(nbp.getPixel(k, j));
    	    	  }
    		  }
    		  
    		  int image_out[][] = new int[bp.getHeight()][bp.getWidth()];
    		  
    		  
    		  int g1_filter[][] = { { -1, -1, -1 }, { 0, 0, 0 }, { 1, 1, 1 } };
    		  int g2_filter[][] = { { -1, 0, 1 }, { -1, 0, 1 }, { -1, 0, 1 } };
    		  
    		  int g1_image[][] = new int[bp.getHeight()][bp.getWidth()];
    		  int g2_image[][] = new int[bp.getHeight()][bp.getWidth()];
    		  int y_image[][] = new int[bp.getHeight()][bp.getWidth()];
    		  
    		  int row_delta, col_delta;
    		  
    		  int j, k;
    		  int height = bp.getHeight();
    		  int width = bp.getWidth();
    		  for (j = 0; j<height; j++)
    				for (k = 0; k < width; k++)
    				{
    					if (j == 0 || k == 0 || j == height - 1 || k == width - 1)
    						g1_image[j][k] = 0;
    					else
    					{
    						g1_image[j][k] = 0;

    						for (row_delta = -1; row_delta <= 1; row_delta++)
    							for (col_delta = -1; col_delta <= 1; col_delta++)
    							{
    								g1_image[j][k] += image_in[j + row_delta][k + col_delta] * g1_filter[row_delta + 1][col_delta + 1];
    							}
    					}
    				}
    		  for (j = 0; j<height; j++)
    				for (k = 0; k < width; k++)
    				{
    					if (j == 0 || k == 0 || j == height - 1 || k == width - 1)
    						g2_image[j][k] = 0;
    					else
    					{
    						g2_image[j][k] = 0;

    						for (row_delta = -1; row_delta <= 1; row_delta++)
    						for (col_delta = -1; col_delta <= 1; col_delta++)
    						{
    							g2_image[j][k] += image_in[j + row_delta][k + col_delta] * g2_filter[row_delta + 1][col_delta + 1];
    						}
    					}
    				}
    		  
    		  for (j = 0; j<height; j++)
    				for (k = 0; k < width; k++)
    				{
    					y_image[j][k] = Math.abs(g2_image[j][k]) + Math.abs(g1_image[j][k]);
    				}
    		  
    		  int threshold, old_threshold, lower_sum, lower_count, upper_sum, upper_count;
    			double upper_mean, lower_mean;
    			int delta_threshold = 2;

    			threshold = 50;
    			
    			do
    			{

    				lower_sum = 0;
    				lower_count = 0;
    				upper_sum = 0;
    				upper_count = 0;

    				for (j = 0; j<height; j++)
    					for (k = 0; k<width; k++)
    					{
    						if(y_image[j][k] <= threshold)
    						{
    							lower_sum += y_image[j][k];
    							lower_count++;
    						}
    						else
    						{
    							upper_sum += y_image[j][k];
    							upper_count++;
    						}
    					}

    					if (lower_count == 0)
    					{
    						lower_mean = 0.0;
    					}
    					else
    					{
    						lower_mean = (double)lower_sum / lower_count;
    					}

    					if (upper_count == 0)
    					{
    						upper_mean = 0.0;
    					}
    					else
    					{
    						upper_mean = (double)upper_sum / upper_count;
    					}

    					
    				old_threshold = threshold;
    				threshold = (int) (0.5 * (upper_mean + lower_mean));

    			} while (Math.abs(threshold - old_threshold) >= delta_threshold);
    		  
    			for (j = 0; j<height; j++)
    				for (k = 0; k<width; k++)
    				{
    					if (y_image[j][k] <= threshold)
    					{
    						image_out[j][k] = 0;
    					}
    					else
    					{
    						image_out[j][k] = 255;
    					}
    				}
    		
    			for (j = 0; j<height; j++)
    				for (k = 0; k<width; k++)
    				{
    					nbp.setPixel(k,j,Color.rgb(image_out[j][k], image_out[j][k],image_out[j][k]));
    				}
    			
    			imgView.setImageBitmap(nbp);
    	  }
      });    

   }
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      // TODO Auto-generated method stub
      super.onActivityResult(requestCode, resultCode, data);
      
      bp = BitmapFactory.decodeFile(path);
      
      //return grayscale image
      nbp = bp.copy(Bitmap.Config.ARGB_8888, true);      
      for(int x=0; x < bp.getWidth(); x++){
    	  for(int y = 0; y < bp.getHeight(); y++){
    		  int pixelvalue = bp.getPixel(x, y);
    		  
    		  int rValue = (int) (Color.red(pixelvalue) * .299);
    		  int gValue = (int) (Color.green(pixelvalue) * .587);
    		  int bValue = (int) (Color.blue(pixelvalue) * .114);
    		  
    		  rValue = gValue = bValue = rValue + gValue + bValue;
    		  
    		  nbp.setPixel(x,y,Color.rgb(rValue, gValue, bValue));
    		  
    	  }
      }
      Log.v(TAG, Integer.toString(bp.getWidth()));
      imgView.setImageBitmap(nbp);
   }
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

}