package com.example.myfirst3dcube;

import android.content.Context;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
//import es2.learning.ViewPortRenderer;

public class Cubesurfaceview extends GLSurfaceView {

	float touchedX = 0;
	float touchedY = 0;
	Cuberenderer renderer;
    private static int[] listOfSTL = {R.raw.textstl};
    private Uri stlUri;
    private Context appContext;

    public Cubesurfaceview(Context context) {
		super(context);
        appContext = context;

        init();

        byte[] stlBytes = null;
        try {
            stlBytes = getSTLBytes(context, stlUri);
        } catch (Exception e) {
        }

        if (stlBytes == null) {
            Toast.makeText(context, "Error fetching data", Toast.LENGTH_LONG).show();
            return;
        }

        STLObject stlObject = new STLObject(stlBytes, context);

		setEGLContextClientVersion(2);
        setRenderer(renderer = new Cuberenderer(this, stlObject));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			touchedX = event.getX();
			touchedY = event.getY();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			renderer.xAngle += (touchedX - event.getX())/2f;
			renderer.yAngle += (touchedY - event.getY())/2f;
			
			touchedX = event.getX();
			touchedY = event.getY();
		}
		return true;
		
	}

    private void init(){
        shuffleArray(listOfSTL);
        appContext = getContext();
        stlUri = Uri.parse("android.resource://" + appContext.getPackageName() + "/" + listOfSTL[0]);
    }

    static void shuffleArray(int[] ar)
    {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    /**
     * @param context
     * @return
     */
    private byte[] getSTLBytes(Context context, Uri uri) {
        byte[] stlBytes = null;
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            stlBytes = IO.toByteArray(inputStream);
        } catch (IOException e) {
        } finally {
            IO.closeQuietly(inputStream);
        }
        return stlBytes;
    }
}