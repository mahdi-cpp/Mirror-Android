package com.mahdi.car.feed;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import com.mahdi.car.core.BaseFragment;
import com.mahdi.car.core.RootView;
import com.mahdi.car.dialog.popup.QDialog;
import com.mahdi.car.direct.DirectView;
import com.mahdi.car.library.viewAnimator.ViewAnimator;
import com.mahdi.car.server.dtos.FeedDTO;
import com.mahdi.car.server.https.Server;
import com.mahdi.car.server.model.Post;
import com.mahdi.car.server.model.User;
import com.mahdi.car.share.CustomLinearLayoutManager;
import com.mahdi.car.share.component.ui.LayoutHelper;
import com.mahdi.car.story.camera.StoryCameraView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FeedFragment extends BaseFragment {

    private DirectView directView;

    private StoryCameraView storyCameraView;

    private CustomLinearLayoutManager layoutManager;

    private List<User> stories = new ArrayList<>();
    private List<Post> posts = new ArrayList<>();
    private HashMap<Integer, Integer> hashMap = new HashMap<>();

    private boolean checkPermission = true;

    private GestureDetector gestureDetector;

    private boolean isAnimation = false;
    private boolean isShowDirect = false;


    @Override
    public View createView(Context context) {

        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };

//        super.linearLayoutManager = layoutManager = new CustomLinearLayoutManager(context);
        super.createView(context);

        toolbar.settFeed();
        toolbar.setTransparent(false);
        toolbar.setName("Car Application");

        QDialog.getInstance().setParentActivity(getParentActivity());


        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                processScroll(e1, distanceX, distanceY);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                velocity = velocityX;
                return false;
            }

        });

        directView = new DirectView(context);
        directView.setTranslationX(width);
        directView.setDelegate(new DirectView.Delegate() {
            @Override
            public void leftPressed() {
                hide(0);
            }
        });
        contentView.addView(directView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        parentView.invalidate();


        return contentView;
    }

    @Override
    public void toolbarLeftPressed() {

        RootView.instance().showFloatView("Parse", "screen Mirror 12");

//        if (directView.isShow()) {
//            hide(0);
//        } else {
//            if (storyCameraView == null) {
//                storyCameraView = new StoryCameraView(App.context, getParentActivity(), contentView);
//                QZoomView.getInstance().addCamera(storyCameraView);
//            }
//
//            storyCameraView.show();
//        }

    }

    @Override
    public void toolbarCenterPressed() {

    }

    @Override
    public void toolbarRightPressed() {
        RootView.instance().hideFloatView();
        //showDirect();
    }


    private void processScroll(MotionEvent e1, float distanceX, float distanceY) {
        dX -= distanceX;
        dY -= distanceY;

        if (Math.abs(dX) > Math.abs(dY) && !swipe.isSwipe()) {

            layoutManager.setScrollEnabled(false);
            swipe.setScrollEnabled(false);

            swipe.recyclerView.setTranslationX(dX);

//            if (dX < 0) {
//            directView.setDirectTranslationX(dX);
//            }
        }
    }

    private void resetX() {
        if (!permissionSwipe) {
            return;
        }

        layoutManager.setScrollEnabled(true);
        swipe.setScrollEnabled(true);

        if (dX < -centerX || velocity < -1000) {
            ViewAnimator.animate(swipe.recyclerView).setInterpolator(new LinearOutSlowInInterpolator()).translationX(-width).setDuration(300).start();
            directView.showDirect();
        } else {
            directView.resetDirect();
            ViewAnimator.animate(swipe.recyclerView).setInterpolator(new LinearOutSlowInInterpolator()).translationX(0).setDuration(300).start();
        }

        velocity = 0;
        permissionSwipe = false;
    }

    @Override
    public void serverSend() {
        super.serverSend();
        //Toast.makeText(contentView.getContext(), "mahdi error ", Toast.LENGTH_LONG).show();
        server(Server.post.getFeed(page * size, size));
    }

    @Override
    public <T> void serverOnResponse(Call<T> call, Response<T> response) {
        super.serverOnResponse(call, response);

        if (response.isSuccessful()) {

            FeedDTO feed = (FeedDTO) response.body();
//            List<Post> feed_posts  = (List<Post>) response.body();

            //Toast.makeText(contentView.getContext(), "response.code():  " + feed.posts.size(), Toast.LENGTH_SHORT).show();

            if (feed != null) {

                //if (page == 0) {

                stories = feed.stories;
                directView.setUsers(stories);
                Collections.shuffle(stories);
                //Collections.shuffle(feed.posts);

                RootView.instance().setOwner(stories.get(0));
                //FatherView.instance().setOwner(feed.user);

                //App.userid = feed.user.userid;
                //App.cartCount = feed.cartCount;
                //}

                //Toast.makeText(contentView.getContext(), "posts size:  " + posts.size(), Toast.LENGTH_LONG).show();

                for (Post post : feed.posts) {
                    if (post.Medias != null) {
                        if (post.Medias.size() > 0) {
                            //if (post.medias.get(0).video != null) {
                            posts.add(post);
                            //}
                        }
                    }
                }


                //                int videoPosition = 0;
                //                List<Uri> uris = new ArrayList<>();
                //
                //                for (int i = 0; i < posts.size(); i++) {
                //                    if (posts.get(i).medias.get(0).video != null) {
                //                        uris.add(Uri.parse(App.videos + posts.get(i).medias.get(0).video));
                //                        hashMap.put(i, videoPosition);
                //                        videoPosition++;
                //                    }
                //                }
                //
                //                player.addMediaSources(uris);


//                if (feed.posts.size() != size) {
//                    has_next_page = false;
//                }
                page++;

//                adapter.notifyDataSetChanged();
            }

        } else {

            if (response.code() == 403) {

//                LoginFragment loginFragment = new LoginFragment();
//                loginFragment.setDelegate(() -> serverSend());
//                presentFragment(loginFragment);

            } else {
                //Log.e("FeedDTO", "Access denied: " + response.code());
                //Toast.makeText(getParentActivity(), "Access denied: " + response.code(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void swipeScrolled() {
        for (int i = 0; i < swipe.recyclerView.getChildCount(); i++) {

            View view = swipe.recyclerView.getChildAt(i);

//            if (view instanceof PostCell) {
//                PostCell cell = (PostCell) view;
//                cell.setSwipe(swipe.isSwipe());
//            }
        }
    }

    @Override
    public void endStoryShow() {
        super.endStoryShow();
        if (directView != null)
            directView.endStoryShow();

//        storiesListView.endStoryShow();
    }

    public void showDirect() {
        if (isAnimation) {
            return;
        }

        isShowDirect = true;
        isAnimation = true;
        directView.setShow(true);

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(swipe, "translationX", -width);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(directView, "translationX", 0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator1, objectAnimator2);
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.setDuration(300);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = false;
                directView.refresh();
            }
        });
        animatorSet.start();
    }

    public void hide(int delay) {
        if (isAnimation) {
            return;
        }

        directView.setShow(false);
        isShowDirect = false;
        isAnimation = true;

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(swipe, "translationX", 0);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(directView, "translationX", width);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setStartDelay(delay);
        animatorSet.playTogether(objectAnimator1, objectAnimator2);
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.setDuration(300);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = false;
                super.onAnimationEnd(animation);
            }
        });
        animatorSet.start();
    }


    @Override
    protected void onFragmentDestroy() {
        super.onFragmentDestroy();


    }

    @Override
    public void onResume() {
        super.onResume();

        if (storyCameraView != null)
            storyCameraView.onResume();

        if (checkPermission && Build.VERSION.SDK_INT >= 23) {

            Activity activity = getParentActivity();

            if (activity != null) {
                checkPermission = false;
                askForPermissons();
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        super.onBackPressed();

        if (directView.isShow()) {
            hide(0);
            return false;
        }

//        if (storyView != null && storyView.isShow()) {
//            storyView.hide(storiesListView);
//            return false;
//        }

        if (storyCameraView != null && storyCameraView.isShow()) {
            storyCameraView.onBackPressed();
            return false;
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void askForPermissons() {
        Activity activity = getParentActivity();
        if (activity == null) {
            return;
        }
        ArrayList<String> permissons = new ArrayList<>();

        if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissons.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissons.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        String[] items = permissons.toArray(new String[permissons.size()]);
        try {
            activity.requestPermissions(items, 1);
        } catch (Exception ignore) {
        }
    }

    @Override
    public void onRequestPermissionsResultFragment(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int a = 0; a < permissions.length; a++) {
                if (grantResults.length <= a || grantResults[a] != PackageManager.PERMISSION_GRANTED) {
                    continue;
                }
                switch (permissions[a]) {
                    case Manifest.permission.CAMERA:
                        //ContactsController.getInstance().forceImportContacts();
                        break;
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        //ImageUtils.getInstance().checkMediaPaths();
                        break;
                }
            }
        }
    }

}

