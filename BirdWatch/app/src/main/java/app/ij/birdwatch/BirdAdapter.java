package app.ij.birdwatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class BirdAdapter extends ArrayAdapter<Row> {

    private int resourceLayout;
    private Context context;
    List<Row> list;

    public BirdAdapter(Context context, int resource, List<Row> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.context = context;
        list = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(resourceLayout, null);
        }
        Row bird = list.get(position);

        RelativeLayout bg = v.findViewById(R.id.bg);
        if (position % 2 == 1)
            bg.setBackgroundColor(Color.WHITE);
        else
            bg.setBackgroundColor(Color.parseColor("#ECFFC0"));

        ImageView image = v.findViewById(R.id.image);
        TextView name = v.findViewById(R.id.name);
        TextView prob = v.findViewById(R.id.prob);
        CardView box1, box2;
        RelativeLayout rl = v.findViewById(R.id.w1);
        RelativeLayout rl2 = v.findViewById(R.id.w2);
        box2 = v.findViewById(R.id.card2);

        box1 = v.findViewById(R.id.card1);
        box1.setOnClickListener(selectListener(bird.bird1, rl, image));
        // README
        // Display results - image has to be based off of name
        name.setText(bird.bird1);
        prob.setText(" " + bird.prob1 + " %");

        // No second bird
        if (!bird.present) {
            box2.setVisibility(View.INVISIBLE);
            return v;
        }else{
            box2.setVisibility(View.VISIBLE);
        }
        setSecond(position, v);
        box2.setOnClickListener(selectListener(bird.bird2, rl2, (ImageView) v.findViewById(R.id.image2)));
        return v;
    }

    public View.OnClickListener selectListener(final String s, final RelativeLayout rel, final ImageView img) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int backgroundColor = ((CardView) (view)).getCardBackgroundColor().getDefaultColor();
                if (Selected.chosen) {
                    if (Selected.selection.equals(s)) {
                        Selected.chosen = false;
                        rel.setBackgroundColor(Color.parseColor("#ffffff"));
                        grayOut(img);
                    } else
                        makeToast("You have already selected: " + Selected.selection);
                } else {
                    Selected.selection = s;
                    grayOut(img);
                    Selected.chosen = true;
                    rel.setBackgroundColor(Color.parseColor("#b5b5b5"));
                }
                Log.wtf("*Color:", " " + backgroundColor);
            }
        };
    }

    public void grayOut(ImageView view) {
        if (view.getTag() != "grayed") {
            view.setColorFilter(Color.argb(150, 200, 200, 200));
            view.setTag("grayed");
        } else {
            view.setColorFilter(null);
            view.setTag("");
        }
    }

    public void setSecond(int pos, View v) {
        Row bird = list.get(pos);
        ImageView image = v.findViewById(R.id.image2);
        TextView name = v.findViewById(R.id.name2);
        TextView prob = v.findViewById(R.id.prob2);
        name.setText(bird.bird2);
        prob.setText(" " + bird.prob2 + " %");
    }


    public void makeToast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }

}