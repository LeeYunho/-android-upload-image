package com.example.uploadimage_android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	Button btnSelect, btnSend;
	final static int SELECT = 1;
	File file;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnSelect = (Button) findViewById(R.id.btnSelect);
        btnSelect.setOnClickListener(new View.OnClickListener() {
 
        	@Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                startActivityForResult(intent, SELECT);
            }
        });
 
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            HttpClient client = new DefaultHttpClient();
                            String url = "http://www.***.com/***.php";
                            HttpPost post = new HttpPost(url);
                            FileBody image = new FileBody(file);
                            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                            reqEntity.addPart("uploadImage", image);
                            post.setEntity(reqEntity);
                            HttpResponse response = client.execute(post);
                            HttpEntity resEntity = response.getEntity();
 
                            /*if (resEntity != null)
                            {
                                Log.i("RESPONSE", EntityUtils.toString(resEntity));
                            }*/
                        }
                        catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Bitmap bitmap = null;
 
        if (resultCode == RESULT_OK && requestCode == SELECT) {
            Uri image = data.getData();
            try {
                bitmap = Images.Media.getBitmap(getContentResolver(), image);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
 
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            OutputStream outStream = null;
            file = new File(extStorageDirectory, "image.png");
            try {
                outStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.flush();
                outStream.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
 
        }
    }

}
