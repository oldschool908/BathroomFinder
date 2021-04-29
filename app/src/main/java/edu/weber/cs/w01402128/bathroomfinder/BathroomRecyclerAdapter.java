package edu.weber.cs.w01402128.bathroomfinder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.weber.cs.w01402128.bathroomfinder.db.Bathroom;

public class BathroomRecyclerAdapter extends RecyclerView.Adapter<BathroomRecyclerAdapter.ViewHolder> {

    private List<Bathroom> bathroomList;
    private OnClickListener mCallBack;

    public interface OnClickListener{
        void listButtonClicked(Bathroom bathroom);
    }

    public BathroomRecyclerAdapter(List<Bathroom> bathroomList, OnClickListener mCallBack) {
        this.bathroomList = bathroomList;
        this.mCallBack = mCallBack;
    }

    public void setBathroomList(List<Bathroom> list){
        bathroomList.clear();
        bathroomList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear(){
        this.bathroomList.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bathroom_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bathroom bathroom = bathroomList.get(position);
        if (bathroom != null){
            holder.item = bathroom;
            holder.tv1.setText(bathroom.getBathroom_name());
            holder.tv2.setText(bathroom.getUnavailable_note());
            holder.itemRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("test", "bathroom: " + bathroom);
                    if(bathroom != null) {
                        mCallBack.listButtonClicked(bathroom);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return bathroomList.size();
    }

    // View Holder HOLD the UI of an "individual" item in the list.
    public class ViewHolder extends RecyclerView.ViewHolder{
        public View itemRoot;
        public TextView tv1, tv2;
        public Bathroom item;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemRoot = itemView;
            tv1 = itemRoot.findViewById(R.id.tvLine1);
            tv2 = itemRoot.findViewById(R.id.tvLine2);

        }
    }


}
