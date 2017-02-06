package csilva2810.udacity.com.popularmovies.utils;

import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import csilva2810.udacity.com.popularmovies.R;

import static android.support.design.R.attr.srcCompat;

/**
 * Created by carlinhos on 2/3/17.
 */

@BindingMethods({
        @BindingMethod(
                type = android.support.design.widget.FloatingActionButton.class,
                attribute = "app:srcCompat",
                method = "setImageResource"
        )
})

public class CustomBinders {

    @BindingAdapter({"android:src", "placeholder"})
    public static void loadImage(ImageView view, String url, Drawable plaholder) {
                Picasso.with(view.getContext())
                .load(url)
                .placeholder(plaholder)
                .into(view);
    }

    @BindingAdapter({"toggleFavorite"})
    public static void setFavorite(FloatingActionButton fab, boolean isFavorite) {
        fab.setImageResource(isFavorite ? R.drawable.ic_star_24dp : R.drawable.ic_star_border_24dp);
    }

}
