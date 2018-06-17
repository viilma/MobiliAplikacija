package com.byethost12.kitm.mobiliaplikacija.View;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.byethost12.kitm.mobiliaplikacija.Model.Child;
import com.byethost12.kitm.mobiliaplikacija.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by mokytojas on 2018-02-14.
 */

public class ChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private List<Child> children = Collections.emptyList();
    //private Child currentPokemon;

    public static final String ENTRY_ID = "com.byethost12.kitm.mobiliaplikacija";

    //konstruktorius reikalingas susieti
    // esama langa ir perduoti sarasa vaikui is DB
    public ChildAdapter(Context context, List<Child> children){
        this.context = context;
        this.children = children;
        inflater = LayoutInflater.from(context);
    }

    @Override
    //inflate the layout when viewholder is created
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.container_child,parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    //bind data
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //get current position of item in
        // recyclerview to bind data and assign value from list
        MyHolder myHolder = (MyHolder) holder;
        Child currentChild = children.get(position);
        myHolder.tvName.setText(           currentChild.getName());
        myHolder.tvSurname.setText(        currentChild.getSurname());
        myHolder.tvGroup.setText("Grupė: "      + currentChild.getGroup());
        myHolder.tvUgdymas.setText("Ugdymas: "      + currentChild.getEducation());
        myHolder.tvId.setText("ID: "            + currentChild.getId());
        myHolder.tvActivities.setText("Lankomi užsiėmimai: "  + currentChild.getActivities());
    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvName, tvSurname, tvGroup, tvUgdymas, tvActivities, tvId; //tvSurname

        public MyHolder(View itemView){
            super(itemView);
            tvName       = (TextView)itemView.findViewById(R.id.name);
            tvSurname    = (TextView)itemView.findViewById(R.id.surname);
            tvGroup      = (TextView)itemView.findViewById(R.id.grupe);
            tvUgdymas    = (TextView)itemView.findViewById(R.id.ugdymas);
            tvActivities = (TextView)itemView.findViewById(R.id.activities);
            tvId         = (TextView)itemView.findViewById(R.id.id);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int itemPosition = getAdapterPosition();
            int childID = children.get(itemPosition).getId();

            // TODO: siųsti pasirinkto vaiko objektą vietoj id
            Child child = children.get(itemPosition);

            Intent intent = new Intent(context, EntryActivity.class);

            intent.putExtra(ENTRY_ID, childID);
            context.startActivity(intent);
        }
    }
}


