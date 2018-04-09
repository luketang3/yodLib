package com.yod.common.ui

import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * Created by tangJ on 2017/11/13
 */

class Holder(view: View) : RecyclerView.ViewHolder(view) {

  private val sa = SparseArray<View>()
  fun child(@IdRes id: Int): View {
    var view = sa.get(id)
    if (view == null) {
      view = itemView.findViewById(id)
      sa.put(id, view)
    }
    return view
  }

  @Suppress("UNCHECKED_CAST")
  fun <T : View> find(@IdRes id: Int): T {
    return child(id) as T
  }
}

abstract class BaseRecyclerAdapter<T>(val dataList: MutableList<T>,
    val itemId: Int) : RecyclerView.Adapter<Holder>() {

  constructor(dataList: MutableList<T>,
      itemId: Int, sectionId: Int) : this(dataList, itemId) {
    this.sectionId = sectionId
  }

  var headView: View? = null
  var bottomView: View? = null
  var msgView: View? = null
  private var msgShowFlag = false
  var sectionId = 0

  var supportHead = false //只头部

  var objectMatcher: ((Any, Any) -> Boolean)? = null

  internal var sectionSA = SparseArray<Any>()

  companion object {
    val TYPE_HEAD = 1
    val TYPE_BOTTOM = 2
    val TYPE_SECTION = 3 //section
    val TYPE_MESSAGE = 4 //无数据/异常
  }

  override fun onViewAttachedToWindow(holder: Holder) {
    super.onViewAttachedToWindow(holder)
    val type = holder.itemViewType
    if (type == TYPE_HEAD || type == TYPE_SECTION || type == TYPE_BOTTOM || type == TYPE_MESSAGE) {
      setFullSpan(holder)
    }

    if (type == TYPE_MESSAGE) {
      val layoutParams = holder.itemView.layoutParams
      layoutParams.width = ViewGroup.MarginLayoutParams.MATCH_PARENT
      layoutParams.height = ViewGroup.MarginLayoutParams.MATCH_PARENT
    }
  }

  protected fun setFullSpan(holder: Holder) {
    if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
      val params = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
      params.isFullSpan = true
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
    return when (viewType) {
      TYPE_HEAD -> Holder(headView!!)
      TYPE_BOTTOM -> Holder(bottomView!!)
      TYPE_MESSAGE -> Holder(msgView!!)
      TYPE_SECTION -> {
        Holder(LayoutInflater.from(parent.context).inflate(sectionId, null))
      }
      else -> {
        val view = LayoutInflater.from(parent.context).inflate(itemId, null)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        Holder(view)
      }
    }
  }

  override fun onBindViewHolder(holder: Holder, position: Int) {
    when (holder.itemViewType) {
      TYPE_HEAD -> {
      }
      TYPE_BOTTOM -> {
      }
      TYPE_MESSAGE -> {
      }
      TYPE_SECTION -> {
        val key = getHeadKey(position - headCount())
        bindSection(holder, sectionSA.get(key))
      }
      else -> {
        val p = getPosition(position - headCount())
        val entity = dataList[p]
        holder.itemView.setOnClickListener(object : DebouncingOnClickListener() {
          override fun doClick(view: View) {
            itemClick(holder, entity, p)
          }
        })
        holder.itemView.setOnLongClickListener { itemLongClick(holder, entity, p) }
        bindView(holder, entity, p)
      }
    }
  }

  internal fun getPosition(position: Int): Int {
    for (i in 0..sectionSA.size() - 1) {
      val t = sectionSA.keyAt(i)
      if (position > t + i) {
        if (i + 1 == sectionSA.size()) {
          return position - i - 1
        } else {
          continue
        }

      }
      return position - i
    }
    return position
  }


  private fun showMessage(): Boolean {
    return msgView != null && msgShowFlag
  }

  private fun showHead(): Boolean {
    return supportHead && dataList.isEmpty() && headCount() > 0
  }

  override fun getItemCount(): Int {
    if (showHead()) return 1
    if (showMessage()) return 1
    return headCount() + bottomCount() + sectionCount() + itemCount()
  }

  fun headCount(): Int {
    return if (headView == null) 0 else 1
  }

  fun bottomCount(): Int {
    return if (bottomView == null) 0 else 1
  }

  internal fun sectionCount(): Int {
    return sectionSA.size()
  }

  fun itemCount(): Int {
    return dataList.size
  }

  internal fun isSection(position: Int): Boolean {
    for (i in 0..sectionSA.size() - 1) {
      val t = sectionSA.keyAt(i)
      if (position > t + i) continue
      return t + i == position
    }
    return false
  }

  private fun getHeadKey(position: Int): Int {
    for (i in 0..sectionSA.size() - 1) {
      val t = sectionSA.keyAt(i)
      if (position > t + i) continue
      return t
    }
    return -1
  }

  override fun getItemViewType(position: Int): Int {
    if (showHead()) return TYPE_HEAD
    if (showMessage()) {
      return TYPE_MESSAGE
    } else {
      if (headCount() == 1 && position == 0)
        return TYPE_HEAD
      if (bottomCount() == 1 && position == itemCount - 1)
        return TYPE_BOTTOM
      if (isSection(position - headCount())) {
        return TYPE_SECTION
      }
      return super.getItemViewType(position)
    }
  }


  open fun bindSection(holder: Holder, target: Any) {

  }

  abstract fun bindView(holder: Holder, entity: T, position: Int)

  open fun itemClick(holder: Holder, entity: T, position: Int) {}

  open fun itemLongClick(holder: Holder, entity: T, position: Int): Boolean {
    return false
  }

  /**
   * 添加头部文件
   */
  fun addHead(view: View) {
    this.headView = view
  }

  /**
   * 底部文件
   */
  fun addFoot(view: View) {
    this.bottomView = view
    notifyDataSetChanged()
  }

  fun addMsgView(view: View) {
    this.msgView = view
  }

  fun isEmpty() = dataList.isEmpty()

  fun addData(list: List<T>, notify: Boolean = true) {
    dataList.addAll(list)
    if (notify) notifyDataSetChanged()
  }

  fun addSingleData(entity: T) {
    dataList.add(entity)
    notifyDataSetChanged()
  }

  fun clear() {
    resetData(listOf())
  }

  /**
   * 重置
   */
  fun resetSA(sa: SparseArray<Any>, notify: Boolean = true) {
    this.sectionSA.clear()
    (0 until sa.size())
        .map { sa.keyAt(it) }
        .forEach { sectionSA.put(it, sa.get(it)) }
    if (notify) notifyDataSetChanged()
  }

  fun resetData(iterator: List<T>, notify: Boolean = true) {
    dataList.clear()
    dataList.addAll(iterator)
    if (notify) notifyDataSetChanged()
  }

  fun showMsgView(show: Boolean) {
    this.msgShowFlag = show
    notifyDataSetChanged()
  }

  fun getMatcherIndex(from: Any): Int {

    objectMatcher?.let {
      for (i in 0..sectionSA.size() - 1) {
        val compare = sectionSA.valueAt(i)
        if (it(from, compare)) {
          return sectionSA.keyAt(i) + i + headCount()
        }
      }
    }
    return -1
  }

  operator fun get(index: Int): T = dataList[index]
}

