package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {
    private List<Contact> contactListFull;

    public ContactAdapter(@NonNull Context context, @NonNull List<Contact> contactList) {
        super(context, 0, contactList);
        contactListFull = new ArrayList<>(contactList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return contactFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_contact_list, parent, false
            );
        }

        TextView txt_name = convertView.findViewById(R.id.txt_name);
        TextView txt_contactid = convertView.findViewById(R.id.txt_contactid);

        Contact contactItem = getItem(position);
        if(contactItem != null){
            txt_name.setText("["+contactItem.contact_type+"] - "+contactItem.contact_name);
            txt_contactid.setText(contactItem.contact_id);
        }

        return  convertView;
    }

    private Filter contactFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Contact> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(contactListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Contact item : contactListFull) {
                    if (item.getContactName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Contact) resultValue).getContactId();
        }
    };
}
