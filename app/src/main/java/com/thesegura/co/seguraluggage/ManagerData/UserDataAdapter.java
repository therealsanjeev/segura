package com.thesegura.co.seguraluggage.ManagerData;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;
import com.thesegura.co.seguraluggage.R;
import com.thesegura.co.seguraluggage.UserData.UserData;

import java.util.List;
import java.util.UUID;

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.userViewHolder>{
    private Context context;
    private List<UserData> userDataList;

    public UserDataAdapter(Context context,List<UserData> userDataList){
        this.context=context;
        this.userDataList=userDataList;
    }

    @NonNull
    @Override
    public userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new userViewHolder(LayoutInflater.from(context).inflate(R.layout.list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull userViewHolder holder, int position) {
        UserData userData=userDataList.get(position);
        holder.name.setText(userData.getUserName());
        holder.number.setText(userData.getUserPhone());
        holder.luggage.setText(userData.getUserLuggage());
    }

    @Override
    public int getItemCount() {
        return userDataList.size();
    }

    public class userViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,number,luggage;

        public userViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.orderName);
            number=itemView.findViewById(R.id.orderNumber);
            luggage=itemView.findViewById(R.id.orderLuggage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            UserData userData=userDataList.get(getAdapterPosition());
//            Intent intent=new Intent(context,);
        }
    }

}
