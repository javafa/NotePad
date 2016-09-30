package com.kodonho.android.notepad;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerCardAdapter extends RecyclerView.Adapter<RecyclerCardAdapter.ViewHolder>{

    ArrayList<RecyclerData> datas;
    int itemLayout;
    Main2Activity main2Activity;

    // 아답터를 생성하면서 Context 대신 Main2Activity를 타입으로 지정한다
    // 그러면 인자로 받은 액티비티의 public 함수들을 사용할수 있게된다
    public RecyclerCardAdapter(ArrayList<RecyclerData> datas, int itemLayout, Main2Activity context){
        this.datas = datas;
        this.itemLayout = itemLayout;
        this.main2Activity = context;
    }

    // view 를 만들어서 홀더에 저장하는 역할
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(view);
    }

    // listView getView 를 대체하는 함수
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        RecyclerData data = datas.get(position);
        holder.textTitle.setText(data.contents);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                main2Activity.toggleFab();
                main2Activity.callWrite(position);
            }
        });
        setAnimation(holder.cardView, position);
    }

    int lastPosition = -1;
    public void setAnimation(View view, int position){
        if(position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(main2Activity, android.R.anim.slide_in_left);
            view.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            textTitle = (TextView) itemView.findViewById(R.id.title);
            cardView = (CardView) itemView.findViewById(R.id.cardItem);
        }
    }
}
