package es.lost2found.lost2foundUI.chatUI;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import es.lost2found.R;

public class ChatViewHolder extends RecyclerView.ViewHolder {

    CardView cv2;
    TextView chatTitle;
    //TextView lastmsg;
    ImageView chaticon;

    public ChatViewHolder(View itemView) {
        super(itemView);
        cv2 = (CardView) itemView.findViewById(R.id.chat_cardView);
        chatTitle = (TextView) itemView.findViewById(R.id.chattitle);
        //lastmsg = (TextView) itemView.findViewById(R.id.lastmsg);
        chaticon = (ImageView) itemView.findViewById(R.id.chaticon);
    }

    public TextView getChatTitle() {
        return this.chatTitle;
    }

    public ImageView getChaticon() {
        return this.chaticon;
    }
}
