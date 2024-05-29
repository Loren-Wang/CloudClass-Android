// Generated by view binder compiler. Do not edit!
package io.agora.online.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import io.agora.online.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FcrOnlineEduHeadComponentBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final AppCompatTextView agoraRecordStatus;

  @NonNull
  public final LinearLayoutCompat agoraStatusBarCenter;

  @NonNull
  public final View agoraStatusBarCenterDivider;

  @NonNull
  public final AppCompatTextView agoraStatusBarClassStartedText;

  @NonNull
  public final AppCompatTextView agoraStatusBarClassTimeText;

  @NonNull
  public final AppCompatTextView agoraStatusBarClassroomName;

  @NonNull
  public final AppCompatImageView agoraStatusBarExitIcon;

  @NonNull
  public final AppCompatImageView agoraStatusBarNetworkStateIcon;

  @NonNull
  public final AppCompatImageView agoraStatusBarSettingIcon;

  private FcrOnlineEduHeadComponentBinding(@NonNull RelativeLayout rootView,
      @NonNull AppCompatTextView agoraRecordStatus,
      @NonNull LinearLayoutCompat agoraStatusBarCenter, @NonNull View agoraStatusBarCenterDivider,
      @NonNull AppCompatTextView agoraStatusBarClassStartedText,
      @NonNull AppCompatTextView agoraStatusBarClassTimeText,
      @NonNull AppCompatTextView agoraStatusBarClassroomName,
      @NonNull AppCompatImageView agoraStatusBarExitIcon,
      @NonNull AppCompatImageView agoraStatusBarNetworkStateIcon,
      @NonNull AppCompatImageView agoraStatusBarSettingIcon) {
    this.rootView = rootView;
    this.agoraRecordStatus = agoraRecordStatus;
    this.agoraStatusBarCenter = agoraStatusBarCenter;
    this.agoraStatusBarCenterDivider = agoraStatusBarCenterDivider;
    this.agoraStatusBarClassStartedText = agoraStatusBarClassStartedText;
    this.agoraStatusBarClassTimeText = agoraStatusBarClassTimeText;
    this.agoraStatusBarClassroomName = agoraStatusBarClassroomName;
    this.agoraStatusBarExitIcon = agoraStatusBarExitIcon;
    this.agoraStatusBarNetworkStateIcon = agoraStatusBarNetworkStateIcon;
    this.agoraStatusBarSettingIcon = agoraStatusBarSettingIcon;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FcrOnlineEduHeadComponentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FcrOnlineEduHeadComponentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fcr_online_edu_head_component, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FcrOnlineEduHeadComponentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.agora_record_status;
      AppCompatTextView agoraRecordStatus = ViewBindings.findChildViewById(rootView, id);
      if (agoraRecordStatus == null) {
        break missingId;
      }

      id = R.id.agora_status_bar_center;
      LinearLayoutCompat agoraStatusBarCenter = ViewBindings.findChildViewById(rootView, id);
      if (agoraStatusBarCenter == null) {
        break missingId;
      }

      id = R.id.agora_status_bar_center_divider;
      View agoraStatusBarCenterDivider = ViewBindings.findChildViewById(rootView, id);
      if (agoraStatusBarCenterDivider == null) {
        break missingId;
      }

      id = R.id.agora_status_bar_class_started_text;
      AppCompatTextView agoraStatusBarClassStartedText = ViewBindings.findChildViewById(rootView, id);
      if (agoraStatusBarClassStartedText == null) {
        break missingId;
      }

      id = R.id.agora_status_bar_class_time_text;
      AppCompatTextView agoraStatusBarClassTimeText = ViewBindings.findChildViewById(rootView, id);
      if (agoraStatusBarClassTimeText == null) {
        break missingId;
      }

      id = R.id.agora_status_bar_classroom_name;
      AppCompatTextView agoraStatusBarClassroomName = ViewBindings.findChildViewById(rootView, id);
      if (agoraStatusBarClassroomName == null) {
        break missingId;
      }

      id = R.id.agora_status_bar_exit_icon;
      AppCompatImageView agoraStatusBarExitIcon = ViewBindings.findChildViewById(rootView, id);
      if (agoraStatusBarExitIcon == null) {
        break missingId;
      }

      id = R.id.agora_status_bar_network_state_icon;
      AppCompatImageView agoraStatusBarNetworkStateIcon = ViewBindings.findChildViewById(rootView, id);
      if (agoraStatusBarNetworkStateIcon == null) {
        break missingId;
      }

      id = R.id.agora_status_bar_setting_icon;
      AppCompatImageView agoraStatusBarSettingIcon = ViewBindings.findChildViewById(rootView, id);
      if (agoraStatusBarSettingIcon == null) {
        break missingId;
      }

      return new FcrOnlineEduHeadComponentBinding((RelativeLayout) rootView, agoraRecordStatus,
          agoraStatusBarCenter, agoraStatusBarCenterDivider, agoraStatusBarClassStartedText,
          agoraStatusBarClassTimeText, agoraStatusBarClassroomName, agoraStatusBarExitIcon,
          agoraStatusBarNetworkStateIcon, agoraStatusBarSettingIcon);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
