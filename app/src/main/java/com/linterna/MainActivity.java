package com.linterna;


import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton btnLinterna;
    private boolean linternaOn = false;
    private boolean release   = false;
    private boolean tieneFlash;
    private Camera objCamara;
    Camera.Parameters parametrosCamara;

// un comentario.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("");

        btnLinterna = (FloatingActionButton) findViewById(R.id.fab);
        btnLinterna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 if (objCamara==null){
                     getobjCamara();
                 }
                if(!linternaOn){
                    encenderFlash();
                }else{
                    apagarFlash();
                }
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               //         .setAction("Action", null).show();
            }
        });

        prepararLinterna();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause()
    {
        if( objCamara != null )
        {
            objCamara.release();
            objCamara = null;

        }
        super.onPause();
    }


    private void  getobjCamara(){
            if(objCamara==null){
                try{
                objCamara = Camera.open();
                parametrosCamara = objCamara.getParameters();

                }catch (RuntimeException e){
                    Log.d("debug",e.getMessage());
                    btnLinterna.setEnabled(false);
                }

        }

    }


    private void encenderFlash(){
        if(!linternaOn) {
            if (objCamara == null || parametrosCamara == null) {
                return;
            }


            parametrosCamara = objCamara.getParameters();
            if(parametrosCamara==null){
                return;
            }

            parametrosCamara.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            objCamara.setParameters(parametrosCamara);
            objCamara.startPreview();
            linternaOn = true;

        }

    }


    private void apagarFlash(){
        if(linternaOn){
            if(objCamara==null || parametrosCamara==null){
                return;
            }

            parametrosCamara = objCamara.getParameters();
            if(parametrosCamara==null){
                return;
            }
            parametrosCamara.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            objCamara.setParameters(parametrosCamara);
            objCamara.stopPreview();
            linternaOn = false;

        }

    }

    private void prepararLinterna(){
        tieneFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if(!tieneFlash){
            btnLinterna.setEnabled(false);
            return;
        }else{
            this.getobjCamara();
        }

    }

}
