package com.priyank.sendsms.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.priyank.sendsms.Constant.AppUtils;
import com.priyank.sendsms.Constant.SharedPreference;
import com.priyank.sendsms.Model.GroupModel;
import com.priyank.sendsms.R;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyAdapter> {
    Activity activity;
    ArrayList<GroupModel> list;

    public GroupAdapter(Activity activity, ArrayList<GroupModel> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.group_adapter, parent, false);
        return new GroupAdapter.MyAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, final int position) {

        holder.adapterGroupName.setText(list.get(position).getGroup());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0; i<(list.get(position).getList()).size(); i++) {
                    AppUtils.LogE("List>> " + (list.get(position).getList()).get(i).getName() + ":" +
                            (list.get(position).getList()).get(i).getNumber());
                }
            }
        });

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                SharedPreference.enterPreferenceList(activity, "GroupList", list);
                notifyDataSetChanged();
                activity.recreate();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView adapterGroupName;
        ImageView removeBtn;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            adapterGroupName = itemView.findViewById(R.id.adapter_group_name);
            removeBtn = itemView.findViewById(R.id.cross_img);
        }
    }
}
