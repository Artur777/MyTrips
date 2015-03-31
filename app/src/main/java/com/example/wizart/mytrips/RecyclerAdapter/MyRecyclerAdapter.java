package com.example.wizart.mytrips.RecyclerAdapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.wizart.mytrips.TripTableActivity;
import com.example.wizart.mytrips.*;
import com.example.wizart.mytrips.Utils;

import java.util.Collections;
import java.util.List;


/**
 * Created by egutierr on 2015-03-03.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<DataItem> mData = Collections.emptyList();
    private String TAG="MyRecyclerAdapter";
    //private List<Memos> mMemos;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public MyRecyclerAdapter() {
        // Pass context or other static stuff that will be needed.
    }

    public void updateList (List<DataItem> data){
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return mData.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                                         int position) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.list_row,viewGroup,false);
        return new RecyclerViewHolder(itemView);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {

        // - get element from your data at this position
        // - replace the contents of the view with that element
/*
        viewHolder.name.setText(mData.get(position).Name);
        viewHolder.address.setText(mData.get(position).Address);
        viewHolder.timeStamp.setText(mData.get(position).TimeStamp);
        viewHolder.temperature.setText(mData.get(position).Temperature);
        viewHolder.humidity.setText(mData.get(position).Humidity);
        viewHolder.pictureThumbnail.setImageBitmap((mData.get(position).PictureThumbnail));
*/


        viewHolder.tvStartTime.setText(mData.get(position).startTime);
        String dist=mData.get(position).Distance;

        viewHolder.tvDistance.setText("Dist: " + dist);
        Log.d(TAG, "onBIndViewHolder position =" + position);
        Log.d(TAG, "onBIndViewHolder dist=" + dist);
        double as=mData.get(position).AveragePace;
        String ssp=MapsActivity.formatSpeed(as);
        Log.d(TAG,"onBIndViewHolder speed=="+ ssp);
        viewHolder.tvAverageSpeed.setText(ssp);
        String et=mData.get(position).elapsedTime;
        Log.d(TAG, "onBIndViewHolder time ==" + et);
        viewHolder.tvTime.setText(et);
        String st=mData.get(position).Steps+ " steps";
        viewHolder.tvSteps.setText(st);

        viewHolder.setClickListener(new ClickListener() {
            @Override
            public void onClick(View v, int pos, boolean isLongClick) {
                if (isLongClick) {
                    // View v at position pos is long-clicked.
                    Utils.makeToast(TripTableActivity.mContext, "long click on item with idex pos=" + pos);

                } else {
                    // View v at position pos is clicked.
                    Utils.makeToast(TripTableActivity.mContext, "click on item with idex pos=" + pos);
                }
            }
        });
    }
    public void addItem(int position, DataItem data) {
        mData.add(position, data);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }
}