package br.com.budismo.nikkyojers.ui.feed;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.budismo.nikkyojers.R;
import br.com.budismo.nikkyojers.data.Post;
import br.com.budismo.nikkyojers.util.Util;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddPostActivityFragment extends Fragment {

    private EditText mEditTitle;
    private EditText mEditDescription;

    private AddPostListener mListener;

    public interface AddPostListener {
        void onEnableToPost(boolean enable);
    }

    public AddPostActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mListener = (AddPostListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        mEditTitle = view.findViewById(R.id.edit_post_title);
        mEditDescription = view.findViewById(R.id.edit_post_description);
        bindUser(view);
        return view;
    }

    private void bindUser(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ImageView imageView = view.findViewById(R.id.iv_profile_image);
        if (user!= null) {
            if (user.getPhotoUrl() != null) {
                Util.bindUserPictureIntoView(view.getContext(), user.getPhotoUrl(), imageView);
            } else {
                Util.bindPlaceholderPictureIntoView(view.getContext(), imageView);
            }
            TextView tvUserName = view.findViewById(R.id.tv_username);
            tvUserName.setText(user.getDisplayName());
            mEditDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().trim().length() > 0) {
                        mListener.onEnableToPost(true);
                    } else {
                        mListener.onEnableToPost(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        } else {
            Util.startFirebaseUIActivity(getActivity());
        }
    }

    public Post getPost() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return new Post(user.getUid(), mEditTitle.getText().toString(), mEditDescription.getText().toString());
        } else {
            return null;
        }
    }
}
