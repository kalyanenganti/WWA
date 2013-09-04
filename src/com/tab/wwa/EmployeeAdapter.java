package com.tab.wwa;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
public class EmployeeAdapter extends ArrayAdapter<Employee>{
    private ArrayList<Employee> entries;
    private Activity activity;
 
    public EmployeeAdapter(Activity a, int textViewResourceId, ArrayList<Employee> entries) {
        super(a, textViewResourceId, entries);
        this.entries = entries;
        this.activity = a;

    }
 
    public static class ViewHolder{
        public TextView item1;
        public TextView item2;
        public TextView item3;
        public ImageView item4;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {         //override getView
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi =
                (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.employee_item_row, null);
            holder = new ViewHolder();
            holder.item1 = (TextView) v.findViewById(R.id.txtName);
            holder.item2 = (TextView) v.findViewById(R.id.txtDep);
            holder.item3 = (TextView) v.findViewById(R.id.txtBio);
            holder.item4 = (ImageView) v.findViewById(R.id.imgIcon);

            v.setTag(holder);
            
        }
        else
            holder=(ViewHolder)v.getTag();
 
        final Employee emp = entries.get(position);
        if (emp != null) {
            holder.item1.setText(emp.getName());
            holder.item2.setText(emp.getDepartment());
            holder.item3.setText(emp.getBiography());
        	ImageLoader.getInstance().displayImage(emp.getPhoto(), holder.item4); // sets the image to the view using lazy load


        }
        return v;
    }
 
}
