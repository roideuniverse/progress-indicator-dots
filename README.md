# progress-indicator-dots
A progress indicator that uses dots to show progress

#Motivation

I once wrote this progress dots indicator for one app, then wrote it again for another app. Now I want to use it again in my next app, so I thought this is a good time to release it as a library and make it open source. 

# Install
I am in the process of releasing it, in the meantime you have download it and import it as a submodule.

#How it looks like

![alt tag](https://cloud.githubusercontent.com/assets/233539/16936627/1ede4b60-4d36-11e6-976c-d3c707c86c50.gif)

#Usage
1. Declare it in a layout that you need it in. 
  ```
  <com.roide.progressdotslib.ProgressDotWidget
      android:id="@+id/progress_dots"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:background="#3F51B5"
      android:padding="@dimen/standard_margins"
      />
  ```

2. Get the reference of the View in the activity
  ```
  mViewPager = (ViewPager) findViewById(R.id.container);
  mProgressDotWidget = (ProgressDotWidget) findViewById(R.id.progress_dots);
  ```

3. Set the no of `dots` that you want to show
  ```
  mProgressDotWidget.setDotCount(mSectionsPagerAdapter.getCount());
  ```
  
4. When there is a change in the page, or you want to move the `dot` around, just call  
  ```
  mProgressDotWidget.setActivePosition(position);
  ```
  **OR**
  
  When using with ViewPager, 
  ```
  mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageSelected(int position) {
      mProgressDotWidget.setActivePosition(position);
    }
  });
        
  ```

  **Note**: Although the example is with ViewPager, this library does not depend on ViewPager

## License

[MIT](LICENSE)
