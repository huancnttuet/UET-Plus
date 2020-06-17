package com.example.uetplus2.ui.components.grades;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uetplus2.R;

import java.util.List;

public class GradesAdapter extends BaseAdapter {

    private Context context;
    private List<List<String>> listGrades;

    public GradesAdapter(Context context, List<List<String>> listGrades) {
        this.context = context;
        this.listGrades = listGrades;
    }

    @Override
    public int getCount() {
        if(listGrades!=null){
            return listGrades.size();
        }
        else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return listGrades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View rowView = inflater.inflate(R.layout.grade_item,parent,false);

        TextView name_subjectView = (TextView) rowView.findViewById(R.id.name_subject);
        TextView name_classView = (TextView) rowView.findViewById(R.id.name_class);
        TextView time_uploadView = (TextView) rowView.findViewById(R.id.time_upload);

        List<String> Grades_Subject = listGrades.get(position);
        name_subjectView.setText(Grades_Subject.get(0));
        name_classView.setText(Grades_Subject.get(1));
        time_uploadView.setText(Grades_Subject.get(3));



        return rowView;
    }
}
