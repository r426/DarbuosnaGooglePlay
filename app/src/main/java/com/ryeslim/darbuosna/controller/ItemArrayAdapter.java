package com.ryeslim.darbuosna.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import com.ryeslim.darbuosna.R;
import com.ryeslim.darbuosna.model.Proverb;
import com.ryeslim.darbuosna.model.WorkWithProverbs;

public class ItemArrayAdapter extends ArrayAdapter<Proverb> {
    public ItemArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Proverb> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view;
        final TextView text;
        final Button theDeleteButton;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.one_line, parent, false);
        } else {
            view = convertView;
        }
        text = view.findViewById(R.id.list_item);
        text.setText(this.getItem(position).getProverb());

        theDeleteButton = view.findViewById(R.id.delete_one);
        theDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(getItem(position));
                WorkWithProverbs.getInstance().saveTheUpdatedList();
            }
        });
        return view;
    }
}
