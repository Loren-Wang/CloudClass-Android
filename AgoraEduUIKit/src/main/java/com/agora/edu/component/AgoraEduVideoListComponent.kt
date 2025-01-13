package com.agora.edu.component

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.agora.edu.component.common.AbsAgoraEduComponent
import com.agora.edu.component.common.IAgoraUIProvider
import com.agora.edu.component.helper.AgoraRenderUtils
import com.agora.edu.component.helper.AgoraRenderUtils.TAG
import com.agora.edu.component.helper.GridSpacingItemDecoration
import com.agora.edu.component.teachaids.presenter.FCRLargeWindowManager
import io.agora.agoraeducore.core.context.AgoraEduContextAudioSourceType
import io.agora.agoraeducore.core.context.AgoraEduContextMediaSourceState
import io.agora.agoraeducore.core.context.AgoraEduContextMediaStreamType
import io.agora.agoraeducore.core.context.AgoraEduContextUserInfo
import io.agora.agoraeducore.core.context.AgoraEduContextUserRole
import io.agora.agoraeducore.core.context.AgoraEduContextVideoSourceType
import io.agora.agoraeducore.core.context.AgoraEduContextVideoSubscribeLevel
import io.agora.agoraeducore.core.internal.framework.proxy.RoomType
import io.agora.agoraeducore.core.internal.framework.utils.GsonUtil
import io.agora.agoraeducore.core.internal.launch.AgoraEduClassRoom
import io.agora.agoraeducore.core.internal.log.LogX
import io.agora.agoraeduuikit.R
import io.agora.agoraeduuikit.interfaces.listeners.IAgoraUIVideoListener
import io.agora.agoraeduuikit.provider.AgoraUIUserDetailInfo
import io.agora.agoraeduuikit.provider.UIDataProviderListenerImpl
import io.agora.agoraeduuikit.util.VideoUtils
import java.lang.reflect.Parameter


/**
 * author : felix
 * date : 2022/1/26
 * description : 小班课学生列表
 */
