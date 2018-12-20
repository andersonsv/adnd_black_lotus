package br.com.andersonsv.blacklotus.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.andersonsv.blacklotus.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CardsViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.textViewCardName)
    TextView mCardName;

    @BindView(R.id.imageViewCardImage)
    ImageView mCardImage;

    private CardsViewHolder.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);
    }

    public CardsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });
    }

    public TextView getmCardName() {
        return mCardName;
    }

    public ImageView getmCardImage() {
        return mCardImage;
    }

    public ClickListener getmClickListener() {
        return mClickListener;
    }
}
