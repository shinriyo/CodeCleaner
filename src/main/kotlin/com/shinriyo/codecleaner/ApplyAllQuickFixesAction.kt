package com.shinriyo.codecleaner

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.codeInsight.intention.IntentionManager

class ApplyAllQuickFixesAction : IntentionAction {
    override fun getText() = "Apply All Quick Fixes & Reformat"
    override fun getFamilyName() = "CustomFixes"
    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?) = true

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        WriteCommandAction.runWriteCommandAction(project) {
            val intentions = file?.let { f ->
                IntentionManager.getInstance().getAvailableIntentions()
            } ?: return@runWriteCommandAction

            intentions.forEach { intention: IntentionAction ->
                intention.isAvailable(project, editor, file)
                intention.invoke(project, editor, file)
            }

            // コードフォーマット適用
            editor?.document?.let { doc ->
                com.intellij.psi.codeStyle.CodeStyleManager.getInstance(project)
                    .reformatText(file, 0, doc.textLength)
            }
        }
    }

    override fun startInWriteAction() = true
}
