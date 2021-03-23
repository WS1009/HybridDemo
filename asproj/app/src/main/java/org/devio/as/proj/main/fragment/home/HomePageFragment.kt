package org.devio.`as`.proj.main.fragment.home

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_home.*
import org.devio.`as`.proj.common.ui.component.HiBaseFragment
import org.devio.`as`.proj.main.R
import org.devio.`as`.proj.main.http.ApiFactory
import org.devio.`as`.proj.main.http.api.HomeApi
import org.devio.`as`.proj.main.model.TabCategory
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse
import org.devio.hi.ui.tab.bottom.HiTabBottomLayout
import org.devio.hi.ui.tab.common.IHiTabLayout
import org.devio.hi.ui.tab.top.HiTabTopInfo

class HomePageFragment : HiBaseFragment() {
    private var topTabSelectIndex: Int = 0
    private val DEFAULT_SELECT_INDEX: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HiTabBottomLayout.clipBottomPadding(view_pager)
        queryTabList()
    }

    private fun queryTabList() {

        ApiFactory.create(HomeApi::class.java)
            .queryTabList().enqueue(object : HiCallback<List<TabCategory>> {
                override fun onSuccess(response: HiResponse<List<TabCategory>>) {
                    val data = response.data
                    if (response.successful() && data != null) {
                        //两次回调：一次缓存数据，一次接口数据
                        updateUI(data)
                    }
                }

                override fun onFailed(throwable: Throwable) {

                }
            })
    }

    private val onTabSelectedListener =
        IHiTabLayout.OnTabSelectedListener<HiTabTopInfo<*>> { index, prevInfo, nextInfo ->
            //点击之后选中的那个下标
            if (view_pager.currentItem != index) {
                view_pager.setCurrentItem(index, false)
            }
        }

    private fun updateUI(data: List<TabCategory>) {
        //需要小心处理  ---viewmodel+livedata
        if (!isAlive) return

        val topTabs = mutableListOf<HiTabTopInfo<Int>>()
        data.forEachIndexed { index, tabCategory ->
            val defaultColor = ContextCompat.getColor(context!!, R.color.color_333)
            val selectColor = ContextCompat.getColor(context!!, R.color.color_dd2)
            val tabTopInfo = HiTabTopInfo<Int>(tabCategory.categoryName, defaultColor, selectColor)

            topTabs.add(tabTopInfo)
        }

        val viewPager = view_pager
        val topTabLayout = top_tab_layout
        topTabLayout.inflateInfo(topTabs as List<HiTabTopInfo<*>>)
        topTabLayout.defaultSelected(topTabs[DEFAULT_SELECT_INDEX])

        topTabLayout.addTabSelectedChangeListener(onTabSelectedListener)

        if (viewPager.adapter == null) {
            viewPager.adapter = HomePagerAdapter(
                childFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            )

            viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    //这个方法被触发有两种可能，第一种切换顶部tab  第二种 手动滑动翻页
                    //如果是 手动滑动翻页人

                    if (position != topTabSelectIndex) {
                        //去通知topTabLayout进行切换
                        topTabLayout.defaultSelected(topTabs[position])
                        topTabSelectIndex = position
                    }
                }
            })
        }

        (viewPager.adapter as HomePagerAdapter).update(data)
    }


    inner class HomePagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(fm, behavior) {
        val tabs = mutableListOf<TabCategory>()
        val fragments = SparseArray<Fragment>(tabs.size)
        override fun getItem(position: Int): Fragment {
            val categoryId = tabs[position].categoryId
            val categoryIdKey = categoryId.toInt()
            var fragment = fragments.get(categoryIdKey, null)
            if (fragment == null) {
                fragment = HomeTabFragment.newInstance(tabs[position].categoryId)
                fragments.put(categoryIdKey, fragment)
            }
            return fragment
        }

        override fun getItemPosition(`object`: Any): Int {
            //需要判断刷新前后两次Fragment在ViewPager中的位置有没有改变，如果改变就返回POSITION_NONE，否则返回POSITION_UNCHANGED
            //是为了避免缓存数据和接口数据 犯规的顶部导航栏数据一样的情况，导致页面fragment会被先detach，在attach重复执行生命周期，
            //同时还能兼顾缓存数据返回的顶部导航栏和接口返回的数据不一致

            //拿到刷新之前该位置对应它的fragment对象
            val indexOfValue = fragments.indexOfValue(`object` as Fragment)
            val fragment = getItem(indexOfValue)

            return if (fragment == `object`) PagerAdapter.POSITION_UNCHANGED else PagerAdapter.POSITION_NONE
        }

        override fun getItemId(position: Int): Long {
            return tabs[position].categoryId.toLong()
        }

        override fun getCount(): Int {
            return tabs.size
        }

        fun update(list: List<TabCategory>) {
            tabs.clear()
            tabs.addAll(list)
            notifyDataSetChanged()
        }

    }
}