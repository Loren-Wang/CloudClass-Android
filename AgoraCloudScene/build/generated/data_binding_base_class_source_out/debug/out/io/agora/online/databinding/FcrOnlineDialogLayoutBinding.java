// Generated by view binder compiler. Do not edit!
package io.agora.online.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import io.agora.online.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FcrOnlineDialogLayoutBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppCompatImageView agoraDialogIcon;

  @NonNull
  public final ConstraintLayout agoraDialogLayout;

  @NonNull
  public final AppCompatTextView agoraDialogMessage;

  @NonNull
  public final AppCompatTextView agoraDialogNegativeButton;

  @NonNull
  public final AppCompatTextView agoraDialogPositiveButton;

  @NonNull
  public final AppCompatTextView agoraDialogTitleText;

  @NonNull
  public final View line1;

  @NonNull
  public final View line2;

  private FcrOnlineDialogLayoutBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppCompatImageView agoraDialogIcon, @NonNull ConstraintLayout agoraDialogLayout,
      @NonNull AppCompatTextView agoraDialogMessage,
      @NonNull AppCompatTextView agoraDialogNegativeButton,
      @NonNull AppCompatTextView agoraDialogPositiveButton,
      @NonNull AppCompatTextView agoraDialogTitleText, @NonNull View line1, @NonNull View line2) {
    this.rootView = rootView;
    this.agoraDialogIcon = agoraDialogIcon;
    this.agoraDialogLayout = agoraDialogLayout;
    this.agoraDialogMessage = agoraDialogMessage;
    this.agoraDialogNegativeButton = agoraDialogNegativeButton;
    this.agoraDialogPositiveButton = agoraDialogPositiveButton;
    this.agoraDialogTitleText = agoraDialogTitleText;
    this.line1 = line1;
    this.line2 = line2;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FcrOnlineDialogLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FcrOnlineDialogLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fcr_online_dialog_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FcrOnlineDialogLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.agora_dialog_icon;
      AppCompatImageView agoraDialogIcon = ViewBindings.findChildViewById(rootView, id);
      if (agoraDialogIcon == null) {
        break missingId;
      }

      ConstraintLayout agoraDialogLayout = (ConstraintLayout) rootView;

      id = R.id.agora_dialog_message;
      AppCompatTextView agoraDialogMessage = ViewBindings.findChildViewById(rootView, id);
      if (agoraDialogMessage == null) {
        break missingId;
      }

      id = R.id.agora_dialog_negative_button;
      AppCompatTextView agoraDialogNegativeButton = ViewBindings.findChildViewById(rootView, id);
      if (agoraDialogNegativeButton == null) {
        break missingId;
      }

      id = R.id.agora_dialog_positive_button;
      AppCompatTextView agoraDialogPositiveButton = ViewBindings.findChildViewById(rootView, id);
      if (agoraDialogPositiveButton == null) {
        break missingId;
      }

      id = R.id.agora_dialog_title_text;
      AppCompatTextView agoraDialogTitleText = ViewBindings.findChildViewById(rootView, id);
      if (agoraDialogTitleText == null) {
        break missingId;
      }

      id = R.id.line1;
      View line1 = ViewBindings.findChildViewById(rootView, id);
      if (line1 == null) {
        break missingId;
      }

      id = R.id.line2;
      View line2 = ViewBindings.findChildViewById(rootView, id);
      if (line2 == null) {
        break missingId;
      }

      return new FcrOnlineDialogLayoutBinding((ConstraintLayout) rootView, agoraDialogIcon,
          agoraDialogLayout, agoraDialogMessage, agoraDialogNegativeButton,
          agoraDialogPositiveButton, agoraDialogTitleText, line1, line2);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}