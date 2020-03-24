package org.baylasan.sudanmap.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_header_layout.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.GpsChecker
import org.baylasan.sudanmap.common.doseNotHaveLocationPermission
import org.baylasan.sudanmap.common.gone
import org.baylasan.sudanmap.common.visible
import org.baylasan.sudanmap.ui.AboutActivity
import org.baylasan.sudanmap.ui.auth.AuthActivity
import org.baylasan.sudanmap.ui.editprofile.EditUserProfileActivity
import org.baylasan.sudanmap.ui.faq.FAQActivity
import org.baylasan.sudanmap.ui.main.entity.EntityMapFragment
import org.baylasan.sudanmap.ui.main.event.EventMapFragment
import org.baylasan.sudanmap.ui.myentities.MyEntitiesActivity
import org.baylasan.sudanmap.ui.myevents.MyEventsActivity
import org.baylasan.sudanmap.ui.privacy.PrivacyPolicyActivity
import org.baylasan.sudanmap.ui.terms.TOSActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener,
    BottomNavigationView.OnNavigationItemSelectedListener, GpsChecker.OnGpsListener {

    private lateinit var gpsChecker: GpsChecker
    private var didSetupViewPager = false
    private val picasso by inject<Picasso>()
    private val profileViewModel by viewModel<UserProfileViewModel>()
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GpsChecker.GPS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                checkPermission()
            } else {
                gpsChecker.turnGPSOn(this)
            }
        }
    }

    override fun onBackPressed() {
        when {
            fragmentLayout.isDrawerOpen(navigationView) -> closeDrawer()
            viewPager.currentItem == 1 -> {
                selectFirstPage()


            }
            else -> super.onBackPressed()

        }
    }

    private fun selectFirstPage() {
        viewPager.setCurrentItem(0, false)
        bottomNavigation.menu.findItem(R.id.action_places).isChecked = true
    }

    private fun closeDrawer() {
        fragmentLayout.closeDrawer(navigationView, true)
    }

    private var permissionDialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        profileViewModel.loadUser()
        observeProfileEvents()
        editProfileButton.setOnClickListener {
            startActivityAndCloseDrawer<EditUserProfileActivity>()
        }
        gpsChecker = GpsChecker(this)
        gpsChecker.turnGPSOn(this)

        tosButton.setOnClickListener {
            startActivityAndCloseDrawer<TOSActivity>()
        }
        openEventsButton.setOnClickListener {
            startActivityAndCloseDrawer<MyEventsActivity>()
        }
        actionPrivacyButton.setOnClickListener {
            startActivityAndCloseDrawer<PrivacyPolicyActivity>()
        }
        openAboutButton.setOnClickListener {
            startActivityAndCloseDrawer<AboutActivity>()
        }
        openFaqButton.setOnClickListener {
            startActivityAndCloseDrawer<FAQActivity>()
        }
        logoutButton.setOnClickListener {
            profileViewModel.logout()
        }
        profileViewModel.logoutEvent.observe(this, Observer {
            startActivityAndCloseDrawer<AuthActivity>()
            finishAffinity()
        })
    }

    private fun observeProfileEvents() {
        profileViewModel.listenToUserProfile().observe(this, Observer { profile ->
            if (profile == null) {
                userNameTextView.text = getString(R.string.guest_user)
                userEmailTextView.text = getString(R.string.guest_user_tap_to_login)
                editProfileButton.gone()
                openPlacesButton.gone()
                openEventsButton.gone()
                logoutButton.text = getString(R.string.login)
                navigationDrawerHeader.setOnClickListener {
                    startActivityAndCloseDrawer<AuthActivity>()
                    finish()
                }
            } else {
                userNameTextView.text = profile.name
                userEmailTextView.text = profile.email
                if (profile.isCompany()) {
//                    editProfileButton.visible()
                    openPlacesButton.visible()
                } else {
                    editProfileButton.gone()
                    openPlacesButton.gone()

                }
                openEventsButton.visible()

                openPlacesButton.setOnClickListener {
                    startActivityAndCloseDrawer<MyEntitiesActivity>()
                }

                editProfileButton.setOnClickListener {
                    startActivityAndCloseDrawer<EditUserProfileActivity>()
                }
            }
        })
    }

    private inline fun <reified T> startActivityAndCloseDrawer() {
        startActivity(Intent(this, T::class.java))
        closeDrawer()

    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            showPermissionDialog()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION
            )

        }
    }

    private fun showPermissionDialog() {
        if (permissionDialog != null)
            permissionDialog?.dismiss()
        permissionDialog = AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog)
            .setTitle(R.string.permission_denied_settings_title)
            .setCancelable(false)
            .setMessage(R.string.permission_denied_settings_subtitle)
            .setPositiveButton(R.string.grant) { dialog, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION
                )
                dialog.dismiss()

            }.create()
        permissionDialog?.show()
    }

    fun openDrawer() {
        fragmentLayout.openDrawer(navigationView, true)

    }


    private fun setupViewpager() {
        didSetupViewPager = true
        viewPager.adapter = FragmentAdapter(supportFragmentManager)
        viewPager.addOnPageChangeListener(this)
        viewPager.offscreenPageLimit = 2
        bottomNavigation.setOnNavigationItemSelectedListener(this)
    }


    override fun onResume() {
        super.onResume()
        Log.d("MEGA", "onResume")
        val fragment = supportFragmentManager.findFragmentByTag("perm")
        if (fragment != null) {
            if (!doseNotHaveLocationPermission()) {
                if (!didSetupViewPager) {
                    Log.d("MEGA", "did setup view pager")

                    setupViewpager()
                }
                supportFragmentManager.beginTransaction().remove(fragment).commit()
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION) {
            if (permissions.isNotEmpty() &&
                permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                setupViewpager()
            } else {
                showSettingsFragment()
            }
        }
    }

    private fun showSettingsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentLayout, PermissionDeniedFragment(), "perm")
            .commit()
    }

    override fun onPageSelected(position: Int) {
        when (position) {
            0 -> {
                bottomNavigation.menu.findItem(R.id.action_places).isChecked = true
                bottomNavigation.menu.findItem(R.id.action_events).isChecked = false
            }
            1 -> {

                bottomNavigation.menu.findItem(R.id.action_places).isChecked = false
                bottomNavigation.menu.findItem(R.id.action_events).isChecked = true
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_events -> {
                viewPager.setCurrentItem(1, false)
            }
            else -> {
                viewPager.setCurrentItem(0, false)

            }
        }
        return true
    }

    companion object {
        const val LOCATION_PERMISSION = 42
        const val REQUEST_CHECK_SETTINGS = 40
    }

    override fun onGpsEnabled() {
        checkPermission()
    }

    private fun checkPermission() {
        if (doseNotHaveLocationPermission()) {
            requestPermission()
        } else {
            setupViewpager()
        }
    }
}

class FragmentAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> EntityMapFragment()
            1 -> EventMapFragment()
            else -> throw  UnknownError()
        }
    }

    override fun getCount(): Int = 2

}