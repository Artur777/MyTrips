package com.example.wizart.mytrips.RecyclerAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wizart.mytrips.R;


/**
 * Created by AChojecki on 2015-03-04.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView tvDistance;
    public TextView tvElapsedTime;
    public TextView tvStartTime;
    public TextView tvTime;
    public TextView tvEndTime;
    public TextView tvCalories;
    public TextView tvSteps;
    public TextView tvAverageSpeed;
    public ImageView imgViewActivity;

    private ClickListener clickListener;

    public RecyclerViewHolder (View itemView) {
        super(itemView);
        tvDistance= (TextView) itemView.findViewById(R.id.id_distance);
        tvTime= (TextView) itemView.findViewById(R.id.id_time);
        tvAverageSpeed= (TextView) itemView.findViewById(R.id.id_average_speed);
        tvStartTime= (TextView) itemView.findViewById(R.id.id_time_start_route);
        tvSteps= (TextView) itemView.findViewById(R.id.id_steps);
/*
        address = (TextView) itemView.findViewById(R.id.id_address);
        timeStamp = (TextView) itemView.findViewById(R.id.id_time_stamp);
        temperature = (TextView) itemView.findViewById(R.id.id_temperature);
        humidity = (TextView) itemView.findViewById(R.id.id_humidity);
        pictureThumbnail = (ImageView) itemView.findViewById(R.id.id_thumbnail);
*/

        //Setting listeners

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }
    /* Setter for listener. */
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {

        // If not long clicked, pass last variable as false.
        clickListener.onClick(v, getPosition(), false);
    }

    @Override
    public boolean onLongClick(View v) {

        // If long clicked, passed last variable as true.
        clickListener.onClick(v, getPosition(), true);
        return true;
    }
}
