package hackgt.directionapp;

import java.util.Arrays;
import java.util.Collections;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class CameraActivity extends Activity {

    private int counter;

    private final static String TAG = "SimpleCamera";
    private TextureView mTextureView = null;
    private Size mPreviewSize = null;
    private CameraDevice mCameraDevice = null;
    private CaptureRequest.Builder mPreviewBuilder = null;
    private CameraCaptureSession mPreviewSession = null;
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new PreviewDisplay();
    private CameraDevice.StateCallback mStateCallback = new StateCallback();
    private CameraCaptureSession.StateCallback mPreviewStateCallback = new PreviewStateCallback();
    private LayoutInflater cameraOverlay;
    private Drawable overlayArrow;
    private ImageView overlayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        counter = 0;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);

        mTextureView = (TextureView) findViewById(R.id.textureView);
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);

        overlayArrow = ContextCompat.getDrawable(this, R.drawable.arrow);
        cameraOverlay = LayoutInflater.from(getBaseContext());
        View viewControl = cameraOverlay.inflate(R.layout.camera_overlay, null);
        ViewGroup.LayoutParams layoutParamsControl
                = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        this.addContentView(viewControl, layoutParamsControl);
        overlayView = (ImageView) findViewById(R.id.overlayView);
        overlayView.setRotation(90);
        overlayView.setImageDrawable(overlayArrow);
    }

    private double getDistanceToTarget() {
        final double DISTANCE = 500;
        double dist = DISTANCE - counter;
        counter++;
        return dist;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mCameraDevice != null)
        {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    class PreviewDisplay implements TextureView.SurfaceTextureListener {
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            // Log.i(TAG, "onSurfaceTextureUpdated()");
            Log.d(TAG, surface.toString());
            double newDist = getDistanceToTarget();
            float newScaleX = (float) (1 - newDist / 500);
            float newScaleY = (float) (1 - newDist / 500);
            Log.d(TAG, newScaleX + ", " + newScaleY);
            overlayView.setScaleX(newScaleX);
            overlayView.setScaleY(newScaleY);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
                                                int height) {
            Log.i(TAG, "onSurfaceTextureSizeChanged(" + width + ", " + height + ")");

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.i(TAG, "onSurfaceTextureDestroyed()");
            return false;
        }

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
                                              int height) {
            Log.i(TAG, "onSurfaceTextureAvailable(" + width + ", " + height + ")");

            CameraManager manager = (CameraManager) getSystemService(CAMERA_SERVICE);
            try{
                String cameraId = manager.getCameraIdList()[0];
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                StreamConfigurationMap map =
                        characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                mPreviewSize = map.getOutputSizes(SurfaceTexture.class)[0];

                Log.d(TAG, mPreviewSize.toString());

                manager.openCamera(cameraId, mStateCallback, null);
            }
            catch(CameraAccessException e)
            {
                e.printStackTrace();
            }

        }
    }

    class StateCallback extends CameraDevice.StateCallback {
        @Override
        public void onOpened(CameraDevice camera) {
            Log.i(TAG, "onOpened");
            mCameraDevice = camera;

            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            if (texture == null) {
                Log.e(TAG, "texture is null");
                return;
            }

            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface surface = new Surface(texture);

            try {
                mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            } catch (CameraAccessException e){
                e.printStackTrace();
            }

            mPreviewBuilder.addTarget(surface);

            try {
                mCameraDevice.createCaptureSession(Collections.singletonList(surface),
                        mPreviewStateCallback, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Log.e(TAG, "onError");
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            Log.e(TAG, "onDisconnected");
        }
    }

    class PreviewStateCallback extends CameraCaptureSession.StateCallback {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            Log.i(TAG, "onConfigured");
            mPreviewSession = session;

            mPreviewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            HandlerThread backgroundThread = new HandlerThread("CameraPreview");
            backgroundThread.start();
            Handler backgroundHandler = new Handler(backgroundThread.getLooper());

            try {
                mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, backgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {
            Log.e(TAG, "CameraCaptureSession Configure failed");
        }
    }
}
