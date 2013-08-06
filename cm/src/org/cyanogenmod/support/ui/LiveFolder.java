package org.cyanogenmod.support.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.UserHandle;
import android.text.TextUtils;

public class LiveFolder {

    public static final class Constants {

        public static final String CREATE_LIVE_FOLDER =
                "cyanogenmod.intent.action.CREATE_LIVE_FOLDER";
        public static final String UPDATE_LIVE_FOLDER =
                "cyanogenmod.intent.action.UPDATE_LIVE_FOLDER";
        public static final String LIVE_FOLDER_UPDATES =
                "cyanogenmod.intent.action.LIVE_FOLDER_UPDATES";

        public static final String FOLDER_UPDATE_TYPE_EXTRA = "update_type";

        public static final String NEW_FOLDER_CREATED =
                "cyanogenmod.intent.action.NEW_LIVE_FOLDER_CREATED";
        public static final String EXISTING_FOLDERS_CREATED =
                "cyanogenmod.intent.action.EXISTING_LIVE_FOLDERS_CREATED";
        public static final String FOLDER_DELETED =
                "cyanogenmod.intent.action.LIVE_FOLDER_DELETED";
        public static final String FOLDER_ITEM_SELECTED =
                "cyanogenmod.intent.action.LIVE_FOLDER_ITEM_SELECTED";
        public static final String FOLDER_ITEM_REMOVED =
                "cyanogenmod.intent.action.LIVE_FOLDER_ITEM_REMOVED";

        public static final String FOLDER_RECEIVER_EXTRA = "folder_receiver";
        public static final String FOLDER_TITLE_EXTRA = "folder_title";

        public static final int MAX_ITEMS = 9;

        public static final String FOLDER_ID_EXTRA = "folder_id";
        public static final String FOLDER_ENTRIES_EXTRA = "folder_entries";
        public static final String FOLDER_ITEM_TITLE_EXTRA = "item_title";
        public static final String FOLDER_ITEM_ID_EXTRA = "item_id";
        public static final String FOLDER_UPDATE_ALL = "update_all";

        private static final String LAUNCHER_PERMISSION =
                "com.cyanogenmod.trebuchet.permission.MANAGE_LIVE_FOLDERS";
    }

    /**
     * Renames a live folder under the assumption that the folderId
     * is valid and belongs to the calling package
     */
    public static void renameLiveFolder(Context ctx, long folderId, String folderName) {
        String errorMsg = null;

        if (TextUtils.isEmpty(folderName)) {
            errorMsg = "Folder name cannot be empty";
        } else if (folderId <= 0) {
            errorMsg = "Invalid folder id " + folderId;
        }

        if (errorMsg != null) {
            throw new IllegalArgumentException(errorMsg);
        }

        Intent i = new Intent(Constants.UPDATE_LIVE_FOLDER);
        i.putExtra(Constants.FOLDER_TITLE_EXTRA, folderName);
        i.putExtra(Constants.FOLDER_ID_EXTRA, folderId);
        ctx.sendBroadcastAsUser(i, UserHandle.CURRENT_OR_SELF, Constants.LAUNCHER_PERMISSION);
    }

    /**
     * Updates a single live folder with the items provided
     * under the assumption that the folderId is valid and
     * belongs to the calling package
     */
    public static void updateSingleFolder(Context ctx, long folderId, ArrayList<Item> folderItems) {
        updateFolder(ctx, folderItems, false, folderId);
    }

    /**
     * Updates all live folders that belong to the calling package
     * with the items provided
     */
    public static void updateAllFolders(Context ctx, ArrayList<Item> folderItems) {
        updateFolder(ctx, folderItems, true, 0);
    }

    private static void updateFolder(Context ctx, ArrayList<Item> folderItems, boolean updateAll, long folderId) {
        String errorMsg = null;

        if (folderItems == null || folderItems.size() == 0) {
            errorMsg = "Cannot populate folder with 0 items";
        } else if (folderItems.size() > 9) {
            errorMsg = "Folder cannot contain more than " + Constants.MAX_ITEMS;
        } else if (folderId <= 0 && !updateAll) {
            errorMsg = "Invalid folder id " + folderId;
        }

        if (errorMsg != null) {
            throw new IllegalArgumentException(errorMsg);
        }

        Intent i = new Intent(Constants.UPDATE_LIVE_FOLDER);
        if (updateAll) {
            i.putExtra(Constants.FOLDER_UPDATE_ALL, updateAll);
        } else {
            i.putExtra(Constants.FOLDER_ID_EXTRA, folderId);
        }
        i.putParcelableArrayListExtra(Constants.FOLDER_ENTRIES_EXTRA, folderItems);
        ctx.sendBroadcastAsUser(i, UserHandle.CURRENT_OR_SELF, Constants.LAUNCHER_PERMISSION);
    }

    /**
     * A live folder is populated with objects of this class
     */
    public static final class Item implements Parcelable {
        /**
         * (Optional) Bitmap to visually represent live folder item
         * If null, default_resource_id is used
         */
        private Bitmap mIcon;

        /**
         * Title of the live folder item
         */
        private String mLabel;

        /**
         * (Optional) Intent to launch on select.
         * If null, LIVE_FOLDER_ITEM_SELECTED is invoked
         * to the assigned receiver
         */
        private Intent mIntent;

        /**
         * (Optional) Identifier for item
         * If value is (<= 0) LIVE_FOLDER_ITEM_SELECTED is not invoked
         */
        private int mId;

        public Bitmap getIcon() {
            return mIcon;
        }

        public void setIcon(Bitmap mIcon) {
            this.mIcon = mIcon;
        }

        public String getLabel() {
            return mLabel;
        }

        public void setLabel(String mLabel) {
            this.mLabel = mLabel;
        }

        public Intent getIntent() {
            return mIntent;
        }

        public void setIntent(Intent mIntent) {
            this.mIntent = mIntent;
        }

        public int getId() {
            return mId;
        }

        public void setId(int mId) {
            this.mId = mId;
        }

        public Item() {
        }

        Item(Parcel in) {
            if (in.readInt() == 1) {
                mIcon = Bitmap.CREATOR.createFromParcel(in);
            }
            mLabel = in.readString();
            if (in.readInt() == 1) {
                mIntent = Intent.CREATOR.createFromParcel(in);
            }
            mId = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (mIcon == null) {
                dest.writeInt(0);
            } else {
                dest.writeInt(1);
                mIcon.writeToParcel(dest, 0);
            }
            dest.writeString(mLabel);
            if (mIntent == null) {
                dest.writeInt(0);
            } else {
                dest.writeInt(1);
                mIntent.writeToParcel(dest, 0);
            }
            dest.writeInt(mId);
        }

        public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
            public Item createFromParcel(Parcel in) {
                return new Item(in);
            }
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };
    }
}
