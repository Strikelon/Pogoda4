package com.strikalov.pogoda4.adapters;

import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.strikalov.pogoda4.R;
import com.strikalov.pogoda4.models.BackgroundPicture;

import java.util.List;

public class PictureBackgroundRecyclerViewAdapter extends
        RecyclerView.Adapter<PictureBackgroundRecyclerViewAdapter.ViewHolder>{

    private List<BackgroundPicture> backgroundPicturesList;
    private Resources resources;
    private int positionIsChecked;

    public PictureBackgroundRecyclerViewAdapter(List<BackgroundPicture> backgroundPicturesList, Resources resources){
        this.backgroundPicturesList = backgroundPicturesList;
        this.resources = resources;
    }

    @Override
    public int getItemCount(){
        return backgroundPicturesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    @Override
    public PictureBackgroundRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_picture_background, parent, false);
        return new PictureBackgroundRecyclerViewAdapter.ViewHolder(cv);

    }

    @Override
    public void onBindViewHolder(PictureBackgroundRecyclerViewAdapter.ViewHolder holder, final int position){

        CardView cardView = holder.cardView;

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundPicture backgroundPictureItem = backgroundPicturesList.get(position);
                if(!backgroundPictureItem.isChecked()) {
                    BackgroundPicture backgroundPictureLastCheked = backgroundPicturesList.get(positionIsChecked);
                    backgroundPictureLastCheked.setChecked(false);
                    notifyItemChanged(positionIsChecked);
                    backgroundPictureItem.setChecked(true);
                    notifyItemChanged(position);
                }
            }
        });

        ImageView pictureBackgroundImage = cardView.findViewById(R.id.picture_background_image);
        TextView pictureBackgroundTextName = cardView.findViewById(R.id.picture_background_text_name);

        BackgroundPicture backgroundPicture = backgroundPicturesList.get(position);

        String name = backgroundPicture.getName();
        int imageResourceId = backgroundPicture.getImageResourceId();
        boolean isChecked = backgroundPicture.isChecked();

        pictureBackgroundImage.setImageResource(imageResourceId);
        pictureBackgroundTextName.setText(name);

        if(isChecked){
            positionIsChecked = position;
            cardView.setCardBackgroundColor(resources.getColor(R.color.colorCardViewAccent));
        }else {
            cardView.setCardBackgroundColor(resources.getColor(R.color.white));
        }


    }

}
