package com.priyank.sendsms.Constant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.priyank.sendsms.Model.SmsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AppUtils {

    public static void LogE(String msg) {
        Log.e("TAG", msg);
    }

    public static void Toast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    public static ArrayList<SmsModel> getContactList(Activity activity) {
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        ArrayList<SmsModel> contactList = new ArrayList<>();
        ArrayList<SmsModel> noRepeat = null;
        ContentResolver cr = activity.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        SmsModel model = new SmsModel();
                        model.setSelected(false);
                        model.setName(name);
                        model.setNumber(phoneNo);
                        contactList.add(model);
                    }

                    Collections.sort(contactList, new Comparator<SmsModel>() {
                        @Override
                        public int compare(SmsModel o1, SmsModel o2) {
                            return o1.getName().compareToIgnoreCase(o2.getName());
                        }
                    });

                    noRepeat = new ArrayList<SmsModel>();

                    for (SmsModel event : contactList) {
                        boolean isFound = false;
                        for (SmsModel e : noRepeat) {
                            if (e.getName().equals(event.getName()) || (e.equals(event))) {
                                isFound = true;
                                break;
                            }
                        }
                        if (!isFound) noRepeat.add(event);
                    }

                    pCur.close();
                    dialog.dismiss();
                }
            }
        }
        if (cur != null) {
            cur.close();
            dialog.dismiss();
        }
//        return contactList;
        return noRepeat;
    }

    public static String CommaString(ArrayList<SmsModel> theAray, String itemType) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        for (int i = 0; i < theAray.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            if (itemType.equals("Names")) {
                String item = theAray.get(i).getName();
                sb.append(item);
            } else {
                String item = theAray.get(i).getNumber();
                sb.append(item);
            }
        }
        sb.append("");
        return sb.toString();
    }
}
