package net.mizucoffee.hatsuyuki_chinachu.dashboard.f_live;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.mizucoffee.hatsuyuki_chinachu.R;
import net.mizucoffee.hatsuyuki_chinachu.VideoPlayActivity;
import net.mizucoffee.hatsuyuki_chinachu.chinachu.model.Program;
import net.mizucoffee.hatsuyuki_chinachu.tools.Shirayuki;

import java.util.List;

import butterknife.ButterKnife;

public class LiveCardRecyclerAdapter extends RecyclerView.Adapter<LiveCardRecyclerAdapter.ViewHolder>{

    private List<Program> mProgram;
    private LayoutInflater      mLayoutInflater;
    private Context             mContext;
    private String              mAddress;

    public LiveCardRecyclerAdapter(Context context,String address) {
        super();
        this.mContext        = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        mAddress = address;
    }

    @Override
    public int getItemCount() {
        return mProgram.size();
    }

    public void setLiveList(List<Program> program){
        this.mProgram = program;
    }

    void setAddress(String a){
        mAddress = a;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {
        Program program = mProgram.get(vh.getAdapterPosition());

        vh.channelTv.setText(program.getChannel().getName());
        vh.titleTv.setText(program.getTitle());
//        vh.detailTv.setText(program.getDetail());
        Picasso.with(mContext).load("http://" + mAddress + "/api/channel/" + program.getChannel().getId() + "/logo.png").into(vh.imageView);
//        Shirayuki.log("http://" + mDataModel.getServerConnection().getAddress() + "/api/channel/" + program.getChannel().getId() + "/logo.png");
        vh.colorll.setBackgroundColor(ContextCompat.getColor(mContext, Shirayuki.getBackgroundColorFromCategory(program.getCategory())));

        vh.cardLayout.setOnClickListener(v -> {//http://192.168.50.50:10472/api/channel/1gudbls/watch.webm
            SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(mContext);
            String url = "http://" + mAddress + "/api/channel/" + program.getChannel().getId() + "/watch.webm" +
                    "?ext=webm" +
                    "&c%3Av=vp9" +
                    Shirayuki.getResolutionFromVideoSize(spf.getString("download_video_size", "720p (HD) (Recommended)")) +
                    Shirayuki.getVideoBitrate(spf.getString("download_video_bitrate", "1Mbps (Recommended)")) +
                    Shirayuki.getAudioBitrate(spf.getString("download_audio_bitrate", "128kbps (Recommended)")) +
                    "&ss=0";
            mContext.startActivity(new Intent(Intent.ACTION_VIEW).setClass(mContext, VideoPlayActivity.class).putExtra("url",url));
        });
    }

    @Override
    public LiveCardRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.card_live_layout, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView  channelTv;
        private TextView  titleTv;
//        private TextView detailTv;
        private CardView cardLayout;
        private LinearLayout colorll;

        private ViewHolder(View v) {
            super(v);
            imageView = ButterKnife.findById(v,R.id.channel_iv);
            channelTv   = ButterKnife.findById(v,R.id.channel_tv);
            titleTv   = ButterKnife.findById(v,R.id.title_tv);
//            detailTv = ButterKnife.findById(v,R.id.detail_tv);
            cardLayout = ButterKnife.findById(v,R.id.card_view);
            colorll = ButterKnife.findById(v,R.id.color_ll);
        }
    }
}
