package com.justin.mysightsecurity;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.justin.mysightsecurity.ui.history.HistoryFragment;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter{

    private Context context;
    private Fragment fragment;
    private List<String> expandableListTitle;
    private HashMap<String, HashMap<String, String>> expandableListDetail;
    private ItemClickListener mClickListener;

    public CustomExpandableListAdapter(Context context, Fragment fragment, List<String> expandableListTitle,
                                       HashMap<String, HashMap<String, String>> expandableListDetail) {
        this.context = context;
        this.fragment = fragment;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public HashMap<String, String> getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition));
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final HashMap<String, String> expandedListItemContent = getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItemTime);
        expandedListTextView.setText(expandedListItemContent.get("time"));
        ImageButton expandedListImageView = (ImageButton) convertView
                .findViewById(R.id.expandedListItemImage);

        // Something Like bellow have to be accomplished

       Toast.makeText(CustomExpandableListAdapter.this.context,expandedListItemContent.get("image"), Toast.LENGTH_SHORT).show();
         String pathName = Environment.getExternalStorageDirectory().getPath() ;
        Bitmap bitmap = BitmapFactory.decodeFile(expandedListItemContent.get("image"));
        expandedListImageView.setImageBitmap(bitmap);
        //expandedListImageView.setImageURI(Uri.parse();


        expandedListImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CustomExpandableListAdapter.this.context, String.format("%d", listPosition), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("historyinfo", String.format("%d", listPosition));
                Intent intent = new Intent(CustomExpandableListAdapter.this.context,RecordviewAcitvity.class);
                intent.putExtra("historyinfo", String.format("%d", listPosition));
                CustomExpandableListAdapter.this.context.startActivity(intent);
            }
        });
        //Something have to be done above-BINGO
        return convertView;
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size()/2;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