class AgoraEduListVideoComponent : AbsAgoraEduComponent {
    constructor(context: Context) : super(context)
    constructor(context: Context, attr: AttributeSet) : super(context, attr)
    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, defStyleAttr)

    private val TAG = "AgoraEduUserListVideoComponent"

    private lateinit var mVideoAdapter: CoHostVideoAdapter
    private val layout = LayoutInflater.from(context).inflate(R.layout.agora_edu_userlist_video_component, this, true)
    private val volumeUpdateRun = VolumeUpdateRun()
    private val waveStateUpdateRun = WaveStateUpdateRun()
    private val recyclerView = layout.findViewById<MyRecyclerView>(R.id.fcr_stu_list_recycler_view)
    private var localUserInfo: AgoraEduContextUserInfo? = null
    private var lastTeacherLayoutParams: ViewGroup.LayoutParams? = null
    private var teacherVideoView: AgoraEduVideoComponent? = null
    var classRoomType: RoomType = RoomType.SMALL_CLASS // 教室类型
    val mCurView: AbsAgoraEduComponent = this //当前的videoList 布局
    var hand = Handler(Looper.getMainLooper())

    private val itemMargin = context.resources.getDimensionPixelOffset(R.dimen.agora_video_distance)

    var roomUuid: String? = null

    val uiDataProviderListener = object : UIDataProviderListenerImpl() {
        override fun onUserListChanged(userList: List<AgoraUIUserDetailInfo>) {
            super.onUserListChanged(userList)
            uiHandler.post { updateCoHostList(userList.toMutableList()) }
        }

        override fun onVolumeChanged(volume: Int, streamUuid: String) {
            super.onVolumeChanged(volume, streamUuid)
            updateAudioVolumeIndication(volume, streamUuid)
        }

        override fun onUserHandsWave(userUuid: String, duration: Int, payload: Map<String, Any>?) {
            updateUserWaveState(userUuid, true)
        }

        override fun onUserHandsDown(userUuid: String, payload: Map<String, Any>?) {
            updateUserWaveState(userUuid, false)
        }
    }

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            resetShowAdapterList()
//            mVideoAdapter.notifyItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            resetShowAdapterList()
//            mVideoAdapter.notifyItemRangeRemoved(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            resetShowAdapterList()
//            mVideoAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            if(position >= currentPage * currentPageSize && position < (currentPage + 1) * currentPageSize){
                (recyclerView.findViewHolderForAdapterPosition(position - currentPage * currentPageSize) as? VideoHolder)?.let { holder ->
                    payload?.let { payload ->
                        if (payload is Pair<*, *>) {
                            (payload.second as? VideoItem)?.let { item ->
                                holder.bind(roomUuid, item, agoraUIProvider, mCurView)
                            }
                        }
                    }
                }
            }
        }
    }

    private val videoItemMatcher = VideoListItemMatcher()

    private val differ = AsyncListDiffer(listUpdateCallback, AsyncDifferConfig.Builder(videoItemMatcher).build())


    init {
        val leftArrow = layout.findViewById<ImageView>(R.id.iv_arrow_left)
        val rightArrow = layout.findViewById<ImageView>(R.id.iv_arrow_right)
        bindArrowWithRv(leftArrow, rightArrow, recyclerView)
        adjustRvItemAnimator(recyclerView)
        initRvAdapter(recyclerView, AgoraEduContextVideoSubscribeLevel.LOW)
    }

    override fun initView(agoraUIProvider: IAgoraUIProvider) {
        super.initView(agoraUIProvider)
        uiDataProvider?.addListener(uiDataProviderListener)
        roomUuid = eduContext?.roomContext()?.getRoomInfo()?.roomUuid
        localUserInfo = eduContext?.userContext()?.getLocalUserInfo()
    }

    private fun initRvAdapter(rv: RecyclerView, level: AgoraEduContextVideoSubscribeLevel) {//init adapter of the recyclerview
        mVideoAdapter = CoHostVideoAdapter(object : IAgoraUIVideoListener {
            override fun onRendererContainer(viewGroup: ViewGroup?, info: AgoraUIUserDetailInfo) {
                // 台上订阅指定的流类型
                eduContext?.streamContext()?.setRemoteVideoStreamSubscribeLevel(info.streamUuid, level)
                render(viewGroup, info)
            }
        })
        rv.adapter = mVideoAdapter
    }

    private fun render(viewGroup: ViewGroup?, info: AgoraUIUserDetailInfo) {
        AgoraRenderUtils.renderView(eduCore, viewGroup, info)
    }

    fun getItemViewPosition(streamUuid: String): Rect? {
        differ.currentList.forEachIndexed { index, item ->
            if (item != null && item.info.streamUuid == streamUuid) {
                try {
                    recyclerView.layoutManager?.findViewByPosition(index)?.let {
                        val rvLeft = this.left //this.left 当前AgoraEduListVideoComponent对象的left
                        return Rect(it.left + rvLeft, it.top, it.right + rvLeft, it.bottom)
                    }
                } catch (ignore: Exception) {
                }
            }
        }
        return null
    }

    fun show(show: Boolean) {
        layout.post {
            layout.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

//    fun isShown(): Boolean {
//        return layout.visibility == View.VISIBLE
//    }

    private fun adjustRvItemAnimator(rv: RecyclerView) {
//        rv.itemAnimator = FadeInDownAnimator()
//        rv.itemAnimator?.addDuration = 200
//        rv.itemAnimator?.removeDuration = 200
//        rv.itemAnimator?.moveDuration = 200
//        rv.itemAnimator?.changeDuration = 200
    }

    private fun bindArrowWithRv(leftArrow: ImageView, rightArrow: ImageView, rv: RecyclerView) {
        val updateRun = Runnable {
            leftArrow.isVisible = isShowArrow(rv)
            rightArrow.isVisible = isShowArrow(rv)

//            val videoW = calculateVideoW()
//            leftArrow.isVisible = getRvLeftDistance(rv) > videoW
//            rightArrow.isVisible = getRvRightDistance(rv) > videoW
        }

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    hand.removeCallbacksAndMessages(null)
                    hand.postDelayed(updateRun, 200)
                }
            }
        })

        val lm = object : LinearLayoutManager(layout.context, HORIZONTAL, false) {
            override fun onLayoutCompleted(state: RecyclerView.State?) {
                super.onLayoutCompleted(state)
                hand.removeCallbacksAndMessages(null)
                hand.post(updateRun)
            }

            override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
                try {
                    super.onLayoutChildren(recycler, state)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        rv.layoutManager = lm

        rv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect, view: View,
                parent: RecyclerView, state: RecyclerView.State
            ) {
                outRect.right = itemMargin
            }
        })

        leftArrow.setOnClickListener {
            if(showScreenDisplay){
                currentPage = 0.coerceAtLeast(currentPage - 1)
                resetShowAdapterList()
            }else{
                val layoutManager = rv.layoutManager as LinearLayoutManager
                val firstItem: Int = layoutManager.findFirstVisibleItemPosition()
                if (firstItem > 0) {
                    rv.smoothScrollToPosition(firstItem - 1)
                } else {
                    val left: Int = rv.getChildAt(0).left
                    rv.smoothScrollBy(left, 0)
                }
            }
        }

        rightArrow.setOnClickListener {
            if(showScreenDisplay){
                currentPage += 1
                resetShowAdapterList()
            }else{
                val layoutManager = rv.layoutManager as LinearLayoutManager
                val lastItem: Int = layoutManager.findLastVisibleItemPosition()
                if (lastItem <= rv.childCount - 1) {
                    rv.smoothScrollToPosition(lastItem + 1)
                } else {
                    val right: Int = rv.getChildAt(rv.childCount - 1).right
                    rv.smoothScrollBy(right, 0)
                }
            }
        }
    }

    fun isShowArrow(rv: RecyclerView): Boolean {
        val count = rv.childCount
        if (count <= 3) return false

        val layoutManager = rv.layoutManager as LinearLayoutManager
        val lastItemPosition = layoutManager.findLastVisibleItemPosition()
        val firstItemPosition = layoutManager.findFirstVisibleItemPosition()

        //LogX.i(TAG, "count=$count firstItemPosition=$firstItemPosition || lastItemPosition=$lastItemPosition")

        if (lastItemPosition < (count - 1) || firstItemPosition != 0) {
            return true
        }

        if (lastItemPosition == (count - 1) || firstItemPosition == 0) {
            // 是否全部可见
            val firstView = rv.getChildAt(0)
            val endView = rv.getChildAt(lastItemPosition)
            //LogX.i(TAG, "第一个是否全部显示：${isCompletelyVisible(firstView)} ，第二个是否全部显示：${isCompletelyVisible(endView)}")
            return !isCompletelyVisible(firstView) || !isCompletelyVisible(endView)
        }
        return false
    }

    /**
     * 调用此方法最好延时 16ms （即一帧）
     */
    fun isCompletelyVisible(view: View): Boolean {
        val rect = Rect()
        val isVisible = view.getGlobalVisibleRect(rect)
        //LogX.i(TAG, "cal ${rect.right - rect.left} width=${view.width} ")
        return isVisible && ((rect.right - rect.left) >= view.width)
    }


    private fun getRvRightDistance(rv: RecyclerView): Int {
        if (rv.childCount == 0) return 0
        val layoutManager = rv.layoutManager as LinearLayoutManager
        val lastVisibleItem: View = rv.getChildAt(rv.childCount - 1)
        val lastItemPosition = layoutManager.findLastVisibleItemPosition()
        if (lastItemPosition < 0) {
            return 0
        }
        val itemCount = layoutManager.itemCount
        val recycleViewWidth: Int = rv.width
        val itemWidth = lastVisibleItem.width
        val lastItemRight = layoutManager.getDecoratedRight(lastVisibleItem)
        val distance = (itemCount - lastItemPosition - 1) * itemWidth - recycleViewWidth + lastItemRight
        return distance
    }

    private fun getRvLeftDistance(rv: RecyclerView): Int {
        if (rv.childCount == 0) return 0
        val layoutManager = rv.layoutManager as LinearLayoutManager
        val firstVisibleItem: View = rv.getChildAt(0)
        val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (firstItemPosition < 0) {
            return 0
        }
        val itemWidth = firstVisibleItem.width
        val firstItemLeft = layoutManager.getDecoratedLeft(firstVisibleItem)
        val distance = firstItemPosition * itemWidth - firstItemLeft
        return distance
    }

    private fun updateCoHostList(list: MutableList<AgoraUIUserDetailInfo>) {
        if (list.size > 0) {
            val showList = getAllShowList(list.map { VideoItem(it, 0) })
            listUpdateCallback.onChanged(0, showList.size, null)
            differ.submitList(showList.toList())
        } else {
            differ.submitList(null)
        }
    }

    private fun updateAudioVolumeIndication(value: Int, streamUuid: String) {
        layout.removeCallbacks(volumeUpdateRun)
        volumeUpdateRun.value = value
        volumeUpdateRun.streamUuid = streamUuid
        recyclerView.post(volumeUpdateRun)
    }

    private fun updateUserWaveState(userUuid: String, waving: Boolean) {
        layout.removeCallbacks(waveStateUpdateRun)
        waveStateUpdateRun.waving = waving
        waveStateUpdateRun.userUuid = userUuid
        recyclerView.post(waveStateUpdateRun)
    }

    //显示副屏
    fun addTeacherScreenDisplayShow(teacherVideoView: AgoraEduVideoComponent) {
        showScreenDisplay = true
        this.teacherVideoView = teacherVideoView
        resetShowAdapterList()
//        lastTeacherLayoutParams = teacherVideoView.layoutParams
//        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                LayoutParams(width / showColumnCount!! - itemMargin, height / showRowCount!! - itemMargin).let {
////                    addView(teacherVideoView, it)
//                }
//                viewTreeObserver.removeOnGlobalLayoutListener(this)
//            }
//        })
    }

    //隐藏副屏
    fun hideScreenDisplayShow(teacherVideoView: AgoraEduVideoComponent) {
        showScreenDisplay = false
        this.teacherVideoView = null
        resetShowAdapterList()
//        removeView(teacherVideoView)
//        teacherVideoView.layoutParams = lastTeacherLayoutParams
//        lastTeacherLayoutParams = null
    }

    inner class VolumeUpdateRun : Runnable {
        var value: Int = 0
        var streamUuid: String = ""

        override fun run() {
            if (TextUtils.isEmpty(streamUuid)) {
                return
            }
            run outside@{
                differ.currentList.forEachIndexed { index, item ->
                    if (item.info.streamUuid == streamUuid) {
                        item.audioVolume = value
                        val curHolder = recyclerView.findViewHolderForAdapterPosition(index)
                        if (curHolder != null && curHolder is VideoHolder) {
                            curHolder.updateAudioVolumeIndication(value, streamUuid)
                        }
                        return@outside
                    }
                }
            }
        }
    }

    inner class WaveStateUpdateRun : Runnable {
        var waving: Boolean = false
        var userUuid: String = ""

        override fun run() {
            if (TextUtils.isEmpty(userUuid)) {
                return
            }
            run outside@{
                differ.currentList.forEachIndexed { index, item ->
                    if (item.info.userUuid == userUuid) {
                        val curHolder = recyclerView.findViewHolderForAdapterPosition(index)
                        if (curHolder != null && curHolder is VideoHolder) {
                            curHolder.updateUserWaveState(userUuid, waving)
                        }
                        return@outside
                    }
                }
            }
        }
    }

    private inner class CoHostVideoAdapter(val callback: IAgoraUIVideoListener?) : RecyclerView.Adapter<VideoHolder>() {

        var showList = arrayListOf<VideoItem>()

        var layoutInflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
            if (classRoomType == RoomType.LARGE_CLASS) {
                return VideoHolder(
                    layoutInflater.inflate(R.layout.agora_edu_userlist_video_item_v2, parent, false),
                    showColumnCount, showRowCount, width, height,
                    callback
                )
            }

            return VideoHolder(
                layoutInflater.inflate(R.layout.agora_edu_userlist_video_item, parent, false),
                showColumnCount, showRowCount, width, height,
                callback
            )
        }

        override fun onBindViewHolder(holder: VideoHolder, position: Int) {
            val index = holder.bindingAdapterPosition
            holder.bind(roomUuid, showList[index], agoraUIProvider, mCurView)
        }

        override fun getItemCount(): Int {
            return if (showRowCount != null && showColumnCount != null) showRowCount!! * showColumnCount!! else differ.currentList.size
        }
    }

    //当前页
    private var currentPage = 0

    //当前页数据大小
    private var currentPageSize = 4

    //当前显示的数据列表
    private var showAdapterList = arrayListOf<VideoItem>()

    //是否显示了双屏
    private var showScreenDisplay = false

    //是否有下一页
    private var haveNext = false

    //显示列数量
    private var showColumnCount: Int? = null

    //显示行数量
    private var showRowCount: Int? = null

    //流类型
    private var streamLevel: AgoraEduContextVideoSubscribeLevel = AgoraEduContextVideoSubscribeLevel.LOW

    //获取显示的数据列表
    private fun getAllShowList(list: List<VideoItem>): ArrayList<VideoItem> {
        val allList = arrayListOf<VideoItem>()
        list.find { item -> AgoraEduContextUserRole.Teacher == item.info.role && item.info.isLocal }.let {
            if (it != null) {
                allList.add(it)
            } else {
                if (this.teacherVideoView != null && this.teacherVideoView?.curUserDetailInfo != null) {
                    val videoItem = getEmptyItem()
                    videoItem.info = this.teacherVideoView!!.curUserDetailInfo!!
                    allList.add(0, videoItem)
                } else {

                }
            }
        } //先添加老师
        list.find { item -> item.info.isLocal }?.let { allList.add(it) } //添加自己
        list.filter { item -> !item.info.isLocal && AgoraEduContextUserRole.Teacher != item.info.role }.let { allList.addAll(it) } //添加其他人
        val showList = arrayListOf<VideoItem>()
        if (showScreenDisplay) {
            if (currentPage == 0) {
                //添加其他的数据
                allList.forEachIndexed { index, videoItem ->
                    if (index < currentPageSize - 1) {
                        showList.add(videoItem)
                    }
                }
                haveNext = (allList.size - 1) > currentPageSize
            } else {
                val start = currentPage * currentPageSize
                allList.forEachIndexed { index, videoItem ->
                    if (start >= index && index < currentPageSize + start) {
                        showList.add(videoItem)
                    }
                }
                haveNext = (allList.size - 1) > start + currentPageSize
            }
            //进行数据补位
            for (index in 0 until currentPageSize - showAdapterList.size) {
                showList.add(getEmptyItem())
            }
        } else {
            showList.addAll(allList)
        }
        return showList
    }

    //重置显示的适配器列表
    private fun resetShowAdapterList() {
        for (i in 0 until recyclerView.itemDecorationCount) {
            recyclerView.removeItemDecorationAt(i)
        }
        //列表布局显示
        if (showScreenDisplay) {
            val allSize = differ.currentList.size
            if (allSize < 4) {
                showColumnCount = 2
                showRowCount = 2
                streamLevel = AgoraEduContextVideoSubscribeLevel.HIGH
            } else if (allSize < 6) {
                showColumnCount = 3
                showRowCount = 2
                streamLevel = AgoraEduContextVideoSubscribeLevel.LOW
            } else if (allSize < 9) {
                showColumnCount = 3
                showRowCount = 3
                streamLevel = AgoraEduContextVideoSubscribeLevel.LOW
            }
            val gridLayoutManager = GridLayoutManager(context, showColumnCount!!) // 3列布局
            recyclerView.layoutManager = gridLayoutManager
            initRvAdapter(recyclerView, streamLevel)
            recyclerView.addItemDecoration(GridSpacingItemDecoration(showColumnCount!!,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10F, Resources.getSystem().displayMetrics).toInt(),false))
            recyclerView?.setPadding(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80F, Resources.getSystem().displayMetrics).toInt(),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50F, Resources.getSystem().displayMetrics).toInt(),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80F, Resources.getSystem().displayMetrics).toInt(),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50F, Resources.getSystem().displayMetrics).toInt()
            )
