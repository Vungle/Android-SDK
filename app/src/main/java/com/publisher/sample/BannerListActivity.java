package com.publisher.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BannerListActivity extends AppCompatActivity {

    private static final String PLACEMENT_ID = "placement_id";
    private static final Integer SIZE = 100;
    private static final int POSITION = 50;

    private RecyclerView recyclerView;
    private VungleBannerAdAdapter adapter;

    public static Intent getIntent(Context ctx, String placementReferenceId) {
        Intent intent = new Intent(ctx, BannerListActivity.class);
        intent.putExtra(PLACEMENT_ID, placementReferenceId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_list);
        String placement = getIntent().getStringExtra(PLACEMENT_ID);

        recyclerView = findViewById(R.id.rv_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        RVAdapter originalAdapter = new RVAdapter(SIZE);
        adapter = new VungleBannerAdAdapter(placement, POSITION, originalAdapter, null);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onDestroy() {
        adapter.destroy();
        recyclerView.setAdapter(null);
        super.onDestroy();
    }

    //simple adapter implementation
    private static class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final Integer size;

        RVAdapter(Integer size) {
            this.size = size;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item, viewGroup, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            ((ItemHolder) viewHolder).bind(String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return size;
        }
    }
}
