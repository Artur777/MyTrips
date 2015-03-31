package com.example.wizart.mytrips.Parse;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wizart.mytrips.R;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by AChojeck on 31/03/2015.
 */
class TripQueryParseAdapter extends ParseQueryAdapter<TripP> {


        public TripQueryParseAdapter(Context context,ParseQueryAdapter.QueryFactory<TripP> queryFactory) {
            super(context, queryFactory);
        }

        @Override
        public View getItemView(TripP trip, View view, ViewGroup parent) {
            ViewHolder holder;
//            if (view == null) {
//                view = inflater.inflate(R.layout.list_item_todo, parent, false);
//                holder = new ViewHolder();
//                holder.todoTitle = (TextView) view
//                        .findViewById(R.id.todo_title);
//                view.setTag(holder);
//            } else {
//                holder = (ViewHolder) view.getTag();
//            }
//            TextView todoTitle = holder.todoTitle;
//            todoTitle.setText(String.valueOf(trip.getStartTime()));
//            todoTitle.setTypeface(null, Typeface.ITALIC);
            return view;
        }
    }

    class ViewHolder {
        TextView todoTitle;

}
