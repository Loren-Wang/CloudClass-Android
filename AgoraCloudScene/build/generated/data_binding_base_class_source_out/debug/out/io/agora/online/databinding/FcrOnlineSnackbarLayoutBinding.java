// Generated by view binder compiler. Do not edit!
package io.agora.online.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import io.agora.online.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FcrOnlineSnackbarLayoutBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final LinearLayoutCompat agoraDialogLayout;

  @NonNull
  public final ImageView fcrSClose;

  @NonNull
  public final TextView fcrSContent;

  private FcrOnlineSnackbarLayoutBinding(@NonNull LinearLayoutCompat rootView,
      @NonNull LinearLayoutCompat agoraDialogLayout, @NonNull ImageView fcrSClose,
      @NonNull TextView fcrSContent) {
    this.rootView = rootView;
    this.agoraDialogLayout = agoraDialogLayout;
    this.fcrSClose = fcrSClose;
    this.fcrSContent = fcrSContent;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static FcrOnlineSnackbarLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FcrOnlineSnackbarLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fcr_online_snackbar_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FcrOnlineSnackbarLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.agora_dialog_layout;
      LinearLayoutCompat agoraDialogLayout = ViewBindings.findChildViewById(rootView, id);
      if (agoraDialogLayout == null) {
        break missingId;
      }

      id = R.id.fcr_s_close;
      ImageView fcrSClose = ViewBindings.findChildViewById(rootView, id);
      if (fcrSClose == null) {
        break missingId;
      }

      id = R.id.fcr_s_content;
      TextView fcrSContent = ViewBindings.findChildViewById(rootView, id);
      if (fcrSContent == null) {
        break missingId;
      }

      return new FcrOnlineSnackbarLayoutBinding((LinearLayoutCompat) rootView, agoraDialogLayout,
          fcrSClose, fcrSContent);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
