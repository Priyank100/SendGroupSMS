package com.priyank.sendsms.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.priyank.sendsms.Activity.SMSActivity;
import com.priyank.sendsms.Constant.AppUtils;
import com.priyank.sendsms.Constant.SharedPreference;
import com.priyank.sendsms.CustomProgress.SpotsDialog;
import com.priyank.sendsms.Model.GroupModel;
import com.priyank.sendsms.Model.SmsModel;
import com.priyank.sendsms.R;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyAdapter> {
    Activity activity;
    ArrayList<GroupModel> grList;

    ArrayList<SmsModel> contactList;
    ContactListAdapter clAdapter;

    SpotsDialog pd;

    public GroupAdapter(Activity activity, ArrayList<GroupModel> list) {
        this.activity = activity;
        this.grList = list;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.group_adapter, parent, false);
        return new GroupAdapter.MyAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, final int position) {

        holder.adapterGroupName.setText(grList.get(position).getGroup());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SMSActivity.class);
                intent.putExtra("GroupName", grList.get(position).getGroup());
                intent.putExtra("GroupMembers", grList.get(position).getList());
                activity.startActivity(intent);
            }
        });

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new SpotsDialog(activity, "Loading...", R.style.SpotsDialogDefault, false, null);
                pd.show();
                contactList = new ArrayList<>();
                contactList = new ArrayList<>();
                contactList = AppUtils.getContactList(activity);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showContactList(position, contactList);
                    }
                },3000);
//                showContactList(position, contactList);
            }
        });

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setTitle("Delete Group")
                        .setMessage("Are you sure you want to delete this group ? ")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                grList.remove(position);
                                SharedPreference.enterPreferenceList(activity, "GroupList", grList);
                                notifyDataSetChanged();
                                activity.recreate();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return grList.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView adapterGroupName;
        ImageView removeBtn, editBtn;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            adapterGroupName = itemView.findViewById(R.id.adapter_group_name);
            removeBtn = itemView.findViewById(R.id.cross_img);
            editBtn = itemView.findViewById(R.id.edit_img);
        }
    }

    public void showContactList(final int pos, final ArrayList<SmsModel> list) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.add_group_layout);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        final EditText groupName = dialog.findViewById(R.id.group_name);
        final EditText searchName = dialog.findViewById(R.id.search_name);
        final RecyclerView recyclerView2 = dialog.findViewById(R.id.recycler_view2);
        Button addBtn = dialog.findViewById(R.id.add_btn);
        addBtn.setText("Update");

        recyclerView2.setLayoutManager(new LinearLayoutManager(activity));
        clAdapter = new ContactListAdapter(activity, list);
        recyclerView2.setAdapter(clAdapter);

        groupName.setText(grList.get(pos).getGroup());
        ContactListAdapter.filterList = new ArrayList<>();
        ContactListAdapter.filterList = grList.get(pos).getList();

        for (int i = 0; i < (grList.get(pos).getList()).size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                SmsModel m = list.get(j);
                if ((grList.get(pos).getList()).get(i).getName().equals(m.getName())) {
                    AppUtils.LogE("pos>> " + j);
                    list.get(j).setSelected(true);
                }
            }
        }

        searchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupName.getText().toString().trim().isEmpty()) {
                    AppUtils.Toast(activity, "Enter Group Name");
                    return;
                }
                if (clAdapter.getFilterList().isEmpty()) {
                    AppUtils.Toast(activity, "Select Atleast 1 Contact");
                    return;
                }

                grList.get(pos).setGroup(groupName.getText().toString().trim().toString());
                grList.get(pos).setList(clAdapter.getFilterList());
                notifyDataSetChanged();

                SharedPreference.enterPreferenceList(activity, "GroupList", grList);
                dialog.dismiss();
            }
        });
        pd.dismiss();
        dialog.show();
    }

    public void filter(String text) {
        ArrayList<SmsModel> temp = new ArrayList();
        for (SmsModel d : contactList) {
            if (d.getName().contains(text)) {
                temp.add(d);
            }
        }
        clAdapter.updateList(temp);
    }
}
