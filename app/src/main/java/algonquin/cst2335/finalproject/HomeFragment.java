package algonquin.cst2335.finalproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    FloatingActionButton fabMain;
    boolean isMenuVisible = false;

    EditText height,width;
    Button btnGenerate;
    ImageView imgview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);


        fabMain = fragmentView.findViewById(R.id.fabMain);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu();
            }
        });


        height=fragmentView.findViewById(R.id.heightEDT);
        width=fragmentView.findViewById(R.id.widthEDT);
        btnGenerate=fragmentView.findViewById(R.id.generateBTN);
        imgview=fragmentView.findViewById(R.id.ImageMain);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(height.getText().toString().isEmpty() || width.getText().toString().isEmpty())
                {
                    Toast.makeText(requireContext(), "Please Enter Height and Width", Toast.LENGTH_SHORT).show();
                }
                else {
                    generateImage();
                }
            }
        });
        return fragmentView;
    }

    private void toggleMenu() {
        if (isMenuVisible) {
            hideMenu();
        } else {
            showMenu();
        }
    }

    private void showMenu() {
        isMenuVisible = true;
        View menuView = getLayoutInflater().inflate(R.layout.bottom_sheet_menu, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(menuView);

        Button fabSteps = menuView.findViewById(R.id.fabSteps);
        Button fabStep = menuView.findViewById(R.id.fabStep);

        fabSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStepsBottomSheet();
                bottomSheetDialog.dismiss();
            }
        });

        fabStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguagesBottomSheet();
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    private void hideMenu() {
        isMenuVisible = false;
    }

    private void showStepsBottomSheet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(
                "Step 1: Enter the height and width of the image you want to generate.\n" +
                "Step 2: Click on the 'Generate Image' button to load the image.\n" +
                "Step 3: The generated image will be displayed below.\n"+
                "Step 4: You can check all the generated images on the 'Images' Page.");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        generatingDialog = builder.create();
        generatingDialog.show();
    }

    private void generateImage() {
        String heightText = height.getText().toString();
        String widthText = width.getText().toString();
        int imgHeight = Integer.parseInt(heightText);
        int imgWidth = Integer.parseInt(widthText);

        showGeneratingDialog();
        String URL="https://placebear.com/"+imgHeight+"/"+imgWidth;
        Picasso.get()
                .load(URL)
                .into(imgview, new Callback() {
                    @Override
                    public void onSuccess() {
                        dismissGeneratingDialog();
                        showToast("Image loaded successfully!");
                        saveImageDetailsToFirebase(URL, imgHeight, imgWidth);
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.e("Error:",e.toString());
                        dismissGeneratingDialog();
                    }
                });
        resizeImageView(imgHeight, imgWidth);
    }

    private void saveImageDetailsToFirebase(String imageUrl, int imgHeight, int imgWidth) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("images");
        String key = databaseReference.push().getKey();
        Map<String, Object> imageValues = new HashMap<>();
        imageValues.put("imageUrl", imageUrl);
        imageValues.put("height", imgHeight);
        imageValues.put("width", imgWidth);
        databaseReference.child(key).setValue(imageValues);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Dialog generatingDialog;
    private void showGeneratingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Generating image, please wait...");
        builder.setCancelable(false);
        generatingDialog = builder.create();
        generatingDialog.show();
    }

    private void dismissGeneratingDialog() {
        if (generatingDialog != null && generatingDialog.isShowing()) {
            generatingDialog.dismiss();
        }
    }

    private void resizeImageView(int imgHeight, int imgWidth) {
        ViewGroup.LayoutParams layoutParams = imgview.getLayoutParams();
        layoutParams.height = imgHeight;
        layoutParams.width = imgWidth;
        imgview.setLayoutParams(layoutParams);
    }


    private void showLanguagesBottomSheet() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_language, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(bottomSheetView);

        Button btnEnglishUK = bottomSheetView.findViewById(R.id.btnEnglishUK);
        Button btnEnglishUSA = bottomSheetView.findViewById(R.id.btnEnglishUSA);

        btnEnglishUK.setOnClickListener(this::onLanguageSelected);
        btnEnglishUSA.setOnClickListener(this::onLanguageSelected);
        bottomSheetDialog.show();
    }

    public void onLanguageSelected(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btnEnglishUK) {
            setLocale("en", "GB");
        } else if (viewId == R.id.btnEnglishUSA) {
            setLocale("en", "US");
        }
    }

    private void setLocale(String languageCode, String countryCode) {
        Locale locale = new Locale(languageCode, countryCode);
        Resources res = getResources();
        Configuration config = res.getConfiguration();
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());
        requireActivity().recreate();
        Toast.makeText(requireContext(), "Language Changed Successfully!!", Toast.LENGTH_LONG).show();
    }
}