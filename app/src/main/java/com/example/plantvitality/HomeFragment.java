package com.example.plantvitality;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.getIntent;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.example.plantvitality.ml.FinalModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collections;


public class HomeFragment extends Fragment {

   RelativeLayout rl_intro1,rl_intro2;
   LottieAnimationView cam,gal;
   Button b1;
   ImageView leafImg;
   public static final int RequestPermissionCode = 1;
    int imageSize = 224;
    Bitmap img;
TextView result,about;
    AlertDialog.Builder builder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rl_intro1=view.findViewById(R.id.rl_intro1);
        rl_intro2=view.findViewById(R.id.rl_intro2);
        cam=view.findViewById(R.id.cam);
        gal=view.findViewById(R.id.gal);
        leafImg=view.findViewById(R.id.leafImg);
        result=view.findViewById(R.id.result);
        about=view.findViewById(R.id.ss);
        builder = new AlertDialog.Builder(getContext());

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // builder.setMessage("Its the result of the") .setTitle("Fertilizers:");
                builder.setMessage(R.string.spot)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //finish();
                                Toast.makeText(getContext(),"you choose yes action for alertbox",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Toast.makeText(getContext(),"you choose no action for alertbox",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Fertilizers:🌱");
                alert.show();
            }
        });
        EnableRuntimePermission(); // to get initial permission

        // if my camera chosen
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
        });

        //if my gallery chosen
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });


        // to change the first layout to next
        b1=view.findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_intro1.setVisibility(View.INVISIBLE);
                rl_intro2.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    // for the camera permission
    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                android.Manifest.permission.CAMERA)) {
            Toast.makeText(getContext(),"CAMERA permission allows us to Access CAMERA app",     Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
        super.onRequestPermissionsResult(requestCode, permissions, result);
        switch (requestCode) {
            case RequestPermissionCode:
                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    // set up img on selecting image pickers
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {

                    Bitmap bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    int dimension = Math.min(bitmap.getWidth(),bitmap.getHeight()); // to fit the screen
                    bitmap= ThumbnailUtils.extractThumbnail(bitmap,dimension,dimension);// to fit the screen
                    leafImg.setImageBitmap(bitmap);
                   // classifyImage(bitmap);

                    img = Bitmap.createScaledBitmap(bitmap,224,224,true);

                    try {
                        FinalModel model = FinalModel.newInstance(getContext());

                        // Creates inputs for reference.
                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

                        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                        tensorImage.load(img);
                        ByteBuffer byteBuffer = tensorImage.getBuffer();

                        inputFeature0.loadBuffer(byteBuffer);

                        // Runs model inference and gets result.
                        FinalModel.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                        float[] confidence = outputFeature0.getFloatArray();

                        // Releases model resources if no longer used.
                        model.close();

                        String[] classess = { "Tomato_Bacterial_spot", "Tomato_Early_blight", "Tomato_Late_blight", "Tomato_Leaf_Mold", "Tomato_Septoria_leaf_spot", "Tomato_Spider_mites Two-spotted_spider_mite", "Tomato_Target_Spot", "Tomato_Yellow_Leaf_Curl_Virus", "Tomato_mosaic_virus", "Tomato_healthy"};

                        int maxPos=0;
                        float maxConfidence=0;
                        for(int i=0;i<confidence.length;i++){
                            if(confidence[i]>maxConfidence){
                                maxConfidence=confidence[i];
                                maxPos=i;
                            }
                        }

                        //result.setText(outputFeature0.getFloatArray()[0] + "\n"+outputFeature0.getFloatArray()[1]+"\n"+outputFeature0.getFloatArray()[2] + "\n"+outputFeature0.getFloatArray()[3] + "\n"+outputFeature0.getFloatArray()[4] + "\n"+outputFeature0.getFloatArray()[5] + "\n"+outputFeature0.getFloatArray()[6] + "\n"+outputFeature0.getFloatArray()[7] + "\n"+outputFeature0.getFloatArray()[8] + "\n"+outputFeature0.getFloatArray()[9] + "\n"+classess[maxPos]);
                        result.setText(classess[maxPos]);
                        result.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // to search the disease on internet
                                startActivity(new Intent((Intent.ACTION_VIEW),Uri.parse("https://www.google.com/search?q="+result.getText())));
                            }
                        });
                    } catch (IOException e) {
                        // TODO Handle the exception
                    }

                }
                break;

            case 1:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap;
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        // don't forget its , getContext().getContentResolver() , in fragments ***.

                        bitmap=MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),selectedImage);
                        int dimension = Math.min(bitmap.getWidth(),bitmap.getHeight());
                        bitmap= ThumbnailUtils.extractThumbnail(bitmap,dimension,dimension);
                        //classifyImage(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    leafImg.setImageBitmap(bitmap);

                   // classifyImage(bitmap);
                    img = Bitmap.createScaledBitmap(bitmap,224,224,true);

                    try {
                        FinalModel model = FinalModel.newInstance(getContext());

                        // Creates inputs for reference.
                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

                        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                        tensorImage.load(img);
                        ByteBuffer byteBuffer = tensorImage.getBuffer();

                        inputFeature0.loadBuffer(byteBuffer);

                        // Runs model inference and gets result.
                        FinalModel.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                        float[] confidence = outputFeature0.getFloatArray();

                        // Releases model resources if no longer used.
                        model.close();

                        String[] classess = { "Tomato_Bacterial_spot", "Tomato_Early_blight", "Tomato_Late_blight", "Tomato_Leaf_Mold", "Tomato_Septoria_leaf_spot", "Tomato_Spider_mites Two-spotted_spider_mite", "Tomato_Target_Spot", "Tomato_Yellow_Leaf_Curl_Virus", "Tomato_mosaic_virus", "Tomato_healthy"};

                        int maxPos=0;
                        float maxConfidence=0;
                        for(int i=0;i<confidence.length;i++){
                            if(confidence[i]>maxConfidence){
                                maxConfidence=confidence[i];
                                maxPos=i;
                            }
                        }

                       // result.setText(outputFeature0.getFloatArray()[0] + "\n"+outputFeature0.getFloatArray()[1]+"\n"+outputFeature0.getFloatArray()[2] + "\n"+outputFeature0.getFloatArray()[3] + "\n"+outputFeature0.getFloatArray()[4] + "\n"+outputFeature0.getFloatArray()[5] + "\n"+outputFeature0.getFloatArray()[6] + "\n"+outputFeature0.getFloatArray()[7] + "\n"+outputFeature0.getFloatArray()[8] + "\n"+outputFeature0.getFloatArray()[9] + "\n"+classess[maxPos]);
                        result.setText(classess[maxPos]);
                        result.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // to search the disease on internet
                                startActivity(new Intent((Intent.ACTION_VIEW),Uri.parse("https://www.google.com/search?q="+result.getText())));
                            }
                        });

                    } catch (IOException e) {
                        // TODO Handle the exception
                    }
                }
                break;
        }
    }

    private void classifyImage(Bitmap bitmap) {
        try{
            //DiseaseDetection model = DiseaseDetection.newInstance(getActivity().getApplicationContext());
            //TfLiteModel model = TfLiteModel.newInstance(getActivity().getApplicationContext());
            FinalModel model = FinalModel.newInstance(getContext().getApplicationContext());

            TensorBuffer inputFeature0=TensorBuffer.createFixedSize(new int[]{1,224,224,3}, DataType.FLOAT32);
            ByteBuffer byteBuffer=ByteBuffer.allocateDirect(4*imageSize*imageSize*3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValue =new int[imageSize*imageSize];
            //bitmap.getPixels(intValue,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());

            int pixel=0;
            for(int i=0;i<imageSize;i++){
                for(int j=0;j<imageSize;j++){
                    int val = intValue[pixel++];
                    byteBuffer.putFloat(((val>>16)&0xFF)*(1.f/255.f));
                    byteBuffer.putFloat(((val>>8)&0xFF)*(1.f/255.f));
                    byteBuffer.putFloat((val &0xFF)*(1.f/255.f));

                }
            }
            inputFeature0.loadBuffer(byteBuffer);


            FinalModel.Outputs outputs = model.process(inputFeature0);
            //run model interface and get results
           // TfLiteModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeatures0=outputs.getOutputFeature0AsTensorBuffer();

            float[] confidence= outputFeatures0.getFloatArray();
//            Arrays.sort(confidence);

            // find the index of the class with the biggest confidence
            int maxPos=0;
            float maxConfidence=0;
            for(int i=0;i<confidence.length;i++){
                if(confidence[i]>maxConfidence){
                    maxConfidence=confidence[i];
                    maxPos=i;
                }
            }
            // getting classes from vlaues folder , strings.xml
            Resources res = getResources();
            //String[] classess = res.getStringArray(R.array.classes);
            String[] classess = {
                    "Tomato Bacterial spot",
                    "Tomato Early_blight",
                    "Tomato healthy",
                    "Tomato Late_blight",
                    "Tomato Leaf_Mold",
                    "Tomato Septoria leaf_spot",
                    "Tomato Spider_mitesTwo-spotted_spider_mite",
                    "Tomato Target_Spot",
                    "Tomato Tomato_mosaic_virus",
                    "Tomato Tomato_Yellow_Leaf_Curl_Virus"};


            result.setText(classess[maxPos]+" "+maxPos+" "+ maxConfidence);
            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // to search the disease on internet
                    startActivity(new Intent((Intent.ACTION_VIEW),Uri.parse("https://www.google.com/search?q="+result.getText())));
                }
            });
        }catch (IOException e){

        }
    }


}