//            (recyclerView.layoutParams as MarginLayoutParams?)?.setMargins(
//                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80F, Resources.getSystem().displayMetrics).toInt(),
//                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50F, Resources.getSystem().displayMetrics).toInt(),
//                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80F, Resources.getSystem().displayMetrics).toInt(),
//                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50F, Resources.getSystem().displayMetrics).toInt()
//            )
        } else {
            if (recyclerView.layoutManager !is LinearLayoutManager) {
                streamLevel = AgoraEduContextVideoSubscribeLevel.LOW
                showColumnCount = null
                showRowCount = null
                recyclerView.layoutManager = LinearLayoutManager(context)
                initRvAdapter(recyclerView, streamLevel)
            }
        }
        //数据逻辑处理
        val allList = getAllShowList(differ.currentList)
        showAdapterList.clear()
        if (showScreenDisplay) {
            if (currentPage == 0) {
                //添加其他的数据
                allList.forEachIndexed { index, videoItem ->
                    if (index < currentPageSize - 1) {
                        showAdapterList.add(videoItem)
                    }
                }
                haveNext = (allList.size - 1) > currentPageSize
            } else {
                val start = currentPage * currentPageSize - 1//去除老师占位
                allList.forEachIndexed { index, videoItem ->
                    if (start >= index && index < currentPageSize + start) {
                        showAdapterList.add(videoItem)
                    }
                }
                haveNext = (allList.size - 1) > start + currentPageSize
            }
            //进行数据补位
            for (index in 0 until currentPageSize - showAdapterList.size) {
                showAdapterList.add(getEmptyItem())
            }
            this.findViewById<View>(R.id.iv_arrow_left)?.visibility = if (currentPage == 0) View.GONE else View.VISIBLE
            this.findViewById<View>(R.id.iv_arrow_right)?.visibility = if (haveNext) View.VISIBLE else View.GONE
        } else {
            showAdapterList.addAll(allList)
        }
        if (showAdapterList.size < mVideoAdapter.showList.size) {
            val removeList = arrayListOf<VideoItem>()
            for (index in showAdapterList.size until mVideoAdapter.showList.size) {
                removeList.add(mVideoAdapter.showList.get(index))
                mVideoAdapter.notifyItemRemoved(index)
            }
            mVideoAdapter.showList.removeAll(removeList.toSet())
        } else {
            showAdapterList.forEachIndexed { index, videoItem ->
                if (index >= mVideoAdapter.showList.size) {
                    mVideoAdapter.showList.add(videoItem)
                    mVideoAdapter.notifyItemInserted(index)
                } else {
                    mVideoAdapter.showList[index] = videoItem
                    mVideoAdapter.notifyItemChanged(index)
                }
            }
        }
    }

    //空数据实体
    private fun getEmptyItem(): VideoItem {
        return VideoItem(
            AgoraUIUserDetailInfo(
                Math.random().toString(), "", AgoraEduContextUserRole.Assistant, false, 0, whiteBoardGranted = false, isLocal = true,
                hasAudio = false, hasVideo = false, streamUuid = "", streamName = "", streamType = AgoraEduContextMediaStreamType.None, audioSourceType = AgoraEduContextAudioSourceType.None,
                videoSourceType = AgoraEduContextVideoSourceType.None, audioSourceState = AgoraEduContextMediaSourceState.Close, videoSourceState = AgoraEduContextMediaSourceState.Close
            ), 0
        )
    }
}

