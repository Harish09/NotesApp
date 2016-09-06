package harish.notesui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


/**
 * Created by Harish on 02/09/16.
 */
public class uploadhandler extends Activity{

    EditText name;
    EditText status;
    EditText drivelink;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploaddetails);
    }

    public void submit(View view) {
        name=(EditText)findViewById(R.id.name);
        status=(EditText)findViewById(R.id.status);
        drivelink=(EditText)findViewById(R.id.drivelink);



    }
}
