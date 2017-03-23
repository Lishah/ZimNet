package samples.android.elisha.com.zimnet.mAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import samples.android.elisha.com.zimnet.R;
import samples.android.elisha.com.zimnet.UserCategories;

/**
 * Created by elisha on 3/12/17.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>  {

    private ArrayList<UserCategories> arrayList;
    private Context mcontext;

    public CategoriesAdapter(ArrayList<UserCategories> arrayList, Context mcontext) {
        this.arrayList = arrayList;
        this.mcontext = mcontext;
    }

    @Override
    public void onBindViewHolder(CategoriesAdapter.ViewHolder holder, int i) {

        holder.textView.setText(arrayList.get(i).getRecyclerViewTitleText());
        holder.imageView.setImageResource(arrayList.get(i).getRecyclerViewImage());
    }

    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup vGroup, int i) {

        View view = LayoutInflater.from(vGroup.getContext()).inflate(R.layout.home_grid_item, vGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;

        public ViewHolder(View v) {
            super(v);

            textView = (TextView) v.findViewById(R.id.categoryTextView);
            imageView = (ImageView) v.findViewById(R.id.image);
        }



}}
