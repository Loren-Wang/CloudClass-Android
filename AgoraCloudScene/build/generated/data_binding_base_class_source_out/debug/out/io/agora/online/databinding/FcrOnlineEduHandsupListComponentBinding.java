// Generated by view binder compiler. Do not edit!
package io.agora.online.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import io.agora.online.R;
import io.agora.online.options.AgoraEduHandsUpListComponent;
import java.lang.NullPointerException;
import java.lang.Override;

public final class FcrOnlineEduHandsupListComponentBinding implements ViewBinding {
  @NonNull
  private final AgoraEduHandsUpListComponent rootView;

  @NonNull
  public final AgoraEduHandsUpListComponent agoraHandsupListWidget;

  private FcrOnlineEduHandsupListComponentBinding(@NonNull AgoraEduHandsUpListComponent rootView,
      @NonNull AgoraEduHandsUpListComponent agoraHandsupListWidget) {
    this.rootView = rootView;
    this.agoraHandsupListWidget = agoraHandsupListWidget;
  }

  @Override
  @NonNull
  public AgoraEduHandsUpListComponent getRoot() {
    return rootView;
  }

  @NonNull
  public static FcrOnlineEduHandsupListComponentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FcrOnlineEduHandsupListComponentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fcr_online_edu_handsup_list_component, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FcrOnlineEduHandsupListComponentBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    AgoraEduHandsUpListComponent agoraHandsupListWidget = (AgoraEduHandsUpListComponent) rootView;

    return new FcrOnlineEduHandsupListComponentBinding((AgoraEduHandsUpListComponent) rootView,
        agoraHandsupListWidget);
  }
}