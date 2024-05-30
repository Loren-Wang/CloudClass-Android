// Generated by view binder compiler. Do not edit!
package io.agora.online.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import io.agora.online.R;
import io.agora.online.component.SquareRelativeLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FcrOnlineHandsupListDialogListitemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final RelativeLayout agoraRosterListItemDesktopLayout;

  @NonNull
  public final AppCompatImageView rosterItemDesktopIcon;

  @NonNull
  public final SquareRelativeLayout rosterItemDesktopTouchArea;

  @NonNull
  public final TextView tvWavingName;

  private FcrOnlineHandsupListDialogListitemBinding(@NonNull LinearLayout rootView,
      @NonNull RelativeLayout agoraRosterListItemDesktopLayout,
      @NonNull AppCompatImageView rosterItemDesktopIcon,
      @NonNull SquareRelativeLayout rosterItemDesktopTouchArea, @NonNull TextView tvWavingName) {
    this.rootView = rootView;
    this.agoraRosterListItemDesktopLayout = agoraRosterListItemDesktopLayout;
    this.rosterItemDesktopIcon = rosterItemDesktopIcon;
    this.rosterItemDesktopTouchArea = rosterItemDesktopTouchArea;
    this.tvWavingName = tvWavingName;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FcrOnlineHandsupListDialogListitemBinding inflate(
      @NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FcrOnlineHandsupListDialogListitemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fcr_online_handsup_list_dialog_listitem, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FcrOnlineHandsupListDialogListitemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.agora_roster_list_item_desktop_layout;
      RelativeLayout agoraRosterListItemDesktopLayout = ViewBindings.findChildViewById(rootView, id);
      if (agoraRosterListItemDesktopLayout == null) {
        break missingId;
      }

      id = R.id.roster_item_desktop_icon;
      AppCompatImageView rosterItemDesktopIcon = ViewBindings.findChildViewById(rootView, id);
      if (rosterItemDesktopIcon == null) {
        break missingId;
      }

      id = R.id.roster_item_desktop_touch_area;
      SquareRelativeLayout rosterItemDesktopTouchArea = ViewBindings.findChildViewById(rootView, id);
      if (rosterItemDesktopTouchArea == null) {
        break missingId;
      }

      id = R.id.tv_waving_name;
      TextView tvWavingName = ViewBindings.findChildViewById(rootView, id);
      if (tvWavingName == null) {
        break missingId;
      }

      return new FcrOnlineHandsupListDialogListitemBinding((LinearLayout) rootView,
          agoraRosterListItemDesktopLayout, rosterItemDesktopIcon, rosterItemDesktopTouchArea,
          tvWavingName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}