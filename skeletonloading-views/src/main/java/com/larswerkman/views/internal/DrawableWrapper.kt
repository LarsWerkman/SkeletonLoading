package com.larswerkman.views.internal

import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.View

/**
 * Drawable which delegates all calls to its wrapped [Drawable].
 *
 *
 * The wrapped [Drawable] *must* be fully released from any [View]
 * before wrapping, otherwise internal [Callback] may be dropped.
 *
 * @hide
 */
open class DrawableWrapper(var wrappedDrawable: Drawable) : Drawable(), Drawable.Callback {

    override fun draw(canvas: Canvas) {
        wrappedDrawable.draw(canvas)
    }

    override fun onBoundsChange(bounds: Rect) {
        wrappedDrawable.bounds = bounds
    }

    override fun setChangingConfigurations(configs: Int) {
        wrappedDrawable.changingConfigurations = configs
    }

    override fun getChangingConfigurations(): Int {
        return wrappedDrawable.changingConfigurations
    }

    override fun setDither(dither: Boolean) {
        wrappedDrawable.setDither(dither)
    }

    override fun setFilterBitmap(filter: Boolean) {
        wrappedDrawable.isFilterBitmap = filter
    }

    override fun setAlpha(alpha: Int) {
        wrappedDrawable.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        wrappedDrawable.colorFilter = cf
    }

    override fun isStateful(): Boolean {
        return wrappedDrawable.isStateful
    }

    override fun setState(stateSet: IntArray): Boolean {
        return wrappedDrawable.setState(stateSet)
    }

    override fun getState(): IntArray {
        return wrappedDrawable.state
    }

    override fun jumpToCurrentState() {
        wrappedDrawable.jumpToCurrentState()
    }

    override fun getCurrent(): Drawable {
        return wrappedDrawable.current
    }

    override fun setVisible(
        visible: Boolean,
        restart: Boolean
    ): Boolean {
        return super.setVisible(visible, restart) || wrappedDrawable.setVisible(visible, restart)
    }

    override fun getOpacity(): Int {
        return wrappedDrawable.opacity
    }

    override fun getTransparentRegion(): Region? {
        return wrappedDrawable.transparentRegion
    }

    override fun getIntrinsicWidth(): Int {
        return wrappedDrawable.intrinsicWidth
    }

    override fun getIntrinsicHeight(): Int {
        return wrappedDrawable.intrinsicHeight
    }

    override fun getMinimumWidth(): Int {
        return wrappedDrawable.minimumWidth
    }

    override fun getMinimumHeight(): Int {
        return wrappedDrawable.minimumHeight
    }

    override fun getPadding(padding: Rect): Boolean {
        return wrappedDrawable.getPadding(padding)
    }

    /**
     * {@inheritDoc}
     */
    override fun invalidateDrawable(who: Drawable) {
        invalidateSelf()
    }

    /**
     * {@inheritDoc}
     */
    override fun scheduleDrawable(
        who: Drawable,
        what: Runnable,
        `when`: Long
    ) {
        scheduleSelf(what, `when`)
    }

    /**
     * {@inheritDoc}
     */
    override fun unscheduleDrawable(
        who: Drawable,
        what: Runnable
    ) {
        unscheduleSelf(what)
    }

    override fun onLevelChange(level: Int): Boolean {
        return wrappedDrawable.setLevel(level)
    }

    override fun setAutoMirrored(mirrored: Boolean) {
        wrappedDrawable.isAutoMirrored = mirrored
    }

    override fun isAutoMirrored(): Boolean {
        return wrappedDrawable.isAutoMirrored
    }

    override fun setTint(tint: Int) {
        wrappedDrawable.setTint(tint)
    }

    override fun setTintList(tint: ColorStateList?) {
        wrappedDrawable.setTintList(tint)
    }

    override fun setTintMode(tintMode: PorterDuff.Mode?) {
        wrappedDrawable.setTintMode(tintMode)
    }

    override fun setHotspot(x: Float, y: Float) {
        wrappedDrawable.setHotspot(x, y)
    }

    override fun setHotspotBounds(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        wrappedDrawable.setHotspotBounds(left, top, right, bottom)
    }

    override fun invalidateSelf() {
        wrappedDrawable.bounds = bounds
        super.invalidateSelf()
    }
}