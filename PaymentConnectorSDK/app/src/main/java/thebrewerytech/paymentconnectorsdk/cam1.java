package thebrewerytech.paymentconnectorsdk;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class cam1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam1);
        ImageView fail = (ImageView) findViewById(R.id.sad);
        fail.setImageResource(R.drawable.sad);
    }
    public void open_camera(View v){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                Intent i = new Intent(this, displaysuccess.class);
                startActivity(i);
            }
        }
    }


}