internal data class VideoItem(
    var info: AgoraUIUserDetailInfo,
    var audioVolume: Int
)

internal class VideoListItemMatcher : DiffUtil.ItemCallback<VideoItem>() {
    override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
        return (oldItem.info.userUuid == newItem.info.userUuid)
    }

    override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
        return (oldItem.info.userName == newItem.info.userName
                && oldItem.info.role == newItem.info.role
                && oldItem.info.isCoHost == newItem.info.isCoHost
                && oldItem.info.reward == newItem.info.reward
                && oldItem.info.whiteBoardGranted == newItem.info.whiteBoardGranted
                && oldItem.info.isLocal == newItem.info.isLocal
                && oldItem.info.hasAudio == newItem.info.hasAudio
                && oldItem.info.hasVideo == newItem.info.hasVideo
                && oldItem.info.streamUuid == newItem.info.streamUuid
                && oldItem.info.streamName == newItem.info.streamName
                && oldItem.info.streamType == newItem.info.streamType
                && oldItem.info.audioSourceType == newItem.info.audioSourceType
                && oldItem.info.videoSourceType == newItem.info.videoSourceType
                && oldItem.info.audioSourceState == newItem.info.audioSourceState
                && oldItem.info.videoSourceState == newItem.info.videoSourceState
                && oldItem.audioVolume == newItem.audioVolume)
    }

    override fun getChangePayload(oldItem: VideoItem, newItem: VideoItem): Any {
        return Pair(oldItem, newItem)
    }
}


