package za.co.botcoin.utils;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipBoardUtils
{
    public static void copyToClipBoard(Context context, String data)
    {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("data", data);
        clipboard.setPrimaryClip(clip);

        GeneralUtils.makeToast(context, "Copied to clipboard!");
    }

}
