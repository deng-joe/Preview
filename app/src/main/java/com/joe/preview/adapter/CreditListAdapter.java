package com.joe.preview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joe.preview.R;
import com.joe.preview.constants.PreviewConstants;
import com.joe.preview.data.remote.model.Cast;
import com.joe.preview.data.remote.model.Crew;
import com.joe.preview.databinding.CreditListWithItemBinding;
import com.joe.preview.glide.GlideApp;

import java.util.Collections;
import java.util.List;

import static com.joe.preview.constants.PreviewConstants.CREDIT_CAST;

public class CreditListAdapter extends RecyclerView.Adapter<CreditListAdapter.CreditViewHolder> {

    private Context context;
    private String type;
    private List<Cast> casts;
    private List<Crew> crews;

    public CreditListAdapter(Context context, String type) {
        this.context = context;
        this.type = type;
        casts = Collections.emptyList();
        crews = Collections.emptyList();
    }

    public CreditListAdapter(Context context, List<Cast> casts) {
        this.context = context;
        type = CREDIT_CAST;
        this.casts = casts;
        crews = Collections.emptyList();
    }

    public CreditListAdapter(Context context, String type, List<Crew> crews) {
        this.context = context;
        this.type = type;
        casts = Collections.emptyList();
        this.crews = crews;
    }

    @NonNull
    @Override
    public CreditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CreditListWithItemBinding itemBinding = CreditListWithItemBinding.inflate(inflater, parent, false);
        return new CreditViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditViewHolder holder, int position) {
        if (isCast()) {
            Cast cast = getCastItem(position);
            GlideApp.with(context).load(String.format(PreviewConstants.IMAGE_URL, cast.getProfilePath()))
                    .error(R.drawable.ic_account_circle)
                    .into(holder.binding.profileImage);
            holder.binding.textViewName.setText(cast.getName());
            holder.binding.textViewInfo.setText(cast.getCharacter());
        } else {
            Crew crew = getCrewItem(position);
            GlideApp.with(context).load(String.format(PreviewConstants.IMAGE_URL, crew.getProfilePath()))
                    .error(R.drawable.ic_account_circle)
                    .into(holder.binding.profileImage);
            holder.binding.textViewName.setText(crew.getName());
            holder.binding.textViewInfo.setText(crew.getJob());
        }
    }

    @Override
    public int getItemCount() {
        if (isCast())
            return casts.size();
        return crews.size();
    }

    private Boolean isCast() {
        if (type.equalsIgnoreCase(CREDIT_CAST))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Cast getCastItem(int position) {
        return casts.get(position);
    }

    private Crew getCrewItem(int position) {
        return crews.get(position);
    }


    class CreditViewHolder extends RecyclerView.ViewHolder {

        private CreditListWithItemBinding binding;

        CreditViewHolder(CreditListWithItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