internal class VideoHolder(var view: View, val showColumnCount: Int?, val showRowCount: Int?, val width: Int, val height: Int, val callback: IAgoraUIVideoListener?) : RecyclerView.ViewHolder(view),
    IAgoraUIVideoListener {

    var uiVideo = view.findViewById<AgoraEduVideoComponent>(R.id.agora_edu_video).also {
        if (showColumnCount != null && showRowCount != null) {
            it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (height -
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100F + 10 * (showRowCount - 1), Resources.getSystem().displayMetrics).toInt()) / showRowCount
            )
        }
    }

    fun bind(roomUuid: String?, item: VideoItem?, agoraUIProvider: IAgoraUIProvider, mCurView: AbsAgoraEduComponent) {
        if (item != null) {
            roomUuid?.let {
                uiVideo.largeWindowOpened = FCRLargeWindowManager.isLargeWindow(it, item.info.streamUuid)
            }
            uiVideo.videoListener = this
            uiVideo.initView(agoraUIProvider)

            //记录当前item 相对于整个讲台的left
            uiVideo.setVideoItemLeft(mCurView.left.toFloat() + absoluteAdapterPosition * uiVideo.context.resources.getDimensionPixelOffset(R.dimen.agora_small_video_w))
            uiVideo.upsertUserDetailInfo(item.info)
            uiVideo.updateAudioVolumeIndication(item.audioVolume, item.info.streamUuid)
        }
    }

    fun updateAudioVolumeIndication(volume: Int, streamUuid: String) {
        uiVideo.updateAudioVolumeIndication(volume, streamUuid)
    }

    fun updateUserWaveState(userUuid: String, waving: Boolean) {
        uiVideo.updateWaveState(userUuid, waving)
    }

    override fun onRendererContainer(viewGroup: ViewGroup?, info: AgoraUIUserDetailInfo) {
        callback?.onRendererContainer(viewGroup, info)
    }
}




