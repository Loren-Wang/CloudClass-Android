// Generated by view binder compiler. Do not edit!
package io.agora.online.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import io.agora.online.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FcrOnlineEduWbColorItemBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final AppCompatImageView agoraApplianceImageview;

  @NonNull
  public final FrameLayout agoraApplianceItem;

  private FcrOnlineEduWbColorItemBinding(@NonNull FrameLayout rootView,
      @NonNull AppCompatImageView agoraApplianceImageview,
      @NonNull FrameLayout agoraApplianceItem) {
    this.rootView = rootView;
    this.agoraApplianceImageview = agoraApplianceImageview;
    this.agoraApplianceItem = agoraApplianceItem;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FcrOnlineEduWbColorItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FcrOnlineEduWbColorItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fcr_online_edu_wb_color_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FcrOnlineEduWbColorItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.agora_appliance_imageview;
      AppCompatImageView agoraApplianceImageview = ViewBindings.findChildViewById(rootView, id);
      if (agoraApplianceImageview == null) {
        break missingId;
      }

      FrameLayout agoraApplianceItem = (FrameLayout) rootView;

      return new FcrOnlineEduWbColorItemBinding((FrameLayout) rootView, agoraApplianceImageview,
          agoraApplianceItem);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
