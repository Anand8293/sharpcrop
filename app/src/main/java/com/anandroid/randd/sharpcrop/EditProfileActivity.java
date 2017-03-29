package com.anandroid.randd.sharpcrop;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.anandroid.randd.utils.Constants;
import com.anandroid.randd.utils.UserPrefs;
import com.anandroid.randd.utils.Utils;
import com.anandroid.randd.web.GetServices;
import com.anandroid.randd.web.ServiceEvents;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.internal.FacebookRequestErrorClassification.KEY_NAME;

public class EditProfileActivity extends BaseActivity implements ServiceEvents{

    private static final int SELECT_PICTURE = 3;
    private static final int TAKE_PICTURE = 1;
    CircleImageView profile_image;
    File profileFile;
    ProgressDialog pd;
    Bitmap proImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        profile_image  = (CircleImageView)findViewById(R.id.profile_image);
        setClick(R.id.profile_changer);
        setClick(R.id.done);
        setClick(R.id.cancel);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId())
        {
            case R.id.profile_changer:
                selectImage();
                break;
            case R.id.done:
                if (!isEmpty())
                    uploadImage();
                break;
            case R.id.cancel:
                finish();
                break;
            default:
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    pickImageFromCamera();
                } else if (items[item].equals("Choose from Library")) {
                    pickImageFromGallary();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void pickImageFromGallary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_PICTURE);
    }

    protected void pickImageFromCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                /*create instance of File with name img.jpg*/
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
				/*put uri as extra in intent object*/
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				/*start activity for result pass intent as argument and request code */
        startActivityForResult(intent,TAKE_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                String selectedImagePath;
                Uri selectedImageUri = data.getData();
                selectedImagePath = Utils.getPath(getApplicationContext(),
                        selectedImageUri);
                profileFile = new File(selectedImagePath);

                try {
                   cropCapturedImage(Uri.fromFile(profileFile));
                   profile_image.setImageURI(Uri.parse(String.valueOf(profileFile)));

                } catch (ActivityNotFoundException aNFE) {
                    //display an error message if user device doesn't support
                    String errorMessage = "Sorry - your device doesn't support the crop action!";
                    Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
                // decodeFile(profileImagePath, profileFile);
            }
            else if (requestCode == TAKE_PICTURE) {
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                //Crop the captured image using an other intent
                try {
				/*the user's device may not support cropping*/
                    cropCapturedImage(Uri.fromFile(file));
                } catch (ActivityNotFoundException aNFE) {
                    //display an error message if user device doesn't support
                    String errorMessage = "Sorry - your device doesn't support the crop action!";
                    Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
//            Toast.makeText(getApplicationContext(), requestCode+"", Toast.LENGTH_LONG).show();
            if (requestCode == 2) {
                //Create an instance of bundle and get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap from extras
                proImg = extras.getParcelable("data");
                //set image bitmap to image view

                try {
                    String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            "/GlammedIn/profile_pic";
                    File dir = new File(file_path);
                    if (!dir.exists())
                        dir.mkdirs();
                    profileFile = new File(dir, UserPrefs.getFname(this) + System.currentTimeMillis() + ".jpg");
                    FileOutputStream fOut = new FileOutputStream(profileFile);
                    proImg.compress(Bitmap.CompressFormat.PNG, 50, fOut);
                    profile_image.setImageBitmap(proImg);
                    // os.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void cropCapturedImage(Uri picUri) {
        //call the standard crop action intent
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri of image
        cropIntent.setDataAndType(picUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        //start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, 2);
    }

    public void decodeFile(String filePath, File profileFile) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        final int REQUIRED_SIZE = 1024;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        proImg= BitmapFactory.decodeFile(filePath, o2);
        profile_image.setImageBitmap(proImg);


    }

    private void uploadImage(){
        Utils.LogMessage(Constants.TAG, "User Edit Profile Request::> " + uploadImageRequest()
                + "!!");
        if (Utils.isOnline(this)) {
            GetServices bzzzLoginServices = new GetServices(EditProfileActivity.this, this);
            try {
                bzzzLoginServices.RestCallAsync("", uploadImageRequest());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
            Utils.showDialog(EditProfileActivity.this, "",
                    getString(R.string.no_network));
    }
    private Map uploadImageRequest() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("tag", "edit_profile");
        params.put("name", profileFile.getName());
        params.put("image", getStringImage(proImg));
        params.put("fname", getTextViewText(R.id.fname));
        params.put("lname", getTextViewText(R.id.lname));
        params.put("mobile", getTextViewText(R.id.mobile));
        params.put("email", UserPrefs.getUserUniqueId(EditProfileActivity.this));
        return params;
    }


    @Override
    public void StartedRequest() {
//        Toast.makeText(EditProfileActivity.this,""+profileFile.getName(),Toast.LENGTH_LONG).show();
        pd = ProgressDialog.show(EditProfileActivity.this, "",
                getString(R.string.pls_wait));
        pd.setCancelable(false);
    }

    @Override
    public void Finished(String methodName, Object Data) {
        if (pd != null)
            pd.dismiss();
        String res = (String) Data;

        Log.e(Constants.TAG, " List Response::> " + res);
        if (res.trim().length() > 0) {
          try {
                JSONObject jsonObject = new JSONObject(res);
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                String msg = jsonObject1.getString("success");
                if (msg.equalsIgnoreCase("1")) {
                    JSONObject jsonObject2 = jsonObject.getJSONObject("responsedata");
                    UserPrefs.setProfileImage(jsonObject2.getString("photo"),EditProfileActivity.this);
                    UserPrefs.setName(jsonObject2.getString("fname")+" "+jsonObject2.getString("lname"),EditProfileActivity.this);
                    UserPrefs.setMobile(jsonObject2.getString("mobile"),EditProfileActivity.this);
                    Toast.makeText(EditProfileActivity.this,jsonObject1.getString("msg"),Toast.LENGTH_LONG).show();
                    startActivity(new Intent(EditProfileActivity.this, HomeActivity.class));
                    finish();
                }
                else
                    Toast.makeText(this,jsonObject1.getString("msg"),Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else {
            Utils.showDialog(EditProfileActivity.this, "",
                    "Response not getting from server");
        }
    }

    @Override
    public void FinishedWithException(Exception ex) {
        // TODO Auto-generated method stub
        if (pd != null)
            pd.dismiss();
    }

    @Override
    public void EndedRequest() {
        // TODO Auto-generated method stub
        if (pd != null)
            pd.dismiss();
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private boolean isEmpty() {
        boolean empty = false;
        if (getTextViewText(R.id.fname).toString().trim().length() == 0) {
            Utils.showDialog(EditProfileActivity.this, "", "Enter first name");
            empty = true;
        } else if (getTextViewText(R.id.lname).toString().trim().length() == 0) {
            Utils.showDialog(EditProfileActivity.this, "", "Enter last name");
            empty = true;
        }
        else if (getTextViewText(R.id.mobile).toString().trim().length() == 0) {
            Utils.showDialog(EditProfileActivity.this, "", "Enter last name");
            empty = true;
        }
        return empty;
    }

}
