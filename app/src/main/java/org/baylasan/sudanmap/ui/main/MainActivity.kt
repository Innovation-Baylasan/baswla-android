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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.ui.terms.TOSActivity
import org.baylasan.sudanmap.ui.event.EventsActivity
import org.baylasan.sudanmap.ui.faq.FAQActivity
import org.baylasan.sudanmap.ui.main.event.EventMapFragment
import org.baylasan.sudanmap.ui.main.place.PlaceMapFragment
import org.baylasan.sudanmap.ui.place.PlacesActivity
import org.baylasan.sudanmap.ui.privacy.PrivacyPolicyActivity
import org.baylasan.sudanmap.utils.GpsChecker


class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener,
    BottomNavigationView.OnNavigationItemSelectedListener, GpsChecker.OnGpsListener {

    private lateinit var gpsChecker: GpsChecker
    private var didSetupViewPager = false

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

        gpsChecker = GpsChecker(this)
        gpsChecker.turnGPSOn(this)

        openPlacesButton.setOnClickListener {
            startActivityAndCloseDrawer<PlacesActivity>()
        }
        actionPrivacyButton.setOnClickListener {
            startActivityAndCloseDrawer<PrivacyPolicyActivity>()
        }
        tosButton.setOnClickListener {
            startActivityAndCloseDrawer<TOSActivity>()
        }
        openEventsButton.setOnClickListener {
            startActivityAndCloseDrawer<EventsActivity>()
        }
        openFaqButton.setOnClickListener {
            startActivityAndCloseDrawer<FAQActivity>()
        }
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

    private fun doseNotHaveLocationPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
                != PackageManager.PERMISSION_GRANTED)
    }

    override fun onResume() {
        super.onResume()
        Log.d("MEGA","onResume")
        val fragment = supportFragmentManager.findFragmentByTag("perm")
        if (fragment != null) {
            if (!doseNotHaveLocationPermission()) {
                if (!didSetupViewPager) {
                    Log.d("MEGA","did setup view pager")

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
            0 -> PlaceMapFragment()
            1 -> EventMapFragment()
            else -> throw  UnknownError()
        }
    }

    override fun getCount(): Int = 2

}