/**
 * pager Adapter
 */
abstract class BasePagerAdapter<T>(val dataList: MutableList<T>, val itemId: Int,
    val context: Context, val isRepeat: Boolean = false) : PagerAdapter() {

  override fun isViewFromObject(view: View, `object`: Any): Boolean {
    return view == `object`
  }

  override fun getCount(): Int {
    return if (isRepeat && dataList.size > 1) Int.MAX_VALUE else dataList.size
  }

  override fun instantiateItem(container: ViewGroup, position: Int): Any {

    val view = getView()
    val p = getRealPosition(position)
    bindView(view.tag as Holder, p, dataList[p])
    container.addView(view)
    return view
  }

  abstract fun bindView(holder: Holder, position: Int, info: T)

  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    container.removeView(`object` as View)
  }

  private fun getView(): View {
    val target = View.inflate(context, itemId, null)
    val holder = Holder(target)
    target.tag = holder
    return target
  }

  fun getRealCount(): Int {
    return dataList.size
  }

  fun getRealPosition(position: Int): Int {
    return if (isRepeat) position % dataList.size else position
  }
}

abstract class BaseListAdapter<T : Any>(val dataList: MutableList<T>, val context: Context,
    @LayoutRes val layoutId: Int) : BaseAdapter() {
  val inflater: LayoutInflater = LayoutInflater.from(context)

  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    var targetView = convertView
    if (targetView == null) {
      targetView = inflater.inflate(layoutId, null)
      val holder = Holder(targetView)
      targetView.tag = holder
    }
    (targetView?.tag as Holder).apply { bindItem(this, position, dataList[position]) }
    return targetView
  }

  override fun getItem(position: Int): Any = dataList[position]
  override fun getItemId(position: Int): Long = position.toLong()
  override fun getCount(): Int = dataList.size

  fun getItemData(position: Int): T = dataList[position]

  fun resetData(list: List<T>, notify: Boolean = true) {
    dataList.clear()
    dataList.addAll(list)
    if (notify) notifyDataSetChanged()
  }

  abstract fun bindItem(holder: Holder, position: Int, info: T)
}