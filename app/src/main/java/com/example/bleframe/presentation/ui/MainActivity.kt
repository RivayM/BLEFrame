package com.example.bleframe.presentation.ui

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bleframe.R
import com.example.bleframe.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainVM>()
    private var binding: ActivityMainBinding? = null
    private var navController: NavController? = null
    private var menu: Menu? = null

    private val job = SupervisorJob()
    private val scope = CoroutineScope( job + lifecycleScope.coroutineContext + Dispatchers.Main)

    private var bleLauncher: ActivityResultLauncher<Intent>? = null
    private var locationLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initNav()
        registerBleLauncher()
    }

    override fun onStart() {
        observers()
        super.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.top_menu,this.menu)
        return super.onCreateOptionsMenu(this.menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            menu?.findItem(R.id.top_menu_state_ble)?.itemId ?: NULL_ICON_ID-> {
                showSnackBar("touch icon Ble")
                startBleLauncher()
                true
            }
            menu?.findItem(R.id.top_menu_state_location)?.itemId ?: NULL_ICON_ID-> {
                showSnackBar("touch icon Location")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initNav(){
        val navView: BottomNavigationView = binding!!.activityMainNavView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.activity_main_nav_host_fragment_navigation) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragment_device, R.id.fragment_logs, R.id.fragment_settings,
            )
        )
        setupActionBarWithNavController(navController!!, appBarConfiguration)
        navView.setupWithNavController(navController!!)
    }

    private fun registerBleLauncher(){
        bleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == Activity.RESULT_OK){
                showSnackBar("Ble on")
            }
        }
    }

    private fun startBleLauncher() = bleLauncher?.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))

    private fun registerLocationLauncher(){
        bleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == Activity.RESULT_OK){

            }
        }
    }

    private fun startLocationLauncher(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bleLauncher?.launch(Intent(LocationManager.ACTION_GNSS_CAPABILITIES_CHANGED))
        }
    }

    private fun showSnackBar(text:String) = Snackbar.make(binding!!.root,text,Snackbar.LENGTH_SHORT).show()

    private fun observers(){
        viewModel.stateBle.onEach {
            try {
                when(it){
                    true -> menu?.getItem(0)?.icon =
                        AppCompatResources.getDrawable(applicationContext,R.drawable.ic_bluetooth_on_24)
                    false -> menu?.getItem(0)?.icon =
                        AppCompatResources.getDrawable(applicationContext,R.drawable.ic_bluetooth_off_24)
                }
            }
            catch (e:Exception){
                Log.d("MyLog","error $e")
            }

        }.launchIn(scope)
    }

    companion object{
        const val NULL_ICON_ID = 9999
    }
}