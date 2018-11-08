package ca.cmpt276.greengoblins.foodsurveydata;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import java.util.List;
import ca.cmpt276.greengoblins.emission.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context mContext;
    private List<User> userList;

    public UserAdapter(Context mContext, List<User> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_pledge, null);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i) {
        User user = userList.get(i);

        // userViewHolder.profilePic.set TO DO **********
        userViewHolder.name.setText(user.getFirstName() + " " + user.getLastName());
        userViewHolder.municipality.setText(user.getCity());
        userViewHolder.pledgeAmount.setText(Double.toString(user.getPledgeAmount()) + " tonnes of CO2e");

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Contains UI elements
    class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePic;
        TextView name;
        TextView municipality;
        TextView pledgeAmount;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.fragment_pledge_list_profile_pic);
            name = itemView.findViewById(R.id.fragment_pledge_list_name);
            municipality = itemView.findViewById(R.id.fragment_pledge_list_municipality);
            pledgeAmount = itemView.findViewById(R.id.fragment_pledge_list_pledge_amount);
        }
    }

}
