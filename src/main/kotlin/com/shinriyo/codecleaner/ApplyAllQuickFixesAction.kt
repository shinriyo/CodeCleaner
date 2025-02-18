package com.shinriyo.codecleaner

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.codeInsight.intention.IntentionManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.AnAction

class ApplyAllQuickFixesAction : AnAction("Apply All Quick Fixes & Reformat") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getRequiredData(CommonDataKeys.EDITOR)
        val file = e.getRequiredData(CommonDataKeys.PSI_FILE)

        WriteCommandAction.runWriteCommandAction(project) {
            val intentions = IntentionManager.getInstance().availableIntentions
            
            intentions.forEach { intention: IntentionAction ->
                if (intention.isAvailable(project, editor, file)) {
                    intention.invoke(project, editor, file)
                }
            }

            // Apply code formatting
            editor.document.let { doc ->
                com.intellij.psi.codeStyle.CodeStyleManager.getInstance(project)
                    .reformatText(file, 0, doc.textLength)
            }
        }
    }
}
