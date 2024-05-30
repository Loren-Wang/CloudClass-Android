// Generated by view binder compiler. Do not edit!
package io.agora.online.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import io.agora.online.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FcrOnlineIclickerAnswerListItemBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppCompatTextView answersGridView;

  @NonNull
  public final AppCompatTextView name;

  @NonNull
  public final AppCompatTextView time;

  @NonNull
  public final View view;

  private FcrOnlineIclickerAnswerListItemBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppCompatTextView answersGridView, @NonNull AppCompatTextView name,
      @NonNull AppCompatTextView time, @NonNull View view) {
    this.rootView = rootView;
    this.answersGridView = answersGridView;
    this.name = name;
    this.time = time;
    this.view = view;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FcrOnlineIclickerAnswerListItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FcrOnlineIclickerAnswerListItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fcr_online_iclicker_answer_list_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FcrOnlineIclickerAnswerListItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.answers_gridView;
      AppCompatTextView answersGridView = ViewBindings.findChildViewById(rootView, id);
      if (answersGridView == null) {
        break missingId;
      }

      id = R.id.name;
      AppCompatTextView name = ViewBindings.findChildViewById(rootView, id);
      if (name == null) {
        break missingId;
      }

      id = R.id.time;
      AppCompatTextView time = ViewBindings.findChildViewById(rootView, id);
      if (time == null) {
        break missingId;
      }

      id = R.id.view;
      View view = ViewBindings.findChildViewById(rootView, id);
      if (view == null) {
        break missingId;
      }

      return new FcrOnlineIclickerAnswerListItemBinding((ConstraintLayout) rootView,
          answersGridView, name, time, view);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}