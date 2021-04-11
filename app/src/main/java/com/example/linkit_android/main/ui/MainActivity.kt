package com.example.linkit_android.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.linkit_android.R
import com.example.linkit_android.chatting.ui.ChatFragment
import com.example.linkit_android.community.ui.CommunityFragment
import com.example.linkit_android.databinding.ActivityMainBinding
import com.example.linkit_android.main.adapter.ViewPagerAdapter
import com.example.linkit_android.mypage.ui.MypageFragment
import com.example.linkit_android.portfolio.ui.PortfolioFragment
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewBinding()

        initViewPager()

        initBottomNavigation()

        setPagingListenerOnViewPager()
    }

    private fun setViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun  initViewPager() {
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.fragments = listOf(
            CommunityFragment(),
            PortfolioFragment(),
            ChatFragment(),
            MypageFragment()
        )
        binding.viewpager.adapter = viewPagerAdapter
    }

    private fun initBottomNavigation() {
        binding.bottomnavi.setOnNavigationItemSelectedListener {
            var index by Delegates.notNull<Int>()
            index = when (it.itemId) {
                R.id.menu_community -> 0
                R.id.menu_portfolio -> 1
                R.id.menu_chat -> 2
                else -> 3
            }
            binding.viewpager.currentItem = index
            true
        }
    }

    private fun setPagingListenerOnViewPager() {
        binding.viewpager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                binding.bottomnavi.menu.getItem(position).isChecked = true
            }
        })
    }
}