package com.mcode.app2.activities

import android.Manifest
import android.R
import android.R.attr.data
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.mcode.app2.BuildConfig
import com.mcode.app2.adapter.ClickOnQuickLinks
import com.mcode.app2.adapter.QuickLinksAdapter
import com.mcode.app2.adapter.QuickLinksRepository
import com.mcode.app2.adapter.TestFragmentAdapter
import com.mcode.app2.behaviour.uc.UcNewsHeaderPagerBehavior
import com.mcode.app2.behaviour.uc.UcNewsHeaderPagerBehavior.OnPagerStateListener
import com.mcode.app2.databinding.ActivityUcMainPagerBinding
import com.mcode.app2.fragment.TestFragment
import com.mcode.app2.interfaces.DrawableClickListener
import java.util.*


class MainActivity : AppCompatActivity(), OnTabSelectedListener, OnPagerStateListener{
    private var mFragments: MutableList<TestFragment>? = null
    private var mPagerBehavior: UcNewsHeaderPagerBehavior? = null
    private var mQuickAdapter:QuickLinksAdapter? = null
    val quickLink =  QuickLinksRepository().getAllQuickLinks()
    lateinit var binding: ActivityUcMainPagerBinding
    private var speechRecognizer : SpeechRecognizer? = null
    private val REQUEST_CODE_SPEECH_INPUT = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_uc_main_pager)
        binding = ActivityUcMainPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        if(ContextCompat.checkSelfPermission(this@MainActivity,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission()
        }
        val speechRecognizerIntent =  Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this@MainActivity)
        searchInitilizer()
        binding.button.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer?.stopListening()
                }
                if (event?.getAction() == MotionEvent.ACTION_DOWN){
                    binding.button.setImageResource(R.drawable.ic_dialog_alert);
                    speechRecognizer?.startListening(speechRecognizerIntent)
                }
                return false
            }

        })

        binding.button.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                Toast
                    .makeText(
                        this@MainActivity, " " + e.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }
    }

    private fun searchInitilizer() {
        val speechRecognizerIntent =  Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        binding.tietUser.setDrawableClickListener(object :DrawableClickListener{
            override fun onClick(
                target: DrawableClickListener.DrawablePosition?,
                motion: MotionEvent?
            ) {
                if (motion?.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer?.stopListening()
                }
                if (motion?.getAction() == MotionEvent.ACTION_DOWN){
//                    binding.tietUser.setImageResource(R.drawable.ic_mic_black_24dp);
                    speechRecognizer?.startListening(speechRecognizerIntent)
                }
            }
        })



        speechRecognizer?.setRecognitionListener(object :RecognitionListener{
            override fun onReadyForSpeech(params: Bundle?) {

            }

            override fun onBeginningOfSpeech() {
                binding.tietUser.setText("")
                binding.tietUser.setHint("Listening...")

            }

            override fun onRmsChanged(rmsdB: Float) {

            }

            override fun onBufferReceived(buffer: ByteArray?) {

            }

            override fun onEndOfSpeech() {

            }

            override fun onError(error: Int) {

            }

            override fun onResults(results: Bundle?) {
//                binding.tietUser.setImageResource(R.drawable.ic_mic_black_off)
                val data: ArrayList<String> =
                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)!!
                binding.tietUser.setText(data[0])
            }

            override fun onPartialResults(partialResults: Bundle?) {
                TODO("Not yet implemented")
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer?.destroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty()){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_CODE_SPEECH_INPUT) {
            if (requestCode === Activity.RESULT_OK && data != null) {
                val result: ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)!!
                binding.tietUser.setText(
                    Objects.requireNonNull(result)[0]
                )
            }
        }
    }


    protected fun initView() {
        mPagerBehavior =
            (binding.idUcNewsHeaderPager.layoutParams as CoordinatorLayout.LayoutParams).behavior as UcNewsHeaderPagerBehavior?
        mPagerBehavior!!.setPagerStateListener(this)
        val layoutmanager = GridLayoutManager(this,5)
        binding.rvQuickLinks.apply {
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
            binding.idUcNewsTab!!.addTab(binding.idUcNewsTab!!.newTab().setText("Tab$i"))
        }
        binding.idUcNewsTab!!.tabMode = TabLayout.MODE_FIXED
        binding.idUcNewsTab!!.setOnTabSelectedListener(this)
        binding.idUcNewsContent!!.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding.idUcNewsTab))
        binding.idUcNewsContent!!.adapter = TestFragmentAdapter(mFragments, supportFragmentManager)
    }

    private fun openMyGitHub() {
        val uri = Uri.parse("https://github.com/codewith-fun")
        val it = Intent(Intent.ACTION_VIEW, uri)
        startActivity(it)
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        binding.idUcNewsContent!!.currentItem = tab.position
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {}
    override fun onTabReselected(tab: TabLayout.Tab) {}
    override fun onPagerClosed() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onPagerClosed: ")
        }
        Snackbar.make(binding.idUcNewsContent!!, "pager closed", Snackbar.LENGTH_SHORT).show()
    }

    override fun onPagerOpened() {
        Snackbar.make(binding.idUcNewsContent!!, "pager opened", Snackbar.LENGTH_SHORT).show()
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

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1
            )
        }
    }

}