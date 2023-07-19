package org.teamvoided.voidlib.vui.v2.extension

import net.minecraft.client.render.entity.EntityRenderDispatcher

interface EntityRendererDispatcherExt {
    var void_showNametag: Boolean
    var void_counterRotate: Boolean

    companion object {
        var EntityRenderDispatcher.showNametag
            get() = (this as EntityRendererDispatcherExt).void_showNametag
            set(value) { (this as EntityRendererDispatcherExt).void_showNametag = value }

        var EntityRenderDispatcher.counterRotate
            get() = (this as EntityRendererDispatcherExt).void_counterRotate
            set(value) { (this as EntityRendererDispatcherExt).void_counterRotate = value }
    }
}
