package com.priyank.sendsms.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.priyank.sendsms.Adapter.ContactListAdapter;
import com.priyank.sendsms.Adapter.GroupAdapter;
import com.priyank.sendsms.Constant.AppUtils;
import com.priyank.sendsms.Constant.SharedPreference;
import com.priyank.sendsms.Model.GroupModel;
import com.priyank.sendsms.Model.SmsModel;
import com.priyank.sendsms.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String[] allPermissions;
    final int MULTIPLE_PERMISSIONS = 123;

    FloatingActionButton addFabBtn;
    public TextView noGroupText;
    public RecyclerView recyclerView;
    ArrayList<GroupModel> grpNameList;
    GroupAdapter grpAdapter;

    ContactListAdapter clAdapter;

    boolean searchFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addFabBtn = findViewById(R.id.add_fab_btn);
        noGroupText = findViewById(R.id.no_group_text);
        recyclerView = findViewById(R.id.recycler_view);

        grpNameList = new ArrayList<>();

        allPermissions = new String[]{
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS};

        if (hasPermissions(MainActivity.this, allPermissions)) {
        } else {
            ActivityCompat.requestPermissions(this, allPermissions, MULTIPLE_PERMISSIONS);
        }

        if (SharedPreference.getPreferences(MainActivity.this).contains("GroupList")) {
            grpNameList = SharedPreference.getPreferenceList(MainActivity.this, "GroupList");
        }

        if (grpNameList.size() == 0) {
            noGroupText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noGroupText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        grpAdapter = new GroupAdapter(MainActivity.this, grpNameList);
        recyclerView.setAdapter(grpAdapter);

        addFabBtn.setOnClickListener(this);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                             ActivityCompat.requestPermissions(MainActivity.this, allPermissions, MULTIPLE_PERMISSIONS);
                            return;
                        }
                    }
                    AppUtils.Toast(MainActivity.this, "Permission Allowed...");
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, allPermissions, MULTIPLE_PERMISSIONS);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_fab_btn:
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_group_layout);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                final EditText groupName = dialog.findViewById(R.id.group_name);
                final EditText searchName = dialog.findViewById(R.id.search_name);
                final RecyclerView recyclerView2 = dialog.findViewById(R.id.recycler_view2);
                ImageButton filterBtn = dialog.findViewById(R.id.filter_btn);
                Button addBtn = dialog.findViewById(R.id.add_btn);

                groupName.setVisibility(View.VISIBLE);
                searchName.setVisibility(View.GONE);
                filterBtn.setVisibility(View.GONE);

                recyclerView2.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                clAdapter = new ContactListAdapter(MainActivity.this, AppUtils.getContactList(MainActivity.this));
                recyclerView2.setAdapter(clAdapter);

                filterBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (searchFlag) {
                            groupName.setVisibility(View.VISIBLE);
                            searchName.setVisibility(View.GONE);
                            searchName.setText("");
                            searchFlag = false;
                        } else {
                            groupName.setVisibility(View.GONE);
                            searchName.setVisibility(View.VISIBLE);
                            searchFlag = true;
                        }
                    }
                });

                searchName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        filter(s.toString());
                    }
                });

                /*searchName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            return true;
                        }
                        return false;
                    }
                });*/

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (groupName.getText().toString().trim().isEmpty()) {
                            AppUtils.Toast(MainActivity.this, "Enter Group Name");
                            return;
                        }
                        if (clAdapter.getSelectedList().isEmpty()) {
                            AppUtils.Toast(MainActivity.this, "Select Atleast 1 Contact");
                            return;
                        }

                        GroupModel gModel = new GroupModel();
                        gModel.setGroup(groupName.getText().toString().trim().toString());
                        gModel.setList(clAdapter.getSelectedList());
                        grpNameList.add(gModel);
                        grpAdapter.notifyDataSetChanged();

                        SharedPreference.enterPreferenceList(MainActivity.this, "GroupList", grpNameList);
                        dialog.dismiss();
                        recreate();
                    }
                });

                dialog.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        searchFlag = false;
    }

    public void filter(String text) {
        ArrayList<SmsModel> temp = new ArrayList();
        for(SmsModel d: AppUtils.getContactList(MainActivity.this)){
            if(d.getName().contains(text)){
                temp.add(d);
            }
        }
        clAdapter.updateList(temp);
    }
}
