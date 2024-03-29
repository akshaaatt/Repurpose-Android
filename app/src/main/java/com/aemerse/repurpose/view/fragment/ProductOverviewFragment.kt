package com.aemerse.repurpose.view.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.aemerse.repurpose.R
import com.aemerse.repurpose.domain.mock.FakeWebServer
import com.aemerse.repurpose.model.CenterRepository
import com.aemerse.repurpose.util.AppConstants
import com.aemerse.repurpose.util.Utils
import com.aemerse.repurpose.view.activities.HomeActivity
import com.aemerse.repurpose.view.adapter.ProductsInCategoryPagerAdapter

class ProductOverviewFragment : Fragment() {
    private var mToolbar: Toolbar? = null
    private var viewPager: ViewPager? = null
    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(
            R.layout.frag_category_details,
            container, false
        )
        requireActivity().title = "Products"

        // Simulate Web service calls
        FakeWebServer.fakeWebServer!!.getAllProducts(
            AppConstants.CURRENT_CATEGORY
        )

        viewPager = view.findViewById<View>(R.id.htab_viewpager) as ViewPager?
        collapsingToolbarLayout = view.findViewById<View>(R.id.htab_collapse_toolbar) as CollapsingToolbarLayout?
        collapsingToolbarLayout!!.isTitleEnabled = false
        mToolbar = view.findViewById<View>(R.id.htab_toolbar) as Toolbar?
        if (mToolbar != null) {
            (activity as HomeActivity?)!!.setSupportActionBar(mToolbar)
        }
        if (mToolbar != null) {
            (activity as HomeActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            mToolbar!!.setNavigationIcon(R.drawable.ic_drawer)
        }
        mToolbar!!.setNavigationOnClickListener {
            (activity as HomeActivity?)!!.getmDrawerLayout()!!.openDrawer(GravityCompat.START)
        }
        setUpUi()
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { v, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK)
            ) {
                Utils.switchContent(
                    R.id.frag_container,
                    Utils.HOME_FRAGMENT,
                    ((context) as HomeActivity?)!!,
                    Utils.AnimationType.SLIDE_RIGHT
                )
            }
            true
        }
        return view
    }

    private fun setUpUi() {
        setupViewPager(viewPager)
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ProductsInCategoryPagerAdapter(requireActivity().supportFragmentManager)
        val keys: Set<String> = CenterRepository.centerRepository!!.getMapOfProductsInCategory().keys
        for (string: String in keys) {
            adapter.addFrag(ProductListFragment(string), string)
        }
        viewPager!!.adapter = adapter
    }
}