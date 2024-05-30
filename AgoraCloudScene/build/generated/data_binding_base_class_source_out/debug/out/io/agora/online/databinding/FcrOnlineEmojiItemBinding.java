// Generated by view binder compiler. Do not edit!
package io.agora.online.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import io.agora.online.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FcrOnlineEmojiItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView emoji;

  private FcrOnlineEmojiItemBinding(@NonNull LinearLayout rootView, @NonNull TextView emoji) {
    this.rootView = rootView;
    this.emoji = emoji;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FcrOnlineEmojiItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FcrOnlineEmojiItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fcr_online_emoji_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FcrOnlineEmojiItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.emoji;
      TextView emoji = ViewBindings.findChildViewById(rootView, id);
      if (emoji == null) {
        break missingId;
      }

      return new FcrOnlineEmojiItemBinding((LinearLayout) rootView, emoji);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}