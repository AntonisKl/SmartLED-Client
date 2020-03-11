package com.example.antonis.rgbstripcontrol;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ColorModesListAdapter extends ArrayAdapter<ColorModesListAdapter.ColorModeListItem> {

    public interface ColorModeListItemOnClickI {
        void onClick();
    }

    public static class ColorModeListItem {
        String colorModeName;
        int imageId;
        int[] rgb;
        ColorModeListItemOnClickI onClickI;

        public ColorModeListItem(String name, int imageId, int[] rgb, ColorModeListItemOnClickI onClickI) {
            colorModeName = name;
            this.imageId = imageId;
            this.rgb = rgb;
            this.onClickI = onClickI;
        }

        void setColorModeName(String name) {
            colorModeName = name;
        }

        String getColorModeName() {
            return colorModeName;
        }

        void setImageId(int Id) {
            imageId = Id;
        }

        int getImageId() {
            return imageId;
        }
    }

    // View lookup cache
    private static class ViewHolder {
        TextView colorModeName;
        ImageView colorModeImage;
    }

    public ColorModesListAdapter(Context context, List<ColorModeListItem> colorModeItems) {
        super(context, R.layout.color_modes_list_item, colorModeItems);

        this.colorModeItems = colorModeItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ColorModeListItem colorModeItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.color_modes_list_item, parent, false);
            viewHolder.colorModeName = (TextView) convertView.findViewById(R.id.color_mode_text);
            viewHolder.colorModeImage = (ImageView) convertView.findViewById(R.id.color_mode_image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (colorModeItem != null) {
            viewHolder.colorModeName.setText(colorModeItem.getColorModeName());
            if (colorModeItem.getImageId() != -1)
                viewHolder.colorModeImage.setImageResource(colorModeItem.getImageId());
        }

        return convertView;
    }

    List<ColorModeListItem> colorModeItems;
}
