package com.priyank.sendsms.Model;

import java.util.ArrayList;

public class GroupModel {

    private String group;
    private ArrayList<SmsModel> list;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public ArrayList<SmsModel> getList() {
        return list;
    }

    public void setList(ArrayList<SmsModel> list) {
        this.list = list;
    }
}
