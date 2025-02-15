package com.shinriyo.codecleaner

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.codeInspection.ex.GlobalInspectionContextBase
import com.intellij.codeInspection.InspectionManager
import com.intellij.analysis.AnalysisScope

class RunCodeCleanupAction : AnAction("Run Code Cleanup") {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project ?: return
        runCodeCleanup(project)
    }

    private fun runCodeCleanup(project: Project) {
        val context = GlobalInspectionContextBase(project)
        val scope = AnalysisScope(project)
        context.initializeTools(emptyList(), emptyList(), mutableListOf())
        context.doInspections(scope)
    }
}
