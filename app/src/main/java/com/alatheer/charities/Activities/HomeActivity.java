package com.alatheer.charities.Activities;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.charities.Adapters.DiscreteAdapter;
import com.alatheer.charities.Adapters.SliderAdapter;
import com.alatheer.charities.Fragments.Fragment_AboutUs;
import com.alatheer.charities.Fragments.Fragment_ContactUs;
import com.alatheer.charities.Fragments.Fragment_News;
import com.alatheer.charities.Fragments.Fragment_Notification;
import com.alatheer.charities.Fragments.Fragment_Profile;
import com.alatheer.charities.Fragments.Fragment_Programs;
import com.alatheer.charities.Models.DiscreteModel;
import com.alatheer.charities.Models.SliderModel;
import com.alatheer.charities.R;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import q.rorbin.badgeview.QBadgeView;

public class HomeActivity extends AppCompatActivity {
    private ContextMenuDialogFragment contextMenuDialogFragment;
    private TabLayout tab;
    private ViewPager pager;
    private SliderAdapter sliderAdapter;
    private List<SliderModel> sliderModelList;
    private List<DiscreteModel> discreteModelList;
    private DiscreteAdapter discreteAdapter;
    private DiscreteScrollView discreteScrollView;
    private ImageView img_back,img_next;
    private BottomNavigationView navBottom;
    private View view;
    private BottomSheetBehavior behavior;
    //////////////////////////////////////////
    private Fragment_Profile fragment_profile;
    private Fragment_Notification fragment_notification;
    private Fragment_AboutUs fragment_aboutUs;
    private Fragment_ContactUs fragment_contactUs;
    private Fragment_News fragment_news;
    private Fragment_Programs fragment_programs;
    //////////////////////////////////////////
    private ImageView image_back;
    private TextView tv_title;
    private Toolbar toolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    private void initView() {
        sliderModelList = new ArrayList<>();
        discreteModelList = new ArrayList<>();
        //////////////////////////////////////////////////////
        discreteAdapter = new DiscreteAdapter(discreteModelList,this);
        view = findViewById(R.id.root);
        behavior = BottomSheetBehavior.from(view);
        image_back = findViewById(R.id.image_back);
        tv_title = findViewById(R.id.tv_title);
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        tab = findViewById(R.id.tab);
        pager = findViewById(R.id.pager);
        pager.beginFakeDrag();
        discreteScrollView = findViewById(R.id.recView);
        img_back = findViewById(R.id.img_back);
        img_next = findViewById(R.id.img_next);


        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = discreteScrollView.getCurrentItem()+1;
                discreteScrollView.smoothScrollToPosition(pos);
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = discreteScrollView.getCurrentItem()-1;
                discreteScrollView.smoothScrollToPosition(pos);
            }
        });


        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                navBottom.setSelectedItemId(R.id.home);
            }
        });
        //////////////////////////////////////////////////////
        discreteScrollView.setAdapter(discreteAdapter);
        discreteScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.BOTTOM)
                .build());
        tab.setupWithViewPager(pager);

        navBottom = findViewById(R.id.navBottom);
        BottomNavigationMenuView navigationMenuView = (BottomNavigationMenuView) navBottom.getChildAt(0);
        View v = navigationMenuView.getChildAt(1);
        new QBadgeView(this)
                .bindTarget(v)
                .setGravityOffset(35f,5f,true)
                .setBadgeGravity(Gravity.END | Gravity.TOP)
                .setBadgeBackground(ContextCompat.getDrawable(this,R.drawable.budged_not_bg))
                .setShowShadow(false)
                .setBadgeNumber(5);

        navBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id)
                {
                    case R.id.profile:
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        fragment_profile = Fragment_Profile.getInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container,fragment_profile).commit();
                        tv_title.setText("Profile");
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        break;

                    case R.id.notification:
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        fragment_notification = Fragment_Notification.getInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container,fragment_notification).commit();
                        tv_title.setText("Notifications");
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        break;
                }
                return true;
            }
        });

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState==BottomSheetBehavior.STATE_DRAGGING)
                {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        /////////////////////////////////////////////////////////
        MenuObject close = new MenuObject("إغلاق    ");
        close.setResource(R.drawable.close_icon);
        MenuObject mostfeed = new MenuObject("المستفيدين    ");
        MenuObject motbare = new MenuObject("المتبرعين    ");
        MenuObject motatawe = new MenuObject("المتطوعين    ");
        MenuObject kafeel = new MenuObject("الكفيل    ");
        MenuObject mowazuf = new MenuObject("الموظفين    ");
        MenuObject edarah = new MenuObject("الادارة    ");

        MenuObject logout = new MenuObject("خروج    ");
        logout.setResource(R.drawable.logut);

        List<MenuObject> menuObjects = new ArrayList<>();
        menuObjects.add(close);
        menuObjects.add(mostfeed);
        menuObjects.add(motbare);
        menuObjects.add(motatawe);
        menuObjects.add(kafeel);
        menuObjects.add(mowazuf);
        menuObjects.add(edarah);

        menuObjects.add(logout);

        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(menuObjects);
        menuParams.setClosableOutside(true);
        contextMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        /////////////////////////////////////////////////////////

        discreteScrollView.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
                if (discreteModelList.size()==1)
                {
                    img_back.setVisibility(View.INVISIBLE);
                    img_next.setVisibility(View.INVISIBLE);

                }else if (discreteModelList.size()>1)
                {
                    if (adapterPosition==0)
                    {
                        img_back.setVisibility(View.INVISIBLE);

                    }else if (adapterPosition==discreteModelList.size()-1)
                    {
                        img_next.setVisibility(View.INVISIBLE);
                    }else
                        {
                            img_back.setVisibility(View.VISIBLE);
                            img_next.setVisibility(View.VISIBLE);
                        }
                }
            }
        });

        getDataSlider();
        discreteModelList.addAll(getDiscreteData());
        discreteAdapter.notifyDataSetChanged();


        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        discreteScrollView.scrollToPosition(1);
                        img_back.setVisibility(View.VISIBLE);
                        img_next.setVisibility(View.VISIBLE);
                    }
                },500);

    }

    private List<DiscreteModel> getDiscreteData() {
        List<DiscreteModel> discreteModels = new ArrayList<>();
        discreteModels.add(new DiscreteModel(R.drawable.news_icon,"اخبار"));
        discreteModels.add(new DiscreteModel(R.drawable.prog,"برامج"));
        discreteModels.add(new DiscreteModel(R.drawable.news,"عن الجمعية"));
        discreteModels.add(new DiscreteModel(R.drawable.contact,"اتصل بنا"));

        return discreteModels;

    }

    private void getDataSlider() {
        sliderModelList.add(new SliderModel(R.drawable.news_icon,"الخبر الاول","تفاصيل الخبر الاول"));
        sliderModelList.add(new SliderModel(R.drawable.contact,"الخبر الثاني","تفاصيل الخبر الثاني"));
        sliderModelList.add(new SliderModel(R.drawable.prog,"الخبر الثالث","تفاصيل الخبر الثالث"));

        sliderAdapter = new SliderAdapter(sliderModelList,this);
        pager.setAdapter(sliderAdapter);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new timerTask(),4000,6000);
    }

    public void setSelectDiscretePos(int pos)
    {
        switch (pos)
        {
            case 0:
                if (fragment_news==null)
                {
                    fragment_news = Fragment_News.getInstance();

                }
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container,fragment_news).commit();
                tv_title.setText("News");
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                break;
            case 1:
                if (fragment_programs==null)
                {
                    fragment_programs = Fragment_Programs.getInstance();

                }
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container,fragment_programs).commit();
                tv_title.setText("Programs");
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case 2:
                if (fragment_aboutUs==null)
                {
                    fragment_aboutUs = Fragment_AboutUs.getInstance();

                }
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container,fragment_aboutUs).commit();
                tv_title.setText("About us");
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case 3:
                if (fragment_contactUs==null)
                {
                    fragment_contactUs = Fragment_ContactUs.getInstance();

                }
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container,fragment_contactUs).commit();
                tv_title.setText("Contact us");
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item:
                contextMenuDialogFragment.show(getSupportFragmentManager(), "ContextMenuDialogFragment");
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private class timerTask extends TimerTask{
        @Override
        public void run() {
           HomeActivity.this.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   if (pager.getCurrentItem()<pager.getChildCount()-1)
                   {
                       pager.setCurrentItem(pager.getCurrentItem()+1);
                   }else
                   {
                       sliderAdapter = new SliderAdapter(sliderModelList,HomeActivity.this);
                       pager.setAdapter(sliderAdapter);
                   }
               }
           });

        }
    }

    @Override
    public void onBackPressed() {
        if (contextMenuDialogFragment.isVisible())
        {
            contextMenuDialogFragment.dismiss();
            navBottom.setSelectedItemId(R.id.home);

        }
        else if (behavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
        {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            navBottom.setSelectedItemId(R.id.home);

        }else
            {
                super.onBackPressed();

            }


    }
}