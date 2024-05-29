// Generated by view binder compiler. Do not edit!
package io.agora.online.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import io.agora.online.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FcrOnlineVoteResultItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView voteResultItemChoice;

  @NonNull
  public final ProgressBar voteResultItemProgressBar;

  @NonNull
  public final TextView voteResultItemProportion;

  private FcrOnlineVoteResultItemBinding(@NonNull LinearLayout rootView,
      @NonNull TextView voteResultItemChoice, @NonNull ProgressBar voteResultItemProgressBar,
      @NonNull TextView voteResultItemProportion) {
    this.rootView = rootView;
    this.voteResultItemChoice = voteResultItemChoice;
    this.voteResultItemProgressBar = voteResultItemProgressBar;
    this.voteResultItemProportion = voteResultItemProportion;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FcrOnlineVoteResultItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FcrOnlineVoteResultItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fcr_online_vote_result_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FcrOnlineVoteResultItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.vote_result_item_choice;
      TextView voteResultItemChoice = ViewBindings.findChildViewById(rootView, id);
      if (voteResultItemChoice == null) {
        break missingId;
      }

      id = R.id.vote_result_item_progressBar;
      ProgressBar voteResultItemProgressBar = ViewBindings.findChildViewById(rootView, id);
      if (voteResultItemProgressBar == null) {
        break missingId;
      }

      id = R.id.vote_result_item_proportion;
      TextView voteResultItemProportion = ViewBindings.findChildViewById(rootView, id);
      if (voteResultItemProportion == null) {
        break missingId;
      }

      return new FcrOnlineVoteResultItemBinding((LinearLayout) rootView, voteResultItemChoice,
          voteResultItemProgressBar, voteResultItemProportion);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
