package com.macmads.whatsaccess.services

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat

class WhatsappAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (rootInActiveWindow == null) {
            return
        }
        val rootInActiveWindow = AccessibilityNodeInfoCompat.wrap(rootInActiveWindow)

        val messageNodeList =
            rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry")
        if (messageNodeList == null || messageNodeList.isEmpty()) {
            return
        }

        val messageField = messageNodeList[0]
        if (messageField.text == null || messageField.text.isEmpty() || !messageField.text.toString()
                .endsWith("mysuffix")
        ) {
            return
        }

        val sendMessageNodeInfoList =
            rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send")
        if (sendMessageNodeInfoList == null || sendMessageNodeInfoList.isEmpty()) {
            return
        }
        val sendMessageButton = sendMessageNodeInfoList[0]
        if (!sendMessageButton.isVisibleToUser) {
            return
        }

        sendMessageButton.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        try {
//            Thread.sleep(500)
            performGlobalAction(GLOBAL_ACTION_BACK)
//            Thread.sleep(500)
        } catch (ignored: InterruptedException) {
            Log.e("Action Not Performed", "Action not performed")
        }
        performGlobalAction(GLOBAL_ACTION_BACK)
    }

    override fun onInterrupt() {
    }
}
