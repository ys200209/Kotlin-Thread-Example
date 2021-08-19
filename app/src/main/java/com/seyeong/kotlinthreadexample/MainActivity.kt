package com.seyeong.kotlinthreadexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.seyeong.kotlinthreadexample.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    var total = 0
    var started = false

    val handler = object : Handler(Looper.getMainLooper()) { // 핸들러 생성
        override fun handleMessage(msg : Message) {
            val minute = String.format("%02d", total/60)
            val second = String.format("%02d", total%60)
            binding.textTimer.text = "$minute:$second"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonStart.setOnClickListener {
            started = true // 시작 활성화
            Log.d("태그", "시작버튼 : " + started)
            thread(start=true) { // 시작 버튼이 활성화 되었다면
                while (started) { // 현재 시작 중인동안
                    Thread.sleep(1000)
                    if (started) { // 현재 시작 중이라면
                        total = total + 1
                        Log.d("태그", "total = " + total)
                        handler?.sendEmptyMessage(0) // 서브 스레드에서 동작하기 때문에 UI를 핸들러에게 요청해서 바꾸어야 한다.
                    }
                }
            }
        }

        binding.buttonStop.setOnClickListener {
            if (started) {
                started = false
                Log.d("태그", "시작버튼 : " + started)
                total = 0
                binding.textTimer.text = "00:00" // 이건 서브 스레드에서 동작하지 않기 때문에 UI를 변경할 수 있다.
            }
        }
    }

}