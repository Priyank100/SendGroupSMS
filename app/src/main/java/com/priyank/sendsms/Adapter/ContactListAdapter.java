package com.priyank.sendsms.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.priyank.sendsms.Model.SmsModel;
import com.priyank.sendsms.R;

import java.util.ArrayList;
import java.util.Iterator;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyAdapter> {
    Activity activity;
    ArrayList<SmsModel> myList;
    ArrayList<SmsModel> filterList;

    public ContactListAdapter(Activity activity, ArrayList<SmsModel> list) {
        this.activity = activity;
        this.myList = list;
        filterList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ContactListAdapter.MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.contact_list_adapter, parent, false);
        return new ContactListAdapter.MyAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactListAdapter.MyAdapter holder, int position) {

        holder.checkBox.setChecked(myList.get(position).isSelected());
        holder.name.setText(myList.get(position).getName());
        holder.number.setText(myList.get(position).getNumber());

        holder.checkBox.setTag(position);
        holder.checkBox.setChecked(myList.get(position).isSelected());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) holder.checkBox.getTag();

                if (myList.get(pos).isSelected()) {
                    myList.get(pos).setSelected(false);

                    Iterator<SmsModel> iterator = filterList.iterator();
                    while(iterator.hasNext()) {
                        SmsModel next = iterator.next();
                        if(next.getName().equals(myList.get(pos).getName())) {
                            iterator.remove();
                        }
                    }

                } else {
                    myList.get(pos).setSelected(true);
                    SmsModel model = new SmsModel();
                    model.setName(myList.get(pos).getName());
                    model.setNumber(myList.get(pos).getNumber());
                    filterList.add(model);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView name, number;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            name = itemView.findViewById(R.id.name_text);
            number = itemView.findViewById(R.id.num_text);
        }
    }

    public ArrayList<SmsModel> getSelectedList() {
        return filterList;
    }

    public void updateList(ArrayList<SmsModel> list){
        myList = list;
        notifyDataSetChanged();
    }
}
