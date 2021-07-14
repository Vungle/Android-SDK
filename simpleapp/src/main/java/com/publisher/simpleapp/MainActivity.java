package com.publisher.simpleapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.vungle.warren.AdConfig;
import com.vungle.warren.InitCallback;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.VungleApiClient;
import com.vungle.warren.error.VungleException;

import java.lang.reflect.Field;
import java.util.Collection;

public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String APP_ID = "5ae0db55e2d43668c97bd65e";

    private String placementID;
    private Spinner spinnerPlacement;
    private ProgressBar progressBar;
    private Button buttonInitAd, buttonLoadAd, buttonPlayAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        spinnerPlacement = findViewById(R.id.spinnerPlacement);
        spinnerPlacement.setOnItemSelectedListener(this);

        progressBar = findViewById(R.id.progressBar);

        buttonInitAd = findViewById(R.id.btnInitAd);
        buttonLoadAd = findViewById(R.id.btnLoadAd);
        buttonPlayAd = findViewById(R.id.btnPlayAd);

        buttonInitAd.setOnClickListener(this);
        buttonLoadAd.setOnClickListener(this);
        buttonPlayAd.setOnClickListener(this);

        setSpinnerAndProgressbarState(false);
        setButtonState(true, false, false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnInitAd) {
            initVungle();
        } else if (v.getId() == R.id.btnLoadAd) {
            loadAd();
        } else if (v.getId() == R.id.btnPlayAd) {
            playAd();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinnerPlacement) {
            String selected = spinnerPlacement.getSelectedItem().toString();
            if (!selected.equalsIgnoreCase(placementID)) {
                placementID = selected;

                if (Vungle.canPlayAd(placementID)) {
                    setButtonState(false, false, true);
                } else {
                    setButtonState(false, true, false);
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //
    }

    private void initVungle() {
        setSpinnerAndProgressbarState(true);
        setButtonState(false, false, false);

        Vungle.init(APP_ID, MainActivity.this.getApplicationContext(), new InitCallback() {
            @Override
            public void onSuccess() {
                setSpinnerAndProgressbarState(false);
                setButtonState(false, true, false);

                Collection<String> placements = Vungle.getValidPlacements();
                String[] placementsArray = placements.toArray(new String[0]);

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, placementsArray);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerPlacement.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();

                placementID = placementsArray[0];
                spinnerPlacement.setSelection(0);
            }

            @Override
            public void onError(VungleException e) {
                setSpinnerAndProgressbarState(false);
                setButtonState(true, false, false);

                showToastMessage("SDK Init Error : " + e.getLocalizedMessage());
            }

            @Override
            public void onAutoCacheAdAvailable(String pid) {
                showToastMessage("Auto Cache Ad Available For Placement : " + pid);
            }
        });
    }

    private void loadAd() {
        setSpinnerAndProgressbarState(true);
        setButtonState(false, false, false);

        Vungle.loadAd(placementID, new LoadAdCallback() {
            @Override
            public void onAdLoad(String id) {
                setSpinnerAndProgressbarState(false);
                setButtonState(false, false, true);
            }

            @Override
            public void onError(String id, VungleException e) {
                setSpinnerAndProgressbarState(false);
                setButtonState(false, true, false);

                showToastMessage("Ad Load Error : " + e.getLocalizedMessage());
            }
        });
    }

    private void playAd() {
        setButtonState(false, false, false);

        Vungle.playAd(placementID, new AdConfig(), new PlayAdCallback() {
            @Override
            public void onAdStart(String placementReferenceID) { showToastMessage("Ad Start"); }

            @Override
            public void onAdViewed(String placementReferenceID) { showToastMessage("Ad Viewed");}

            // Deprecated
            @Override
            public void onAdEnd(String id, boolean completed, boolean isCTAClicked) {
                setButtonState(false, true, false);

                showToastMessage("Ad End : Completed : " + completed + " Clicked : " + isCTAClicked);
            }

            @Override
            public void onAdEnd(String placementReferenceID) {
                showToastMessage("Ad End");
            }

            @Override
            public void onAdClick(String placementReferenceID) {
                showToastMessage("Ad Clicked");
            }

            @Override
            public void onAdRewarded(String placementReferenceID) {
                showToastMessage("User Rewarded");
            }

            @Override
            public void onAdLeftApplication(String placementReferenceID) {
                showToastMessage("User Left Application");
            }

            @Override
            public void creativeId(String creativeId) {
                showToastMessage("Will play creative " + creativeId);
            }

            @Override
            public void onError(String id, VungleException e) {
                setButtonState(false, true, false);

                showToastMessage("Ad Play Error : " + e.getLocalizedMessage());
            }
        });
    }

    private void setButtonState(boolean btnInitState, boolean btnLoadState, boolean btnPlayState) {
        buttonInitAd.setEnabled(btnInitState);
        buttonLoadAd.setEnabled(btnLoadState);
        buttonPlayAd.setEnabled(btnPlayState);
    }

    private void setSpinnerAndProgressbarState(boolean show) {
        spinnerPlacement.setEnabled(!show);
        progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void showToastMessage(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}

