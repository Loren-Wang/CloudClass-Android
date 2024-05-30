// Generated by view binder compiler. Do not edit!
package io.agora.online.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import io.agora.online.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FcrOnlineChatTabPublicLayoutBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final View agoraChatDivider;

  @NonNull
  public final RelativeLayout agoraChatLayout;

  @NonNull
  public final AppCompatImageView agoraChatNoMessageIcon;

  @NonNull
  public final RelativeLayout agoraChatNoMessagePlaceholder;

  @NonNull
  public final RecyclerView agoraChatRecycler;

  @NonNull
  public final RelativeLayout agoraChatStudentMuteContent;

  @NonNull
  public final AppCompatImageView agoraChatStudentMuteIcon;

  @NonNull
  public final RelativeLayout agoraChatStudentMuteLayout;

  private FcrOnlineChatTabPublicLayoutBinding(@NonNull RelativeLayout rootView,
      @NonNull View agoraChatDivider, @NonNull RelativeLayout agoraChatLayout,
      @NonNull AppCompatImageView agoraChatNoMessageIcon,
      @NonNull RelativeLayout agoraChatNoMessagePlaceholder,
      @NonNull RecyclerView agoraChatRecycler, @NonNull RelativeLayout agoraChatStudentMuteContent,
      @NonNull AppCompatImageView agoraChatStudentMuteIcon,
      @NonNull RelativeLayout agoraChatStudentMuteLayout) {
    this.rootView = rootView;
    this.agoraChatDivider = agoraChatDivider;
    this.agoraChatLayout = agoraChatLayout;
    this.agoraChatNoMessageIcon = agoraChatNoMessageIcon;
    this.agoraChatNoMessagePlaceholder = agoraChatNoMessagePlaceholder;
    this.agoraChatRecycler = agoraChatRecycler;
    this.agoraChatStudentMuteContent = agoraChatStudentMuteContent;
    this.agoraChatStudentMuteIcon = agoraChatStudentMuteIcon;
    this.agoraChatStudentMuteLayout = agoraChatStudentMuteLayout;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FcrOnlineChatTabPublicLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FcrOnlineChatTabPublicLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fcr_online_chat_tab_public_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FcrOnlineChatTabPublicLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.agora_chat_divider;
      View agoraChatDivider = ViewBindings.findChildViewById(rootView, id);
      if (agoraChatDivider == null) {
        break missingId;
      }

      id = R.id.agora_chat_layout;
      RelativeLayout agoraChatLayout = ViewBindings.findChildViewById(rootView, id);
      if (agoraChatLayout == null) {
        break missingId;
      }

      id = R.id.agora_chat_no_message_icon;
      AppCompatImageView agoraChatNoMessageIcon = ViewBindings.findChildViewById(rootView, id);
      if (agoraChatNoMessageIcon == null) {
        break missingId;
      }

      id = R.id.agora_chat_no_message_placeholder;
      RelativeLayout agoraChatNoMessagePlaceholder = ViewBindings.findChildViewById(rootView, id);
      if (agoraChatNoMessagePlaceholder == null) {
        break missingId;
      }

      id = R.id.agora_chat_recycler;
      RecyclerView agoraChatRecycler = ViewBindings.findChildViewById(rootView, id);
      if (agoraChatRecycler == null) {
        break missingId;
      }

      id = R.id.agora_chat_student_mute_content;
      RelativeLayout agoraChatStudentMuteContent = ViewBindings.findChildViewById(rootView, id);
      if (agoraChatStudentMuteContent == null) {
        break missingId;
      }

      id = R.id.agora_chat_student_mute_icon;
      AppCompatImageView agoraChatStudentMuteIcon = ViewBindings.findChildViewById(rootView, id);
      if (agoraChatStudentMuteIcon == null) {
        break missingId;
      }

      id = R.id.agora_chat_student_mute_layout;
      RelativeLayout agoraChatStudentMuteLayout = ViewBindings.findChildViewById(rootView, id);
      if (agoraChatStudentMuteLayout == null) {
        break missingId;
      }

      return new FcrOnlineChatTabPublicLayoutBinding((RelativeLayout) rootView, agoraChatDivider,
          agoraChatLayout, agoraChatNoMessageIcon, agoraChatNoMessagePlaceholder, agoraChatRecycler,
          agoraChatStudentMuteContent, agoraChatStudentMuteIcon, agoraChatStudentMuteLayout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}