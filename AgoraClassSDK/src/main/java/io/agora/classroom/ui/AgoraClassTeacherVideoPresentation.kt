package io.agora.classroom.ui

import android.content.Context
import android.os.Bundle
import android.view.Display
import io.agora.agoraclasssdk.databinding.ActivityAgoraClassTeacherVideoBinding
import io.agora.classroom.common.AgoraBaseClassPresentation

class AgoraClassTeacherVideoPresentation(context: Context, display: Display) : AgoraBaseClassPresentation(context, display) {
    lateinit var binding: ActivityAgoraClassTeacherVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgoraClassTeacherVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}