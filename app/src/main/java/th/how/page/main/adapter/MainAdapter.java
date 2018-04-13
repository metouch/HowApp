package th.how.page.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import th.how.app.R;

/**
 * Created by me_touch on 2017/8/22.
 *
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{

    @Override
    public int getItemCount() {
        return 100;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_content_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvContent.setText(position + "");
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvContent;
        public ViewHolder(View view){
            super(view);
            tvContent = (TextView)view.findViewById(R.id.content_item_tv);
        }

    }
}
