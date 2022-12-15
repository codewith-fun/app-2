package com.mcode.app2.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.mcode.app2.behaviour.uc.UcNewsHeaderPagerBehavior.OnPagerStateListener
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.mcode.app2.fragment.TestFragment
import com.mcode.app2.behaviour.uc.UcNewsHeaderPagerBehavior
import android.os.Bundle
import com.mcode.app2.R
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mcode.app2.BuildConfig
import com.google.android.material.snackbar.Snackbar
import com.mcode.app2.adapter.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), OnTabSelectedListener, OnPagerStateListener{
    private var mNewsPager: ViewPager? = null
    private var mTableLayout: TabLayout? = null
    private var mFragments: MutableList<TestFragment>? = null
    private var mPagerBehavior: UcNewsHeaderPagerBehavior? = null
    lateinit var rv_quick_links :RecyclerView
    private var mQuickAdapter:QuickLinksAdapter? = null
    val quickLink =  QuickLinksRepository().getAllQuickLinks()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uc_main_pager)
        initView()
    }


    protected fun initView() {
        mPagerBehavior =
            (findViewById<View>(R.id.id_uc_news_header_pager).layoutParams as CoordinatorLayout.LayoutParams).behavior as UcNewsHeaderPagerBehavior?
        mPagerBehavior!!.setPagerStateListener(this)
        mNewsPager = findViewById<View>(R.id.id_uc_news_content) as ViewPager
        mTableLayout = findViewById<View>(R.id.id_uc_news_tab) as TabLayout
        rv_quick_links = findViewById(R.id.rv_quick_links)
        val layoutmanager = GridLayoutManager(this,2)
        rv_quick_links.apply {
            layoutManager =layoutmanager
            mQuickAdapter = QuickLinksAdapter(this@MainActivity, object :ClickOnQuickLinks{
                override fun OnQuickLink(position: Int?) {

                }
            })
            adapter = mQuickAdapter
        }
        mQuickAdapter?.upDateList(quickLink.toList())
        mFragments = ArrayList()
        for (i in 0..3) {
            (mFragments as ArrayList<TestFragment>).add(TestFragment.newInstance(i.toString(), false))
            mTableLayout!!.addTab(mTableLayout!!.newTab().setText("Tab$i"))
        }
        mTableLayout!!.tabMode = TabLayout.MODE_FIXED
        mTableLayout!!.setOnTabSelectedListener(this)
        mNewsPager!!.addOnPageChangeListener(TabLayoutOnPageChangeListener(mTableLayout))
        mNewsPager!!.adapter = TestFragmentAdapter(mFragments, supportFragmentManager)
    }

    private fun openMyGitHub() {
        val uri = Uri.parse("https://github.com/codewith-fun")
        val it = Intent(Intent.ACTION_VIEW, uri)
        startActivity(it)
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        mNewsPager!!.currentItem = tab.position
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {}
    override fun onTabReselected(tab: TabLayout.Tab) {}
    override fun onPagerClosed() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onPagerClosed: ")
        }
        Snackbar.make(mNewsPager!!, "pager closed", Snackbar.LENGTH_SHORT).show()
    }

    override fun onPagerOpened() {
        Snackbar.make(mNewsPager!!, "pager opened", Snackbar.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (mPagerBehavior != null && mPagerBehavior!!.isClosed) {
            mPagerBehavior!!.openPager()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        fun newIntent(context: Context?): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }


}