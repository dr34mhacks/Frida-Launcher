package com.cybersandeep.fridalauncher

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.cybersandeep.fridalauncher.databinding.ActivityMainBinding
import com.cybersandeep.fridalauncher.databinding.RootRequiredBannerBinding
import com.cybersandeep.fridalauncher.utils.FridaUtils
import com.cybersandeep.fridalauncher.utils.Logger
import com.cybersandeep.fridalauncher.viewmodel.FridaViewModel

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val viewModel: FridaViewModel by viewModels()
    private var rootBannerAdded = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        initializeApp()
    }
    
    private fun initializeApp() {
        setupUI()
        setupObservers()
        viewModel.checkStatus(this)
        viewModel.loadAvailableReleases()
    }
    
    private fun showRootRequiredBanner() {
        if (rootBannerAdded) return
        
        // Inflate the banner
        val bannerBinding = RootRequiredBannerBinding.inflate(layoutInflater)
        
        // Add the banner at the top of the layout, below the app bar
        val container = findViewById<ViewGroup>(R.id.main)
        container.addView(bannerBinding.root, 1) // Add after AppBarLayout
        
        // Show a dialog explaining the issue
        AlertDialog.Builder(this)
            .setTitle("Root Access Required")
            .setMessage("This app requires root access to function properly. Without root access, you won't be able to install or run Frida server.\n\nPlease root your device or use a device with root access.")
            .setPositiveButton("OK", null)
            .show()
        
        rootBannerAdded = true
    }
    
    private fun setupUI() {
        binding.statusMessageTextView.movementMethod = ScrollingMovementMethod()
        binding.statusMessageTextView.setTextIsSelectable(true)
        
        val deviceArch = FridaUtils.getDeviceArchitecture()
        binding.deviceInfoTextView.text = "Device: ${Build.MODEL}"
        binding.architectureTextView.text = deviceArch
        
        viewModel.setSelectedArchitecture(deviceArch)
        
        binding.installButton.setOnClickListener {
            viewModel.downloadAndInstallFridaServer(this)
        }
        
        binding.startButton.setOnClickListener {
            viewModel.startFridaServer()
        }
        
        binding.stopButton.setOnClickListener {
            viewModel.stopFridaServer()
        }
        
        binding.uninstallButton.setOnClickListener {
            viewModel.uninstallFridaServer()
        }
        
        binding.refreshButton.setOnClickListener {
            viewModel.checkStatus(this)
            viewModel.loadAvailableReleases()
        }
        
        binding.copyLogsButton.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Frida Launcher Logs", binding.statusMessageTextView.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Logs copied to clipboard", Toast.LENGTH_SHORT).show()
        }
        
        binding.clearLogsButton.setOnClickListener {
            val timestamp = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
            val clearMessage = "[$timestamp] Logs cleared"
            binding.statusMessageTextView.text = clearMessage
            Toast.makeText(this, "Logs cleared", Toast.LENGTH_SHORT).show()
        }
        
        val timestamp = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
        binding.statusMessageTextView.text = "[$timestamp] Frida Launcher initialized"
    }
    
    private fun setupObservers() {
        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
            
            binding.installButton.isEnabled = !isLoading
            binding.startButton.isEnabled = !isLoading
            binding.stopButton.isEnabled = !isLoading
            binding.uninstallButton.isEnabled = !isLoading
            binding.refreshButton.isEnabled = !isLoading
            binding.versionSpinner.isEnabled = !isLoading
            binding.copyLogsButton.isEnabled = !isLoading
            binding.clearLogsButton.isEnabled = !isLoading
        })
        
        viewModel.statusMessage.observe(this, Observer { message ->
            val timestamp = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
            
            binding.statusMessageTextView.append("\n[$timestamp] $message")
            
            binding.statusMessageTextView.post {
                try {
                    val parent = binding.statusMessageTextView.parent
                    if (parent is ScrollView) {
                        parent.fullScroll(ScrollView.FOCUS_DOWN)
                    }
                } catch (e: Exception) {
                    Logger.e("Error scrolling log view", e)
                }
            }
        })
        
        // Observe root access status
        viewModel.rootAccessStatus.observe(this, Observer { status ->
            when (status) {
                FridaViewModel.RootStatus.NOT_AVAILABLE -> {
                    // Show the root required banner
                    showRootRequiredBanner()
                    
                    // Disable buttons that require root
                    binding.installButton.isEnabled = false
                    binding.startButton.isEnabled = false
                    binding.stopButton.isEnabled = false
                    binding.uninstallButton.isEnabled = false
                }
                FridaViewModel.RootStatus.AVAILABLE -> {
                    // Root is available, no need to show banner
                }
                FridaViewModel.RootStatus.NON_ROOT_MODE -> {
                    // Non-root mode is available, no need to show banner
                }
                else -> {
                    // Unknown status, do nothing
                }
            }
        })
        
        viewModel.isServerInstalled.observe(this, Observer { isInstalled ->
            val statusText = "Installed: "
            val statusValue = if (isInstalled) "Yes" else "No"
            
            val spannable = android.text.SpannableString(statusText + statusValue)
            spannable.setSpan(
                android.text.style.ForegroundColorSpan(
                    ContextCompat.getColor(this, if (isInstalled) R.color.green_primary else R.color.red_primary)
                ),
                statusText.length, spannable.length, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                statusText.length, spannable.length, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            
            binding.installedStatusTextView.text = spannable
            
            binding.startButton.isEnabled = isInstalled && viewModel.isServerRunning.value != true
            binding.stopButton.isEnabled = isInstalled && viewModel.isServerRunning.value == true
            binding.uninstallButton.isEnabled = isInstalled
        })
        
        viewModel.isServerRunning.observe(this, Observer { isRunning ->
            val statusText = "Running: "
            val statusValue = if (isRunning) "Yes" else "No"
            
            val spannable = android.text.SpannableString(statusText + statusValue)
            spannable.setSpan(
                android.text.style.ForegroundColorSpan(
                    ContextCompat.getColor(this, if (isRunning) R.color.green_primary else R.color.red_primary)
                ),
                statusText.length, spannable.length, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                statusText.length, spannable.length, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            
            binding.runningStatusTextView.text = spannable
            
            binding.startButton.isEnabled = !isRunning && viewModel.isServerInstalled.value == true
            binding.stopButton.isEnabled = isRunning
        })
        
        viewModel.installedVersion.observe(this, Observer { version ->
            val statusText = "Version: "
            val versionText = version
            
            val spannable = android.text.SpannableString(statusText + versionText)
            
            if (versionText.isNotEmpty() && versionText != "Not installed") {
                spannable.setSpan(
                    android.text.style.ForegroundColorSpan(
                        ContextCompat.getColor(this, R.color.blue_primary)
                    ),
                    statusText.length, spannable.length, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    statusText.length, spannable.length, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            
            binding.versionTextView.text = spannable
        })
        
        viewModel.availableReleases.observe(this, Observer { releases ->
            if (releases.isNotEmpty()) {
                val versionItems = releases.map { 
                    "Version: ${it.version}\nDate: ${it.releaseDate}" 
                }.toTypedArray()
                
                val versionAdapter = ArrayAdapter(this, R.layout.spinner_item, versionItems)
                versionAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                binding.versionSpinner.adapter = versionAdapter
                
                binding.versionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val selectedVersion = releases[position].version
                        viewModel.setSelectedVersion(selectedVersion)
                    }
                    
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            }
        })
    }
}