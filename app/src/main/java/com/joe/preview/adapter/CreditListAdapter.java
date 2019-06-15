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
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import static com.joe.preview.constants.PreviewConstants.CREDIT_CAST;

public class CreditListAdapter extends RecyclerView.Adapter<CreditListAdapter.CreditViewHolder> {

    private Context context;
    private String type;
    private List<Cast> casts;
    private List<Crew> crews;

    public CreditListAdapter(String type, Context context) {
        this.type = type;
        this.context = context;
        casts = Collections.emptyList();
        crews = Collections.emptyList();
    }

    public CreditListAdapter(Context context, List<Cast> casts) {
        type = CREDIT_CAST;
        this.context = context;
        this.casts = casts;
        crews = Collections.emptyList();
    }

    public CreditListAdapter(Context context, String type, List<Crew> crews) {
        this.type = type;
        this.context = context;
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
            Picasso.get().load(String.format(PreviewConstants.IMAGE_URL, cast.getProfilePath()))
                    .error(R.drawable.ic_account_circle)
                    .into(holder.binding.profileImage);
            holder.binding.textViewName.setText(cast.getName());
            holder.binding.textViewInfo.setText(cast.getCharacter());
        } else {
            Crew crew = getCrewItem(position);
            Picasso.get().load(String.format(PreviewConstants.IMAGE_URL, crew.getProfilePath()))
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

    public Cast getCastItem(int position) {
        return casts.get(position);
    }

    public Crew getCrewItem(int position) {
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
