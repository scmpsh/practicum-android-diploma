package ru.practicum.android.diploma.root.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.practicum.android.diploma.R

class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation.setupWithNavController(navController)

        val hideBottomBarDestinations = setOf(
            R.id.filterSettingsFragment,
            R.id.vacancyDetailsFragment
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavigation.visibility =
                if (destination.id in hideBottomBarDestinations) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
        }
    }
}
