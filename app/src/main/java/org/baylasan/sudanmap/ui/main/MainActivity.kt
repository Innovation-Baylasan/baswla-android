package org.baylasan.sudanmap.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.ui.main.event.EventMapFragment
import org.baylasan.sudanmap.ui.main.place.PlaceMapFragment


class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener,
    BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        if (fragmentLayout.isDrawerOpen(navigationView))
            fragmentLayout.closeDrawer(navigationView, true)
        else
            super.onBackPressed()

    }

    private var gpsDialog: AlertDialog? = null
    private var permissionDialog: AlertDialog? = null
    private var didSetupViewPager = false

    fun isGpsEnabled(): Boolean {
        return LocationManagerCompat.isLocationEnabled(getSystemService(Context.LOCATION_SERVICE) as LocationManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (doseNotHaveLocationPermission()
        ) {
            requestPermission()
        } else {
            setupViewpager()
        }
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

        viewPager.adapter = FragmentAdapter(supportFragmentManager)
        viewPager.addOnPageChangeListener(this)
        viewPager.offscreenPageLimit = 2
        bottomNavigation.setOnNavigationItemSelectedListener(this)
    }

    private fun doseNotHaveLocationPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
                != PackageManager.PERMISSION_GRANTED)
    }

    override fun onResume() {
        super.onResume()
        val fragment = supportFragmentManager.findFragmentByTag("perm")
        if (fragment != null) {
            if (!doseNotHaveLocationPermission()) {
                setupViewpager()
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
}

class FragmentAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PlaceMapFragment()
            1 -> EventMapFragment()
            else -> throw  UnknownError()
        }
    }

    override fun getCount(): Int = 2

}